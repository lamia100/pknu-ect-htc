package client;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import util.msg.Message;
import util.msg.sub.Send;
import static util.Definition.*;

public class Childs implements Runnable {
	private Channel connectChannel;
	private int myPort;
	private ServerSocket forChildSocket;
	private Map<String, Child> childList;
	
	private void debug(String msg) {
		System.out.println("[�ڽĵ�] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[�ڽĵ�] : " + msg + " -> ����");
		}
		else {
			System.out.println("[�ڽĵ�] : " + msg + " -> ����");
		}
	}
	
	public Childs(Channel connectChannel, int myPort) {
		childList = new HashMap<String, Childs.Child>();
		
		this.connectChannel = connectChannel;
		this.myPort = myPort;
	}
	
	/**
	 * �ڽ��� ���� ���� ������ �غ��ϰ� ������� ����, �������� �ʱ�ȭ�� ���
	 * @return �غ� ���� ����, �����̶�� ������� ���ư�
	 */
	public boolean readyForChild() {
		boolean result = false;
		
		try {
			forChildSocket = new ServerSocket(myPort);
			forChildSocket.setSoTimeout(5000);
			
			new Thread(this).start();
			
			result = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("���� �غ�", result);
		
		return result;
	}

	/**
	 * ��� �ڽĵ� ���� ����, �ڽ��� ���� ���� ����, ������ ����
	 */
	public void closeAllChild() {
		Iterator<Child> it = childList.values().iterator();
		while (it.hasNext()) {
			it.next().closeToChild();
		}
		
		debug("��� �ڽĵ� ���� ����", true);
	}
	
	/**
	 * �ڽ� �ϳ� ���� ����, ������ ��� ����
	 * @param childIP
	 */
	public void closeSomeChild(String childIP) {
		childList.get(childIP).closeToChild();
		childList.remove(childIP);
	}
	
	/**
	 * ��� �ڽĵ��� IP�� ��ȯ
	 * @return ��� �ڽĵ��� IP�� ����ִ� �迭
	 */
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
	
	/**
	 * ���� �ڽĵ��� ���� ��ȯ
	 * @return ���� �ڽĵ��� ��
	 */
	public int getChildSize() {
		return childList.size();
	}
	
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * ��� �ڽĵ鿡�� JOIN �޼����� ����
	 * @param channel
	 * @param nickName
	 * @return �ǹ� ����
	 */
	public boolean whoJoinToAllChild(String channel, String nickName) {
		Iterator<Child> it = childList.values().iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.whoJoinToChild(channel, nickName);
		}
		
		return true;
	}
	
	/**
	 * ��� �ڽĵ鿡�� EXIT �޼����� ����
	 * @param channel
	 * @param nickName
	 * @return �ǹ� ����
	 */
	public boolean whoExitToAllChild(String channel, String nickName) {
		Iterator<Child> it = childList.values().iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.whoExitToChild(channel, nickName);
		}
		
		return true;
	}
	
	/**
	 * ��� �ڽĵ鿡�� SEND-BROAD �޼����� ����
	 * @param channel
	 * @param sequence
	 * @param nickName
	 * @param msg
	 * @return �ǹ� ����
	 */
	public boolean sendMsgToAllChild(String channel, int sequence, String nickName, String msg) {
		Iterator<Child> it = childList.values().iterator();
		Child child;
		
		while (it.hasNext()) {
			child = it.next();
			
			child.sendMsgToChild(channel, sequence, nickName, msg);
		}
		
		return true;
	}
	
	/**
	 * Ư�� �ڽĿ��� SEND-BROAD �޼����� ����
	 * @param childIP
	 * @param channel
	 * @param sequence
	 * @param nickName
	 * @param msg
	 * @return ���� ���� ����, ���ж�� Ư�� �ڽ��� �����尡 ����
	 */
	public boolean sendMsgToSomeChild(String childIP, String channel, int sequence, String nickName, String msg) {
		return childList.get(childIP).sendMsgToChild(channel, sequence, nickName, msg);
	}
	
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		if (forChildSocket.isBound()) {			
			try {
				Socket fromChildSocket = forChildSocket.accept();
				
				debug("�ڽ�(" + fromChildSocket.getInetAddress().getHostAddress() + ")�� ������");
				
				Child newChild = new Child(fromChildSocket);
				if (newChild.readyFromChild()) {
					childList.put(fromChildSocket.getInetAddress().getHostAddress(), newChild);
				}
			}
			catch (SocketTimeoutException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private class Child implements Runnable {
		private Socket fromChildSocket;
		private BufferedReader fromChildMsg;
		private BufferedWriter toChildMsg;
		
		private boolean isService;
		
		private void debug(String msg) {
			System.out.println("[�ڽ�(" + getChildIP() + ")] : " + msg);
		}
		
		private void debug(String msg, boolean result) {
			if (result) {
				System.out.println("[�ڽ�(" + getChildIP() + ")] : " + msg + " -> ����");
			}
			else {
				System.out.println("[�ڽ�(" + getChildIP() + ")] : " + msg + " -> ����");
			}
		}
		
		public Child(Socket fromChildSocket) {
			this.fromChildSocket = fromChildSocket;
			
			this.isService = false;
		}
		
		/**
		 * �ڽ� ���Ͽ� ���� ���۸� �غ��ϰ� �ڽ��� ������ �޼����� �޴´ٸ� ������� ����, �������� �ʱ�ȭ�� ���
		 * @return �غ� ���� ����, �����̶�� ������� ���ư�
		 */
		private boolean readyFromChild() {
			boolean result = false;
			
			try {
				fromChildMsg = new BufferedReader(new InputStreamReader(fromChildSocket.getInputStream()));
				toChildMsg = new BufferedWriter(new OutputStreamWriter(fromChildSocket.getOutputStream()));
				
				Send msg = connectChannel.getMsg(connectChannel.getLastSequence());				
				
				if (sendMsgToChild(msg.getChannel(), msg.getSeq(), msg.getNick(), msg.getMsg())) {
					this.isService = true;
					
					new Thread(this).start();
					
					result = true;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("����", result);
			
			return this.isService = result;
		}
		
		/**
		 * �ڽ� ���ϰ� ���� ����, ������ ����
		 */
		public void closeToChild() {
			this.isService = false;
			
			try {
				fromChildSocket.close();
				fromChildSocket = null;
				fromChildMsg = null;
				toChildMsg = null;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("���� ����", true);
		}
		
		/**
		 * �ڽ� IP�� ��ȯ
		 * @return �ڽ� IP
		 */
		public String getChildIP() {
			return fromChildSocket.getInetAddress().getHostAddress();
		}
		
		@SuppressWarnings("unused")
		public boolean isConnect() {
			return this.isService;
		}
		
		// ------------------------------------------------- S E N D -------------------------------------------------
		
		/**
		 * �ڽĿ� JOIN �޼����� ����
		 * @param channel
		 * @param nickName
		 * @return ���� ���� ����, ���ж�� �����尡 ����
		 */
		public boolean whoJoinToChild(String channel, String nickName) {
			boolean result = false;
			
			try {
				toChildMsg.write(HEAD_TYPE_JOIN + TOKEN_HEAD);
				toChildMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toChildMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD + TOKEN_HEAD);
				toChildMsg.flush();
				
				result = true;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			this.debug("JOIN/" + channel + "/" + nickName + "/������", result);
			
			return this.isService = result;
		}
		
		/**
		 * �ڽĿ� EXIT �޼����� ����
		 * @param channel
		 * @param nickName
		 * @return ���� ���� ����, ���ж�� �����尡 ����
		 */
		public boolean whoExitToChild(String channel, String nickName) {
			boolean result = false;
			
			try {
				toChildMsg.write(HEAD_TYPE_EXIT + TOKEN_HEAD);
				toChildMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toChildMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD + TOKEN_HEAD);
				toChildMsg.flush();
				
				result = true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("EXIT/" + channel + "/" + nickName + "/������", result);
			
			return this.isService = result;
		}
		
		/**
		 * �ڽĿ� SEND-BROAD �޼����� ����
		 * @param channel
		 * @param sequence
		 * @param nickName
		 * @param msg
		 * @return ���� ���� ����, ���ж�� �����尡 ����
		 */
		public boolean sendMsgToChild(String channel, int sequence, String nickName, String msg) {
			boolean result = false;
			
			try {
				toChildMsg.write(HEAD_TYPE_SEND + TOKEN_HEAD);
				toChildMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
				toChildMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toChildMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD);
				toChildMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
				toChildMsg.write(HEAD_MSG + ":" + msg + TOKEN_HEAD + TOKEN_HEAD);
				toChildMsg.flush();
				
				result = true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("SEND/BROAD " + channel + "/" + nickName + "/" + msg + "/������", result);
			
			return this.isService = result;
		}
		
		
		// ------------------------------------------------- R E C E I V E -------------------------------------------------
		
		@Override
		public void run() {			
			String line = null;
			Message fromChildMessage = null;
			
			while (this.isService && fromChildSocket.isConnected()) {
				try {
					line = fromChildMsg.readLine();
					this.debug(line + "/����");
					
					if (line == null) {
						closeToChild();
					}
					
					if (fromChildMessage == null) {
						fromChildMessage = Message.parsType(line);
					}
					else if (fromChildMessage.parse(line)) {
						Packet packet = new Packet(fromChildMessage, fromChildSocket.getInetAddress().getHostAddress());
							
						if (packet.getMessage().isValid()) {
							this.debug(packet.getMessage().getType() + "/���� ��Ŷ ����");
							connectChannel.addFamilyPacket(packet);
						}
							
						fromChildMessage = null;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					
					closeToChild();
				}
			}
		}
	}
}