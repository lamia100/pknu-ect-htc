package client;

public class PlanClient {

	// To Server - Start
	void reqeustJoinChannel(int channel) {
		
	}
	
	void requestMsg(String msg) {
		
	}
	
	void requestCertainClientIP(String certainNick) {
		// 어떤 클라이언트에게 파일 전송을 위해 IP를 요청
	}
	
	void reportMyFamilyDisconnect() {
		// 부모 또는 자식의 연결이 끊어졌음
	}
	
	void reportImExit() {
		
	}
	// To Server - End
	
	
	// From Server - Start
	void receiveMyFamily() {
		// 부모 또는 자식의 구성원을 받음
	}
	
	void receiveCertainClientIP() {
		
	}
	// From Server - End
	
	
	// To Parent - Start
	void requestSequenceNumber() {
		// 부모가 가지고있는 메시지의 순서번호(시작 ~ 끝)를 요청 
	}
	
	void requestBufferMsg(int startSeq, int endSeq) {
		// 부모가 가지고있는 버퍼 메시지를 요청
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
