package client;

import java.net.ServerSocket;
import java.net.Socket;
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

public class Childs implements Runnable {
	private Channel connectChannel;
	private int myPort;
	private ServerSocket forChildSocket;
	private Map<String, Child> childList;
	
	private boolean isService;
	
	private void debug(String msg) {
		System.out.println(msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println(msg + " :: 성공");
		}
		else {
			System.out.println(msg + " :: 실패");
		}
	}
	
	public Childs(Channel connectChannel, int myPort) {
		childList = new HashMap<String, Childs.Child>();
		
		this.connectChannel = connectChannel;
		this.myPort = myPort;
		
		isService = true;
	}
	
	/**
	 * 자식을 받을 서버 소켓을 준비하고 쓰레드로 돌림, 실질적인 초기화를 담당
	 * @return 준비 성공 여부, 성공이라면 쓰레드로 돌아감
	 */
	public boolean readyForChild() {
		boolean result = false;
		
		try {
			forChildSocket = new ServerSocket(myPort);
			
			new Thread(this).start();
			
			result = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("자식을 받을 준비", result);
		
		return isService = result;
	}

	/**
	 * 모든 자식들 연결 해제, 자식을 받을 소켓 해제, 쓰레드 멈춤
	 */
	public void closeAllChild() {
		isService = false;
		
		Iterator<Child> it = childList.values().iterator();
		while (it.hasNext()) {
			it.next().closeToChild();
		}
		
		try {
			forChildSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("자식을 받을 소켓 해제", true);
	}
	
	/**
	 * 자식 하나 연결 해제, 쓰레드 계속 진행
	 * @param childIP
	 */
	public void closeSomeChild(String childIP) {
		childList.get(childIP).closeToChild();
		childList.remove(childIP);
		
		debug("자식 하나 " + childIP + " 연결 해제", true);
	}
	
	/**
	 * 모든 자식들의 IP를 반환
	 * @return 모든 자식의 IP가 담겨있는 배열
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
		while (isService && forChildSocket.isBound()) {
			debug("Childs Thread Loop");
			
			try {
				Socket fromChildSocket = forChildSocket.accept();
				
				debug("자식 " + fromChildSocket.getInetAddress().getHostAddress() + " 이 접속함");
				
				Child newChild = new Child(fromChildSocket);
				if (newChild.readyFromChild()) {
					childList.put(fromChildSocket.getInetAddress().getHostAddress(), newChild);
				}
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
			System.out.println("자식 " + getChildIP() + " 로부터 " + msg + " :: 받음");
		}
		
		private void debug(String msg, boolean result) {
			if (result) {
				System.out.println("자식 " + getChildIP() + " 에 "  + msg + " :: 성공");
			}
			else {
				System.out.println("자식 " + getChildIP() + " 에 "  + msg + " :: 실패");
			}
		}
		
		public Child(Socket fromChildSocket) {
			this.fromChildSocket = fromChildSocket;
			
			this.isService = true;
		}
		
		/**
		 * 자식 소켓에 대한 버퍼를 준비하고 쓰레드로 돌림, 실질적인 초기화를 담당
		 * @return 준비 성공 여부, 성공이라면 쓰레드로 돌아감
		 */
		private boolean readyFromChild() {
			boolean result = false;
			
			try {
				fromChildMsg = new BufferedReader(new InputStreamReader(fromChildSocket.getInputStream()));
				toChildMsg = new BufferedWriter(new OutputStreamWriter(fromChildSocket.getOutputStream()));
				
				new Thread(this).start();
				
				result = true;
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
				fromChildMsg.close();
				toChildMsg.close();
				fromChildSocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			this.debug("연결 해제", true);
		}
		
		/**
		 * 자식 IP를 반환
		 * @return 자식 IP
		 */
		public String getChildIP() {
			return fromChildSocket.getInetAddress().getHostAddress();
		}
		
		
		// ------------------------------------------------- S E N D -------------------------------------------------
		
		/**
		 * 자식에 JOIN 메세지를 보냄
		 * @param channel
		 * @param nickName
		 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
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
			
			this.debug("JOIN " + channel + " 보내기", result);
			
			return this.isService = result;
		}
		
		/**
		 * 자식에 EXIT 메세지를 보냄
		 * @param channel
		 * @param nickName
		 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
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
			
			this.debug("EXIT " + channel + " 보내기", result);
			
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
			
			this.debug("SEND-BROAD " + channel + " " + nickName + " " + msg + " 보내기", result);
			
			return this.isService = result;
		}
		
		
		// ------------------------------------------------- R E C E I V E -------------------------------------------------
		
		@Override
		public void run() {
			while (this.isService && fromChildSocket.isConnected()) {
				debug("Child Thread Loop");
				
				String line = null;
				Message fromChildMessage = null;
					
				try {
					while ((line = fromChildMsg.readLine()) != null) {
						this.debug(line);
						
						if (fromChildMessage == null) {
							fromChildMessage = Message.parsType(line);
						}
						else if (fromChildMessage.parse(line)) {
							Packet packet = new Packet(fromChildMessage, fromChildSocket.getInetAddress().getHostAddress());
							
							if (packet.getMessage().isValid()) {
								this.debug(packet.getMessage().toString() + " 정상 패킷");
								connectChannel.addFamilyPacket(packet);
							}
							
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