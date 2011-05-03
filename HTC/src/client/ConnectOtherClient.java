package client;

import java.io.File;

public class ConnectOtherClient {
	private String otherClientIP;
	private int otherClientPort;
	
	public ConnectOtherClient(String otherClientIP, int otherClientPort) {
		this.otherClientIP = otherClientIP;
		this.otherClientPort = otherClientPort;
	}
	
	public boolean sendFile(File file) {
		// 작성해야 함
		
		return true;
	}
	
	public boolean receiveFile(String filePath) {
		// 작성해야 함
		
		return true;
	}
}