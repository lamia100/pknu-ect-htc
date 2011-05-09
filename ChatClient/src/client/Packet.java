package client;

import util.msg.Message;

public class Packet {	
	private Message packet;
	private String fromIP;
	
	public Packet(Message packet, String fromIP) {
		this.packet = packet;
		this.fromIP = fromIP;
	}
	
	public boolean performService() {		
		return false;
	}
}