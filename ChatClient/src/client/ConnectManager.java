package client;

import java.util.LinkedList;
import java.util.Queue;

import util.msg.Message;
import util.msg.sub.*;
import static util.Definition.*;

public class ConnectManager implements Runnable {
	private final static int MAX_CHILD = 2;
	private Queue<Packet> packetQueue;

	private ConnectServer connectServer;
	private ConnectParent connectParent;
	private ConnectChild connectChild;
	
	public ConnectManager() {
		packetQueue = new LinkedList<Packet>();
	}
	
	public boolean addPacket(Packet addPacket) {
		return packetQueue.offer(addPacket);
	}
	
	public boolean connectServer(String serverIP, int serverPort) {
		connectServer = new ConnectServer(this, serverIP, serverPort);
		return connectServer.loginServer();
	}
	
	public boolean sendMsg() {
		return false;
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
				if (send.getSeq() == getLastSequence() - 1) {
					display(send);
					result = connectChild.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
				}
				else {
					result = connectParent.requestMsgToParent(send.getChannel(), Integer.toString(getLastSequence() + 1));
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
			while (startSequence <= getLastSequence() && sendResult) {
				Object msg[] = getMsg(startSequence++);
				sendResult = connectChild.sendMsgToSomeChild(packet.getFromIP(), (String)msg[0], (Integer)msg[1], (String)msg[2], (String)msg[3]);
			}
			
			result = sendResult;
			
			break;
		case SET:
			Set set = ((Set)packet.getMessage());
			
			switch (set.getFamily()) {
			case FAMILY_PARENT:
				ConnectParent newParent = new ConnectParent(this, set.getIp(), DEFAULT_PORT);
				
				if (newParent.loginParent()) {
					connectParent = newParent;
					result = connectServer.successConnectToParent(set.getChannel(), set.getIp());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp());
				}
				
				break;
			case FAMILY_CHILD:
				if (connectChild.getChildSize() < MAX_CHILD) {
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
			result = connectChild.whoJoinToAllChild(join.getChannel(), join.getNick());
			break;
		case EXIT:
			Exit exit = (Exit)packet.getMessage(); 
			
			display(exit);
			result = connectChild.whoExitToAllChild(exit.getChannel(), exit.getNick());
			break;
		}
		
		return result;
	}
	
	
	// GUI Ä¿³ØÆÃ
	
	private void display(Message msg) {
				
	}

	private int getLastSequence() {
		return 0;
	}
	
	private Object[] getMsg(int sequence) {
		return null;
	}
}