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

public class Server  implements Runnable {
	private Manager connectManager;
	private String serverIP;
	private int serverPort;
	
	private Socket toServerSocket;
	private BufferedReader fromServerMsg;
	private BufferedWriter toServerMsg;
	
	private boolean isService;
	
	private void debug(String msg) {
		System.out.println(msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("서버에 " + msg + " :: 성공");
		}
		else {
			System.out.println("서버에 " + msg + " :: 실패");
		}
	}
	
	public Server(Manager connectManager, String serverIP, int serverPort) {
		this.connectManager = connectManager;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		
		isService = true;
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
		
		try {
			fromServerMsg.close();
			toServerMsg.close();
			toServerSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("연결 해제", true);
	}
	
	/**
	 * 서버 IP를 반환
	 * @return 서버 IP
	 */
	public String getServerIP() {
		return serverIP;
	}
	
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * 서버에 JOIN 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean joinChannel(String channel, String nickName) {
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
		
		debug("JOIN " + channel + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 EXIT 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean exitChannel(String channel, String nickName) {
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
		
		debug("EXIT " + channel + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SEND-BROAD 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @param msg
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean sendMsgToServer(String channel, String nickName, String msg) {
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
		
		debug("SEND-BROAD " + channel + " " + nickName + " " + msg + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SCRIPT-BROAD 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @param script
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean sendScriptBroad(String channel, String nickName, String script) {
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
		
		debug("SCRIPT-BROAD " + channel + " " + nickName + " " + script + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SCRIPT-UNI 메세지를 보냄
	 * @param channel
	 * @param nickName
	 * @param script
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean sendScriptUni(String channel, String nickName, String script) {
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
		
		debug("SCRIPT-UNI " + channel + " " + nickName + " " + script + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean successOpenSocketForChild(String channel, String childIP, int sequence) {
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
		
		debug("SUC-CHILD " + channel + " " + childIP + " " + sequence + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 FAIL-CHILD 메세지를 보냄
	 * @param channel
	 * @param childIP
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean failOpenSocketForChild(String channel, String childIP, int sequence) {
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
		
		debug("FAIL-CHILD " + channel + " " + childIP + " " + sequence + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 SUC-PARENT 메세지를 보냄
	 * @param channel
	 * @param parentIP
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean successConnectToParent(String channel, String parentIP, int sequence) {
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
		
		debug("SUC-PARENT " + channel + " " + parentIP + " " + sequence + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 FAIL-PARENT 메세지를 보냄
	 * @param channel
	 * @param parentIP
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean failConnectToParent(String channel, String parentIP, int sequence) {
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
		
		debug("FAIL-PARENT " + channel + " " + parentIP + " " + sequence + " 보내기", result);
		
		return isService = result;
	}
	
	/**
	 * 서버에 REQ 메세지를 보냄
	 * @param channel
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean requestMsgToServer(String channel, int sequence) {
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
		
		debug("REQ " + channel + " " + sequence + " 보내기", result);
		
		return isService = result;
	}
	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		while (isService && toServerSocket.isConnected()) {
			debug("Server Thread Loop");
			
			String line = null;
			Message fromServerMessage = null;
			
			try {
				while ((line = fromServerMsg.readLine()) != null) {
					debug("서버로부터 " + line + " : 받음");
					
					if (fromServerMessage == null) {
						fromServerMessage = Message.parsType(line);
					}
					else if (fromServerMessage.parse(line)) {
						Packet packet = new Packet(fromServerMessage, toServerSocket.getInetAddress().getHostAddress());
						
						if (packet.getMessage().isValid()) {
							debug("서버로부터 " + packet.getMessage().toString() + " 정상 패킷 :: 받음");
							connectManager.addServerPacket(packet);
						}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}