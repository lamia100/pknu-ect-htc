package client;

import java.io.File;

public class ConnectOther {
	private String otherClientIP;
	private int otherClientPort;
	
	public ConnectOther(String otherClientIP, int otherClientPort) {
		this.otherClientIP = otherClientIP;
		this.otherClientPort = otherClientPort;
	}
	
	public boolean sendFile(File file) {
		// �ۼ��ؾ� ��
		
		return true;
	}
	
	public boolean receiveFile(String filePath) {
		// �ۼ��ؾ� ��
		
		return true;
	}
}