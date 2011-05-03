package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.PacketDefinition;

public class ConnectChild implements Runnable {
	private final static String TOKEN = PacketDefinition.TOKEN_HEAD;	
	private final static int MAX_CHILD = 2;
	
	private String myIP;
	private int myPort;
	
	private ServerSocket forChildSocket;
	
	private ArrayList<Child> childList;
	
	public ConnectChild(String myIP, int myPort) {
		this.myIP = myIP;
		this.myPort = myPort;
		
		childList = new ArrayList<Child>();
	}
	
	public boolean readyForChild() {
		try {
			forChildSocket = new ServerSocket(myPort);
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public void run() {
		if(readyForChild()) {
			while (forChildSocket.isBound()) {
				try {
					Socket fromChildSocket = forChildSocket.accept();
					
					if (childList.size() > MAX_CHILD) {
						BufferedWriter toChildMsg = new BufferedWriter(new OutputStreamWriter(fromChildSocket.getOutputStream()));
						toChildMsg.write("자리 없음");
						toChildMsg.flush();
						
						fromChildSocket.close();
					}
					else {
						Child newChild = new Child(fromChildSocket);
						childList.add(newChild);
						
						new Thread(newChild).start();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
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
	
	private class Child implements Runnable {
		private Socket fromChildSocket;
		private BufferedReader fromChildMsg;
		private BufferedWriter toChildMsg;
		
		public Child(Socket fromChildSocket) {
			this.fromChildSocket = fromChildSocket;
		}
		
		private boolean readyFromChild() {
			try {
				fromChildMsg = new BufferedReader(new InputStreamReader(fromChildSocket.getInputStream()));
				toChildMsg = new BufferedWriter(new OutputStreamWriter(fromChildSocket.getOutputStream()));
			}
			catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		@Override
		public void run() {
			if (readyFromChild()) {
				while (fromChildSocket.isConnected()) {
					String fromChildPacket;
					try {
						fromChildPacket = fromChildMsg.readLine();
						while (fromChildPacket != null) {
							StringTokenizer fromChildToken = new StringTokenizer(fromChildPacket, TOKEN);
							
							ArrayList<String> parsePacket = new ArrayList<String>();
							while (fromChildToken.hasMoreTokens()) {
								parsePacket.add(fromChildToken.nextToken());
							}
							
							String packetType = parsePacket.get(0);
							if (packetType == PacketDefinition.REQ_SEQ_MSG) {
								String channel = parsePacket.get(1);
								String startSeq = parsePacket.get(2);
								String endSeq = parsePacket.get(3);
								
								receiveSequenceNumber(channel, startSeq, endSeq);
							}
							
							fromChildPacket = fromParentMsg.readLine();
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}