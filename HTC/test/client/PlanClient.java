package client;

public class PlanClient {

	// To Server - Start
	void reqeustJoinChannel(int channel) {
		
	}
	
	void requestMsg(String msg) {
		
	}
	
	void requestCertainClientIP(String certainNick) {
		// � Ŭ���̾�Ʈ���� ���� ������ ���� IP�� ��û
	}
	
	void reportMyFamilyDisconnect() {
		// �θ� �Ǵ� �ڽ��� ������ ��������
	}
	
	void reportImExit() {
		
	}
	// To Server - End
	
	
	// From Server - Start
	void receiveMyFamily() {
		// �θ� �Ǵ� �ڽ��� �������� ����
	}
	
	void receiveCertainClientIP() {
		
	}
	// From Server - End
	
	
	// To Parent - Start
	void requestSequenceNumber() {
		// �θ� �������ִ� �޽����� ������ȣ(���� ~ ��)�� ��û 
	}
	
	void requestBufferMsg(int startSeq, int endSeq) {
		// �θ� �������ִ� ���� �޽����� ��û
	}
	// To Parent - End
	
	
	// From Parent - Start	
	void receiveSequenceNumber() {
		
	}
	
	void receiveMsg() {
		
	}
	// From Parent - End
	
	
	// To Child - Start
	void responseSequenceNumber() {
		
	}
	
	void sendMsg(String msg) {
		
	}
	// To Child - End
	
	
	// Self Function
	void displayMsg(String msg) {
		
	}
}
