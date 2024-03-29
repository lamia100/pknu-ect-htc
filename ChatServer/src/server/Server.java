package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import util.Definition;
import util.msg.Message;

/**
 * 
 * @author "김성현"
 * 
 */
@SuppressWarnings("unused")
public class Server implements Runnable {
	private static ServerSocket sSocket;
	private static Server server = null;
	private static MessageProcessor messageProcessor = null;

	static {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void initialize() throws IOException {
		server = new Server();
		sSocket = new ServerSocket(Definition.DEFAULT_PORT);
		messageProcessor = new MessageProcessor();
		new Thread(messageProcessor).start();
		User.setMessageProcessor(messageProcessor);
		Channel.setMessageProcessor(messageProcessor);
	}

	public static Server getServer() {
		if (server == null)
			throw new NullPointerException("서버가 없다");
		return server;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			log("채팅서버 시작..." + sSocket.getLocalPort());
			while (true) {
				Socket socket = sSocket.accept();
				log("입장 : " + socket);
				User user = new User(socket);
				new Thread(user).start();
				//sSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getIP() {
		// TODO Auto-generated method stub
		return sSocket.getInetAddress().getHostAddress().toString();
	}

	private void log(Object... logs) {
		System.out.println("Server");
		for (Object log : logs)
			System.out.println(log);
		System.out.println("----------------------------");
	}

	public static void main(String[] args) {
		new Thread(Server.server).start();
	}

}
