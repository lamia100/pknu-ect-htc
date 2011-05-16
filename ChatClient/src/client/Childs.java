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
	private String acceptChildIP;
	private String closeChildIP;
	
	private void debug(String msg) {
		System.out.println("[자식들] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[자식들] : " + msg + " -> 성공");
		}
		else {
			System.out.println("[자식들] : " + msg + " -> 실패");
		}
	}
	
	public Childs(Channel connectChannel, int myPort) {
		childList = new HashMap<String, Childs.Child>();
		
		this.connectChannel = connectChannel;
		this.myPort = myPort;
	}
	
	/**
	 * 자식을 받을 서버 소켓을 준비하고 쓰레드로 돌림, 실질적인 초기화를 담당
	 * @return 준비 성공 여부, 성공이라면 쓰레드로 돌아감
	 */
	public boolean readyForChild(String acceptChildIP) {
		boolean result = false;
		
		try {
			forChildSocket = new ServerSocket(myPort);
			forChildSocket.setSoTimeout(5000);
			
			this.acceptChildIP = acceptChildIP;
			
			new Thread(this).start();
			
			result = true;
		}
		catch (IOException e) {
			e.printStackTrace();
			
			this.acceptChildIP = null;
		}
		
		debug("받을 준비", result);
		
		return result;
	}
	
	public boolean readyForChild(String acceptChildIP, String closeChildIP) {
		boolean result = false;
		
		if (childList.containsKey(closeChildIP)) {
			try {
				forChildSocket = new ServerSocket(myPort);
				forChildSocket.setSoTimeout(5000);
				
				this.acceptChildIP = acceptChildIP;
				this.closeChildIP = closeChildIP;
				
				new Thread(this).start();
				
				result = true;
			}
			catch (IOException e) {
				e.printStackTrace();
				
				this.acceptChildIP = null;
				this.closeChildIP = null;
			}
		}
		else {
			debug("자식 리스트에 연결을 해제해야 할 자식이 없습니다. (SRC IP 불일치)");
		}
		
		debug("받을 준비", result);
		
		return result;
	}

	/**
	 * 모든 자식들 연결 해제, 자식을 받을 소켓 해제, 쓰레드 멈춤
	 */
	public void closeAllChild() {
		Iterator<Child> it = childList.values().iterator();
		while (it.hasNext()) {
			it.next().closeToChild();
		}
		
		debug("모든 자식들 연결 해제", true);
	}
	
	/**
	 * 자식 하나 연결 해제, 쓰레드 계속 진행
	 * @param childIP
	 */
	public void closeSomeChild(String childIP) {
		childList.get(childIP).closeToChild();
	}
	
	/**
	 * 모든 자식들의 IP를 반환
	 * @return 모든 자식들의 IP가 담겨있는 배열
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
	 * 현재 자식들의 수를 반환
	 * @return 현재 자식들의 수
	 */
	public int getChildSize() {
		return childList.size();
	}
	
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * 모든 자식들에게 JOIN 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @return 의미 없음
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
	 * 모든 자식들에게 EXIT 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @return 의미 없음
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
	 * 모든 자식들에게 SEND-BROAD 메세지를 보냄
	 * @param channel
	 * @param sequence
	 * @param nickName
	 * @param msg
	 * @return 의미 없음
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
	 * 특정 자식에게 SEND-BROAD 메세지를 보냄
	 * @param childIP
	 * @param channel
	 * @param sequence
	 * @param nickName
	 * @param msg
	 * @return 전송 성공 여부, 실패라면 특정 자식의 쓰레드가 멈춤
	 */
	public boolean sendMsgToSomeChild(String childIP, String channel, int sequence, String nickName, String msg) {
		return childList.get(childIP).sendMsgToChild(channel, sequence, nickName, msg);
	}
	
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		boolean wait = true;
		
		while (forChildSocket.isBound() && acceptChildIP != null & wait) {			
			try {
				Socket fromChildSocket = forChildSocket.accept();
				
				debug(fromChildSocket.getInetAddress().getHostAddress() + " 가 접속함");
				
				if (acceptChildIP.equals(fromChildSocket.getInetAddress().getHostAddress())) {
					Child newChild = new Child(fromChildSocket);
					if (newChild.readyFromChild()) {
						childList.put(fromChildSocket.getInetAddress().getHostAddress(), newChild);
					}
					
					if (closeChildIP != null) {
						childList.get(closeChildIP).closeToChild();
					}
					
					wait = false;
				}
				else {
					fromChildSocket.close();
					wait = true;
					
					debug(fromChildSocket.getInetAddress().getHostAddress() + " 를 거부함");
				}
			}
			catch (SocketTimeoutException e) {
				e.printStackTrace();
				wait = false;
				
				debug("접속 제한 시간 초과");
			}
			catch (IOException e) {
				e.printStackTrace();
				wait = false;
			}
		}
		
		this.acceptChildIP = null;
		this.closeChildIP = null;
	}
	
	
	// ------------------------------------------------- Child Inner Class -------------------------------------------------
	
	private class Child implements Runnable {
		private Socket fromChildSocket;
		private BufferedReader fromChildMsg;
		private BufferedWriter toChildMsg;
		
		private boolean isService;
		
		private void debug(String msg) {
			System.out.println("[자식(" + getChildIP() + ")] : " + msg);
		}
		
		private void debug(String msg, boolean result) {
			if (result) {
				System.out.println("[자식(" + getChildIP() + ")] : " + msg + " -> 성공");
			}
			else {
				System.out.println("[자식(" + getChildIP() + ")] : " + msg + " -> 실패");
			}
		}
		
		public Child(Socket fromChildSocket) {
			this.fromChildSocket = fromChildSocket;
			
			this.isService = false;
		}
		
		/**
		 * 자식 소켓에 대한 버퍼를 준비하고 자식이 마지막 메세지를 받는다면 쓰레드로 돌림, 실질적인 초기화를 담당
		 * @return 준비 성공 여부, 성공이라면 쓰레드로 돌아감
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
			
			this.debug("연결", result);
			
			return this.isService = result;
		}
		
		/**
		 * 자식 소켓과 연결 해제, 쓰레드 멈춤
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
			
			childList.remove(getChildIP());
			
			this.debug("연결 해제", true);
		}
		
		public String getChildIP() {
			return fromChildSocket.getInetAddress().getHostAddress();
		}
		
		@SuppressWarnings("unused")
		public boolean isConnect() {
			return this.isService;
		}
		
		// ------------------------------------------------- S E N D -------------------------------------------------
		
		/**
		 * 자식에 JOIN 메세지를 보냄
		 * @param channel
		 * @param nickName
		 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
		 */
		public synchronized boolean whoJoinToChild(String channel, String nickName) {
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
			
			this.debug("JOIN/" + channel + "/" + nickName + "/보내기", result);
			
			return this.isService = result;
		}
		
		/**
		 * 자식에 EXIT 메세지를 보냄
		 * @param channel
		 * @param nickName
		 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
		 */
		public synchronized boolean whoExitToChild(String channel, String nickName) {
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
			
			this.debug("EXIT/" + channel + "/" + nickName + "/보내기", result);
			
			return this.isService = result;
		}
		
		/**
		 * 자식에 SEND-BROAD 메세지를 보냄
		 * @param channel
		 * @param sequence
		 * @param nickName
		 * @param msg
		 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
		 */
		public synchronized boolean sendMsgToChild(String channel, int sequence, String nickName, String msg) {
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
			
			this.debug("SEND/BROAD " + channel + "/" + nickName + "/" + msg + "/보내기", result);
			
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
					this.debug(line + " -> 받음");
					
					if (line == null) {
						closeToChild();
					}
					
					if (fromChildMessage == null) {
						fromChildMessage = Message.parsType(line);
					}
					else if (fromChildMessage.parse(line)) {
						Packet packet = new Packet(fromChildMessage, fromChildSocket.getInetAddress().getHostAddress());
							
						if (packet.getMessage().isValid()) {
							this.debug(packet.getMessage().getType() + " -> 정상 패킷 받음");
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