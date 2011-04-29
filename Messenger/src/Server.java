import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends JFrame implements ActionListener {
	JPanel p_log;
	JPanel p_clientList;
	
	JTextArea ta_log;
	JScrollPane sp_log;
	JScrollBar sb_log;
	
	JTextArea ta_clientList;
	JScrollPane sp_clientList;
	JScrollBar sb_clientList;
	
	JButton bt_fileServer;
	
	ServerSocket serverSocket;
	Socket clientSocket;
	String serverIP;
	int serverPort;
	
	Vector<Object> clientList;
	Vector<String> clientIPList;
	Vector<String> clientNickList;
	
	// 생성자 - 서버 UI 생성
	Server(String serverIP, String serverPort_s) {
		super("서버 IP :: " + serverIP + " / 서버 포트 :: " + serverPort_s);
		
		this.serverIP = serverIP; 
		serverPort = Integer.parseInt(serverPort_s); // 서버 포트(String형)를 int형으로 변환
		
		// 클라이언트 리스트를 저장하는 벡터
		clientList = new Vector<Object>();
		clientIPList = new Vector<String>();
		clientNickList = new Vector<String>();
		
		// 로그를 표시하는 패널
		p_log = new JPanel(new BorderLayout());
		{
			ta_log = new JTextArea();
			ta_log.setEditable(false);
			ta_log.setLineWrap(true);
			ta_log.setBackground(Color.black);
			ta_log.setForeground(Color.white);
			ta_log.setMargin(new Insets(5, 5, 5, 5));
			
			// 스크롤 패널(세로 스크롤 항상 표시, 가로스크롤 표시하지 않음)에 텍스트필드를 얹음
			sp_log = new JScrollPane(ta_log);
			sp_log.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_log.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_log = sp_log.getVerticalScrollBar();
		}
		p_log.add(BorderLayout.CENTER, sp_log);
		
		// 클라이언트 리스트를 표시하는 패널
		p_clientList = new JPanel(new BorderLayout());
		{
			ta_clientList = new JTextArea();
			ta_clientList.setEditable(false);
			ta_clientList.setLineWrap(true);
			ta_clientList.setBackground(Color.black);
			ta_clientList.setForeground(Color.white);
			ta_clientList.setMargin(new Insets(5, 5, 5, 5));
			
			// 스크롤 패널(세로 스크롤 항상 표시, 가로스크롤 표시하지 않음)에 텍스트필드를 얹음
			sp_clientList = new JScrollPane(ta_clientList);
			sp_clientList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_clientList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_clientList = sp_clientList.getVerticalScrollBar();
			
			bt_fileServer = new JButton("파일 서버 시작");
			bt_fileServer.addActionListener(this);
		}
		p_clientList.add(BorderLayout.CENTER, sp_clientList);
		p_clientList.add(BorderLayout.SOUTH, bt_fileServer);
		
		getContentPane().add(BorderLayout.CENTER, p_log);
		getContentPane().add(BorderLayout.EAST, p_clientList);
	}
	
	public static void main(String[] args) { // 메인		
		Server myServer = new Server(args[0], args[1]);
		
		//창의 x 버튼을 누르면 프로그램을 종료한다.
		myServer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myServer.setSize(500, 300);
		myServer.setVisible(true);
		
		// 서버 실행 가능 여부를 체크
		boolean serverState = myServer.serverStart();
		
		// 서버 실행이 가능하면, 클라이언트 접속을 기다림
		if(serverState) {
			myServer.serverWait();
		}
		
		// 서버 실행이 불가능하면, 로그를 띄움
		else {
			myServer.dspLog("[Error] 서버 시작에 실패하였습니다.");
		}
	}
	
	// 서버를 시작하는 메쏘드
	boolean serverStart() {
		
		// 서버 실행이 가능하면, 로그를 띄우고 true를 리턴
		try {
			serverSocket = new ServerSocket(serverPort);
			dspLog("[Success] 서버가 시작되었습니다.");
			return true;
		}
		
		// 서버 실행이 불가능하면, 로그를 띄우고 fasle를 리턴 
		catch(Exception e) {
			dspLog("[Error] 서버 시작에 실패하였습니다.");
			e.printStackTrace();
			return false;
		}
	}

	// 클라이언트의 접속을 기다리는 메쏘드
	void serverWait() {
		while(true) {
			try {
				// 클라이언트가 접속될 때까지 기다린다.
				clientSocket = serverSocket.accept();
				
				// 클라이언트가 접속하면, 클라이언트별로 중계 쓰레드를 시작한다.
				ServerRelay myServerRelay = new ServerRelay(clientSocket);
				myServerRelay.start();
				
				// 리스트에 해당 클라이언트<Object>를 벡터에 추가
				clientList.addElement(myServerRelay);
			}
			
			// 예외가 발생하면 로그를 띄운다.
			catch(Exception e) {
				dspLog("[Error] 서버 소켓 생성에 실패하였습니다.");
				e.printStackTrace();
			}
		}
	}
	
	// 클라이언트를 중계하는 클래스(inner)
	class ServerRelay extends Thread {
		Socket clientSocket;
		BufferedReader bufferedReader;
		PrintWriter printWriter;
				
		String clientIP;
		String clientNick;
		
		// 생성자
		ServerRelay(Socket clientSocket) {
			this.clientSocket = clientSocket;
			
			// 접속한 해당 클라이언트의 IP를 저장한다.
			clientIP = clientSocket.getInetAddress().getHostAddress();
		}
		
		// 클라이언트의 중계를 시작 
		public void run() {
			try {
				// 해당 클라이언트와의 스트림을 생성
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
				
				// 해당 클라이언트의 대화명을 저장
				clientNick = bufferedReader.readLine();
				
				// 해당 클라이언트가 접속했다는 것을, 로그와 각 클라이언트에게 전파
				dspLog("[Info] " + clientNick + " (" + clientIP + ") 가 접속하였습니다.");
				broadCast("[Info] " + clientNick + " (" + clientIP + ") 님이 대화에 참여하였습니다.");
				
				// 해당 클라이언트의 IP와 대화명을 벡터에 추가한 뒤, 리스트 표시
				clientIPList.addElement(clientIP);
				clientNickList.addElement(clientNick);
				dspClientList();
				
				// 해당 클라이언트가 보내는 메세지를 각 클라이언트에게 전파
				String serverMsg = null;
				while((serverMsg = bufferedReader.readLine())!= null) {
					broadCast(serverMsg);
				}
			}
			
			// 예외가 발생하면, 해당 클라이언트의 연결이 끊어졌다는 것을 의미
			catch(Exception e) {
				// 해당 클라이언트의 연결이 끊어졌다는 것을, 로그와 각 클라이언트에게 전파
				dspLog("[Info] " + clientNick + " (" + clientIP + ") 가 연결을 끊었습니다.");
				broadCast("[Info] " + clientNick + " (" + clientIP + ") 님이 퇴장하셨습니다.");
				
				// 해당 클라이언트의 IP와 대화명을 벡터에서 삭제
				clientIPList.removeElement(clientIP);
				clientNickList.removeElement(clientNick);
				dspClientList();
			}
			
			// 마지막으로 해당 클라이언트와의 스트림과 소켓을 해제 
			finally {
				if(bufferedReader != null) {
					try {
						bufferedReader.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(printWriter != null) {
					try {
						printWriter.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(clientSocket != null) {
					try {
						clientSocket.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// 다른 클라이언트에게 메세지를 보내는 메쏘드
		void sendMSG(String clientMsg) {
			try {
				printWriter.println(clientMsg);
				printWriter.flush();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 모든 클라이언트에게 메세지를 전파하기 위한 메쏘드
	void broadCast(String serverMsg) {
		// Enumeration 인터페이스에 클라이언트 리스트가 담겨있는 벡터를 할당
		Enumeration<Object> container = clientList.elements();
		
		// 커서에 데이터가 없을 때까지 반복
		while(container.hasMoreElements()) {
			// 객체를 리턴하고, 커서를 뒤로 이동
			ServerRelay myServerRelay = (ServerRelay)container.nextElement();
			
			// 해당 클라이언트에게 메세지를 보냄
			myServerRelay.sendMSG(serverMsg);
		}
	}
	
	// 로그를 띄우는 메쏘드
	void dspLog(String MSG) {
		// 스크롤바의 맨끝으로 이동한 뒤, 로그를 표시한다.
		sb_log.setValue(sb_log.getMaximum()); 
		ta_log.append(MSG + "\n");
	}
	
	// 접속자 목록을 띄우는 메쏘드
	void dspClientList() {
		ta_clientList.setText("접속자 목록 :");
		
		// 클라이언트의 갯수까지 반복
		for ( int i = 0 ; i < clientNickList.size() ; i++ ) {
			// 클라이언트 대화명(IP)를 표시
			ta_clientList.append("\n" + clientNickList.get(i) + " (" + clientIPList.get(i) + ")");
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			// Main 함수가 담겨있는 Client 클래스를 실행한다. 이때, 서버 IP와 포트를 인자값으로 넘겨준다.
			Runtime.getRuntime().exec("java Server_fileServer " + serverIP + " " + serverPort);
			// 프로세스를 따로 띄우기 때문에
			// 파일 서버는 개별적으로 작동되며, 멀티코어 CPU에 유리
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}