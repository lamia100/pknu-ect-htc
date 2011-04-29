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
	
	// ������ - Ŭ���̾�Ʈ UI ����
	Client(String serverIP, String serverPort_s, String nick) {
		super(nick + " �� ä��");
		
		this.serverIP = serverIP;
		serverPort = Integer.parseInt(serverPort_s); // ���� ��Ʈ(String��)�� int������ ��ȯ
		this.nick = nick;
		
		// ������ ������ ������ �����ϱ� ���� ���̾�α� ����
		fd_selectFile = new FileDialog(this, "���� ���� ���� ����", FileDialog.LOAD);
		
		p_main = new JPanel(new BorderLayout());
		{
			p_main_top = new JPanel(new BorderLayout());
			{
				tf_savePathFile = new JTextField();
				bt_savePathFile = new JButton("���� ���� ����");
				
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
				
				// ��ũ�� �г�(���� ��ũ�� �׻� ǥ��, ���ν�ũ�� ǥ������ ����)�� �ؽ�Ʈ�ʵ带 ����
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
				
				bt_sendFile = new JButton("���� ����");
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
		tf_myMsg.requestFocus(); // �޼��� �Է�â�� ��Ŀ���� �̵�
		
		// Ŭ���̾�Ʈ ���� ���� ���θ� üũ
		boolean chatState = this.chatStart();
		
		// Ŭ���̾�Ʈ ������ �����ϸ�, ������ ����
		if(chatState) {
			ClientChat myClientChat = new ClientChat();
			myClientChat.start();
		}
		
		// Ŭ���̾�Ʈ ������ �Ұ����ϸ�, ���� �޼����� ���
		else {
			dspMsg("[Error] �������� ���ῡ ������ �ֽ��ϴ�. ��α��� �ϼ���.");
		}
	}
	
	public static void main(String[] args) {
		Client myClient = new Client(args[0], args[1], args[2]);
		
		//â�� x ��ư�� ������ ���α׷��� �����Ѵ�.
		myClient.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myClient.setSize(500, 300);
		myClient.setVisible(true);
		myClient.setLocation(new Point(600, 200));
		myClient.setResizable(true);
	}
	
	// Ŭ���̾�Ʈ�� �����ϴ� �޽��
	boolean chatStart() {
		
		// Ŭ���̾�Ʈ ������ �����ϸ�, ��Ʈ���� �����ϰ� true�� ����
		try {
			Socket clientSocket = new Socket(serverIP, serverPort);
			bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			return true;
		}
		
		// ���ܰ� �߻��ϸ�, ��Ʈ���� �����ϰ� false�� ����
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
	
	// ä���� �����ϴ� Ŭ����(inner)
	class ClientChat extends Thread {
		public void run() {
			
			// ��ȭ���� ������ ����
			try {
				printWriter.println(nick);
				printWriter.flush();
			}
			
			// ���ܰ� �߻��ϸ�, ���� �޼����� ���
			catch(Exception e) {
				dspMsg("[Error] �޼����� �������� ���Ͽ����ϴ�.");
				e.printStackTrace();
			}
			
			try {
				// ������ ������ �����ǰ� ������, �޼����� ������ ���� 
				while(receiveState == true) {
					String serverMsg = null;
					while((serverMsg = bufferedReader.readLine()) != null) {
						dspMsg(serverMsg);
					}
				}
			}
			
			// ���ܰ� �߻��ϸ�, ���� �޼����� ���� �������� ������ ����ٴ� ���� üũ
			catch(Exception e) {
				dspMsg("[Error] �������� ������ ���������ϴ�.");
				receiveState = false;
				e.printStackTrace();
			}
		}
	}
	
	// �޼����� ������ �޽��
	void sendMSG(String sendMessage) {
		
		// [��ȭ��] �޼����� ������ ����
		try {
			printWriter.println("[" + nick + "] " + sendMessage);
			printWriter.flush();
		}
		
		// ���ܰ� �߻��ϸ�, ���� �޼����� ���
		catch(Exception e) {
			dspMsg("[Error] �޼����� �������� ���Ͽ����ϴ�.");
			e.printStackTrace();
		}
		
		// ���������� �޼��� �Է�â�� ����
		finally {
			tf_myMsg.setText("");
		}
	}
	
	// �޼����� ���� �޽��
	void dspMsg(String inputMessage) {
		sb_allMsg.setValue(sb_allMsg.getMaximum());
		ta_allMsg.append(inputMessage + "\n");
	}
	
	class SendListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			// ����Ű�� �Է����� �� �޼��� �Է�â�� ���������, �Է�â�� �ִ� ���ڿ��� �޼����� ���� 
			if(e.getKeyCode() == KeyEvent.VK_ENTER && !(tf_myMsg.getText()).equals("") && tf_myMsg.getText() != "") {
				sendMSG(tf_myMsg.getText());
			}
		}
	}
	
	class FocusListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			// �޼��� �Է�â�� ��Ŀ���� �̵�
			tf_myMsg.requestFocus();
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		// ���� ���� ���� ��ư�� Ŭ���ߴٸ�
		if(e.getSource()== bt_savePathFile) {
			
			// ������ ������ ������ �����ϱ� ���� ���̾�α׸� ���
			fd_selectFile.setVisible(true);
			
			// ������ ������ ǥ��
			savePathFile = fd_selectFile.getDirectory();
			tf_savePathFile.setText(savePathFile);
			
			try {
				// Main �Լ��� ����ִ� Client_receiveFile Ŭ������ �����Ѵ�. �̶�, ���� IP�� ��Ʈ, ���� ������ ���ڰ����� �Ѱ��ش�.
				Runtime.getRuntime().exec("java Client_receiveFile " + serverIP + " " + serverPort + " " + savePathFile);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// ���� ���� ��ư�� Ŭ���ߴٸ�
		else if(e.getSource()== bt_sendFile) {
			try {
				// Main �Լ��� ����ִ� Client_sendFile Ŭ������ �����Ѵ�. �̶�, ���� IP�� ��Ʈ�� ���ڰ����� �Ѱ��ش�.
				Runtime.getRuntime().exec("java Client_sendFile " + serverIP + " " + serverPort);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}