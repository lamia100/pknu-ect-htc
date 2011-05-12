package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import util.msg.Message;
import static util.Definition.*;

public class Server  implements Runnable {
	private Manager connectManager;
	private String serverIP;
	private int serverPort;
	
	private Socket toServerSocket;
	private BufferedReader fromServerMsg;
	private BufferedWriter toServerMsg;
	
	public Server(Manager connectManager, String serverIP, int serverPort) {
		this.connectManager = connectManager;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	
	public boolean loginServer() {
		try {
			toServerSocket = new Socket(serverIP, serverPort);
			
			fromServerMsg = new BufferedReader(new InputStreamReader(toServerSocket.getInputStream()));
			toServerMsg = new BufferedWriter(new OutputStreamWriter(toServerSocket.getOutputStream()));
			
			new Thread(this).start();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean logoutServer() {
		try {
			fromServerMsg.close();
			toServerMsg.close();
			toServerSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String getServerIP() {
		return serverIP;
	}
	
	// ------------------------------------------------- S E N D ------------------------------------------------- 
	
	public boolean joinChannel(String channel, String nickName) {
		try {
			toServerMsg.write(HEAD_TYPE_JOIN + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean exitChannel(String channel, String nickName) {
		try {
			toServerMsg.write(HEAD_TYPE_EXIT + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean sendMsgToServer(String channel, String nickName, String msg) {
		try {
			toServerMsg.write(HEAD_TYPE_SEND + TOKEN_HEAD);
			toServerMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
			toServerMsg.write(HEAD_MSG + ":" + msg + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean sendScriptBroad(String channel, String nickName, String script) {
		try {
			toServerMsg.write(HEAD_TYPE_SCRIPT + TOKEN_HEAD);
			toServerMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
			toServerMsg.write(HEAD_MSG + ":" + script + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean sendScriptUni(String channel, String nickName, String script) {
		try {
			toServerMsg.write(HEAD_TYPE_SCRIPT + TOKEN_HEAD);
			toServerMsg.write(HEAD_CAST + ":" + HEAD_CAST_UNI + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
			toServerMsg.write(HEAD_MSG + ":" + script + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean successOpenSocketForChild(String channel, String childIP) {
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean failOpenSocketForChild(String channel, String childIP) {
		try {
			toServerMsg.write(HEAD_TYPE_FAIL + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean successConnectToParent(String channel, String parentIP) {
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_PARENT + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + parentIP + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean failConnectToParent(String channel, String parentIP) {
		try {
			toServerMsg.write(HEAD_TYPE_FAIL + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_PARENT + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + parentIP + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestMsgToServer(String channel, int sequence) {
		try {
			toServerMsg.write(HEAD_TYPE_REQUEST + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		while (toServerSocket.isConnected()) {
			String line = null;
			Message fromServerMessage = null;
			
			try {
				while ((line = fromServerMsg.readLine()) != null) {
					if (fromServerMessage == null) {
						fromServerMessage = Message.parsType(line);
					}
					else if (fromServerMessage.parse(line)) {
						Packet packet = new Packet(fromServerMessage, toServerSocket.getInetAddress().getHostAddress());
						
						if (packet.getMessage().isValid()) {
							connectManager.addServerPacket(packet);
						}
						
						fromServerMessage = null;
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}