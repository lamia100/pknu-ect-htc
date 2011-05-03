package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectChild implements Runnable {
	private String myIP;
	private int myPort;
	
	private ServerSocket forChildSocket;
	
	/*
	private BufferedReader fromChildMsg;
	private BufferedWriter toChildMsg;
	*/
	
	public ConnectChild(String myIP, int myPort) {
		this.myIP = myIP;
		this.myPort = myPort;
	}
	
	public boolean readyForChild() {
		try {
			forChildSocket = new ServerSocket(myPort);
			
			/*
			fromChildMsg = new BufferedReader(new InputStreamReader(forChildSocket.getInputStream()));
			toChildMsg = new BufferedWriter(new OutputStreamWriter(forChildSocket.getOutputStream()));
			*/
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public void run() {
		readyForChild();
		
		while (forChildSocket.isBound()) {
			try {
				Socket fromChildSocket = forChildSocket.accept();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
	
	private class SupportChild implements Runnable {
				
		@Override
		public void run() {
			// 작성해야 함			
		}
	}
}