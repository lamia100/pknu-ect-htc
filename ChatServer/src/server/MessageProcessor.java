package server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import util.msg.Message;
import util.msg.sub.*;
/**
 * 
 * @author "±è¼ºÇö"
 *
 */
public class MessageProcessor implements Runnable {
	private boolean isRun = true;
	final Queue<Message> messageQ = new LinkedList<Message>();

	private Map<String , User> users;
	private Map<String , Channel> channels;
	public MessageProcessor()
	{
		channels = new HashMap<String, Channel>();
		users = new HashMap<String, User>();
	}
	
	public synchronized boolean add(User user)
	{
		
		if(users.containsKey(user.getName()))
		{
			user.disconnect();			
			return false;
		}else{
			users.put(user.getName(), user);
			return true;
		}
	}
	
	public synchronized boolean add(Channel channel)
	{
		if(channels.containsKey(channel.getName()))
		{		
			return false;
		}else{
			channels.put(channel.getName(),channel);
			return true;
		}
	}
	public synchronized void enqueue(Message message) {
		messageQ.add(message);
	}
	
	public void stop() {
		isRun = false;
	}
	private synchronized Message dequeue()
	{
		return messageQ.remove();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			if (!messageQ.isEmpty()) {
				Message message = dequeue();
				switch (message.getType()) {
					case SEND:
						//
						send((Send)message);
						break;
					case JOIN:
						join((Join)message);
						break;
					default:
						break;
				}
			}
			
		}
	}
	
	private synchronized void send(Send send)
	{
		Channel channel=channels.get(send.getChannel());
		if(channel != null)
		{
			channel.enqueue(send);
		}
	}
	private synchronized void join(Join join)
	{
		Channel channel=channels.get(join.getChannel());
		if(channel != null)
		{
			channel.enqueue(join);
		}else{
			channel=new Channel(join.getChannel());
			new Thread(channel).start();
			channel.enqueue(join);
			
		}
		
	}
	
}
