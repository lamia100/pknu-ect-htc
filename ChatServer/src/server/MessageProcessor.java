package server;

import java.util.LinkedList;
import java.util.Queue;

import util.msg.Message;

public class MessageProcessor implements Runnable {
	private boolean isRun = true;
	Queue<Message> queue = new LinkedList<Message>();
	
	public synchronized void enqueue(Message message) {
		queue.add(message);
	}
	
	public void stop() {
		isRun = false;
	}
	private synchronized Message dequeue()
	{
		return queue.remove();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			if (!queue.isEmpty()) {
				Message message = dequeue();
				switch (message.getType()) {
					case SEND:
						//
						break;
					default:
						break;
				}
			}
			
		}
	}
	
}
