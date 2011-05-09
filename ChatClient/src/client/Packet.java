package client;

import util.msg.Message;

public class Packet {	
	private Message message;
	private String fromIP;
	
	public Packet(Message message, String fromIP) {
		this.message = message;
		this.fromIP = fromIP;
	}
	
	public Message getMessage() {
		return message;
	}

	public String getFromIP() {
		return fromIP;
	}
}