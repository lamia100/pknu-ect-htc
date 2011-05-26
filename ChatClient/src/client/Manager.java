package client;

import gui.GUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.msg.TYPE;
import util.msg.sub.*;
import static util.Definition.*;

public class Manager implements Runnable {
	private BlockingQueue<Packet> serverPacketQueue;
	private Map<String, Channel> channelList;
	
	private Server connectServer;
	private ServerSocket forChildSocket;
	
	private String nickName;
	private GUI gui;
	
	private boolean isService;
	
	@SuppressWarnings("unused")
	private void debug(String msg) {
		System.out.println("[�Ŵ���] : " + msg);
	}
	
	@SuppressWarnings("unused")
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[�Ŵ���] : " + msg + " -> ����");
		}
		else {
			System.out.println("[�Ŵ���] : " + msg + " -> ����");
		}
	}
	
	public Manager(String nickName, GUI gui) {
		channelList = new HashMap<String, Channel>();
		serverPacketQueue = new LinkedBlockingQueue<Packet>();
		
		this.nickName = nickName;
		this.gui = gui;
		
		isService = true;
	}
	
	public void addServerPacket(Packet addPacket) {
		serverPacketQueue.offer(addPacket);
	}
	
	
	// ------------------------------------------------- GUI input -------------------------------------------------
	
	public boolean connectServer(String serverIP, int serverPort) {
		connectServer = new Server(this, serverIP, serverPort);
		boolean result = connectServer.loginServer();
		
		if (result) {			
			result = connectServer.joinChannel(ALL, nickName);
			
			if (result) {
				try {
					forChildSocket = new ServerSocket(DEFAULT_PORT);
					
					new Thread(this).start();
				}
				catch (IOException e) {
					e.printStackTrace();
					
					result = false;
				}
			}
		}
		
		if (result) {
			gui.dspInfo("���� ���ῡ �����Ͽ����ϴ�.");
		}
		else {
			gui.dspInfo("���� ���ῡ �����Ͽ����ϴ�.");
		}
		
		return isService = result;
	}
	
	public void disconnectServer() {
		isService = false;
		addServerPacket(new Packet(new Send(), ""));
		
		for (Channel ch : channelList.values()) {
			ch.disconnectChannel();
		}
		
		try {
			forChildSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		connectServer.exitChannel(ALL, nickName);
		connectServer.logoutServer();
		
		gui.dspInfo("���� ������ �������ϴ�.");
	}
	
	public boolean joinChannel(String channel) {
		boolean result = connectServer.joinChannel(channel, nickName);
		
		if (!result) {
			disconnectServer();
			
			gui.dspInfo("������ ä�� ���� �޼����� ������ ���߽��ϴ�. ������ ������ ������ ������ϴ�.");
		}
		
		return isService = result;
	}
	
	public void exitChannel(String channel) {
		Channel targetChannel = channelList.get(channel);
		
		if (targetChannel != null) {
			channelList.get(channel).disconnectChannel();
			channelList.remove(channel);
			
			gui.dspInfo("ä�� " + channel + " ������ �������ϴ�.");
		}
		else {
			gui.dspInfo("������ ä�� " + channel + " �� ����Ǿ����� �ʾҽ��ϴ�.");
		}
	}
	
	public boolean sendMsg(String channel, String msg) {
		boolean result = true;
		
		if (channelList.containsKey(channel)) {
			result = connectServer.sendMsgToServer(channel, nickName, msg);
			
			if (!result) {
				disconnectServer();
				
				gui.dspInfo("������ �޼����� ������ ���߽��ϴ�. ������ ������ ������ ������ϴ�.");
			}
		}
		else {
			gui.dspInfo("�ش� ä�ο� ���������� �ʱ� ������ �޼����� ������ ���߽��ϴ�.");
		}
		
		return isService = result;
	}
	
	
	// ------------------------------------------------- P E R F O R M -------------------------------------------------
	
	@Override
	public void run() {
		while (isService) {			
			Packet packet = null;
			
			try {
				if ((packet = serverPacketQueue.take()) != null) {
					performService(packet);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void performService(Packet packet) {
		Channel targetChannel;
		
		switch (packet.getMessage().getType()) {
		case SEND:
			Send send = (Send)packet.getMessage();
			
			targetChannel = channelList.get(send.getChannel());
			
			if (targetChannel != null) {
				targetChannel.addFamilyPacket(packet);
			}
			
			break;
		case JOIN:
			Join join = (Join)packet.getMessage();
			
			targetChannel = channelList.get(join.getChannel());
			
			if (targetChannel != null) {
				targetChannel.addFamilyPacket(packet);
			}		
			
			break;
		case EXIT:
			Exit exit = (Exit)packet.getMessage();
			
			targetChannel = channelList.get(exit.getChannel());
			
			if (targetChannel != null) {
				targetChannel.addFamilyPacket(packet);
			}			
			
			break;
		case SET:
			Set set = (Set)packet.getMessage();
			
			targetChannel = channelList.get(set.getChannel());
			
			if (targetChannel != null) {
				targetChannel.addFamilyPacket(packet);
			}
			else {
				if (set.getFamily() == TYPE.FAMILY_PARENT && !"".equals(set.getDstip())) {					
					Channel newChannel = new Channel(connectServer, forChildSocket, set.getChannel(), nickName, gui);
					
					if (newChannel.connectParent(set.getDstip(), DEFAULT_PORT, set.getSequence())) {
						channelList.put(set.getChannel(), newChannel);
													
						gui.dspInfo("ä�� " + set.getChannel() + " ���ῡ �����Ͽ����ϴ�.");
					}
					else {
						gui.dspInfo("ä�� " + set.getChannel() + " ���ῡ �����Ͽ����ϴ�.");
					}
				}
			}
			
			break;
		}
	}
}