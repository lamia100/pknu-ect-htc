package client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.msg.TYPE;
import util.msg.sub.*;
import static util.Definition.*;
import test.GUI;

public class Manager implements Runnable {
	private Map<String, Channel> channelList;
	private BlockingQueue<Packet> serverPacketQueue;
	
	private Server connectServer;
	
	private String nickName;
	private GUI gui;
	
	private boolean isService;
	
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
	
	public void debug(String msg) {
		System.out.println(msg);
	}
	
	
	// ------------------------------------------------- GUI input -------------------------------------------------
	
	public boolean connectServer(String serverIP, int serverPort) {
		connectServer = new Server(this, serverIP, serverPort);
		boolean result = connectServer.loginServer();
		
		if (result) {
			gui.dspInfo("서버와 연결 설정(3way Handshake)에 성공하였습니다.");
			
			result = connectServer.joinChannel(ALL, nickName);
			
			if (result) {
				new Thread(this).start();
				
				gui.dspInfo("서버에 ALL 메세지 전송에 성공하였습니다.");
			}
			else {
				gui.dspInfo("서버에 ALL 메세지 전송에 실패하였습니다.");
			}
		}
		else {
			gui.dspInfo("서버와 연결 설정(3way Handshake)에 실패하였습니다.");
		}
		
		return result;
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
		
		if (result) {
			gui.dspInfo("서버에 채널 입장 메세지를 보냈습니다.");
		}
		else {
			gui.dspInfo("서버에 채널 입장 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		return result;
	}
	
	public boolean exitChannel(String channel) {
		boolean result = connectServer.exitChannel(channel, nickName);
		
		if (result) {
			gui.dspInfo("서버에 채널 퇴장 메세지를 보냈습니다.");
		}
		else {
			gui.dspInfo("서버에 채널 퇴장 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		if (channel != ALL) {
			channelList.remove(channel);
		}
		
		return result;
	}
	
	public boolean sendMsg(String channel, String msg) {
		boolean result = connectServer.sendMsgToServer(channel, nickName, msg);
		
		if (result) {
			gui.dspInfo("서버에 메세지를 보냈습니다.");
		}
		else {
			gui.dspInfo("서버에 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		return result;
	}
	
	
	// ------------------------------------------------- P E R F O R M -------------------------------------------------
	
	@Override
	public void run() {
		while (isService) {
			debug("Manager Thread Loop");
			
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
			targetChannel.addFamilyPacket(packet);
			
			break;
		case SET:
			Set set = ((Set)packet.getMessage());
			targetChannel = channelList.get(set.getChannel());
			
			if (channelList.containsKey(targetChannel)) {
				targetChannel.addFamilyPacket(packet);
			}
			else {
				if (set.getFamily() == TYPE.FAMILY_PARENT) {
					Channel newChannel = new Channel(connectServer, set.getChannel(), nickName, gui);
					
					if (newChannel.connectParent(set.getIp(), DEFAULT_PORT)) {
						channelList.put(set.getChannel(), newChannel);
						
						gui.dspInfo(set.getChannel() + " 채널이 리스트에 추가되었습니다.");
					}
				}
			}
			
			break;
		}
	}
}