package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import util.PacketDefinition;

public class ConnectParent implements Runnable {
	private final static String TOKEN = PacketDefinition.TOKEN_HEAD;
	
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
	
	public boolean sendMsg(String channel, String seq, String nickName, String msg) {
		try {
			toParentMsg.write(PacketDefinition.SEND_MSG + TOKEN + channel + TOKEN + seq + TOKEN + nickName + TOKEN + msg);
			toParentMsg.flush();
		} catch (IOException e) {
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
						StringTokenizer fromServerToken = new StringTokenizer(fromParentPacket, TOKEN);
						
						ArrayList<String> parsePacket = new ArrayList<String>();
						while (fromServerToken.hasMoreTokens()) {
							parsePacket.add(fromServerToken.nextToken());
						}
						
						String packetType = parsePacket.get(0);
						if (packetType == PacketDefinition.SEND_MSG) {
							String channel = parsePacket.get(1);
							String seq = parsePacket.get(2);
							
							receiveSequenceNumber(channel, seq);
						}
						
						fromParentPacket = fromParentMsg.readLine();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean receiveSequenceNumber(String channel, String seq) {
		// 작성해야 함
		
		return true;
	}
}