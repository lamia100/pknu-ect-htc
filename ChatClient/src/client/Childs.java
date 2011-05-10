package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import util.msg.Message;
import static util.Definition.*;

public class Childs implements Runnable {
	private Manager connectManager;
	private int myPort;
	private ServerSocket forChildSocket;
	private Map<String, Child> childList;
	
	public Childs(Manager connectManager, int myPort) {
		this.connectManager = connectManager;
		this.myPort = myPort;
		
		childList = new HashMap<String, Childs.Child>();
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

	public void closeAllChild() {
		Iterator<Child> it = childList.values().iterator();
				
		while (it.hasNext()) {
			it.next().closeToChild();
		}
	}
	
	public void closeSomeChild(String childIP) {
		childList.get(childIP).closeToChild();
		childList.remove(childIP);
	}
	
	public String[] getChildIPList() {
		String childIPList[] = new String[childList.size()];
		
		Iterator<Child> it = childList.values().iterator();
		int i = 0;
		
		while (it.hasNext()) {
			childIPList[i] = it.next().getChildIP();
			i++;
		}
		
		return childIPList;
	}
	
	public int getChildSize() {
		return childList.size();
	}
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	public boolean whoJoinToAllChild(String channel, String nickName) {
		Iterator<Child> it = childList.values().iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.whoJoinToChild(channel, nickName);
		}
		
		return true;
	}
	
	public boolean whoExitToAllChild(String channel, String nickName) {
		Iterator<Child> it = childList.values().iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.whoExitToChild(channel, nickName);
		}
		
		return true;
	}
	
	public boolean sendMsgToAllChild(String channel, int sequence, String nickName, String msg) {
		Iterator<Child> it = childList.values().iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.sendMsgToChild(channel, sequence, nickName, msg);
		}
		
		return true;
	}
	
	public boolean sendMsgToSomeChild(String childIP, String channel, int sequence, String nickName, String msg) {
		return childList.get(childIP).sendMsgToChild(channel, sequence, nickName, msg);
	}
	
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		if(readyForChild()) {
			while (forChildSocket.isBound()) {
				try {
					Socket fromChildSocket = forChildSocket.accept();
					
					Child newChild = new Child(fromChildSocket);
					childList.put(fromChildSocket.getInetAddress().getHostAddress(), newChild);
						
					new Thread(newChild).start();
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
		
		public void closeToChild() {
			try {
				fromChildMsg.close();
				toChildMsg.close();
				fromChildSocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public String getChildIP() {
			return fromChildSocket.getInetAddress().getHostAddress();
		}
		
		// ------------------------------------------------- S E N D -------------------------------------------------
		
		public boolean whoJoinToChild(String channel, String nickName) {
			try {
				toChildMsg.write(HEAD_TYPE_JOIN + TOKEN_HEAD);
				toChildMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toChildMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
				toChildMsg.flush();
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		public boolean whoExitToChild(String channel, String nickName) {
			try {
				toChildMsg.write(HEAD_TYPE_EXIT + TOKEN_HEAD);
				toChildMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toChildMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
				toChildMsg.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		public boolean sendMsgToChild(String channel, int sequence, String nickName, String msg) {
			try {
				toChildMsg.write(HEAD_TYPE_SEND + TOKEN_HEAD);
				toChildMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
				toChildMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toChildMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD);
				toChildMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
				toChildMsg.write(HEAD_MSG + ":" + msg + TOKEN_HEAD);
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
					String line = null;
					Message fromChildMessage = null;
					
					try {
						while ((line = fromChildMsg.readLine()) != null) {
							if (fromChildMessage == null) {
								fromChildMessage = Message.parsType(line);
							}
							else if (fromChildMessage.parse(line)) {
								connectManager.addPacket(new Packet(fromChildMessage, fromChildSocket.getInetAddress().getHostAddress()));
								fromChildMessage = null;
							}
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