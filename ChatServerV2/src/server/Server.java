package server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import msg.Definition;
import static util.Logger.*;
import util.User;

/**
 * 
 * @author "김성현"
 * 
 */
public class Server implements Runnable {
	private static ServerSocket sSocket;
	private static Server server = null;
	private static String ip;

	//public static final Manager manager;
	static {
		try {
			server = new Server();
			sSocket = new ServerSocket(Definition.DEFAULT_PORT);
			ip = InetAddress.getLocalHost().getHostAddress();
			new Thread(ServerManager.getInstance()).start();
		} catch (UnknownHostException e) {
			log("Server",e);
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

	}

	@Override
	public void run() {
		try {
			log("Server","채팅서버 시작... " + getIP() + " : " + sSocket.getLocalPort());
			while (true) {
				Socket socket = sSocket.accept();
				log("입장 : " + socket);
				User user = new User(socket);
				new Thread(user).start();
				//sSocket.close();
			}
		} catch (IOException e) {
			log("Server",e);
		}
	}

	public static String getIP() {
		return ip;
	}



	public static void main(String[] args) {
		new Thread(Server.server).start();
	}

}
