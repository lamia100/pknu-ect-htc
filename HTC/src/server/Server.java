package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

public class Server implements Runnable {
	public static final int port = 30000;
	public TreeSet<User> users=null;
	public Queue <Message> messageQ=null;
	private ServerSocket sSocket;
	
	private Server()
	{
		try {
			sSocket = new ServerSocket(port);
			users=new TreeSet<User>();
			messageQ=new LinkedList<Message>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("채팅서버 시작..." + sSocket.getLocalPort());
			while (true) {
				Socket socket = sSocket.accept();
				System.out.println("입장 : " + socket);
				User user = new User(socket);
				new Thread(user).start();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args) {
		new Thread(new Server()).start();
	}

}
