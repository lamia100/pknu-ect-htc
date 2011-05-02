package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectFamily {
	private String parentIP;
	private String leftSonIP;
	private String rightSonIP;
	private int parentPort;
	private int leftSonPort;
	private int rightSonPort;
	
	private Socket toParentSocket;
	private Socket toLeftSonSocket;
	private Socket toRightSonSocket;
	
	private BufferedReader fromParentMsg;
	private BufferedReader fromLeftSonMsg;
	private BufferedReader fromRightSonMsg;
	private BufferedWriter toParentMsg;
	private BufferedWriter toLeftSonMsg;
	private BufferedWriter toRightSonMsg;
	
	private ArrayList<String> msgBuffer;
	private int bufferStartSeq;
	private int bufferEndSeq;
	
	public ConnectFamily(String parentIP, int parentPort) {
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
	
	public boolean connectLeftSon() {
		try {
			toLeftSonSocket = new Socket(leftSonIP, leftSonPort);
			
			fromLeftSonMsg = new BufferedReader(new InputStreamReader(toLeftSonSocket.getInputStream()));
			toLeftSonMsg = new BufferedWriter(new OutputStreamWriter(toLeftSonSocket.getOutputStream()));
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
	
	public boolean connectRightSon() {
		try {
			toRightSonSocket = new Socket(rightSonIP, rightSonPort);
			
			fromRightSonMsg = new BufferedReader(new InputStreamReader(toRightSonSocket.getInputStream()));
			toRightSonMsg = new BufferedWriter(new OutputStreamWriter(toRightSonSocket.getOutputStream()));
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
	
	/*
	public boolean receiveSequenceNumber(int startSeq, int endSeq) {
		
	}
	
	public boolean receiveMsg(String msg) {
		
	}
	
	public boolean responseSequenceNumber() {
		
	}
	
	public boolean sendMsg(String msg) {
		
	}
	*/
}
