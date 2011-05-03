package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectParent implements Runnable {
	private String parentIP;
	private int parentPort;
	
	private Socket toParentSocket;
	
	private BufferedReader fromParentMsg;
	private BufferedWriter toParentMsg;
	
	private ArrayList<String> msgBuffer;
	private int bufferStartSeq;
	private int bufferEndSeq;
	
	public ConnectParent(String parentIP, int parentPort) {
		this.parentIP = parentIP;
		this.parentPort = parentPort;
	}
	
	public boolean connectParent() {
		try {
			toParentSocket = new Socket(parentIP, parentPort);
			
			fromParentMsg = new BufferedReader(new InputStreamReader(toParentSocket.getInputStream()));
			toParentMsg = new BufferedWriter(new OutputStreamWriter(toParentSocket.getOutputStream()));
			
			msgBuffer = new ArrayList<String>();
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
	
	public boolean requestSequenceNumber() {		
		try {
			toParentMsg.write("/requestSeq");
			toParentMsg.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestBufferMsg(int startSeq, int endSeq) {
		try {
			toParentMsg.write("/requestBufMsg " + startSeq + " " + endSeq);
			toParentMsg.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public void run() {
		// 작성해야 함
	}
	
	public boolean receiveSequenceNumber(int startSeq, int endSeq) {
		// 작성해야 함
		
		return true;
	}
	
	public boolean receiveMsg(String msg) {
		// 작성해야 함
		
		return true;
	}
}