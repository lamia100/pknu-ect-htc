package client;

import static util.Logger.log;

import gui.ClientGUI;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import msg.Definition;
import msg.Message;
import msg.sub.Join;
import util.User;

public class Client {

	String name;
	String serverIP;
	private static ClientGUI clientGUI;
	ClientManager manager;
	public Client(String name, String serverIP,ClientGUI clientGUI) {
		this.name = name;
		this.serverIP = serverIP;
		Client.clientGUI=clientGUI;
		Socket socket;
		try {
			socket = new Socket(serverIP, Definition.DEFAULT_PORT);
			User user =new User(socket);
			new Thread(user).start();
			log("Client","서버에 접속성공" + user.getIP() + ":" + socket.getPort());
			Client.clientGUI.append("Client");
			Client.clientGUI.append("서버에 접속성공" + user.getIP() + ":" + socket.getPort());
			ClientManager.getInstance().setServer(user);
			new Thread(ClientManager.getInstance()).start();
			user.send(new Join(Definition.ALL,name));
			/*Thread.sleep(200);
			user.send(new Join("1",name));
			Thread.sleep(200);
			user.send(new Send("1", "lamia","abcd"));*/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			log("Client",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("Client",e);
		}
		
		manager=ClientManager.getInstance();
	}
	
	public Client(String name, String serverIP) {
		this.name = name;
		this.serverIP = serverIP;
		Socket socket;
		try {
			socket = new Socket(serverIP, Definition.DEFAULT_PORT);
			User user =new User(socket);
			new Thread(user).start();
			log("Client","서버에 접속성공" + user.getIP() + ":" + socket.getPort());
			ClientManager.getInstance().setServer(user);
			user.send(new Join(Definition.ALL,name));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			log("Client",e);
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("Client",e);
			System.exit(1);
		}
		
		manager=ClientManager.getInstance();
	}

	public Client() {
		// TODO Auto-generated constructor stub
	}
	
//	public static void main(String[] args) {
//		if (args.length > 1) {
//			new Client(args[0], args[1]);
//		}
//	}
	
}
