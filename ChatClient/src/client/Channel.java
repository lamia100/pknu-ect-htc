package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import util.msg.Message;
import util.msg.sub.*;
import static util.Definition.*;
import test.GUI;

public class Channel implements Runnable {
	private Queue<Packet> familyPacketQueue;
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
	
	public Channel(Server connectServer, String channel, String nickName, GUI gui) {
		familyPacketQueue = new LinkedList<Packet>();
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
	
	private int getLastSequence() {
		return msgOffset + msgList.size() - 1;
	}
	
	private Send getMsg(int sequence) {
		return msgList.get(sequence - msgOffset - 1);
	}
	
	public void debug(String msg) {
		System.out.println(msg);
	}
	
	
	// ------------------------------------------------- GUI input -------------------------------------------------
	
	public boolean connectParent(String parentIP, int parentPort) {
		connectParent = new Parent(this, parentIP, parentPort);
		boolean result = connectParent.loginParent();
		
		if (result) {
			gui.dspInfo("부모와 연결 설정(3way Handshake)에 성공하였습니다.");
			
			connectChilds = new Childs(this, DEFAULT_PORT);
			result = connectChilds.readyForChild();
			
			if (result) {
				new Thread(this).start();
				
				gui.dspInfo("자식을 받을 준비에 성공하였습니다.");
			}
			else {
				gui.dspInfo("자식을 받을 준비에 실패하였습니다.");
			}
		}
		else {
			gui.dspInfo("부모와 연결 설정(3way Handshake)에 실패하였습니다.");
		}
		
		return result;
	}
	
	public void disconnectChannel() {
		connectServer.exitChannel(channel, nickName);
		connectChilds.closeAllChild();
		connectParent.logoutParent();
		
		isService = false;
		
		gui.dspInfo("채널 연결을 끊었습니다.");
	}
	
	
	// ------------------------------------------------- GUI output -------------------------------------------------
	
	private void display(Message msg) {
		switch (msg.getType()) {
		case SEND:
			Send send = (Send)msg;
			msgList.add(send);
			gui.dspMsg(send.getNick() + " : " + send.getMsg());
		case JOIN:
			Join join = (Join)msg;
			gui.dspInfo(join.getChannel() + " 채널에 " + join.getNick() + "이(가) 접속하였습니다.");
		case EXIT:
			Exit exit = (Exit)msg;
			gui.dspInfo(exit.getChannel() + " 채널에서 " + exit.getNick() + "이(가) 나가셨습니다.");
		}
	}
	
	// ------------------------------------------------- P E R F O R M -------------------------------------------------
	
	@Override
	public void run() {
		while (isService) {
			Packet packet = null;
			
			if ((packet = familyPacketQueue.poll()) != null) {
				performService(packet);
			}
		}
	}
	
	private boolean performService(Packet packet) {
		boolean result = false;
		
		switch (packet.getMessage().getType()) {
		case SEND:
			Send send = (Send)packet.getMessage();
			
			switch (send.getCast()) {
			case CAST_BROAD:
				if (isFirstConnect) {
					msgOffset = send.getSeq();
					isFirstConnect = false;
					
					display(send);
					result = connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
				}
				else {
					if (send.getSeq() == getLastSequence() - 1) {
						display(send);
						result = connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
					}
					else {
						result = connectParent.requestMsgToParent(send.getChannel(), Integer.toString(getLastSequence() + 1));
					}
				}
				
				break;
			case CAST_UNI:
				display(send);
				result = true;
				
				break;
			default:
			}
			
			break;
		case REQUEST:
			Request request = (Request)packet.getMessage();
			
			int startSequence = request.getSeq();
			boolean sendResult = true;
			
			if (startSequence > getLastSequence()) {
				sendResult = connectServer.requestMsgToServer(request.getChannel(), startSequence);
			}
			else {
				while (startSequence <= getLastSequence() && sendResult) {
					Send msg = getMsg(startSequence++);
					sendResult = connectChilds.sendMsgToSomeChild(packet.getFromIP(), msg.getChannel(), msg.getSeq(), msg.getNick(), msg.getMsg());
				}
			}
			
			result = sendResult;
			
			break;
		case SET:
			Set set = ((Set)packet.getMessage());
			
			switch (set.getFamily()) {
			case FAMILY_PARENT:
				Parent newParent = new Parent(this, set.getIp(), DEFAULT_PORT);
				
				if (newParent.loginParent()) {
					connectParent = newParent;
					result = connectServer.successConnectToParent(set.getChannel(), set.getIp(), set.getSequence());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp(), set.getSequence());
				}
				
				break;
			case FAMILY_CHILD:
				if (connectChilds.getChildSize() < MAX_CHILD) {
					result = connectServer.successOpenSocketForChild(set.getChannel(), set.getIp(), set.getSequence());
					
					Send msg = msgList.get(msgList.size() - 1);
					connectChilds.sendMsgToSomeChild(set.getIp(), msg.getChannel(), msg.getSeq(), msg.getNick(), msg.getMsg());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp(), set.getSequence());
				}
				
				break;
			}
			
			break;
		case JOIN:
			Join join = (Join)packet.getMessage();
			
			display(join);
			result = connectChilds.whoJoinToAllChild(join.getChannel(), join.getNick());
			break;
		case EXIT:
			Exit exit = (Exit)packet.getMessage(); 
			
			display(exit);
			result = connectChilds.whoExitToAllChild(exit.getChannel(), exit.getNick());
			break;
		}
		
		return result;
	}
}