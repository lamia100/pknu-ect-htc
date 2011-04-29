import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client_sendFile extends JFrame implements ActionListener {
	JPanel p_main;
	JPanel p_main_center;
	JPanel p_main_bottom;
	
	JTextField tf_pathFile;
	JButton bt_selectFile;
	JButton bt_sendFile;
	JLabel lb_error;
	
	FileDialog fd_selectFile;
	String pathFile;
	String nameFile;
	
	String serverIP;
	int serverPort;
	
	// 생성자 - 파일 전송 UI 생성 
	Client_sendFile(String title, String serverIP, String serverPort_s) {
		super(title);
		
		this.serverIP = serverIP;
		
		// 서버 포트(String형)를 int형으로 변환한 뒤, 1을 더함
		// 따라서 채팅 서버와 파일 서버의 포트는 달리 수행된다. 
		serverPort = Integer.parseInt(serverPort_s) + 1;
		
		// 파일을 선택하기 위한 다이얼로그 생성
		fd_selectFile = new FileDialog(this, "파일선택", FileDialog.LOAD);
		
		p_main = new JPanel(new BorderLayout());
		{
			tf_pathFile = new JTextField();
			tf_pathFile.setEditable(false); // 텍스트필드-파일경로는 텍스트로 수정 불가
			tf_pathFile.setBackground(Color.WHITE);
			
			p_main_center = new JPanel(new BorderLayout());
			{
				bt_selectFile = new JButton("파일 선택");
				bt_sendFile = new JButton("파일 전송");
				
				bt_selectFile.addActionListener(this);
				bt_sendFile.addActionListener(this);
			}
			p_main_center.add(BorderLayout.WEST, bt_selectFile);
			p_main_center.add(BorderLayout.CENTER, bt_sendFile);
			
			p_main_bottom = new JPanel(new BorderLayout());
			{
				lb_error = new JLabel();
				lb_error.setText("전송할 파일을 선택하세요.");
			}
			p_main_bottom.add(lb_error);
		}
		p_main.add(BorderLayout.NORTH, tf_pathFile);
		p_main.add(BorderLayout.CENTER, p_main_center);
		p_main.add(BorderLayout.SOUTH, p_main_bottom);
		
		getContentPane().add(p_main);
	}
	
	public static void main(String[] args) {
		Client_sendFile myClient_sendFile = new Client_sendFile("파일 전송", args[0], args[1]);
		
		//창의 x 버튼을 누르면 프로그램을 종료한다.
		myClient_sendFile.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myClient_sendFile.setSize(300, 100);
		myClient_sendFile.setVisible(true);
		myClient_sendFile.setLocation(new Point(300, 300));
		myClient_sendFile.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		// 파일 선택 버튼을 눌렀다면
		if(e.getSource() == bt_selectFile) {
			// 파일을 선택하기 위한 다이얼로그를 띄움
			fd_selectFile.setVisible(true);
			
			pathFile = fd_selectFile.getDirectory(); // 선택한 파일의 디렉토리를 저장
			nameFile = fd_selectFile.getFile(); // 선택한 파일의 이름을 저장
			
			tf_pathFile.setText(pathFile + nameFile); // 텍스트필드-파일경로에 파일경로를 표시
		}
		
		// 파일 전송 버튼을 눌렀다면
		else if(e.getSource() == bt_sendFile) {
			// 파일을 선택하지 않았다면, 에러 메세지 표시
			if(tf_pathFile.getText().equals("")) {
				lb_error.setText("전송할 파일을 선택해야 합니다.");
			}
			// 파일 선택했다면, 파일 전송 시작
			else {
				sendStart();
			}
		}
	}
	
	// 파일 전송을 시작하는 메쏘드
	void sendStart() {
		Socket sendFileSocket;
		
		bt_sendFile.setText("파일 전송 중");
		
		
		// 소켓생성 및 파일 이름과 실제 파일 전송
		try {
			sendFileSocket = new Socket(serverIP, serverPort);
			sendNameFile(sendFileSocket);
			sendFile(sendFileSocket);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 파일 이름을 보내는 메쏘드
	void sendNameFile(Socket sendFileSocket) {
		BufferedWriter bw;
		
		// 스트림 생성 및 파일 이름 전송
		try {
			bw = new BufferedWriter(new OutputStreamWriter(sendFileSocket.getOutputStream()));
			bw.write(nameFile);
			bw.flush();
			bw.close();
		}
		
		// 예외가 발생하면, 에러 메세지를 띄움
		catch(Exception e) {
			lb_error.setText("파일 전송에 문제가 발생하였습니다.");
			e.printStackTrace();
		}
	}
	
	// 실제 파일 전송을 위한 메쏘드
	void sendFile(Socket sendFileSocket) {
		DataInputStream dis;
		DataOutputStream dos;
		
		try {
			// 파일 전송을 위한 스트림 생성
			dis = new DataInputStream(new FileInputStream(new File(tf_pathFile.getText())));
			dos = new DataOutputStream(sendFileSocket.getOutputStream());
			
			// 바이트단위로 읽어서 스트림으로 내보냄
			int i = 0;
			while((i = dis.read()) != -1 ) {
				dos.writeByte(i);
				dos.flush();
			}
			dis.close();
			dos.close();
		}
		catch(Exception e) {
			lb_error.setText("파일 전송에 문제가 발생하였습니다.");
			e.printStackTrace();
		}
	}
}