import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Messenger extends JFrame {
	JPanel p_mode;
	JPanel p_main;
	JPanel p_main_top;
	JPanel p_main_bottom;
	JPanel p_main_bottom_in;
	JPanel p_main_bottom_in_in1;
	JPanel p_main_bottom_in_in2;
	JPanel p_main_bottom_in_in3;
	JPanel p_main_error;
	
	ButtonGroup bg_mode;
	JRadioButton rb_server;
	JRadioButton rb_client;
		
	JButton bt_Server;
	JButton bt_client;
	
	JLabel lb_mode;
	JLabel lb_serverIP_1;
	JLabel lb_serverIP;
	JLabel lb_serverPort; 
	JLabel lb_client_serverIP;
	JLabel lb_client_serverPort;
	JLabel lb_nick;
	JLabel lb_error;
	
	JTextField tf_serverPort;
	JTextField tf_client_serverIP;
	JTextField tf_client_serverPort;
	JTextField tf_nick;
		
	String mode;
	Socket clientSocket;
	
	// 생성자 - 기본 UI 생성
	Messenger(String title) {
		super(title);
		
		p_main = new JPanel();
		{	
			p_main_top = new JPanel();
			{
				lb_mode = new JLabel();
				lb_mode.setText("모드 :: ");
				
				p_mode = new JPanel();
				{
					bg_mode = new ButtonGroup();
				
					// 서버||클라이언트 모드를 선택하는 라디오버튼
					rb_server = new JRadioButton("서버");
					rb_server.addActionListener(new LoginListener());
					rb_client = new JRadioButton("클라이언트");
					rb_client.addActionListener(new LoginListener());
				
					bg_mode.add(rb_server);
					bg_mode.add(rb_client);
				}
				p_mode.add(rb_server);
				p_mode.add(rb_client);
			}
			p_main_top.add(BorderLayout.WEST, lb_mode);
			p_main_top.add(BorderLayout.CENTER, p_mode);
		
			p_main_bottom = new JPanel();
			{
				p_main_bottom_in = new JPanel(new BorderLayout());
			}
			p_main_bottom.add(p_main_bottom_in);
		
			p_main_error = new JPanel();
			{
				// 에러 상태를 표시하는 라벨
				lb_error = new JLabel();
			}
			p_main_error.add(lb_error);
		}
		p_main.setLayout(null);
		p_main.add(p_main_top);
		p_main.add(p_main_bottom);
		p_main.add(p_main_error);

		p_main_top.setBounds(0, 0, 280, 100);
		p_main_bottom.setBounds(0, 50, 280, 150);
		p_main_error.setBounds(0, 110, 280, 100);
			
		getContentPane().add(BorderLayout.CENTER, p_main);
	}
	
	public static void main(String[] args) {
		Messenger myMessenger = new Messenger("메신저 - 200611604");
		
		//창의 x 버튼을 누르면 프로그램을 종료한다.
		myMessenger.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
		myMessenger.setSize(290, 180);
		myMessenger.setLocation(new Point(200, 400)); // 프레임이 나타나는 위치를 잡아준다.
		myMessenger.setVisible(true);
		myMessenger.setResizable(false); // 창크기는 조절할 수 없다.
	}
	
	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// 서버 모드를 선택했을 경우
			if(e.getSource() == rb_server) {
				setUI("server"); //UI를 서버 모드로 변경한다.
				mode = "server";
				lb_error.setText("");
			}
			
			// 클라이언트 모드를 선택했을 경우
			else if(e.getSource() == rb_client) {
				setUI("client"); // UI를 클라이언트 모드로 변경한다.
				mode = "client";
				lb_error.setText("");
			}
			
			// 서버실행 버튼을 눌렀을 경우
			else if(e.getSource() == bt_Server) {
				String serverPort = tf_serverPort.getText();
				
				// 서버 포트를 입력하지 않으면 에러
				if(serverPort.equals("")) {
					lb_error.setText("서버 포트를 입력하세요.");
				}
				
				// 서버 포트를 입력했다면 서버 실행
				else {
					String serverIP = lb_serverIP.getText();
					launchServer(serverIP, serverPort);
				}
			}
			
			// (클라이언트) 접속 버튼을 눌렀을 경우
			else if(e.getSource() == bt_client) {
				String client_serverIP = tf_client_serverIP.getText();
				String client_serverPort = tf_client_serverPort.getText();
				String nick = tf_nick.getText();
				
				// 서버 IP, 서버 포트, 대화명을 입력하지 않으면 에러
				if(client_serverIP.equals("")) {
					lb_error.setText("서버 IP를 입력하세요.");
				}
				else if(client_serverPort.equals("")) {
					lb_error.setText("서버 포트를 입력하세요.");
				}
				else if(nick.equals("")) {
					lb_error.setText("대화명을 입력하세요.");
				}
				
				// 모든 것을 입력했다면
				else {
					// 서버 접속가능 여부를 체크
					boolean isReachable = serverConnect(client_serverIP, client_serverPort);
					
					// 서버에 접속이 가능하다면 클라이언트 실행
					if(isReachable) {
						launchClient(client_serverIP, client_serverPort, nick);
					}
					
					// 서버에 접속이 불가능하다면 에러
					else {
						lb_error.setText("서버를 사용할 수 없습니다.");
					}
				}
			}
		}
	}
	
	// UI를 서버||클라이언트 모드에 맞게 수정하는 메쏘드
	void setUI(String modeInput) {
		
		// 모드가 변경되었을 때만, UI 수정
		if(mode != modeInput) {
			
			// 서버 모드 UI
			if(modeInput == "server") {
				p_main_bottom_in.removeAll(); // 하단 패널에 들어있는 컴포넌트들을 제거
				
				p_main_bottom_in_in1 = new JPanel(new BorderLayout());
				{
					lb_serverIP_1 = new JLabel();
					lb_serverIP = new JLabel();
					lb_serverIP.setText(getLocalIP()); // 로컬 IP를 받음
				}
				p_main_bottom_in_in1.add(BorderLayout.WEST, lb_serverIP_1);
				p_main_bottom_in_in1.add(BorderLayout.CENTER, lb_serverIP);
				
				p_main_bottom_in_in2 = new JPanel(new BorderLayout());
				{
					lb_serverPort = new JLabel();
					tf_serverPort = new JTextField();
					tf_serverPort.setText("9000"); // 기본 포트는 9000
				}
				p_main_bottom_in_in2.add(BorderLayout.WEST, lb_serverPort);
				p_main_bottom_in_in2.add(BorderLayout.CENTER, tf_serverPort);
				
				p_main_bottom_in_in3 = new JPanel(new BorderLayout());
				{
					bt_Server = new JButton("서버 시작");
					bt_Server.addActionListener(new LoginListener());
				}
				p_main_bottom_in_in3.add(bt_Server);
				
				p_main_bottom_in.add(BorderLayout.NORTH, p_main_bottom_in_in1);
				p_main_bottom_in.add(BorderLayout.CENTER, p_main_bottom_in_in2);
				p_main_bottom_in.add(BorderLayout.SOUTH, p_main_bottom_in_in3);
				
				tf_serverPort.requestFocus(); // 텍스트필드-서버포트에 포커스를 맞춤
				p_main_bottom_in.validate(); // 하단 패널의 변경사항을 체크
				
				lb_serverIP_1.setText("서버 IP      ::  ");
				lb_serverPort.setText("서버 포트 :: ");
			}
			
			// 클라이언트 모드 UI
			else {
				p_main_bottom_in.removeAll(); // 하단 패널에 들어있는 컴포넌트들을 제거
				
				p_main_bottom_in_in1 = new JPanel(new BorderLayout());
				{
					lb_client_serverIP = new JLabel();
					tf_client_serverIP = new JTextField(10);
					tf_client_serverIP.setText(getLocalIP()); // 로컬 IP를 받음
					tf_client_serverPort = new JTextField(4);
					tf_client_serverPort.setText("9000"); // 기본 포트는 9000
				}
				p_main_bottom_in_in1.add(BorderLayout.WEST, lb_client_serverIP);
				p_main_bottom_in_in1.add(BorderLayout.CENTER, tf_client_serverIP);
				p_main_bottom_in_in1.add(BorderLayout.EAST, tf_client_serverPort);
				
				p_main_bottom_in_in2 = new JPanel(new BorderLayout());
				{
					lb_nick = new JLabel();
					tf_nick = new JTextField(5);
					bt_client = new JButton("접속");
					bt_client.addActionListener(new LoginListener());
				}
				p_main_bottom_in_in2.add(BorderLayout.WEST, lb_nick);
				p_main_bottom_in_in2.add(BorderLayout.CENTER, tf_nick);
				p_main_bottom_in_in2.add(BorderLayout.EAST, bt_client);
				
				p_main_bottom_in.add(BorderLayout.NORTH, p_main_bottom_in_in1);
				p_main_bottom_in.add(BorderLayout.CENTER, p_main_bottom_in_in2);
				
				tf_nick.requestFocus(); // 텍스트필드-대화명에 포커스틑 맞춤
				p_main_bottom_in.validate(); // 하단 패널의 변경사항을 체크
				
				lb_client_serverIP.setText("서버 IP / 포트 :: ");
				lb_nick.setText("유저 ID :: ");
			}
		}
	}
	
	// 컴퓨터의 IP를 반환하는 메쏘드
	String getLocalIP() { 
		String LocalIP = null;
		
		// 로컬 IP를 받아옴
		try {
			LocalIP = InetAddress.getLocalHost().getHostAddress();
		}
		
		// 예외가 발생했을 경우, 127.0.0.1 IP를 반환
		catch(UnknownHostException e) {
			lb_error.setText("Local IP 주소를 알 수 없습니다.");
			LocalIP = "127.0.0.1";
			e.printStackTrace();
		}
		catch(Exception e) {
			lb_error.setText("Local IP 주소를 얻어오는 중 오류가 발생하였습니다.");
			LocalIP = "127.0.0.1";
			e.printStackTrace();
		}
		
		return LocalIP;
	}
	
	// 서버와 연결되는지 검사하는 메쏘드
	boolean serverConnect(String serverIP, String serverPort_s) {
		int serverPort = Integer.parseInt(serverPort_s);
		
		// 서버 IP와 포트로 소켓 연결을 시도한다.
		try {
			clientSocket = new Socket(serverIP, serverPort);
			
			// 연결이 되면 true 리턴
			if(clientSocket != null) {
				return true;
			}
			
			// 연결이 되지 않으면 false 리턴
			else {
				return false;
			}
			
		// 예외가 발생하면 false 리턴
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// 마지막으로 소켓을 해제한다.
		finally {
			clientSocket = null;
		}
	}
	
	// 서버를 실행하는 메쏘드
	void launchServer(String serverIP, String serverPort) {
		try {
			// Main 함수가 담겨있는 Server 클래스를 실행한다. 이때, 서버 IP와 포트를 인자값으로 넘겨준다. 
			Runtime.getRuntime().exec("java Server " + serverIP + " " + serverPort);
			// 프로세스를 따로 띄우기 때문에
			// 포트별로 서버를 실행할 수 있고, 멀티코어 CPU에 유리
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	// 클라이언트를 실행 메쏘드
	void launchClient(String client_serverIP, String client_serverPort, String nick) {
		try {
			// Main 함수가 담겨있는 Client 클래스를 실행한다. 이때, 서버 IP와 포트를 인자값으로 넘겨준다.
			Runtime.getRuntime().exec("java Client " + client_serverIP + " " + client_serverPort + " " + nick);
			// 프로세스를 따로 띄우기 때문에
			// 포트와 대화명 별로 클라이언트를 실행할 수 있고, 멀티코어 CPU에 유리
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}