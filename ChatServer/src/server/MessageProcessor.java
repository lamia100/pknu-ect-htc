package server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.Definition;
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
	
	/*
	public synchronized boolean add(Channel channel) {
		if (channels.containsKey(channel.getName())) {
			return false;
		} else {
			channels.put(channel.getName(), channel);
			return true;
		}
	}
	*/
	public synchronized void enqueue(Message message) {
		log("enqueue\n"+message.toString());
		messageQ.offer(message);
	}
	
	public void stop() {
		isRun = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			log("동작중");
			Message message = null;
			try {
				message = messageQ.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log("메세지 있음\n" + message);
			if (message != null) {
				switch (message.getType()) {
					case SEND:
						send((Send) message);
						break;
					case JOIN:
						join((Join) message);
						break;
					case EXIT:
						exit((Exit) message);
						break;
					default:
						break;
				}
			}
			
		}
		
	}
	
	private void exit(Exit exit) {
		// TODO Auto-generated method stub
		if (Definition.ALL.equals(exit.getChannel())) {
			User user = users.get(exit.getNick());
			if (user != null)
				for (Channel channel : user.getChannels()) {
					channel.enqueue(new Exit(channel.getName(), exit.getNick()));
				}
		}
	}
	
	private synchronized void send(Send send) {
		Channel channel = channels.get(send.getChannel());
		if (channel != null) {
			log("메세지 받았음");
			channel.enqueue(send);
		} else {
			log(send.getChannel() + "체널이 없음");
			
		}
	}
	
	private synchronized void join(Join join) {
		Channel channel = channels.get(join.getChannel());
		if (channel != null) {
			channel.enqueue(join);
		} else {
			channel = new Channel(join.getChannel());
			channels.put(join.getChannel(), channel);
			new Thread(channel).start();
			channel.enqueue(join);
			
		}
		
	}
	
	public User getUser(String nick) {
		// TODO Auto-generated method stub
		return users.get(nick);
	}
	
	public void remove(User user) {
		// TODO Auto-generated method stub
		users.remove(user.getName());
	}
	
	private void log(String message) {
		System.out.println("MessageProcessor");
		System.out.println(message);
		System.out.println("----------------------------");
	}
	
}
