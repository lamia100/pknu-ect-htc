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
import static util.Definition.*;

public class Others implements Runnable {
	private Channel connectChannel;
	private int myPort;
	private ServerSocket forOtherSocket;
	private Map<String, Other> otherList;
	
	private void debug(String msg) {
		System.out.println("[1:1��] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[1:1��] : " + msg + " -> ����");
		}
		else {
			System.out.println("[1:1��] : " + msg + " -> ����");
		}
	}
	
	public Others(Channel connectChannel, int myPort) {
		otherList = new HashMap<String, Other>();
		
		this.connectChannel = connectChannel;
		this.myPort = myPort;
	}
	
	/**
	 * ������ ���� ���� ������ �غ��ϰ� ������� ����, �������� �ʱ�ȭ�� ���
	 * @return �غ� ���� ����, �����̶�� ������� ���ư�
	 */
	public boolean readyForOther() {
		boolean result = false;
		
		try {
			forOtherSocket = new ServerSocket(myPort);
			forOtherSocket.setSoTimeout(5000);
			
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
	 * ��� ����� ���� ����, ������ ���� ���� ����, ������ ����
	 */
	public void closeAllOther() {
		Iterator<Other> it = otherList.values().iterator();
		while (it.hasNext()) {
			it.next().closeToOther();
		}
		
		debug("��� ����� ���� ����", true);
	}
	
	/**
	 * ���� �ϳ� ���� ����, ������ ��� ����
	 * @param otherIP
	 */
	public void closeSomeOther(String otherIP) {
		otherList.get(otherIP).closeToOther();
		otherList.remove(otherIP);
	}
	
	/**
	 * ��� ������� IP�� ��ȯ
	 * @return ��� ������� IP�� ����ִ� �迭
	 */
	public String[] getChildIPList() {
		String otherIPList[] = new String[otherList.size()];
		
		Iterator<Other> it = otherList.values().iterator();
		int i = 0;
		
		while (it.hasNext()) {
			otherIPList[i] = it.next().getChildIP();
			i++;
		}
		
		return otherIPList;
	}
	
	/**
	 * ���� ������� ���� ��ȯ
	 * @return ���� ������� ��
	 */
	public int getOtherSize() {
		return otherList.size();
	}
	
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * Ư�� ���濡�� SEND-UNI �޼����� ����
	 * @param childIP
	 * @param channel
	 * @param sequence
	 * @param nickName
	 * @param msg
	 * @return ���� ���� ����, ���ж�� Ư�� ������ �����尡 ����
	 */
	public boolean sendMsgToSomeChild(String childIP, String channel, int sequence, String nickName, String msg) {
		return otherList.get(childIP).sendMsgToOther(channel, sequence, nickName, msg);
	}
	
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		if (forOtherSocket.isBound()) {			
			try {
				Socket fromOtherSocket = forOtherSocket.accept();
				
				debug("����(" + fromOtherSocket.getInetAddress().getHostAddress() + ")�� ������");
				
				Other newOther = new Other(fromOtherSocket);
				if (newOther.readyFromOther()) {
					otherList.put(fromOtherSocket.getInetAddress().getHostAddress(), newOther);
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
	
	
	private class Other implements Runnable {
		private Socket fromOtherSocket;
		private BufferedReader fromOtherMsg;
		private BufferedWriter toOtherMsg;
		
		private boolean isService;
		
		private void debug(String msg) {
			System.out.println("[1:1(" + getChildIP() + ")] : " + msg);
		}
		
		private void debug(String msg, boolean result) {
			if (result) {
				System.out.println("[1:1(" + getChildIP() + ")] : " + msg + " -> ����");
			}
			else {
				System.out.println("[1:1(" + getChildIP() + ")] : " + msg + " -> ����");
			}
		}
		
		public Other(Socket fromOtherSocket) {
			this.fromOtherSocket = fromOtherSocket;
			
			this.isService = false;
		}
		
		/**
		 * ���� ���Ͽ� ���� ���۸� �غ��ϰ� ������� ����, �������� �ʱ�ȭ�� ���
		 * @return �غ� ���� ����, �����̶�� ������� ���ư�
		 */
		private boolean readyFromOther() {
			boolean result = false;
			
			try {
				fromOtherMsg = new BufferedReader(new InputStreamReader(fromOtherSocket.getInputStream()));
				toOtherMsg = new BufferedWriter(new OutputStreamWriter(fromOtherSocket.getOutputStream()));
				
				this.isService = true;
					
				new Thread(this).start();
					
				result = true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("����", result);
			
			return this.isService = result;
		}
		
		/**
		 * ���� ���ϰ� ���� ����, ������ ����
		 */
		public void closeToOther() {
			this.isService = false;
			
			try {
				fromOtherSocket.close();
				fromOtherSocket = null;
				fromOtherMsg = null;
				toOtherMsg = null;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("���� ����", true);
		}
		
		/**
		 * ���� IP�� ��ȯ
		 * @return ���� IP
		 */
		public String getChildIP() {
			return fromOtherSocket.getInetAddress().getHostAddress();
		}
		
		@SuppressWarnings("unused")
		public boolean isConnect() {
			return this.isService;
		}
		
		// ------------------------------------------------- S E N D -------------------------------------------------
		
		/**
		 * ���濡 SEND-UNI �޼����� ����
		 * @param channel
		 * @param sequence
		 * @param nickName
		 * @param msg
		 * @return ���� ���� ����, ���ж�� �����尡 ����
		 */
		public boolean sendMsgToOther(String channel, int sequence, String nickName, String msg) {
			boolean result = false;
			
			try {
				toOtherMsg.write(HEAD_TYPE_SEND + TOKEN_HEAD);
				toOtherMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
				toOtherMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
				toOtherMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD);
				toOtherMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
				toOtherMsg.write(HEAD_MSG + ":" + msg + TOKEN_HEAD + TOKEN_HEAD);
				toOtherMsg.flush();
				
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
			Message fromOtherMessage = null;
			
			while (this.isService && fromOtherSocket.isConnected()) {
				try {
					line = fromOtherMsg.readLine();
					this.debug(line + "/����");
					
					if (line == null) {
						closeToOther();
					}
					
					if (fromOtherMessage == null) {
						fromOtherMessage = Message.parsType(line);
					}
					else if (fromOtherMessage.parse(line)) {
						Packet packet = new Packet(fromOtherMessage, fromOtherSocket.getInetAddress().getHostAddress());
							
						if (packet.getMessage().isValid()) {
							this.debug(packet.getMessage().getType() + "/���� ��Ŷ ����");
							connectChannel.addFamilyPacket(packet);
						}
							
						fromOtherMessage = null;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					
					closeToOther();
				}
			}
		}
	}
}