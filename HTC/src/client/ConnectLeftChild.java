package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectLeftChild implements Runnable {
	private String leftChildIP;
	private int leftChildPort;
	
	private Socket toLeftChildSocket;
	
	private BufferedReader fromLeftChildMsg;
	private BufferedWriter toLeftChildMsg;
	
	public ConnectLeftChild(String leftChildIP, int leftChildPort) {
		this.leftChildIP = leftChildIP;
		this.leftChildPort = leftChildPort;
	}
	
	public boolean connectLeftSon() {
		try {
			toLeftChildSocket = new Socket(leftChildIP, leftChildPort);
			
			fromLeftChildMsg = new BufferedReader(new InputStreamReader(toLeftChildSocket.getInputStream()));
			toLeftChildMsg = new BufferedWriter(new OutputStreamWriter(toLeftChildSocket.getOutputStream()));
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

	@Override
	public void run() {
		// 작성해야 함
	}
	
	public boolean responseSequenceNumber() {
		// 작성해야 함
		
		return true;
	}
	
	public boolean sendMsg(String msg) {
		// 작성해야 함
		
		return true;
	}
}