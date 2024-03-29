package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import util.msg.Message;
import static util.Definition.*;

public class Server implements Runnable {
	private Manager connectManager;
	private String serverIP;
	private int serverPort;
	
	private Socket toServerSocket;
	private BufferedReader fromServerMsg;
	private BufferedWriter toServerMsg;
	
	private boolean isService;
	
	private void debug(String msg) {
		System.out.println("[서버(" + getServerIP() + ")] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[서버(" + getServerIP() + ")] : " + msg + " -> 성공");
		}
		else {
			System.out.println("[서버(" + getServerIP() + ")] : " + msg + " -> 실패");
		}
	}
	
	public Server(Manager connectManager, String serverIP, int serverPort) {
		this.connectManager = connectManager;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		
		isService = false;
	}
	
	/**
	 * 서버 소켓과 연결하고 연결되었다면 쓰레드로 돌림, 실질적인 초기화를 담당
	 * @return 연결 성공 여부, 성공이라면 쓰레드로 돌아감
	 */
	public boolean loginServer() {
		boolean result = false;
		
		try {
			toServerSocket = new Socket(serverIP, serverPort);
			
			fromServerMsg = new BufferedReader(new InputStreamReader(toServerSocket.getInputStream()));
			toServerMsg = new BufferedWriter(new OutputStreamWriter(toServerSocket.getOutputStream()));
			
			isService = true;
			
			new Thread(this).start();
			
			result = true;
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("연결", result);
		
		return isService = result;
	}
	
	/**
	 * 서버 소켓과 연결 해제, 쓰레드 멈춤
	 */
	public void logoutServer() {
		isService = false;
		
		if (toServerSocket != null) {
			try {
				toServerSocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		toServerSocket = null;
		fromServerMsg = null;
		toServerMsg = null;
		
		debug("연결 해제", true);
	}
	
	public String getServerIP() {
		return serverIP;
	}
	
	public Socket getToServerSocket() {
		return toServerSocket;
	}

	public BufferedReader getFromServerMsg() {
		return fromServerMsg;
	}

	public BufferedWriter getToServerMsg() {
		return toServerMsg;
	}
	
	// ------------------------------------------------- S E N D -------------------------------------------------

	/**
	 * 서버에 JOIN 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean joinChannel(String channel, String nickName) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_JOIN + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("JOIN/" + channel + "/" + nickName + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 EXIT 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @return 전송 성공 여부, 실패라면 쓰레드 멈춤
	 */
	public synchronized boolean exitChannel(String channel, String nickName) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_EXIT + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("EXIT/" + channel + "/" + nickName + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SEND/BROAD 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @param msg
	 * @return 전송 성공 여부, 실패라면 쓰레드 멈춤
	 */
	public synchronized boolean sendMsgToServer(String channel, String nickName, String msg) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SEND + TOKEN_HEAD);
			toServerMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
			toServerMsg.write(HEAD_MSG + ":" + msg + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SEND/BROAD/" + channel + "/" + nickName + "/" + msg + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SCRIPT/BROAD 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @param script
	 * @return 전송 성공 여부, 실패라면 쓰레드 멈춤
	 */
	public synchronized boolean sendScriptBroad(String channel, String nickName, String script) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SCRIPT + TOKEN_HEAD);
			toServerMsg.write(HEAD_CAST + ":" + HEAD_CAST_BROAD + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
			toServerMsg.write(HEAD_MSG + ":" + script + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SCRIPT/BROAD/" + channel + "/" + nickName + "/" + script + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SCRIPT/UNI 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @param script
	 * @return 전송 성공 여부, 실패라면 쓰레드 멈춤
	 */
	public synchronized boolean sendScriptUni(String channel, String nickName, String script) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SCRIPT + TOKEN_HEAD);
			toServerMsg.write(HEAD_CAST + ":" + HEAD_CAST_UNI + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_NICK + ":" + nickName + TOKEN_HEAD);
			toServerMsg.write(HEAD_MSG + ":" + script + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SCRIPT/UNI/" + channel + "/" + nickName + "/" + script + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-Open/CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드 멈춤
	 */
	public synchronized boolean successOpenSocketForChild(String channel, String childIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SUC-Open/CHILD/" + channel + "/" + childIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 FAIL-Open/CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드 멈춤
	 */
	public synchronized boolean failOpenSocketForChild(String channel, String childIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_FAIL + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("FAIL-Open/CHILD/" + channel + "/" + childIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-Con/PARENT 메세지를 보냄
	 * @param channel
	 * @param parentIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean successConnectToParent(String channel, String parentIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_PARENT + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + parentIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SUC-Con/PARENT/" + channel + "/" + parentIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 FAIL-Con/PARENT 메세지를 보냄
	 * @param channel
	 * @param parentIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean failConnectToParent(String channel, String parentIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_FAIL + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_PARENT + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + parentIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("FAIL-Con/PARENT/" + channel + "/" + parentIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-DisCon/PARENT 메세지를 보냄
	 * @param channel
	 * @param parentIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean successDisconnectToParent(String channel, String parentIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_PARENT + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + parentIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SUC-DisCon/PARENT/" + channel + "/" + parentIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-Con/CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean successConnectToChild(String channel, String childIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SUC-Con/CHILD/" + channel + "/" + childIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 FAIL-Con/CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean failConnectToChild(String channel, String childIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_FAIL + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("FAIL-Con/CHILD/" + channel + "/" + childIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-DisCon/CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean successDisconnectToChild(String channel, String childIP, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_SUCCESS + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_FAMILY + ":" + HEAD_FAMILY_CHILD + TOKEN_HEAD);
			toServerMsg.write(HEAD_IP + ":" + childIP + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("SUC-DisCon/CHILD/" + channel + "/" + childIP + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 REQ 메세지를 보냄
	 * @param channel
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public synchronized boolean requestMsgToServer(String channel, int sequence) {
		boolean result = false;
		
		try {
			toServerMsg.write(HEAD_TYPE_REQUEST + TOKEN_HEAD);
			toServerMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toServerMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toServerMsg.flush();
			
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		debug("REQ/" + channel + "/" + sequence + "/보내기", result);
		
		return isService = result;
	}
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		String line = null;
		Message fromServerMessage = null;
		
		while (isService && toServerSocket.isConnected()) {			
			try {
				line = fromServerMsg.readLine();
					
				debug(line + " -> 받음");
				
				if (line == null) {
					logoutServer();
				}
				
				if (fromServerMessage == null) {						
					fromServerMessage = Message.parsType(line);
				}
				else if (fromServerMessage.parse(line)) {						
					Packet packet = new Packet(fromServerMessage, toServerSocket.getInetAddress().getHostAddress());
						
					if (packet.getMessage().isValid()) {
						debug(packet.getMessage().getType() + " -> 정상 패킷 받음");
						connectManager.addServerPacket(packet);
					}
					
					fromServerMessage = null;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				
				logoutServer();
			}
		}
	}
}