package client;

import java.util.LinkedList;
import java.util.Queue;

public class ConnectManager implements Runnable {
	private final static int MAX_CHILD = 2;
	private Queue<Packet> totalQueue;
	
	public ConnectManager() {
		totalQueue = new LinkedList<Packet>();
	}
	
	public boolean addPacket(Packet addPacket) {
		return totalQueue.offer(addPacket);
	}
	
	@Override
	public void run() {
		while (true) {
			Packet process = null;
			
			if ((process = totalQueue.poll()) != null) {
				process.performService();
			}
		}
	}
}