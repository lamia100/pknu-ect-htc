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
	private final static String TOKEN = PacketDefinition.TOKEN;
	
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
	
	public boolean requestSequenceNumber(String channel) {		
		try {
			toParentMsg.write(PacketDefinition.GET_SEQ + TOKEN + channel);
			toParentMsg.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestBufferMsg(String channel, String startSeq, String endSeq) {
		try {
			toParentMsg.write(PacketDefinition.GET_MSG + TOKEN + channel + TOKEN + startSeq + TOKEN + endSeq);
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
		try {
			loginToParent();
			
			while (toParentSocket.isConnected()) {
				String fromParentPacket = fromParentMsg.readLine();
				
				while (fromParentPacket != null) {
					StringTokenizer fromServerToken = new StringTokenizer(fromParentPacket, TOKEN);
					
					ArrayList<String> parsePacket = new ArrayList<String>();
					while (fromServerToken.hasMoreTokens()) {
						parsePacket.add(fromServerToken.nextToken());
					}
					
					String packetType = parsePacket.get(0);
					if (packetType == PacketDefinition.RES_SEQ) {
						String channel = parsePacket.get(1);
						String startSeq = parsePacket.get(2);
						String endSeq = parsePacket.get(3);
						
						receiveSequenceNumber(channel, startSeq, endSeq);
					}
					else if (packetType == PacketDefinition.RES_MSG) {
						String channel = parsePacket.get(1);
						String seqNum = parsePacket.get(2);
						String nickName = parsePacket.get(3);
						String msg = parsePacket.get(4);
						
						for (int i = 5; i < parsePacket.size(); i++) {
							msg.concat(TOKEN + parsePacket.get(i));
						}
						
						receiveMsg(channel, seqNum, nickName, msg);
					}
					
					fromParentPacket = fromParentMsg.readLine();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean receiveSequenceNumber(String channel, String startSeq, String endSeq) {
		// 작성해야 함
		
		return true;
	}
	
	public boolean receiveMsg(String channel, String seqNum, String nickName, String msg) {
		// 작성해야 함
		
		return true;
	}
}