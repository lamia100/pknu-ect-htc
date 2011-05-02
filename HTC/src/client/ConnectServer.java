package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConnectServer {
	private String serverIP;
	private int serverPort;
	
	private Socket toServerSocket;
	private BufferedReader fromServerMsg;
	private BufferedWriter toServerMsg;
	
	public ConnectServer(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	
	public boolean login() {
		try {
			toServerSocket = new Socket(serverIP, serverPort);
			
			fromServerMsg = new BufferedReader(new InputStreamReader(toServerSocket.getInputStream()));
			toServerMsg = new BufferedWriter(new OutputStreamWriter(toServerSocket.getOutputStream()));
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reqeustJoinChannel(int channel) {
		try {
			toServerMsg.write("/join " + channel);
			toServerMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestMsg(String msg) {
		try {
			toServerMsg.write("/sendMsg " + msg);
			toServerMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestCertainClientIP(String certainNick) {
		try {
			toServerMsg.write("/getIP " + certainNick);
			toServerMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportImExit() {
		try {
			toServerMsg.write("/exit");
			toServerMsg.flush();
			
			toServerMsg.close();
			fromServerMsg.close();
			toServerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean receiveMyFamily(String who, String ip) {
		try {
			boolean apply = false;
			
			if ("parent".equals(who)) {
				apply = true; // 나의 부모를 바꾸고
			}
			else if ("leftSon".equals(who)) {
				apply = true; // 나의 왼쪽 자식을 바꾸고
			}
			else if ("rightSon".equals(who)) {
				apply = true; // 나의 오른쪽 자식을 바꾸고
			}
			
			if (apply) {
				toServerMsg.write("/ack " + who + "Apply");
			}
			else {
				toServerMsg.write("/nak " + who + "Apply");
			}
			
			toServerMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveCertainClientIP(String nick, String ip) {
		
	}
}