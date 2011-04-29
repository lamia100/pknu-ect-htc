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
	
	// ������ - ���� ���� UI ���� 
	Client_sendFile(String title, String serverIP, String serverPort_s) {
		super(title);
		
		this.serverIP = serverIP;
		
		// ���� ��Ʈ(String��)�� int������ ��ȯ�� ��, 1�� ����
		// ���� ä�� ������ ���� ������ ��Ʈ�� �޸� ����ȴ�. 
		serverPort = Integer.parseInt(serverPort_s) + 1;
		
		// ������ �����ϱ� ���� ���̾�α� ����
		fd_selectFile = new FileDialog(this, "���ϼ���", FileDialog.LOAD);
		
		p_main = new JPanel(new BorderLayout());
		{
			tf_pathFile = new JTextField();
			tf_pathFile.setEditable(false); // �ؽ�Ʈ�ʵ�-���ϰ�δ� �ؽ�Ʈ�� ���� �Ұ�
			tf_pathFile.setBackground(Color.WHITE);
			
			p_main_center = new JPanel(new BorderLayout());
			{
				bt_selectFile = new JButton("���� ����");
				bt_sendFile = new JButton("���� ����");
				
				bt_selectFile.addActionListener(this);
				bt_sendFile.addActionListener(this);
			}
			p_main_center.add(BorderLayout.WEST, bt_selectFile);
			p_main_center.add(BorderLayout.CENTER, bt_sendFile);
			
			p_main_bottom = new JPanel(new BorderLayout());
			{
				lb_error = new JLabel();
				lb_error.setText("������ ������ �����ϼ���.");
			}
			p_main_bottom.add(lb_error);
		}
		p_main.add(BorderLayout.NORTH, tf_pathFile);
		p_main.add(BorderLayout.CENTER, p_main_center);
		p_main.add(BorderLayout.SOUTH, p_main_bottom);
		
		getContentPane().add(p_main);
	}
	
	public static void main(String[] args) {
		Client_sendFile myClient_sendFile = new Client_sendFile("���� ����", args[0], args[1]);
		
		//â�� x ��ư�� ������ ���α׷��� �����Ѵ�.
		myClient_sendFile.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myClient_sendFile.setSize(300, 100);
		myClient_sendFile.setVisible(true);
		myClient_sendFile.setLocation(new Point(300, 300));
		myClient_sendFile.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		// ���� ���� ��ư�� �����ٸ�
		if(e.getSource() == bt_selectFile) {
			// ������ �����ϱ� ���� ���̾�α׸� ���
			fd_selectFile.setVisible(true);
			
			pathFile = fd_selectFile.getDirectory(); // ������ ������ ���丮�� ����
			nameFile = fd_selectFile.getFile(); // ������ ������ �̸��� ����
			
			tf_pathFile.setText(pathFile + nameFile); // �ؽ�Ʈ�ʵ�-���ϰ�ο� ���ϰ�θ� ǥ��
		}
		
		// ���� ���� ��ư�� �����ٸ�
		else if(e.getSource() == bt_sendFile) {
			// ������ �������� �ʾҴٸ�, ���� �޼��� ǥ��
			if(tf_pathFile.getText().equals("")) {
				lb_error.setText("������ ������ �����ؾ� �մϴ�.");
			}
			// ���� �����ߴٸ�, ���� ���� ����
			else {
				sendStart();
			}
		}
	}
	
	// ���� ������ �����ϴ� �޽��
	void sendStart() {
		Socket sendFileSocket;
		
		bt_sendFile.setText("���� ���� ��");
		
		
		// ���ϻ��� �� ���� �̸��� ���� ���� ����
		try {
			sendFileSocket = new Socket(serverIP, serverPort);
			sendNameFile(sendFileSocket);
			sendFile(sendFileSocket);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// ���� �̸��� ������ �޽��
	void sendNameFile(Socket sendFileSocket) {
		BufferedWriter bw;
		
		// ��Ʈ�� ���� �� ���� �̸� ����
		try {
			bw = new BufferedWriter(new OutputStreamWriter(sendFileSocket.getOutputStream()));
			bw.write(nameFile);
			bw.flush();
			bw.close();
		}
		
		// ���ܰ� �߻��ϸ�, ���� �޼����� ���
		catch(Exception e) {
			lb_error.setText("���� ���ۿ� ������ �߻��Ͽ����ϴ�.");
			e.printStackTrace();
		}
	}
	
	// ���� ���� ������ ���� �޽��
	void sendFile(Socket sendFileSocket) {
		DataInputStream dis;
		DataOutputStream dos;
		
		try {
			// ���� ������ ���� ��Ʈ�� ����
			dis = new DataInputStream(new FileInputStream(new File(tf_pathFile.getText())));
			dos = new DataOutputStream(sendFileSocket.getOutputStream());
			
			// ����Ʈ������ �о ��Ʈ������ ������
			int i = 0;
			while((i = dis.read()) != -1 ) {
				dos.writeByte(i);
				dos.flush();
			}
			dis.close();
			dos.close();
		}
		catch(Exception e) {
			lb_error.setText("���� ���ۿ� ������ �߻��Ͽ����ϴ�.");
			e.printStackTrace();
		}
	}
}