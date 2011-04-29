import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener{
	JPanel p_main;
	JPanel p_main_top;
	JPanel p_main_center;
	JPanel p_main_bottom;
	
	JTextField tf_savePathFile;
	JButton bt_savePathFile;
	
	JLabel lb_myMsg;
	JTextField tf_myMsg;
	
	JScrollPane sp_allMsg;
	JTextArea ta_allMsg;
	JScrollBar sb_allMsg;
	JButton bt_sendFile;
	
	FileDialog fd_selectFile;
	String savePathFile;
	
	String serverIP;
	int serverPort;
	String nick;
	
	BufferedReader bufferedReader = null;
	PrintWriter printWriter = null;
	boolean receiveState = true;
	
	// 생성자 - 클라이언트 UI 생성
	Client(String serverIP, String serverPort_s, String nick) {
		super(nick + " 의 채팅");
		
		this.serverIP = serverIP;
		serverPort = Integer.parseInt(serverPort_s); // 서버 포트(String형)를 int형으로 변환
		this.nick = nick;
		
		// 파일을 저장할 폴더를 선택하기 위한 다이얼로그 생성
		fd_selectFile = new FileDialog(this, "파일 저장 폴더 선택", FileDialog.LOAD);
		
		p_main = new JPanel(new BorderLayout());
		{
			p_main_top = new JPanel(new BorderLayout());
			{
				tf_savePathFile = new JTextField();
				bt_savePathFile = new JButton("파일 저장 폴더");
				
				bt_savePathFile.addActionListener(this);
			}
			p_main_top.add(BorderLayout.CENTER, tf_savePathFile);
			p_main_top.add(BorderLayout.EAST, bt_savePathFile);
			
			p_main_center = new JPanel(new BorderLayout());
			{
				ta_allMsg = new JTextArea();
				ta_allMsg.setEditable(false);
				ta_allMsg.setLineWrap(true);
				ta_allMsg.setBackground(Color.DARK_GRAY);
				ta_allMsg.setForeground(Color.white);
				ta_allMsg.setMargin(new Insets(5, 5, 5, 5));
				ta_allMsg.addMouseListener(new FocusListener());
				
				// 스크롤 패널(세로 스크롤 항상 표시, 가로스크롤 표시하지 않음)에 텍스트필드를 얹음
				sp_allMsg = new JScrollPane(ta_allMsg);
				sp_allMsg.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				sp_allMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				sb_allMsg = sp_allMsg.getVerticalScrollBar();
			}
			p_main_center.add(BorderLayout.CENTER, sp_allMsg);
			
			p_main_bottom = new JPanel(new BorderLayout());
			{
				lb_myMsg = new JLabel(nick + " :: ");
		
				tf_myMsg = new JTextField(30);
				tf_myMsg.addKeyListener(new SendListener());
				
				bt_sendFile = new JButton("파일 전송");
				bt_sendFile.addActionListener(this);
			}
			p_main_bottom.add(BorderLayout.WEST, lb_myMsg);
			p_main_bottom.add(BorderLayout.CENTER, tf_myMsg);
			p_main_bottom.add(BorderLayout.EAST, bt_sendFile);
		
		}
		p_main.add(BorderLayout.NORTH, p_main_top);
		p_main.add(BorderLayout.CENTER, p_main_center);
		p_main.add(BorderLayout.SOUTH, p_main_bottom);
		
		getContentPane().add(p_main);
		tf_myMsg.requestFocus(); // 메세지 입력창에 포커스를 이동
		
		// 클라이언트 실행 가능 여부를 체크
		boolean chatState = this.chatStart();
		
		// 클라이언트 실행이 가능하면, 서버로 접속
		if(chatState) {
			ClientChat myClientChat = new ClientChat();
			myClientChat.start();
		}
		
		// 클라이언트 실행이 불가능하면, 에러 메세지를 띄움
		else {
			dspMsg("[Error] 서버와의 연결에 문제가 있습니다. 재로그인 하세요.");
		}
	}
	
	public static void main(String[] args) {
		Client myClient = new Client(args[0], args[1], args[2]);
		
		//창의 x 버튼을 누르면 프로그램을 종료한다.
		myClient.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myClient.setSize(500, 300);
		myClient.setVisible(true);
		myClient.setLocation(new Point(600, 200));
		myClient.setResizable(true);
	}
	
	// 클라이언트를 시작하는 메쏘드
	boolean chatStart() {
		
		// 클라이언트 실행이 가능하면, 스트림을 생성하고 true를 리턴
		try {
			Socket clientSocket = new Socket(serverIP, serverPort);
			bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			return true;
		}
		
		// 예외가 발생하면, 스트림을 해제하고 false를 리턴
		catch(Exception e) {
			e.printStackTrace();
			
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch(Exception bufferedReaderException) {
					bufferedReaderException.printStackTrace();
				}
			}
			if(printWriter != null) {
				try {
					printWriter.close();
				}
				catch(Exception printWriterException) {
					printWriterException.printStackTrace();
				}
			}
			return false;
		}
	}
	
	// 채팅을 수행하는 클래스(inner)
	class ClientChat extends Thread {
		public void run() {
			
			// 대화명을 서버로 보냄
			try {
				printWriter.println(nick);
				printWriter.flush();
			}
			
			// 예외가 발생하면, 에러 메세지를 띄움
			catch(Exception e) {
				dspMsg("[Error] 메세지를 전송하지 못하였습니다.");
				e.printStackTrace();
			}
			
			try {
				// 서버와 연결이 유지되고 있으면, 메세지를 서버로 보냄 
				while(receiveState == true) {
					String serverMsg = null;
					while((serverMsg = bufferedReader.readLine()) != null) {
						dspMsg(serverMsg);
					}
				}
			}
			
			// 예외가 발생하면, 에러 메세지를 띄우고 서버와의 연결이 끊겼다는 것을 체크
			catch(Exception e) {
				dspMsg("[Error] 서버와의 연결이 끊어졌습니다.");
				receiveState = false;
				e.printStackTrace();
			}
		}
	}
	
	// 메세지를 보내는 메쏘드
	void sendMSG(String sendMessage) {
		
		// [대화명] 메세지를 서버로 보냄
		try {
			printWriter.println("[" + nick + "] " + sendMessage);
			printWriter.flush();
		}
		
		// 예외가 발생하면, 에러 메세지를 띄움
		catch(Exception e) {
			dspMsg("[Error] 메세지를 전송하지 못하였습니다.");
			e.printStackTrace();
		}
		
		// 마지막으로 메세지 입력창을 지움
		finally {
			tf_myMsg.setText("");
		}
	}
	
	// 메세지를 띄우는 메쏘드
	void dspMsg(String inputMessage) {
		sb_allMsg.setValue(sb_allMsg.getMaximum());
		ta_allMsg.append(inputMessage + "\n");
	}
	
	class SendListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			// 엔터키를 입력했을 때 메세지 입력창이 비어있으면, 입력창에 있는 문자열을 메세지로 전송 
			if(e.getKeyCode() == KeyEvent.VK_ENTER && !(tf_myMsg.getText()).equals("") && tf_myMsg.getText() != "") {
				sendMSG(tf_myMsg.getText());
			}
		}
	}
	
	class FocusListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			// 메세지 입력창에 포커스를 이동
			tf_myMsg.requestFocus();
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		// 파일 저장 폴더 버튼을 클릭했다면
		if(e.getSource()== bt_savePathFile) {
			
			// 파일을 저장할 폴더를 선택하기 위한 다이얼로그를 띄움
			fd_selectFile.setVisible(true);
			
			// 선택한 폴더를 표시
			savePathFile = fd_selectFile.getDirectory();
			tf_savePathFile.setText(savePathFile);
			
			try {
				// Main 함수가 담겨있는 Client_receiveFile 클래스를 실행한다. 이때, 서버 IP와 포트, 저장 폴더를 인자값으로 넘겨준다.
				Runtime.getRuntime().exec("java Client_receiveFile " + serverIP + " " + serverPort + " " + savePathFile);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// 파일 전송 버튼을 클릭했다면
		else if(e.getSource()== bt_sendFile) {
			try {
				// Main 함수가 담겨있는 Client_sendFile 클래스를 실행한다. 이때, 서버 IP와 포트를 인자값으로 넘겨준다.
				Runtime.getRuntime().exec("java Client_sendFile " + serverIP + " " + serverPort);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}