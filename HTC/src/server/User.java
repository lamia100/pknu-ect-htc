package server;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class User implements Comparable<User>, Runnable {
	// ���� �ʿ���
	@SuppressWarnings("unused")
	private Socket socket = null;
	private String name = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	public User(Socket socket) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialize() {
		/*
		 * �г��� ������ �ʱ�ȭ
		 */
		
	}
	
	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		
		return name.compareToIgnoreCase(o.name);
	}
	
	public synchronized void send(Message message) {
		ArrayList<String> lines = message.getMessages();
		try {
			for (String str : lines) {
				out.println(str);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// �ʱ�ȭ
		initialize();
		// ���� ����
		String line = "";
		Message message = new Message();
		try {
			while ((line = in.readLine()) != null) {
				if (message.parse(line)) {
					// server �� messageQ�� message�� add
					message = new Message();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
