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
	
	// ������ - �⺻ UI ����
	Messenger(String title) {
		super(title);
		
		p_main = new JPanel();
		{	
			p_main_top = new JPanel();
			{
				lb_mode = new JLabel();
				lb_mode.setText("��� :: ");
				
				p_mode = new JPanel();
				{
					bg_mode = new ButtonGroup();
				
					// ����||Ŭ���̾�Ʈ ��带 �����ϴ� ������ư
					rb_server = new JRadioButton("����");
					rb_server.addActionListener(new LoginListener());
					rb_client = new JRadioButton("Ŭ���̾�Ʈ");
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
				// ���� ���¸� ǥ���ϴ� ��
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
		Messenger myMessenger = new Messenger("�޽��� - 200611604");
		
		//â�� x ��ư�� ������ ���α׷��� �����Ѵ�.
		myMessenger.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
		myMessenger.setSize(290, 180);
		myMessenger.setLocation(new Point(200, 400)); // �������� ��Ÿ���� ��ġ�� ����ش�.
		myMessenger.setVisible(true);
		myMessenger.setResizable(false); // âũ��� ������ �� ����.
	}
	
	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// ���� ��带 �������� ���
			if(e.getSource() == rb_server) {
				setUI("server"); //UI�� ���� ���� �����Ѵ�.
				mode = "server";
				lb_error.setText("");
			}
			
			// Ŭ���̾�Ʈ ��带 �������� ���
			else if(e.getSource() == rb_client) {
				setUI("client"); // UI�� Ŭ���̾�Ʈ ���� �����Ѵ�.
				mode = "client";
				lb_error.setText("");
			}
			
			// �������� ��ư�� ������ ���
			else if(e.getSource() == bt_Server) {
				String serverPort = tf_serverPort.getText();
				
				// ���� ��Ʈ�� �Է����� ������ ����
				if(serverPort.equals("")) {
					lb_error.setText("���� ��Ʈ�� �Է��ϼ���.");
				}
				
				// ���� ��Ʈ�� �Է��ߴٸ� ���� ����
				else {
					String serverIP = lb_serverIP.getText();
					launchServer(serverIP, serverPort);
				}
			}
			
			// (Ŭ���̾�Ʈ) ���� ��ư�� ������ ���
			else if(e.getSource() == bt_client) {
				String client_serverIP = tf_client_serverIP.getText();
				String client_serverPort = tf_client_serverPort.getText();
				String nick = tf_nick.getText();
				
				// ���� IP, ���� ��Ʈ, ��ȭ���� �Է����� ������ ����
				if(client_serverIP.equals("")) {
					lb_error.setText("���� IP�� �Է��ϼ���.");
				}
				else if(client_serverPort.equals("")) {
					lb_error.setText("���� ��Ʈ�� �Է��ϼ���.");
				}
				else if(nick.equals("")) {
					lb_error.setText("��ȭ���� �Է��ϼ���.");
				}
				
				// ��� ���� �Է��ߴٸ�
				else {
					// ���� ���Ӱ��� ���θ� üũ
					boolean isReachable = serverConnect(client_serverIP, client_serverPort);
					
					// ������ ������ �����ϴٸ� Ŭ���̾�Ʈ ����
					if(isReachable) {
						launchClient(client_serverIP, client_serverPort, nick);
					}
					
					// ������ ������ �Ұ����ϴٸ� ����
					else {
						lb_error.setText("������ ����� �� �����ϴ�.");
					}
				}
			}
		}
	}
	
	// UI�� ����||Ŭ���̾�Ʈ ��忡 �°� �����ϴ� �޽��
	void setUI(String modeInput) {
		
		// ��尡 ����Ǿ��� ����, UI ����
		if(mode != modeInput) {
			
			// ���� ��� UI
			if(modeInput == "server") {
				p_main_bottom_in.removeAll(); // �ϴ� �гο� ����ִ� ������Ʈ���� ����
				
				p_main_bottom_in_in1 = new JPanel(new BorderLayout());
				{
					lb_serverIP_1 = new JLabel();
					lb_serverIP = new JLabel();
					lb_serverIP.setText(getLocalIP()); // ���� IP�� ����
				}
				p_main_bottom_in_in1.add(BorderLayout.WEST, lb_serverIP_1);
				p_main_bottom_in_in1.add(BorderLayout.CENTER, lb_serverIP);
				
				p_main_bottom_in_in2 = new JPanel(new BorderLayout());
				{
					lb_serverPort = new JLabel();
					tf_serverPort = new JTextField();
					tf_serverPort.setText("9000"); // �⺻ ��Ʈ�� 9000
				}
				p_main_bottom_in_in2.add(BorderLayout.WEST, lb_serverPort);
				p_main_bottom_in_in2.add(BorderLayout.CENTER, tf_serverPort);
				
				p_main_bottom_in_in3 = new JPanel(new BorderLayout());
				{
					bt_Server = new JButton("���� ����");
					bt_Server.addActionListener(new LoginListener());
				}
				p_main_bottom_in_in3.add(bt_Server);
				
				p_main_bottom_in.add(BorderLayout.NORTH, p_main_bottom_in_in1);
				p_main_bottom_in.add(BorderLayout.CENTER, p_main_bottom_in_in2);
				p_main_bottom_in.add(BorderLayout.SOUTH, p_main_bottom_in_in3);
				
				tf_serverPort.requestFocus(); // �ؽ�Ʈ�ʵ�-������Ʈ�� ��Ŀ���� ����
				p_main_bottom_in.validate(); // �ϴ� �г��� ��������� üũ
				
				lb_serverIP_1.setText("���� IP      ::  ");
				lb_serverPort.setText("���� ��Ʈ :: ");
			}
			
			// Ŭ���̾�Ʈ ��� UI
			else {
				p_main_bottom_in.removeAll(); // �ϴ� �гο� ����ִ� ������Ʈ���� ����
				
				p_main_bottom_in_in1 = new JPanel(new BorderLayout());
				{
					lb_client_serverIP = new JLabel();
					tf_client_serverIP = new JTextField(10);
					tf_client_serverIP.setText(getLocalIP()); // ���� IP�� ����
					tf_client_serverPort = new JTextField(4);
					tf_client_serverPort.setText("9000"); // �⺻ ��Ʈ�� 9000
				}
				p_main_bottom_in_in1.add(BorderLayout.WEST, lb_client_serverIP);
				p_main_bottom_in_in1.add(BorderLayout.CENTER, tf_client_serverIP);
				p_main_bottom_in_in1.add(BorderLayout.EAST, tf_client_serverPort);
				
				p_main_bottom_in_in2 = new JPanel(new BorderLayout());
				{
					lb_nick = new JLabel();
					tf_nick = new JTextField(5);
					bt_client = new JButton("����");
					bt_client.addActionListener(new LoginListener());
				}
				p_main_bottom_in_in2.add(BorderLayout.WEST, lb_nick);
				p_main_bottom_in_in2.add(BorderLayout.CENTER, tf_nick);
				p_main_bottom_in_in2.add(BorderLayout.EAST, bt_client);
				
				p_main_bottom_in.add(BorderLayout.NORTH, p_main_bottom_in_in1);
				p_main_bottom_in.add(BorderLayout.CENTER, p_main_bottom_in_in2);
				
				tf_nick.requestFocus(); // �ؽ�Ʈ�ʵ�-��ȭ�� ��Ŀ���z ����
				p_main_bottom_in.validate(); // �ϴ� �г��� ��������� üũ
				
				lb_client_serverIP.setText("���� IP / ��Ʈ :: ");
				lb_nick.setText("���� ID :: ");
			}
		}
	}
	
	// ��ǻ���� IP�� ��ȯ�ϴ� �޽��
	String getLocalIP() { 
		String LocalIP = null;
		
		// ���� IP�� �޾ƿ�
		try {
			LocalIP = InetAddress.getLocalHost().getHostAddress();
		}
		
		// ���ܰ� �߻����� ���, 127.0.0.1 IP�� ��ȯ
		catch(UnknownHostException e) {
			lb_error.setText("Local IP �ּҸ� �� �� �����ϴ�.");
			LocalIP = "127.0.0.1";
			e.printStackTrace();
		}
		catch(Exception e) {
			lb_error.setText("Local IP �ּҸ� ������ �� ������ �߻��Ͽ����ϴ�.");
			LocalIP = "127.0.0.1";
			e.printStackTrace();
		}
		
		return LocalIP;
	}
	
	// ������ ����Ǵ��� �˻��ϴ� �޽��
	boolean serverConnect(String serverIP, String serverPort_s) {
		int serverPort = Integer.parseInt(serverPort_s);
		
		// ���� IP�� ��Ʈ�� ���� ������ �õ��Ѵ�.
		try {
			clientSocket = new Socket(serverIP, serverPort);
			
			// ������ �Ǹ� true ����
			if(clientSocket != null) {
				return true;
			}
			
			// ������ ���� ������ false ����
			else {
				return false;
			}
			
		// ���ܰ� �߻��ϸ� false ����
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// ���������� ������ �����Ѵ�.
		finally {
			clientSocket = null;
		}
	}
	
	// ������ �����ϴ� �޽��
	void launchServer(String serverIP, String serverPort) {
		try {
			// Main �Լ��� ����ִ� Server Ŭ������ �����Ѵ�. �̶�, ���� IP�� ��Ʈ�� ���ڰ����� �Ѱ��ش�. 
			Runtime.getRuntime().exec("java Server " + serverIP + " " + serverPort);
			// ���μ����� ���� ���� ������
			// ��Ʈ���� ������ ������ �� �ְ�, ��Ƽ�ھ� CPU�� ����
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	// Ŭ���̾�Ʈ�� ���� �޽��
	void launchClient(String client_serverIP, String client_serverPort, String nick) {
		try {
			// Main �Լ��� ����ִ� Client Ŭ������ �����Ѵ�. �̶�, ���� IP�� ��Ʈ�� ���ڰ����� �Ѱ��ش�.
			Runtime.getRuntime().exec("java Client " + client_serverIP + " " + client_serverPort + " " + nick);
			// ���μ����� ���� ���� ������
			// ��Ʈ�� ��ȭ�� ���� Ŭ���̾�Ʈ�� ������ �� �ְ�, ��Ƽ�ھ� CPU�� ����
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}