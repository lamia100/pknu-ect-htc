package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import util.PacketDefinition;

public class ConnectServer  implements Runnable {
	private final static String TOKEN = PacketDefinition.TOKEN;
	
	private String serverIP;
	private int serverPort;
	
	private Socket toServerSocket;
	private BufferedReader fromServerMsg;
	private BufferedWriter toServerMsg;
	
	private String nickName;
	
	public ConnectServer(String serverIP, int serverPort, String nickName) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.nickName = nickName;
	}
	
	// ------------------------------------------------- S E N D ------------------------------------------------- 
	
	public boolean loginToServer() {
		try {
			toServerSocket = new Socket(serverIP, serverPort);
			
			fromServerMsg = new BufferedReader(new InputStreamReader(toServerSocket.getInputStream()));
			toServerMsg = new BufferedWriter(new OutputStreamWriter(toServerSocket.getOutputStream()));
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
	
	public boolean reqeustJoinChannel(String channel) {
		try {
			toServerMsg.write(PacketDefinition.JOIN + TOKEN + channel + TOKEN + nickName);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestMsg(String channel, String msg) {
		try {
			toServerMsg.write(PacketDefinition.SEND_MSG + TOKEN + channel + TOKEN + nickName + " " + msg);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestOtherIP(String otherNick) {
		try {
			toServerMsg.write(PacketDefinition.GET_IP + TOKEN + otherNick);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportImExit(String channel) {
		try {
			toServerMsg.write(PacketDefinition.EXIT + TOKEN + channel + TOKEN + nickName);
			toServerMsg.flush();
			
			if ("0".equals(channel)) {
				toServerMsg.close();
				fromServerMsg.close();
				toServerSocket.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean reportMyFarentDisconnect(String channel, String parentIP) {
		try {
			toServerMsg.write(PacketDefinition.DISCONNECT_PARENT + TOKEN + channel + TOKEN + parentIP);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportMyChildDisconnect(String channel, String childIP) {
		try {
			toServerMsg.write(PacketDefinition.DISCONNECT_CHILD + TOKEN + channel + TOKEN + childIP);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {		
		try {
			loginToServer();
			
			while (toServerSocket.isConnected()) {
				String fromServerPacket = fromServerMsg.readLine();
				
				while (fromServerPacket != null) {
					StringTokenizer fromServerToken = new StringTokenizer(fromServerPacket, TOKEN);
					
					ArrayList<String> parsePacket = new ArrayList<String>();
					while (fromServerToken.hasMoreTokens()) {
						parsePacket.add(fromServerToken.nextToken());
					}
					
					String packetType = parsePacket.get(0);
					if (packetType == PacketDefinition.RESPONSE_IP) {
						String otherNick = parsePacket.get(1);
						String otherIP = parsePacket.get(2);
						
						receiveOtherClientIP(otherNick, otherIP);
					}
					else if (packetType == PacketDefinition.APPLY_PARENT) {
						String channel = parsePacket.get(1);
						String parentIP = parsePacket.get(2);
						
						receiveMyParentApply(channel, parentIP);
					}
					else if (packetType == PacketDefinition.APPLY_CHILD) {
						String channel = parsePacket.get(1);
						String oldChildIP = parsePacket.get(2);
						String newChildIP = parsePacket.get(3);
						
						receiveMyChildApply(channel, oldChildIP, newChildIP);
					}
					
					fromServerPacket = fromServerMsg.readLine();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveOtherClientIP(String otherNick, String otherIP) {
		// 작성해야 함
	}
	
	public boolean receiveMyParentApply(String channel, String parentIP) {
		try {
			boolean apply = false;
			
			// apply = 나의 부모를 바꾸고
			
			if (apply) {
				toServerMsg.write(PacketDefinition.ACK_APPLY_PARENT + TOKEN + channel + TOKEN + parentIP);
			}
			else {
				toServerMsg.write(PacketDefinition.NAK_APPLY_PARENT + TOKEN + channel + TOKEN + parentIP);
			}
			
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveMyChildApply(String channel, String oldChildIP, String newChildIP) {
		try {
			boolean apply = false;
			
			// apply = 나의 왼쪽 자식을 바꾸고
			
			if (apply) {
				toServerMsg.write(PacketDefinition.ACK_APPLY_CHILD + TOKEN + channel + TOKEN + oldChildIP + TOKEN + newChildIP);
			}
			else {
				toServerMsg.write(PacketDefinition.NAK_APPLY_CHILD + TOKEN + channel + TOKEN + oldChildIP + TOKEN + newChildIP);
			}
			
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}