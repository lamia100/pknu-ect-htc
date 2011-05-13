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
			System.out.println("�θ� " + msg + " :: ����");
		}
		else {
			System.out.println("�θ� " + msg + " :: ����");
		}
	}
	
	public Parent(Channel connectChannel, String parentIP, int parentPort) {
		this.connectChannel = connectChannel;
		this.parentIP = parentIP;
		this.parentPort = parentPort;
		
		isService = true;
	}
	
	/**
	 * �θ� ���ϰ� �����ϰ� ����Ǿ��ٸ� ������� ����, �������� �ʱ�ȭ�� ���
	 * @return ���� ���� ����, �����̶�� ������� ���ư�
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
		
		debug("����", result);
		
		return isService = result;
	}
	
	/**
	 * �θ� ���ϰ� ���� ����, ������ ����
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
		
		debug("���� ����", true);
	}
	
	/**
	 * �θ� IP�� ��ȯ
	 * @return �θ� IP
	 */
	public String getParentIP() {
		return parentIP;
	}
	
	// ------------------------------------------------- S E N D -------------------------------------------------
	
	/**
	 * �θ� REQ �޼����� ����
	 * @param channel
	 * @param sequence
	 * @return ���� ���� ����, ���ж�� �����尡 ����
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
		
		debug("REQ " + channel + " " + sequence + " ������", result);
		
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
					debug("�θ�κ��� " + line + " :: ����");
					
					if (fromParentMessage == null) {
						debug("1");
						
						fromParentMessage = Message.parsType(line);
					}
					else if (fromParentMessage.parse(line)) {
						debug("2");
						
						Packet packet = new Packet(fromParentMessage, toParentSocket.getInetAddress().getHostAddress());
						
						if (packet.getMessage().isValid()) {
							debug("�θ�κ��� " + packet.getMessage().toString() + " ���� ��Ŷ");
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