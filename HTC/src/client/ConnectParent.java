package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import static util.PacketDefinition.*;

public class ConnectParent implements Runnable {
	private String parentIP;
	private int parentPort;
	
	private Socket toParentSocket;
	private BufferedReader fromParentMsg;
	private BufferedWriter toParentMsg;
	
	public ConnectParent(String parentIP, int parentPort) {
		this.parentIP = parentIP;
		this.parentPort = parentPort;
	}
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	public boolean loginToParent() {
		try {
			toParentSocket = new Socket(parentIP, parentPort);
			
			fromParentMsg = new BufferedReader(new InputStreamReader(toParentSocket.getInputStream()));
			toParentMsg = new BufferedWriter(new OutputStreamWriter(toParentSocket.getOutputStream()));
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
	
	public boolean requestMsgToParent(String channel, String sequence) {
		try {
			toParentMsg.write(HEAD_TYPE_REQUEST + TOKEN_HEAD);
			toParentMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toParentMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD);
			toParentMsg.flush();
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
		if (loginToParent()) {
			while (toParentSocket.isConnected()) {
				String fromParentPacket;
				try {
					fromParentPacket = fromParentMsg.readLine();
					
					while (fromParentPacket != null) {
						
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}