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
			System.out.println("������ " + msg + " :: ����");
		}
		else {
			System.out.println("������ " + msg + " :: ����");
		}
	}
	
	public Server(Manager connectManager, String serverIP, int serverPort) {
		this.connectManager = connectManager;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		
		isService = true;
	}
	
	/**
	 * ���� ���ϰ� �����ϰ� ����Ǿ��ٸ� ������� ����, �������� �ʱ�ȭ�� ���
	 * @return ���� ���� ����, �����̶�� ������� ���ư�
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
		
		debug("����", result);
		
		return isService = result;
	}
	
	/**
	 * ���� ���ϰ� ���� ����, ������ ����
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
		
		debug("���� ����", true);
	}
	
	/**
	 * ���� IP�� ��ȯ
	 * @return ���� IP
	 */
	public String getServerIP() {
		return serverIP;
	}
	
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * ������ JOIN �޼����� ����
	 * @param channel
	 * @param nickName
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("JOIN " + channel + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ EXIT �޼����� ����
	 * @param channel
	 * @param nickName
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("EXIT " + channel + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ SEND-BROAD �޼����� ����
	 * @param channel
	 * @param nickName
	 * @param msg
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("SEND-BROAD " + channel + " " + nickName + " " + msg + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ SCRIPT-BROAD �޼����� ����
	 * @param channel
	 * @param nickName
	 * @param script
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("SCRIPT-BROAD " + channel + " " + nickName + " " + script + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ SCRIPT-UNI �޼����� ����
	 * @param channel
	 * @param nickName
	 * @param script
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("SCRIPT-UNI " + channel + " " + nickName + " " + script + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ SUC-CHILD �޼����� ����
	 * @param channel
	 * @param childIP
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("SUC-CHILD " + channel + " " + childIP + " " + sequence + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ FAIL-CHILD �޼����� ����
	 * @param channel
	 * @param childIP
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("FAIL-CHILD " + channel + " " + childIP + " " + sequence + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ SUC-PARENT �޼����� ����
	 * @param channel
	 * @param parentIP
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("SUC-PARENT " + channel + " " + parentIP + " " + sequence + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ FAIL-PARENT �޼����� ����
	 * @param channel
	 * @param parentIP
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("FAIL-PARENT " + channel + " " + parentIP + " " + sequence + " ������", result);
		
		return isService = result;
	}
	
	/**
	 * ������ REQ �޼����� ����
	 * @param channel
	 * @param sequence
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("REQ " + channel + " " + sequence + " ������", result);
		
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
					debug("�����κ��� " + line + " : ����");
					
					if (fromServerMessage == null) {
						fromServerMessage = Message.parsType(line);
					}
					else if (fromServerMessage.parse(line)) {
						Packet packet = new Packet(fromServerMessage, toServerSocket.getInetAddress().getHostAddress());
						
						if (packet.getMessage().isValid()) {
							debug("�����κ��� " + packet.getMessage().toString() + " ���� ��Ŷ :: ����");
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