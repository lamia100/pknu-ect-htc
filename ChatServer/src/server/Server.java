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
	public static final int port = Definition.DEFAULT_PORT;
	private static ServerSocket sSocket;
	private static Server server = null;
	private MessageProcessor messageProcessor=null;
	
	
	private Server() throws IOException{
		initialize();
	}
	private void initialize() throws IOException
	{
		sSocket = new ServerSocket(Definition.DEFAULT_PORT);
		messageProcessor=new MessageProcessor();
		new Thread(messageProcessor).start();
		User.setMessageProcessor(messageProcessor);
		Channel.setMessageProcessor(messageProcessor);
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
				User user=new User(socket);
				new Thread(user).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

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
	public static String getIP() {
		// TODO Auto-generated method stub
		return sSocket.getInetAddress().getHostAddress().toString();
	}

}
