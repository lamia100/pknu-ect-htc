package client;

import server.msg.Message;

public class Packet {
	public final static int FROM_SERVER = 0;
	public final static int FROM_PARENT = 1;
	public final static int FROM_CHILD_0 = 2;
	public final static int FROM_CHILD_1 = 3;
	
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