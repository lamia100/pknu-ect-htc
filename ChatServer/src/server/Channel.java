package server;

import java.util.*;

import util.msg.Message;

/**
 * 
 * @author "±è¼ºÇö"
 * 
 */
public class Channel implements Comparable<Channel>, Runnable {
	private ArrayList<User> users = null;
	private Set<String> names = null;
	private Queue<Message> messageQ = null;
	private String name;
	private boolean isRun = true;
	
	public Channel(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		users = new ArrayList<User>();
		names = new HashSet<String>();
	}
	
	public synchronized void enqueue(Message message) {
		messageQ.offer(message);
	}
	
	private synchronized Message dequeue() {
		return messageQ.poll();
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public synchronized void add(User user) {
		if (!names.contains(user.getName())) {
			names.add(user.getName());
			users.add(user);
		}
	}
	
	/*
	public Queue<Message> getMessageQ() {
		return messageQ;
	}
	*/

	public String getName() {
		return name;
	}
	
	@Override
	public int compareTo(Channel o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			if (!messageQ.isEmpty()) {
				Message message = dequeue();
				switch (message.getType()) {
					case SEND:
						users.get(0).send(message);
						break;
					case JOIN:
						users.get(0).send(message);
						break;
					default:
						break;
				}
			}
		}
	}
	
}
