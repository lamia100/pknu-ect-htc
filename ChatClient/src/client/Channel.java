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
		System.out.println("[ä��(" + channel + ")] : " + msg);
	}
	
	private void debug(String msg, boolean result) {
		if (result) {
			System.out.println("[ä��(" + channel + ")] : " + msg + " -> ����");
		}
		else {
			System.out.println("[ä��(" + channel + ")] : " + msg + " -> ����");
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
		
		debug("����", result);
		
		return isService = result;
	}
	
	public void disconnectChannel() {
		connectServer.exitChannel(channel, nickName);
		
		isService = false;
		addFamilyPacket(new Packet(new Send(), ""));
		
		connectChilds.closeAllChild();
		
		connectParent.logoutParent();
		
		debug("���� ����", true);
	}
	
	
	// ------------------------------------------------- GUI output -------------------------------------------------
	
	private void display(Message msg) {
		switch (msg.getType()) {
		case SEND:
			Send send = (Send)msg;
			msgList.add(send);
			debug(send.getSeq() + " ������ " + send.getMsg().trim() + " �޼����� �����Ͽ����ϴ�.");
			gui.dspMsg(channel, send.getNick(), send.getMsg());
			
			break;
		case JOIN:
			Join join = (Join)msg;
			gui.dspInfo(join.getChannel() + " ä�ο� " + join.getNick() + "��(��) �����Ͽ����ϴ�.");
			
			break;
		case EXIT:
			Exit exit = (Exit)msg;
			gui.dspInfo(exit.getChannel() + " ä�ο��� " + exit.getNick() + "��(��) �����̽��ϴ�.");
			
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
				// dst�� ���� ��, ���� �θ�� ������ ���� dst�� �θ� ����
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
				
				// src�� ���� ��, src�� �θ��� ��� �θ�(src)�� ������ ����
				else if ("".equals(set.getDstip()) && !"".equals(set.getSrcip())) {
					if (connectParent.getParentIP().equals(set.getSrcip())) {
						connectParent.logoutParent();
						
						connectParent = null;
						
						connectServer.successDisconnectToParent(set.getChannel(), set.getSrcip(), set.getSequence());
					}
				}
				
				// dst, src ���ÿ� ���� ��, src�� ���� �θ��� ��� ���� �θ�� ������ ���� dst�� �θ� ����
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
				// dst�� ���� ��, �ڽ�(dst) �߰�
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
				
				// src�� ���� ��, �ڽ�(src) ����
				else if ("".equals(set.getDstip()) && !"".equals(set.getSrcip())) {
					connectChilds.closeSomeChild(set.getSrcip());
					
					connectServer.successDisconnectToChild(set.getChannel(), set.getSrcip(), set.getSequence());
				}
				
				// dst, src ���ÿ� ���� ��, �ڽ�(src)�� �����ϰ� �ڽ�(dst) �߰�
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