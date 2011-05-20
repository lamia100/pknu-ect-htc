package client;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.msg.Message;
import util.msg.sub.*;
import static util.Definition.*;
import test.GUI;

public class Channel implements Runnable {
	private BlockingQueue<Packet> familyPacketQueue;
	private ArrayList<Send> msgList;
	private int msgOffset;
	
	private Server connectServer;
	private Parent connectParent;
	private Childs connectChilds;
	
	private String channel;
	private String nickName;
	private GUI gui;
	
	private boolean isService;
	private boolean isFirstConnect;
	
	private void debug(String msg) {
		System.out.println("[채널(" + channel + ")] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[채널(" + channel + ")] : " + msg + " -> 성공");
		}
		else {
			System.out.println("[채널(" + channel + ")] : " + msg + " -> 실패");
		}
	}
	
	public Channel(Server connectServer, String channel, String nickName, GUI gui) {
		familyPacketQueue = new LinkedBlockingQueue<Packet>();
		msgList = new ArrayList<Send>();
		msgOffset = 0;
		
		this.connectServer = connectServer;
		this.channel = channel;
		this.nickName = nickName;
		this.gui = gui;
		
		isFirstConnect = true;
		isService = true;
	}
	
	public boolean addFamilyPacket(Packet addPacket) {
		return familyPacketQueue.offer(addPacket);
	}
	
	public int getLastSequence() {
		return msgOffset + msgList.size() - 1;
	}
	
	public Send getMsg(int sequence) {
		return msgList.get(sequence - msgOffset - 1);
	}
	
	public String getServerIP() {
		return connectServer.getServerIP();
	}
	
	public Server getConnectServer() {
		return connectServer;
	}
	
	// ------------------------------------------------- GUI input -------------------------------------------------
	
	public boolean connectParent(String parentIP, int parentPort, int sequence) {
		connectParent = new Parent(this, parentIP, parentPort);
		boolean result = connectParent.loginParent();
		
		if (result) {			
			connectChilds = new Childs(this, FAMILY_PORT);
			
			new Thread(this).start();
			
			connectServer.successConnectToParent(channel, parentIP, sequence);
		}
		else {
			connectServer.failConnectToParent(channel, parentIP, sequence);
		}
		
		debug("연결", result);
		
		return isService = result;
	}
	
	public void disconnectChannel() {
		connectServer.exitChannel(channel, nickName);
		
		isService = false;
		addFamilyPacket(new Packet(new Send(), ""));
		
		connectChilds.closeAllChild();
		
		connectParent.logoutParent();
		
		debug("연결 해제", true);
	}
	
	
	// ------------------------------------------------- GUI output -------------------------------------------------
	
	private void display(Message msg) {
		switch (msg.getType()) {
		case SEND:
			Send send = (Send)msg;
			msgList.add(send);
			debug(send.getSeq() + " 순번의 " + send.getMsg().trim() + " 메세지를 버퍼하였습니다.");
			gui.dspMsg(channel, send.getNick(), send.getMsg());
			
			break;
		case JOIN:
			Join join = (Join)msg;
			gui.dspInfo(join.getChannel() + " 채널에 " + join.getNick() + "이(가) 접속하였습니다.");
			
			break;
		case EXIT:
			Exit exit = (Exit)msg;
			gui.dspInfo(exit.getChannel() + " 채널에서 " + exit.getNick() + "이(가) 나가셨습니다.");
			
			break;
		}
	}
	
	// ------------------------------------------------- P E R F O R M -------------------------------------------------
	
	@Override
	public void run() {
		while (isService) {			
			Packet packet = null;
			
			try {
				if ((packet = familyPacketQueue.take()) != null) {
					performService(packet);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void performService(Packet packet) {
		switch (packet.getMessage().getType()) {
		case SEND:
			Send send = (Send)packet.getMessage();
			
			switch (send.getCast()) {
			case CAST_BROAD:
				if (isFirstConnect) {
					msgOffset = send.getSeq();
					isFirstConnect = false;
					
					display(send);
					connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
				}
				else {					
					if (send.getSeq() == getLastSequence() + 1) {
						display(send);
						connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
					}
					else {
						connectParent.requestMsgToParent(send.getChannel(), Integer.toString(getLastSequence() + 1));
					}
				}
				
				break;
			case CAST_UNI:
				display(send);
				
				break;
			default:
			}
			
			break;
		case REQUEST:
			Request request = (Request)packet.getMessage();
			
			int startSequence = request.getSeq();
			
			if (startSequence > getLastSequence()) {
				connectServer.requestMsgToServer(request.getChannel(), startSequence);
			}
			else {
				while (startSequence <= getLastSequence()) {
					Send msg = getMsg(startSequence++);
					connectChilds.sendMsgToSomeChild(packet.getFromIP(), msg.getChannel(), msg.getSeq(), msg.getNick(), msg.getMsg());
				}
			}
			
			break;
		case SET:
			Set set = ((Set)packet.getMessage());
			
			switch (set.getFamily()) {
			case FAMILY_PARENT:
				// dst만 왔을 때, 기존 부모와 연결을 끊고 dst로 부모 변경
				if (!"".equals(set.getDstip()) && "".equals(set.getSrcip())) {
					Parent newParent = new Parent(this, set.getDstip(), FAMILY_PORT);
					
					if (newParent.loginParent()) {
						connectParent.logoutParent();
							
						connectParent = newParent;
						connectServer.successConnectToParent(set.getChannel(), set.getDstip(), set.getSequence());
					}
					else {
						connectServer.failConnectToParent(set.getChannel(), set.getDstip(), set.getSequence());
					}
				}
				
				// src만 왔을 때, src가 부모일 경우 부모(src)와 연결을 끊음
				else if ("".equals(set.getDstip()) && !"".equals(set.getSrcip())) {
					if (connectParent.getParentIP().equals(set.getSrcip())) {
						connectParent.logoutParent();
						
						connectParent = null;
						
						connectServer.successDisconnectToParent(set.getChannel(), set.getSrcip(), set.getSequence());
					}
				}
				
				// dst, src 동시에 왔을 때, src가 기존 부모일 경우 기존 부모와 연결을 끊고 dst로 부모 변경
				else {
					if (connectParent.getParentIP().equals(set.getSrcip())) {
						Parent newParent = new Parent(this, set.getDstip(), FAMILY_PORT);
						
						if (newParent.loginParent()) {
							connectParent.logoutParent();
							
							connectParent = newParent;
							connectServer.successConnectToParent(set.getChannel(), set.getDstip(), set.getSequence());
						}
						else {
							connectServer.failConnectToParent(set.getChannel(), set.getDstip(), set.getSequence());
						}
					}
					else {
						connectServer.failConnectToParent(set.getChannel(), set.getDstip(), set.getSequence());
					}
				}
				
				break;
			case FAMILY_CHILD:
				// dst만 왔을 때, 자식(dst) 추가
				if (!"".equals(set.getDstip()) && "".equals(set.getSrcip())) {
					if (connectChilds.getChildSize() < MAX_CHILD) {
						if (connectChilds.readyForChild(set.getDstip())) {
							connectServer.successOpenSocketForChild(set.getChannel(), set.getDstip(), set.getSequence());
						}
						else {
							connectServer.failOpenSocketForChild(set.getChannel(), set.getDstip(), set.getSequence());
						}
					}
					else {
						connectServer.failOpenSocketForChild(set.getChannel(), set.getDstip(), set.getSequence());
					}
				}
				
				// src만 왔을 때, 자식(src) 삭제
				else if ("".equals(set.getDstip()) && !"".equals(set.getSrcip())) {
					connectChilds.closeSomeChild(set.getSrcip());
					
					connectServer.successDisconnectToChild(set.getChannel(), set.getSrcip(), set.getSequence());
				}
				
				// dst, src 동시에 왔을 때, 자식(src)를 삭제하고 자식(dst) 추가
				else {
					if (connectChilds.getChildSize() <= MAX_CHILD) {
						if (connectChilds.readyForChild(set.getDstip(), set.getSrcip())) {
							connectServer.successOpenSocketForChild(set.getChannel(), set.getDstip(), set.getSequence());
						}
						else {
							connectServer.failOpenSocketForChild(set.getChannel(), set.getDstip(), set.getSequence());
						}
					}
					else {
						connectServer.failOpenSocketForChild(set.getChannel(), set.getDstip(), set.getSequence());
					}
				}
				
				break;
			}
			
			break;
		case JOIN:
			Join join = (Join)packet.getMessage();
			
			display(join);
			connectChilds.whoJoinToAllChild(join.getChannel(), join.getNick());
			
			break;
		case EXIT:
			Exit exit = (Exit)packet.getMessage(); 
			
			display(exit);
			connectChilds.whoExitToAllChild(exit.getChannel(), exit.getNick());
			
			break;
		}
	}
}