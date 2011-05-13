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

public class Parent implements Runnable {
	private Channel connectChannel;
	private String parentIP;
	private int parentPort;
	
	private Socket toParentSocket;
	private BufferedReader fromParentMsg;
	private BufferedWriter toParentMsg;
	
	private boolean isService;
	
	private void debug(String msg) {
		System.out.println(msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("부모에 " + msg + " :: 성공");
		}
		else {
			System.out.println("부모에 " + msg + " :: 실패");
		}
	}
	
	public Parent(Channel connectChannel, String parentIP, int parentPort) {
		this.connectChannel = connectChannel;
		this.parentIP = parentIP;
		this.parentPort = parentPort;
		
		isService = true;
	}
	
	/**
	 * 부모 소켓과 연결하고 연결되었다면 쓰레드로 돌림, 실질적인 초기화를 담당
	 * @return 연결 성공 여부, 성공이라면 쓰레드로 돌아감
	 */
	public boolean loginParent() {
		boolean result = false;
		
		try {
			toParentSocket = new Socket(parentIP, parentPort);
			
			fromParentMsg = new BufferedReader(new InputStreamReader(toParentSocket.getInputStream()));
			toParentMsg = new BufferedWriter(new OutputStreamWriter(toParentSocket.getOutputStream()));
			
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
	 * 부모 소켓과 연결 해제, 쓰레드 멈춤
	 */
	public void logoutParent() {
		isService = false;
		
		try {
			fromParentMsg.close();
			toParentMsg.close();
			toParentSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("연결 해제", true);
	}
	
	/**
	 * 부모 IP를 반환
	 * @return 부모 IP
	 */
	public String getParentIP() {
		return parentIP;
	}
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * 부모에 REQ 메세지를 보냄
	 * @param channel
	 * @param sequence
	 * @return 전송 성공 여부, 실패라면 쓰레드가 멈춤
	 */
	public boolean requestMsgToParent(String channel, String sequence) {
		boolean result = false;
		
		try {
			toParentMsg.write(HEAD_TYPE_REQUEST + TOKEN_HEAD);
			toParentMsg.write(HEAD_CHANNEL + ":" + channel + TOKEN_HEAD);
			toParentMsg.write(HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD);
			toParentMsg.flush();
			
			result = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("REQ " + channel + " " + sequence + " 보내기", result);
		
		return isService = result;
	}

	
	// ------------------------------------------------- R E C E I V E -------------------------------------------------
	
	@Override
	public void run() {
		while (isService && toParentSocket.isConnected()) {
			debug("Parent Thread Loop :: Start");
			
			String line = null;
			Message fromParentMessage = null;
				
			try {
				while ((line = fromParentMsg.readLine()) != null) {
					debug("부모로부터 " + line + " :: 받음");
					
					if (fromParentMessage == null) {
						debug("1");
						
						fromParentMessage = Message.parsType(line);
					}
					else if (fromParentMessage.parse(line)) {
						debug("2");
						
						Packet packet = new Packet(fromParentMessage, toParentSocket.getInetAddress().getHostAddress());
						
						if (packet.getMessage().isValid()) {
							debug("부모로부터 " + packet.getMessage().toString() + " 정상 패킷");
							connectChannel.addFamilyPacket(packet);
						}
						
						fromParentMessage = null;
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		debug("Parent Thread Loop :: End");
	}
}