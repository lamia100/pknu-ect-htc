package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
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

	// ------------------------------------------------- S E N D -------------------------------------------------
	
	public boolean sendMsgToAllChild(String channel, String seq, String nickName, String msg) {
		Iterator<Child> it = childList.iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.sendMsgToChild(channel, seq, nickName, msg);
		}
		
		return false;
	}
	
	
	@Override
	public void run() {
		if(readyForChild()) {
			while (forChildSocket.isBound()) {
				try {
					Socket fromChildSocket = forChildSocket.accept();
					
					if (childList.size() > MAX_CHILD) {
						BufferedWriter toChildMsg = new BufferedWriter(new OutputStreamWriter(fromChildSocket.getOutputStream()));
						toChildMsg.write("�ڸ� ����");
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
		
		// ------------------------------------------------- S E N D -------------------------------------------------
		
		public boolean sendMsgToChild(String channel, String seq, String nickName, String msg) {
			try {
				// toChildMsg.write(PacketDefinition.SEND_MSG + TOKEN + channel + TOKEN + seq + TOKEN + nickName + TOKEN + msg);
				toChildMsg.flush();
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
																
								// receiveSequenceNumber(channel, seq);
							}
							
							fromChildPacket = fromChildMsg.readLine();
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public boolean receiveSeqenceNumber(String channel, String seq) {
			// �ۼ��ؾ� ��
			
			return false;
		}
	}
}