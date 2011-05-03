package util;

public class PacketDefinition {
	public final static String TOKEN = "|=|";
	
	// ---------------------------------------------- C L I E N T -> S E R V E R ---------------------------------------------- 
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 요청 / 채널에 접속하고자할 때
	 * 패킷 형태 : JOIN 채널번호 닉네임
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 100 1 inter6
	 */
	public final static String JOIN = "100";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 요청 / 메세지를 보낼 때
	 * 패킷 형태 : SEND_MSG 채널번호 닉네임 메세지
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 101 1 inter6 Hello
	 */
	public final static String SEND_MSG = "101";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 요청 / 특정 클라이언트의 IP를 요청할 때
	 * 패킷 형태 : REQUEST_IP 특정닉네임
	 * ex) 102 inter6
	 */
	public final static String GET_IP = "102";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 알림 / 클라이언트가 특정 채널에서 접속을 종료할 때
	 * 패킷 형태 : EXIT 채널번호 닉네임
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 103 1 inter6
	 */
	public final static String EXIT = "103";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 알림 / 클라이언트의 부모가 접속이 끊겼을 때
	 * 패킷 형태 : DISCONNECT_PARENT 채널번호 부모IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 104 1 123.213.123.32
	 */
	public final static String DISCONNECT_PARENT = "104";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 알림 / 클라이언트의 왼쪽 자식이 접속이 끊겼을 때
	 * 패킷 형태 : DISCONNECT_LEFT_SON 채널번호 부모IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 105 1 123.213.123.32
	 */
	public final static String DISCONNECT_CHILD = "105";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 알림 / 클라이언트의 부모를 정상적으로 연결했을 때
	 * 패킷 형태 : ACK_APPLY_PARENT 채널번호 부모IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 107 1 123.213.123.32
	 */
	public final static String ACK_APPLY_PARENT = "107";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 응답 / 클라이언트의 새로운 자식을 정상적으로 연결했을 때
	 * 패킷 형태 : ACK_APPLY_LEFT_SON 채널번호 이전자식IP 새로운자식IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 108 1 123.213.123.32 123.213.123.33
	 */
	public final static String ACK_APPLY_CHILD = "108";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 응답 / 클라이언트의 부모를 연결하지 못했을 때
	 * 패킷 형태 : NAK_APPLY_PARENT 채널번호 부모IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 110 1 123.213.123.32
	 */
	public final static String NAK_APPLY_PARENT = "110";
	
	/*
	 * 분류 : 클라이언트 -> 서버
	 * 용도 : 응답 / 클라이언트의 새로운 자식을 연결하지 못했을 때
	 * 패킷 형태 : NAK_APPLY_LEFT_SON 채널번호 이전자식IP 새로운자식IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 111 1 123.213.123.32
	 */
	public final static String NAK_APPLY_CHILD = "111";
	
	
	// ---------------------------------------------- S E R V E R -> C L I E N T ----------------------------------------------
	
	/*
	 * 분류 : 서버 -> 클라이언트
	 * 용도 : 응답 / 클라이언트가 요청한 특정 클라이언트의 IP를 응답
	 * 패킷 형태 : RESPONSE_IP 특정닉네임 특정IP
	 * ex) 200 inter6 123.213.123.32
	 */
	public final static String RESPONSE_IP = "200";
	
	/*
	 * 분류 : 서버 -> 클라이언트
	 * 용도 : 응답 / 클라이언트의 부모를 가르쳐줌
	 * 패킷 형태 : APPLY_PARENT 채널번호 부모IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 201 1 123.213.123.32
	 */
	public final static String APPLY_PARENT = "201";
	
	/*
	 * 분류 : 서버 -> 클라이언트
	 * 용도 : 응답 / 클라이언트의 왼쪽 자식을 가르쳐줌
	 * 패킷 형태 : APPLY_LEFT_SON 채널번호 이전자식IP 새로운자식IP
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 202 1 123.213.123.32
	 */
	public final static String APPLY_CHILD = "202";
	
	
	// ---------------------------------------------- C H I L D -> P A R E N T ----------------------------------------------
	
	/*
	 * 분류 : 자식 -> 부모
	 * 용도 : 요구 / 부모가 가지고있는 버퍼의 시작과 끝 번호를 요구
	 * 패킷 형태 : GET_SEQ 채널번호
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 300 1
	 */
	public final static String GET_SEQ = "300";
	
	/*
	 * 분류 : 자식 -> 부모
	 * 용도 : 요구 / 부모가 가지고있는 버퍼의 시작과 끝번호에 대한 메세지를 요구
	 * 패킷 형태 : GET_MSG 채널번호 시작번호 끝번호
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 301 1 2348 2458
	 */
	public final static String GET_MSG = "301";
	
	
	// ---------------------------------------------- P A R E N T -> C H I L D ----------------------------------------------
	
	/*
	 * 분류 : 부모 -> 자식
	 * 용도 : 요구 / 부모(내)가 가지고있는 버퍼의 시작과 끝번호를 알려줌
	 * 패킷 형태 : RES_SEQ 채널번호 시작번호 끝번호
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 401 1 2348 2458
	 */
	public final static String RES_SEQ = "401";
	
	/*
	 * 분류 : 부모 -> 자식
	 * 용도 : 요구 / 부모(내)가 가지고있는 메세지를 전송
	 * 패킷 형태 : RES_SEQ 채널번호 메세지번호 닉네임 메세지
	 * -- 이 때 채널번호가 0이면 전체 채널
	 * ex) 402 1 2348 inter6 Hello
	 */
	public final static String RES_MSG = "402";
}