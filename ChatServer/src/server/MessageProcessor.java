package server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.msg.Message;
import util.msg.sub.*;

/**
 * 
 * @author "김성현"
 * 
 */
public class MessageProcessor implements Runnable {
	private boolean isRun = true;
	private final BlockingQueue<Message> messageQ;//blockingQueue;
	//final Queue<Message> messageQ1 = new LinkedList<Message>();
	
	private final Map<String, User> users;
	private final Map<String, Channel> channels;
	
	public MessageProcessor() {
		channels = new HashMap<String, Channel>();
		users = new HashMap<String, User>();
		messageQ = new LinkedBlockingQueue<Message>();
	}
	
	public synchronized boolean add(User user) {
		if (users.containsKey(user.getName())) {
			user.disconnect();
			return false;
		} else {
			users.put(user.getName(), user);
			return true;
		}
	}
	
	public synchronized boolean add(Channel channel) {
		if (channels.containsKey(channel.getName())) {
			return false;
		} else {
			channels.put(channel.getName(), channel);
			return true;
		}
	}
	
	public synchronized void enqueue(Message message) {
		System.out.println("enqueue : " + message);
		messageQ.offer(message);
	}
	
	public void stop() {
		isRun = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			System.out.println("MP 동작중");
			Message message = null;
			try {
				message = messageQ.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("MP : 메세지 있음");
			System.out.println(message);
			if (message != null) {
				switch (message.getType()) {
					case SEND:
						//
						send((Send) message);
						break;
					case JOIN:
						join((Join) message);
						break;
					default:
						break;
				}
			}
			
		}
		
	}
	
	private synchronized void send(Send send) {
		Channel channel = channels.get(send.getChannel());
		if (channel != null) {
			System.out.println("메세지 받았음");
			channel.enqueue(send);
		}
	}
	
	private synchronized void join(Join join) {
		Channel channel = channels.get(join.getChannel());
		if (channel != null) {
			channel.enqueue(join);
		} else {
			channel = new Channel(join.getChannel());
			new Thread(channel).start();
			channel.enqueue(join);
			
		}
		
	}
	
	public User getUser(String nick) {
		// TODO Auto-generated method stub
		return users.get(nick);
	}
	
}
