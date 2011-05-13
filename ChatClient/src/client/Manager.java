package client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.msg.TYPE;
import util.msg.sub.*;
import static util.Definition.*;
import test.GUI;

public class Manager implements Runnable {
	private BlockingQueue<Packet> serverPacketQueue;
	private Map<String, Channel> channelList;
	
	private Server connectServer;
	
	private String nickName;
	private GUI gui;
	
	private boolean isService;
	
	@SuppressWarnings("unused")
	private void debug(String msg) {
		System.out.println("[매니저] : " + msg);
	}
	
	@SuppressWarnings("unused")
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[매니저] : " + msg + " -> 성공");
		}
		else {
			System.out.println("[매니저] : " + msg + " -> 실패");
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
				new Thread(this).start();
			}
		}
		
		if (result) {
			gui.dspInfo("서버 연결에 성공하였습니다.");
		}
		else {
			gui.dspInfo("서버 연결에 실패하였습니다.");
		}
		
		return isService = result;
	}
	
	public void disconnectServer() {
		for (Channel ch : channelList.values()) {
			ch.disconnectChannel();
		}
		
		connectServer.exitChannel(ALL, nickName);
		isService = false;
		connectServer.logoutServer();
		
		gui.dspInfo("서버 연결을 끊었습니다.");
	}
	
	public boolean joinChannel(String channel) {
		boolean result = connectServer.joinChannel(channel, nickName);
		
		if (!result) {
			gui.dspInfo("서버에 채널 입장 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		return isService = result;
	}
	
	public boolean exitChannel(String channel) {
		boolean result = connectServer.exitChannel(channel, nickName);
		
		if (!result) {
			gui.dspInfo("서버에 채널 퇴장 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		if (channel != ALL) {
			channelList.get(channel).disconnectChannel();
			channelList.remove(channel);
		}
		
		return isService = result;
	}
	
	public boolean sendMsg(String channel, String msg) {
		boolean result = connectServer.sendMsgToServer(channel, nickName, msg);
		
		if (!result) {
			gui.dspInfo("서버에 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
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
			
			/*
			targetChannel = channelList.get(send.getChannel());
			
			if (targetChannel != null) {
				targetChannel.addFamilyPacket(packet);
			}
			*/
			
			// 채널 우회 처리
			gui.dspMsg(send.getNick(), send.getMsg());
			
			break;
		case SET:
			Set set = (Set)packet.getMessage();
			
			targetChannel = channelList.get(set.getChannel());
			
			if (targetChannel != null) {
				targetChannel.addFamilyPacket(packet);
			}
			else {
				if (set.getFamily() == TYPE.FAMILY_PARENT) {
					Channel newChannel = new Channel(connectServer, set.getChannel(), nickName, gui);
					
					if (newChannel.connectParent(set.getIp(), DEFAULT_PORT, set.getSequence())) {
						channelList.put(set.getChannel(), newChannel);
						connectServer.successConnectToParent(set.getChannel(), set.getIp(), set.getSequence());
							
						gui.dspInfo(set.getChannel() + " 채널이 리스트에 추가되었습니다.");
					}
					else {
						connectServer.failConnectToParent(set.getChannel(), set.getIp(), set.getSequence());
					}
				}
			}
			
			break;
		}
	}
}