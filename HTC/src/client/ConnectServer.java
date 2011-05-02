package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import util.PacketDefinition;

public class ConnectServer  implements Runnable {	
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
	
	public boolean login() {
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
	
	public boolean reqeustJoinChannel(int channel) {
		try {
			toServerMsg.write(PacketDefinition.JOIN + " " + channel + " " + nickName);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestMsg(int channel, String msg) {
		try {
			toServerMsg.write(PacketDefinition.SEND_MSG + " " + channel + " " + nickName + " " + msg);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestOtherClientIP(String otherNickName) {
		try {
			toServerMsg.write(PacketDefinition.GET_IP + " " + otherNickName);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportImExit(int channel) {
		try {
			toServerMsg.write(PacketDefinition.EXIT + " " + channel + " " + nickName);
			toServerMsg.flush();
			
			if (channel == 0) {
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

	public boolean reportMyFarentDisconnect(int channel, String parentIp) {
		try {
			toServerMsg.write(PacketDefinition.DISCONNECT_PARENT + " " + channel + " " + parentIp);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportMyLeftSonDisconnect(int channel, String leftSonIp) {
		try {
			toServerMsg.write(PacketDefinition.DISCONNECT_LEFT_SON + " " + channel + " " + leftSonIp);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportMyRightSonDisconnect(int channel, String rightSonIp) {
		try {
			toServerMsg.write(PacketDefinition.DISCONNECT_RIGHT_SON + " " + channel + " " + rightSonIp);
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveMyParentApply(int channel, String ip) {
		try {
			boolean apply = false;
			
			// apply = 나의 부모를 바꾸고
			
			if (apply) {
				toServerMsg.write(PacketDefinition.ACK_APPLY_PARENT + " " + channel + " " + ip);
			}
			else {
				toServerMsg.write(PacketDefinition.NAK_APPLY_PARENT + " " + channel + " " + ip);
			}
			
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveMyLeftSonApply(int channel, String ip) {
		try {
			boolean apply = false;
			
			// apply = 나의 왼쪽 자식을 바꾸고
			
			if (apply) {
				toServerMsg.write(PacketDefinition.ACK_APPLY_LEFT_SON + " " + channel + " " + ip);
			}
			else {
				toServerMsg.write(PacketDefinition.NAK_APPLY_LEFT_SON + " " + channel + " " + ip);
			}
			
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveMyRightSonApply(int channel, String ip) {
		try {
			boolean apply = false;
			
			// apply = 나의 오른쪽 자식을 바꾸고
			
			if (apply) {
				toServerMsg.write(PacketDefinition.ACK_APPLY_RIGHT_SON + " " + channel + " " + ip);
			}
			else {
				toServerMsg.write(PacketDefinition.NAK_APPLY_RIGHT_SON + " " + channel + " " + ip);
			}
			
			toServerMsg.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveOtherClientIP(String nick, String ip) {
		// 파일 전송하고
		
		return true;
	}

	@Override
	public void run() {		
		try {
			login();
			
			while (toServerSocket.isConnected()) {
				String fromServer = fromServerMsg.readLine();
				
				while (fromServer != null) {
					// StringTokenizer fromServerToken = new StringTokenizer(" ");
					
					fromServer = fromServerMsg.readLine();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}