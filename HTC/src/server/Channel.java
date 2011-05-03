package server;

import java.util.*;

public class Channel implements Comparable<Channel>, Runnable {
	private ArrayList<User> users = null;
	private Queue<Message> messageQ = null;
	private String name;

	public Channel(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public Queue<Message> getMessageQ() {
		return messageQ;
	}

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

	}

}
