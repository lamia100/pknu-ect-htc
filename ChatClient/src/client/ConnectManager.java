package client;

import java.util.LinkedList;
import java.util.Queue;

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
	
	@Override
	public void run() {
		while (true) {
			Packet packet = null;
			
			if ((packet = packetQueue.poll()) != null) {
				performService(packet);
			}
		}
	}
	
	public boolean performService(Packet packet) {
		boolean result = false;
		
		switch (packet.getMessage().getType()) {
		case SEND:
			/*
			if (브로드캐스트라면) {
				display();
				result = connectChild.sendMsgToAllChild(channel, sequence, nickName, msg);
			}
			else if (유니캐스트라면) {
				display();
				result = true;
			}
			*/
			
			break;
		case REQUEST:
			/*
			int startSequence = packet.sequence;
			
			boolean sendResult = true;
			while (startSequence <= lastSequence && sendResult) {
				String msg[] = getMsg(startSequence++);
				sendResult = connectChild.sendMsgToAllChild(msg[0], msg[1], msg[2], msg[3]);
			}
			
			result = sendResult;
			*/
			
			break;
		case SET:
			break;
		case SUCCESS:
			break;
		case FAIL:
			break;
		case JOIN:
			// result = connectChild.whoJoinToAllChild(channel, nickName);
			break;
		case EXIT:
			// result = connectChild.whoExitToAllChild(channel, nickName);
			break;
		}
		
		return false;
	}
}