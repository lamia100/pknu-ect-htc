package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConnectServer {
	String serverIP;
	int serverPort;
	
	Socket toServerSocket;
	BufferedReader fromServerMsg;
	BufferedWriter toServerMsg;
	
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
	
	public boolean isConnectServer() {
		return toServerSocket.isConnected();
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
}