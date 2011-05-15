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
		System.out.println("[1:1들] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[1:1들] : " + msg + " -> 성공");
		}
		else {
			System.out.println("[1:1들] : " + msg + " -> 실패");
		}
	}
	
	public Others(Channel connectChannel, int myPort) {
		otherList = new HashMap<String, Other>();
		
		this.connectChannel = connectChannel;
		this.myPort = myPort;
	}
	
	/**
	 * 상대방을 받을 서버 소켓을 준비하고 쓰레드로 돌림, 실질적인 초기화를 담당
	 * @return 준비 성공 여부, 성공이라면 쓰레드로 돌아감
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
		
		debug("받을 준비", result);
		
		return result;
	}

	/**
	 * 모든 상대방들 연결 해제, 상대방을 받을 소켓 해제, 쓰레드 멈춤
	 */
	public void closeAllOther() {
		Iterator<Other> it = otherList.values().iterator();
		while (it.hasNext()) {
			it.next().closeToOther();
		}
		
		debug("모든 상대방들 연결 해제", true);
	}
	
	/**
	 * 상대방 하나 연결 해제, 쓰레드 계속 진행
	 * @param otherIP
	 */
	public void closeSomeOther(String otherIP) {
		otherList.get(otherIP).closeToOther();
		otherList.remove(otherIP);
	}
	
	/**
	 * 모든 상대방들의 IP를 반환
	 * @return 모든 상대방들의 IP가 담겨있는 배열
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
	 * 현재 상대방들의 수를 반환
	 * @return 현재 상대방들의 수
	 */
	public int getOtherSize() {
		return otherList.size();
	}
	
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * 특정 상대방에게 SEND-UNI 메세지를 보냄
	 * @param childIP
	 * @param channel
	 * @param sequence
	 * @param nickName
	 * @param msg
	 * @return 전송 성공 여부, 실패라면 특정 상대방의 쓰레드가 멈춤
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
				
				debug("상대방(" + fromOtherSocket.getInetAddress().getHostAddress() + ")이 접속함");
				
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
				System.out.println("[1:1(" + getChildIP() + ")] : " + msg + " -> 성공");
			}
			else {
				System.out.println("[1:1(" + getChildIP() + ")] : " + msg + " -> 실패");
			}
		}
		
		public Other(Socket fromOtherSocket) {
			this.fromOtherSocket = fromOtherSocket;
			
			this.isService = false;
		}
		
		/**
		 * 상대방 소켓에 대한 버퍼를 준비하고 쓰레드로 돌림, 실질적인 초기화를 담당
		 * @return 준비 성공 여부, 성공이라면 쓰레드로 돌아감
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
			
			this.debug("연결", result);
			
			return this.isService = result;
		}
		
		/**
		 * 상대방 소켓과 연결 해제, 쓰레드 멈춤
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
			
			this.debug("연결 해제", true);
		}
		
		/**
		 * 상대방 IP를 반환
		 * @return 상대방 IP
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
		 * 상대방에 SEND-UNI 메세지를 보냄
		 * @param channel
		 * @param sequence
		 * @param nickName
		 * @param msg
		 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
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
			
			this.debug("SEND/BROAD " + channel + "/" + nickName + "/" + msg + "/보내기", result);
			
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
					this.debug(line + "/받음");
					
					if (line == null) {
						closeToOther();
					}
					
					if (fromOtherMessage == null) {
						fromOtherMessage = Message.parsType(line);
					}
					else if (fromOtherMessage.parse(line)) {
						Packet packet = new Packet(fromOtherMessage, fromOtherSocket.getInetAddress().getHostAddress());
							
						if (packet.getMessage().isValid()) {
							this.debug(packet.getMessage().getType() + "/정상 패킷 받음");
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