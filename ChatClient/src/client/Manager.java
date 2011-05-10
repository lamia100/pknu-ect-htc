package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import test.GUI;
import util.msg.Message;
import util.msg.sub.*;
import static util.Definition.*;

public class Manager implements Runnable {
	private final static int MAX_CHILD = 2;
	
	private Queue<Packet> packetQueue;
	private ArrayList<Send> msgQueue;
	private int msgQueueOffset;
	
	private Server connectServer;
	private Parent connectParent;
	private Childs connectChilds;
	
	private GUI gui;
	private boolean isFirstConnect;
	
	public Manager(GUI gui) {
		packetQueue = new LinkedList<Packet>();
		msgQueue = new ArrayList<Send>();
		msgQueueOffset = 0;
		
		this.gui = gui;
		isFirstConnect = true;
	}
	
	public boolean addPacket(Packet addPacket) {
		return packetQueue.offer(addPacket);
	}
	
	private int getLastSequence() {
		return msgQueueOffset + msgQueue.size() - 1;
	}
	
	private Object[] getMsg(int sequence) {
		Send msg = msgQueue.get(sequence - msgQueueOffset - 1);
		
		Object result[] = new Object[4];
		result[0] = msg.getChannel();
		result[1] = msg.getSeq();
		result[2] = msg.getNick();
		result[3] = msg.getMsg();
		
		return result;
	}
	
	// ------------------------------------------------- GUI input -------------------------------------------------
	
	public boolean connectServer(String serverIP, int serverPort) {
		connectServer = new Server(this, serverIP, serverPort);
		return connectServer.loginServer();
	}
	
	public void disconnectServer() {
		connectParent.logoutParent();
		connectChilds.closeAllChild();
		connectServer.logoutServer();
	}
	
	public boolean joinChannel(String channel, String nickName) {
		connectServer.joinChannel(ALL, nickName);
		return connectServer.joinChannel(channel, nickName);
	}
	
	public boolean exitChannel(String channel, String nickName) {
		return connectServer.exitChannel(channel, nickName);
	}
	
	public boolean sendMsg(String channel, String nickName, String msg) {
		return connectServer.sendMsgToServer(channel, nickName, msg);
	}
	
	
	// ------------------------------------------------- GUI output -------------------------------------------------
	
	private void display(Message msg) {
		switch (msg.getType()) {
		case SEND:
			Send send = (Send)msg;
			msgQueue.add(send);
			gui.setMsg(send.getNick() + " : " + send.getMsg());
		case JOIN:
			Join join = (Join)msg;
			gui.setInfo(join.getChannel() + " 채널에 " + join.getNick() + "이(가) 접속하였습니다.");
		case EXIT:
			Exit exit = (Exit)msg;
			gui.setInfo(exit.getChannel() + " 채널에서 " + exit.getNick() + "이(가) 나가셨습니다.");
		}
	}
	
	
	// ------------------------------------------------- P E R F O R M -------------------------------------------------
	
	@Override
	public void run() {
		while (true) {
			Packet packet = null;
			
			if ((packet = packetQueue.poll()) != null) {
				if (packet.getMessage().isValid()) {
					performService(packet);
				}
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
					msgQueueOffset = send.getSeq();
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
			
			// 내가 없을 때 서버에 요청해야 함
			
			boolean sendResult = true;
			while (startSequence <= getLastSequence() && sendResult) {
				Object msg[] = getMsg(startSequence++);
				sendResult = connectChilds.sendMsgToSomeChild(packet.getFromIP(), (String)msg[0], (Integer)msg[1], (String)msg[2], (String)msg[3]);
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
					result = connectServer.successConnectToParent(set.getChannel(), set.getIp());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp());
				}
				
				break;
			case FAMILY_CHILD:
				if (connectChilds.getChildSize() < MAX_CHILD) {
					result = connectServer.successOpenSocketForChild(set.getChannel(), set.getIp());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp());
				}
				
				break;
			default:	
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