package msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.User;

public class Packet {
	public static final BlockingQueue<Packet> Q = new LinkedBlockingQueue<Packet>();
	Message message;
	User user;
	
	public Packet(Message message, User user) {
		this.message = message;
		this.user = user;
	}
	public HEAD getHead(){
		return message.getHead();
	}
	public Message getMessage() {
		return message;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
