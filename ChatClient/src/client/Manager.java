package client;

import java.util.LinkedList;
import java.util.Queue;

import util.msg.Message;
import util.msg.sub.*;
import static util.Definition.*;

public class Manager implements Runnable {
	private final static int MAX_CHILD = 2;
	private Queue<Packet> packetQueue;

	private Server connectServer;
	private Parent connectParent;
	private Childs connectChilds;
	
	public Manager() {
		packetQueue = new LinkedList<Packet>();
	}
	
	public boolean addPacket(Packet addPacket) {
		return packetQueue.offer(addPacket);
	}
	
	/**
	 * GUI에서 서버 접속 버튼을 눌렀을 때
	 * @param serverIP
	 * @param serverPort
	 * @return
	 */
	public boolean connectServer(String serverIP, int serverPort) {
		connectServer = new Server(this, serverIP, serverPort);
		return connectServer.loginServer();
	}
	
	/**
	 * GUI에서 Send 버튼을 눌렀을 때
	 * @param channel
	 * @param nickName
	 * @param msg
	 * @return
	 */
	public boolean sendMsg(String channel, String nickName, String msg) {
		return connectServer.sendMsgToServer(channel, nickName, msg);
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
					result = connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
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
			// 내가 없을 때 서버에 요청해야 함
			
			Request request = (Request)packet.getMessage();
			
			int startSequence = request.getSeq();
			
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
	
	
	// GUI 커넥팅
	
	private void display(Message msg) {
				
	}

	private int getLastSequence() {
		return 0;
	}
	
	private Object[] getMsg(int sequence) {
		return null;
	}
}