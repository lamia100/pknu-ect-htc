import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server_fileServer extends JFrame {
	JPanel p_logFile;
	JPanel p_clientListFile;
	
	JTextArea ta_logFile;
	JScrollPane sp_logFile;
	JScrollBar sb_logFile;
	
	JTextArea ta_clientListFile;
	JScrollPane sp_clientListFile;
	JScrollBar sb_clientListFile;
	
	ServerSocket serverSocketFile;
	Socket clientSocketFile;
	String serverIP;
	int serverPortFile;
	
	Vector<Object> clientListFile;
	Vector<String> clientIPListFile;
	
	// ������ - ���� ���� UI ����
	Server_fileServer(String serverIP, String serverPort_s) { // ������
		super("���� ���� IP :: " + serverIP + " / ���� ���� ��Ʈ :: " + serverPort_s);
		
		this.serverIP = serverIP;
		
		// ���� ��Ʈ(String��)�� int������ ��ȯ�� ��, 1�� ����
		// ���� ä�� ������ ���� ������ ��Ʈ�� �޸� ����ȴ�. 
		serverPortFile = Integer.parseInt(serverPort_s) + 1; 
		
		// Ŭ���̾�Ʈ ����Ʈ�� �����ϴ� ����
		clientListFile = new Vector<Object>();
		clientIPListFile = new Vector<String>();
		
		// �α׸� ǥ���ϴ� �г�
		p_logFile = new JPanel(new BorderLayout());
		{
			ta_logFile = new JTextArea();
			ta_logFile.setEditable(false);
			ta_logFile.setLineWrap(true);
			ta_logFile.setBackground(Color.black);
			ta_logFile.setForeground(Color.white);
			ta_logFile.setMargin(new Insets(5, 5, 5, 5));
			
			// ��ũ�� �г�(���� ��ũ�� �׻� ǥ��, ���ν�ũ�� ǥ������ ����)�� �ؽ�Ʈ�ʵ带 ����
			sp_logFile = new JScrollPane(ta_logFile);
			sp_logFile.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_logFile.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_logFile = sp_logFile.getVerticalScrollBar();
		}
		p_logFile.add(BorderLayout.CENTER, sp_logFile);
		
		// Ŭ���̾�Ʈ ����Ʈ�� ǥ���ϴ� �г�
		p_clientListFile = new JPanel(new BorderLayout());
		{
			ta_clientListFile = new JTextArea();
			ta_clientListFile.setEditable(false);
			ta_clientListFile.setLineWrap(true);
			ta_clientListFile.setBackground(Color.black);
			ta_clientListFile.setForeground(Color.white);
			ta_clientListFile.setMargin(new Insets(5, 5, 5, 5));
			
			// ��ũ�� �г�(���� ��ũ�� �׻� ǥ��, ���ν�ũ�� ǥ������ ����)�� �ؽ�Ʈ�ʵ带 ����
			sp_clientListFile = new JScrollPane(ta_clientListFile);
			sp_clientListFile.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_clientListFile.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_clientListFile = sp_clientListFile.getVerticalScrollBar();
		}
		p_clientListFile.add(BorderLayout.CENTER, sp_clientListFile);
		
		getContentPane().add(BorderLayout.CENTER, p_logFile);
		getContentPane().add(BorderLayout.EAST, p_clientListFile);
	}
	
	public static void main(String[] args) { // ����
		Server_fileServer myServer_fileServer = new Server_fileServer(args[0], args[1]);
		
		//â�� x ��ư�� ������ ���α׷��� �����Ѵ�.
		myServer_fileServer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myServer_fileServer.setSize(500, 300);
		myServer_fileServer.setLocation(new Point(500, 0));
		myServer_fileServer.setVisible(true);
		
		// ���� ���� ���� ���� ���θ� üũ
		boolean serverState = myServer_fileServer.fileServerStart();
		
		// ���� ���� ������ �����ϸ�, Ŭ���̾�Ʈ ������ ��ٸ�
		if(serverState) {
			myServer_fileServer.fileServerWait();
		}
		
		// ���� ���� ������ �Ұ����ϸ�, �α׸� ���
		else {
			myServer_fileServer.dspLogFile("[Error] ���� ���� ���ۿ� �����Ͽ����ϴ�.");
		}
	}
	
	// ���� ������ �����ϴ� �޽��
	boolean fileServerStart() {
		
		// ���� ���� ������ �����ϸ�, �α׸� ���� true�� ����
		try {
			serverSocketFile = new ServerSocket(serverPortFile);
			
			dspLogFile("[Success] ���� ������ ���۵Ǿ����ϴ�.");
			return true;
		}
		
		// ���� ���� ������ �Ұ����ϸ�, �α׸� ���� fasle�� ���� 
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return false;
		}
	}

	// Ŭ���̾�Ʈ�� ������ ��ٸ��� �޽��
	void fileServerWait() {
		while(true) {
			try {
				// Ŭ���̾�Ʈ�� ���ӵ� ������ ��ٸ���.
				clientSocketFile = serverSocketFile.accept();
				
				// Ŭ���̾�Ʈ�� �����ϸ�, Ŭ���̾�Ʈ���� �߰� �����带 �����Ѵ�.
				FileServerRelay myFileServerRelay = new FileServerRelay(clientSocketFile);
				myFileServerRelay.start();
				
				// ����Ʈ�� �ش� Ŭ���̾�Ʈ<Object>�� ���Ϳ� �߰�
				clientListFile.addElement(myFileServerRelay);
			}
			
			// ���ܰ� �߻��ϸ� �α׸� ����.
			catch(Exception e) {
				System.out.println("[Error] ���� ���� ������ �����Ͽ����ϴ�.");
				e.printStackTrace();
			}
		}
	}
	
	// Ŭ���̾�Ʈ�� �߰��ϴ� Ŭ����(inner)
	class FileServerRelay extends Thread {
		Socket clientSocketFile;
		BufferedReader bufferedReader;
		PrintWriter printWriter;
		DataInputStream dis;
		DataOutputStream dos;
		
		String clientIP;
		String clientNameFile;
		
		// ������
		FileServerRelay(Socket clientSocketFile) {
			this.clientSocketFile = clientSocketFile;
			
			// ������ �ش� Ŭ���̾�Ʈ�� IP�� �����Ѵ�.
			clientIP = clientSocketFile.getInetAddress().getHostAddress();
		}
		
		// Ŭ���̾�Ʈ�� �߰踦 ���� 
		public void run() {
			try {
				// �ش� Ŭ���̾�Ʈ���� ��Ʈ���� ����
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocketFile.getInputStream()));
				printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocketFile.getOutputStream())));
				
				// �ش� Ŭ���̾�Ʈ�� ������ ���ϸ��� ����
				clientNameFile = bufferedReader.readLine();
				
				// �ش� Ŭ���̾�Ʈ�� ������ �����ٴ� ����, �α׿� ǥ��
				dspLogFile("[Info] " + " (" + clientIP + ") �� " + clientNameFile + " �� �����ϴ�.");
				
				// �ش� Ŭ���̾�Ʈ�� IP�� ���Ϳ� �߰��� ��, ����Ʈ ǥ��
				clientIPListFile.addElement(clientIP);
				dspClientListFile();
				
				
				try {
					// �ش� Ŭ���̾�Ʈ���� ���Ͽ� ��Ʈ���� ����
					dis = new DataInputStream(clientSocketFile.getInputStream());
					dos = new DataOutputStream(clientSocketFile.getOutputStream());
					
					// �ش� Ŭ���̾�Ʈ�� ������ ������ �� Ŭ���̾�Ʈ���� ����
					int i = 0;
					while((i = dis.read()) != -1 ){
						broadCastFile(i);
					}
					
					dspLogFile("[Info] " + " (" + clientIP + ") �� " + clientNameFile + " �� ���������� �����Ͽ����ϴ�.");
				}
				
				// ���ܰ� �߻��ϸ�, �ش� Ŭ���̾�Ʈ���� ���Ͽ� ��Ʈ���� ����
				catch(Exception e) {
					if(dis != null) {
						try {
							dis.close();
						}
						catch(Exception ex) {
							e.printStackTrace();
						}
					}
					if(dos != null) {
						try {
							dos.close();
						}
						catch(Exception ex) {
							e.printStackTrace();
						}
					}
				}
			}
			
			// ���ܰ� �߻��ϸ�, �ش� Ŭ���̾�Ʈ�� ������ �������ٴ� ���� �ǹ�
			catch (Exception e) {
				// �ش� Ŭ���̾�Ʈ�� ������ �������ٴ� ����, �α׿� ǥ��
				dspLogFile("[Info] " + " (" + clientIP + ") �� ������ �������ϴ�.");
				
				// �ش� Ŭ���̾�Ʈ�� IP�� ���Ϳ��� ����
				clientIPListFile.removeElement(clientIP);
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
				if(clientSocketFile != null) {
					try {
						clientSocketFile.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// �ٸ� Ŭ���̾�Ʈ���� ������ ������ �޽��
		void sendFile(int clientFile) {
			try {
				dos.writeByte(clientFile);
				dos.flush();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ��� Ŭ���̾�Ʈ���� ������ ������ ���� �޽��
	void broadCastFile(int serverFile) {
		// Enumeration �������̽��� Ŭ���̾�Ʈ ����Ʈ�� ����ִ� ���͸� �Ҵ�
		Enumeration<Object> container = clientListFile.elements();
		
		// Ŀ���� �����Ͱ� ���� ������ �ݺ�
		while(container.hasMoreElements()) {
			// ��ü�� �����ϰ�, Ŀ���� �ڷ� �̵�
			FileServerRelay myFileServerRelay = (FileServerRelay)container.nextElement();
			
			// �ش� Ŭ���̾�Ʈ���� ������ ����
			myFileServerRelay.sendFile(serverFile);
		}
	}
	
	// �α׸� ���� �޽��
	void dspLogFile(String MSG) {
		// ��ũ�ѹ��� �ǳ����� �̵��� ��, �α׸� ǥ���Ѵ�.
		sb_logFile.setValue(sb_logFile.getMaximum());
		ta_logFile.append(MSG + "\n");
	}
	
	// ������ ����� ���� �޽��
	void dspClientListFile() {
		ta_clientListFile.setText("������ ��� :");
		
		// Ŭ���̾�Ʈ�� �������� �ݺ�
		for(int i=0 ; i<clientIPListFile.size(); i++ ) {
			// Ŭ���̾�Ʈ IP�� ǥ��
			ta_clientListFile.append("\n" + " (" + clientIPListFile.get(i) + ")");
		}
	}
}