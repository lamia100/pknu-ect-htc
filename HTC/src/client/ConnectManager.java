package client;

import java.util.LinkedList;
import java.util.Queue;

public class ConnectManager implements Runnable {
	public Queue<Packet> totalQueue;
	
	public ConnectManager() {
		totalQueue = new LinkedList<Packet>();
	}

	@Override
	public void run() {
		while (true) {
			Packet process = totalQueue.poll();
			
			if (process != null) {
				
			}
		}
	}
}
