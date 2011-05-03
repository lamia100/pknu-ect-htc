package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressWarnings("unused")
public class ConnectRightChild implements Runnable {
	private String rightChildIP;
	private int rightChildPort;
	
	private Socket toRightChildSocket;
	
	private BufferedReader fromRightChildMsg;
	private BufferedWriter toRightChildMsg;
	
	public ConnectRightChild(String rightChildIP, int rightChildPort) {
		this.rightChildIP = rightChildIP;
		this.rightChildPort = rightChildPort;
	}
	
	public boolean connectRightSon() {
		try {
			toRightChildSocket = new Socket(rightChildIP, rightChildPort);
			
			fromRightChildMsg = new BufferedReader(new InputStreamReader(toRightChildSocket.getInputStream()));
			toRightChildMsg = new BufferedWriter(new OutputStreamWriter(toRightChildSocket.getOutputStream()));
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