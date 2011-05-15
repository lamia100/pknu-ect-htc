package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressWarnings("unused")
public class Other implements Runnable {
	private String otherIP;
	private int otherPort;
	
	private Socket toOtherSocket;
	private BufferedReader fromOtherMsg;
	private BufferedWriter toOtherMsg;
	
	private boolean isService;
	
	private void debug(String msg) {
		System.out.println("[1:1(" + getOtherIP() + ")] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[1:1(" + getOtherIP() + ")] : " + msg + " -> 성공");
		}
		else {
			System.out.println("[1:1(" + getOtherIP() + ")] : " + msg + " -> 실패");
		}
	}
	
	public Other(String otherIP, int otherPort) {
		this.otherIP = otherIP;
		this.otherPort = otherPort;
	}
	
	public boolean loginOther() {
		boolean result = false;
		
		try {
			toOtherSocket = new Socket(otherIP, otherPort);
			
			fromOtherMsg = new BufferedReader(new InputStreamReader(toOtherSocket.getInputStream()));
			toOtherMsg = new BufferedWriter(new OutputStreamWriter(toOtherSocket.getOutputStream()));
			
			new Thread(this).start();
			
			result = true;
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("연결", result);
		
		return isService = result;
	}
	
	public String getOtherIP() {
		return otherIP;
	}

	@Override
	public void run() {
		
	}
}