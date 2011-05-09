package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import util.msg.Message;

@SuppressWarnings("unused")
public class Server implements Runnable {
	public static final int port = 30000;
	private final TreeSet<User> users;
	private final Queue<Message> messageQ;
	private final ServerSocket sSocket;
	private static Server server = null;
	
	
	
	private Server() throws IOException {
		sSocket = new ServerSocket(port);
		users = new TreeSet<User>();
		messageQ = new LinkedList<Message>();
	}

	public static Server getServer() {
		if(server==null)
			throw new NullPointerException("서버가 없다");
		return server;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("채팅서버 시작..." + sSocket.getLocalPort());
			while (true) {
				Socket socket = sSocket.accept();
				System.out.println("입장 : " + socket);
				new Thread(new User(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args) {

		Server s=null;
		try {
			s=new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		new Thread(s).start();
	}

}
