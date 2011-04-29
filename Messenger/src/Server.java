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
	
	// ������ - ���� UI ����
	Server(String serverIP, String serverPort_s) {
		super("���� IP :: " + serverIP + " / ���� ��Ʈ :: " + serverPort_s);
		
		this.serverIP = serverIP; 
		serverPort = Integer.parseInt(serverPort_s); // ���� ��Ʈ(String��)�� int������ ��ȯ
		
		// Ŭ���̾�Ʈ ����Ʈ�� �����ϴ� ����
		clientList = new Vector<Object>();
		clientIPList = new Vector<String>();
		clientNickList = new Vector<String>();
		
		// �α׸� ǥ���ϴ� �г�
		p_log = new JPanel(new BorderLayout());
		{
			ta_log = new JTextArea();
			ta_log.setEditable(false);
			ta_log.setLineWrap(true);
			ta_log.setBackground(Color.black);
			ta_log.setForeground(Color.white);
			ta_log.setMargin(new Insets(5, 5, 5, 5));
			
			// ��ũ�� �г�(���� ��ũ�� �׻� ǥ��, ���ν�ũ�� ǥ������ ����)�� �ؽ�Ʈ�ʵ带 ����
			sp_log = new JScrollPane(ta_log);
			sp_log.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_log.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_log = sp_log.getVerticalScrollBar();
		}
		p_log.add(BorderLayout.CENTER, sp_log);
		
		// Ŭ���̾�Ʈ ����Ʈ�� ǥ���ϴ� �г�
		p_clientList = new JPanel(new BorderLayout());
		{
			ta_clientList = new JTextArea();
			ta_clientList.setEditable(false);
			ta_clientList.setLineWrap(true);
			ta_clientList.setBackground(Color.black);
			ta_clientList.setForeground(Color.white);
			ta_clientList.setMargin(new Insets(5, 5, 5, 5));
			
			// ��ũ�� �г�(���� ��ũ�� �׻� ǥ��, ���ν�ũ�� ǥ������ ����)�� �ؽ�Ʈ�ʵ带 ����
			sp_clientList = new JScrollPane(ta_clientList);
			sp_clientList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_clientList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_clientList = sp_clientList.getVerticalScrollBar();
			
			bt_fileServer = new JButton("���� ���� ����");
			bt_fileServer.addActionListener(this);
		}
		p_clientList.add(BorderLayout.CENTER, sp_clientList);
		p_clientList.add(BorderLayout.SOUTH, bt_fileServer);
		
		getContentPane().add(BorderLayout.CENTER, p_log);
		getContentPane().add(BorderLayout.EAST, p_clientList);
	}
	
	public static void main(String[] args) { // ����		
		Server myServer = new Server(args[0], args[1]);
		
		//â�� x ��ư�� ������ ���α׷��� �����Ѵ�.
		myServer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myServer.setSize(500, 300);
		myServer.setVisible(true);
		
		// ���� ���� ���� ���θ� üũ
		boolean serverState = myServer.serverStart();
		
		// ���� ������ �����ϸ�, Ŭ���̾�Ʈ ������ ��ٸ�
		if(serverState) {
			myServer.serverWait();
		}
		
		// ���� ������ �Ұ����ϸ�, �α׸� ���
		else {
			myServer.dspLog("[Error] ���� ���ۿ� �����Ͽ����ϴ�.");
		}
	}
	
	// ������ �����ϴ� �޽��
	boolean serverStart() {
		
		// ���� ������ �����ϸ�, �α׸� ���� true�� ����
		try {
			serverSocket = new ServerSocket(serverPort);
			dspLog("[Success] ������ ���۵Ǿ����ϴ�.");
			return true;
		}
		
		// ���� ������ �Ұ����ϸ�, �α׸� ���� fasle�� ���� 
		catch(Exception e) {
			dspLog("[Error] ���� ���ۿ� �����Ͽ����ϴ�.");
			e.printStackTrace();
			return false;
		}
	}

	// Ŭ���̾�Ʈ�� ������ ��ٸ��� �޽��
	void serverWait() {
		while(true) {
			try {
				// Ŭ���̾�Ʈ�� ���ӵ� ������ ��ٸ���.
				clientSocket = serverSocket.accept();
				
				// Ŭ���̾�Ʈ�� �����ϸ�, Ŭ���̾�Ʈ���� �߰� �����带 �����Ѵ�.
				ServerRelay myServerRelay = new ServerRelay(clientSocket);
				myServerRelay.start();
				
				// ����Ʈ�� �ش� Ŭ���̾�Ʈ<Object>�� ���Ϳ� �߰�
				clientList.addElement(myServerRelay);
			}
			
			// ���ܰ� �߻��ϸ� �α׸� ����.
			catch(Exception e) {
				dspLog("[Error] ���� ���� ������ �����Ͽ����ϴ�.");
				e.printStackTrace();
			}
		}
	}
	
	// Ŭ���̾�Ʈ�� �߰��ϴ� Ŭ����(inner)
	class ServerRelay extends Thread {
		Socket clientSocket;
		BufferedReader bufferedReader;
		PrintWriter printWriter;
				
		String clientIP;
		String clientNick;
		
		// ������
		ServerRelay(Socket clientSocket) {
			this.clientSocket = clientSocket;
			
			// ������ �ش� Ŭ���̾�Ʈ�� IP�� �����Ѵ�.
			clientIP = clientSocket.getInetAddress().getHostAddress();
		}
		
		// Ŭ���̾�Ʈ�� �߰踦 ���� 
		public void run() {
			try {
				// �ش� Ŭ���̾�Ʈ���� ��Ʈ���� ����
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
				
				// �ش� Ŭ���̾�Ʈ�� ��ȭ���� ����
				clientNick = bufferedReader.readLine();
				
				// �ش� Ŭ���̾�Ʈ�� �����ߴٴ� ����, �α׿� �� Ŭ���̾�Ʈ���� ����
				dspLog("[Info] " + clientNick + " (" + clientIP + ") �� �����Ͽ����ϴ�.");
				broadCast("[Info] " + clientNick + " (" + clientIP + ") ���� ��ȭ�� �����Ͽ����ϴ�.");
				
				// �ش� Ŭ���̾�Ʈ�� IP�� ��ȭ���� ���Ϳ� �߰��� ��, ����Ʈ ǥ��
				clientIPList.addElement(clientIP);
				clientNickList.addElement(clientNick);
				dspClientList();
				
				// �ش� Ŭ���̾�Ʈ�� ������ �޼����� �� Ŭ���̾�Ʈ���� ����
				String serverMsg = null;
				while((serverMsg = bufferedReader.readLine())!= null) {
					broadCast(serverMsg);
				}
			}
			
			// ���ܰ� �߻��ϸ�, �ش� Ŭ���̾�Ʈ�� ������ �������ٴ� ���� �ǹ�
			catch(Exception e) {
				// �ش� Ŭ���̾�Ʈ�� ������ �������ٴ� ����, �α׿� �� Ŭ���̾�Ʈ���� ����
				dspLog("[Info] " + clientNick + " (" + clientIP + ") �� ������ �������ϴ�.");
				broadCast("[Info] " + clientNick + " (" + clientIP + ") ���� �����ϼ̽��ϴ�.");
				
				// �ش� Ŭ���̾�Ʈ�� IP�� ��ȭ���� ���Ϳ��� ����
				clientIPList.removeElement(clientIP);
				clientNickList.removeElement(clientNick);
				dspClientList();
			}
			
			// ���������� �ش� Ŭ���̾�Ʈ���� ��Ʈ���� ������ ���� 
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
		
		// �ٸ� Ŭ���̾�Ʈ���� �޼����� ������ �޽��
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

	// ��� Ŭ���̾�Ʈ���� �޼����� �����ϱ� ���� �޽��
	void broadCast(String serverMsg) {
		// Enumeration �������̽��� Ŭ���̾�Ʈ ����Ʈ�� ����ִ� ���͸� �Ҵ�
		Enumeration<Object> container = clientList.elements();
		
		// Ŀ���� �����Ͱ� ���� ������ �ݺ�
		while(container.hasMoreElements()) {
			// ��ü�� �����ϰ�, Ŀ���� �ڷ� �̵�
			ServerRelay myServerRelay = (ServerRelay)container.nextElement();
			
			// �ش� Ŭ���̾�Ʈ���� �޼����� ����
			myServerRelay.sendMSG(serverMsg);
		}
	}
	
	// �α׸� ���� �޽��
	void dspLog(String MSG) {
		// ��ũ�ѹ��� �ǳ����� �̵��� ��, �α׸� ǥ���Ѵ�.
		sb_log.setValue(sb_log.getMaximum()); 
		ta_log.append(MSG + "\n");
	}
	
	// ������ ����� ���� �޽��
	void dspClientList() {
		ta_clientList.setText("������ ��� :");
		
		// Ŭ���̾�Ʈ�� �������� �ݺ�
		for ( int i = 0 ; i < clientNickList.size() ; i++ ) {
			// Ŭ���̾�Ʈ ��ȭ��(IP)�� ǥ��
			ta_clientList.append("\n" + clientNickList.get(i) + " (" + clientIPList.get(i) + ")");
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			// Main �Լ��� ����ִ� Client Ŭ������ �����Ѵ�. �̶�, ���� IP�� ��Ʈ�� ���ڰ����� �Ѱ��ش�.
			Runtime.getRuntime().exec("java Server_fileServer " + serverIP + " " + serverPort);
			// ���μ����� ���� ���� ������
			// ���� ������ ���������� �۵��Ǹ�, ��Ƽ�ھ� CPU�� ����
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}