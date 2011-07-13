package msg;


public class Definition {
	public final static int DEFAULT_PORT = 41342;
	//public final static int FAMILY_PORT = 41343;
	public final static String SERVER_IP = "0.0.0.0";//InetAddress.getLocalHost().getHostAddress().toString() 
	
	public final static int MAX_CHILD = 2;
	
	public final static String TOKEN_MEAN = ":";
	public final static String TOKEN_HEAD = "\n";

	// ---------------------------------------------- HEAD ----------------------------------------------
	public final static String HEAD_SEND = "send";
	public final static String HEAD_REQUEST = "req";
	public final static String HEAD_SET = "set";
	public final static String HEAD_SUCCESS = "suc";
	public final static String HEAD_FAIL = "fail";
	public final static String HEAD_JOIN = "join";
	public final static String HEAD_EXIT = "exit";
	public final static String HEAD_SCRIPT = "script";
	
	

	// ---------------------------------------------- TYPE(FIELD) ----------------------------------------------
	public final static String TYPE_CAST = "cast";
	public final static String TYPE_CAST_BROAD = "broad";
	public final static String TYPE_CAST_UNI = "uni";
	
	public final static String TYPE_CHANNEL = "ch";
	public final static String ALL="*";
	
	public final static String TYPE_SEQ = "seq";
	
	public final static String TYPE_NICK = "nick";
	
	public final static String TYPE_FAMILY = "fam";
	public final static String TYPE_FAMILY_PARENT = "p";
	public final static String TYPE_FAMILY_CHILD1 = "c1";
	public final static String TYPE_FAMILY_CHILD2 = "c2";
	
	public final static String TYPE_IP = "ip";
//	public final static String TYPE_DSTIP = "dstip";
//	public final static String TYPE_SRCIP = "srcip";
	

	public final static String TYPE_MSG = "msg";
	
	public final static String TYPE_END = "";
	
	
	// ---------------------------------------------- Client -> Server ----------------------------------------------
	
	/*
	 * 클라이언트가 서버에 최초로 접속했을 때
	 * 
	 * HEAD_TYPE_JOIN TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" ALL TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트가 채널에 접속할려고 할 때
	 * 
	 * HEAD_TYPE_JOIN TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트가 채널에서 나갈려고 할 때
	 * 
	 * HEAD_TYPE_EXIT TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD +
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트가 서버로 메세지 브로드캐스트를 요구할 때
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * (HEAD_CAST ":" HEAD_CAST_BROAD TOKEN_HEAD) + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * HEAD_MSG ":" "메세지" TOKEN_HEAD +
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트가 서버로 스크립트를 요구할 때
	 * 
	 * HEAD_TYPE_SCRIPT TOKEN_HEAD + 
	 * HEAD_CAST ":" (HEAD_CAST_BROAD)/HEAD_CAST_UNI TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * HEAD_MSG ":" "메세지" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 자식에 대한 소켓을 정상적으로 열었을 때 
	 * 
	 * HEAD_TYPE_SUC TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_CHILD TOKEN_HEAD + 
	 * HEAD_IP ":" "자식IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 자식에 대한 소켓을 열지못했을 때
	 * 
	 * HEAD_TYPE_FAIL TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_CHILD TOKEN_HEAD + 
	 * HEAD_IP ":" "자식IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 부모에 정상적으로 연결되었을 때
	 * 
	 * HEAD_TYPE_SUC TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_PARENT TOKEN_HEAD + 
	 * HEAD_IP ":" "부모IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 부모에 연결되지 못했을 때
	 * 
	 * HEAD_TYPE_FAIL TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_PARENT TOKEN_HEAD + 
	 * HEAD_IP ":" "부모IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 서버의 버퍼에 있는 메세지를 요청
	 * 
	 * HEAD_TYPE_REQUEST TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "요구순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * IP 요청 
	 */
	
	
	// ---------------------------------------------- Server -> Client ----------------------------------------------
	
	/*
	 * 자식들에게 메세지를 브로드캐스트할 때
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * (HEAD_CAST ":" HEAD_CAST_BROAD TOKEN_HEAD) + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * HEAD_MSG ":" "메세지" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트에게 메세지를 유니캐스트할 때
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * HEAD_CAST ":" HEAD_CAST_UNI TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * HEAD_MSG ":" "메세지" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트에게 부모 IP를 알려줄 때
	 * 
	 * HEAD_TYPE_SET TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_PARENT TOKEN_HEAD + 
	 * HEAD_IP ":" "부모IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 클라이언트에게 자식 IP를 알려줄 때
	 * 
	 * HEAD_TYPE_SET TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_CHILD TOKEN_HEAD + 
	 * HEAD_IP ":" "자식IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	
	// ---------------------------------------------- Parent -> Child ----------------------------------------------
	
	/*
	 * 어떤 클라이언트가 채널에 접속했을 때
	 * 
	 * HEAD_TYPE_JOIN TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 어떤 클라이언트가 채널에서 나갔을 때
	 * 
	 * HEAD_TYPE_EXIT TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * 자식들에게 메세지를 브로드캐스트할 때
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * (HEAD_CAST ":" HEAD_CAST_BROAD TOKEN_HEAD) + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "순서번호" TOKEN_HEAD + 
	 * HEAD_NICK ":" "닉네임" TOKEN_HEAD + 
	 * HEAD_MSG ":" "메세지" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	
	// ---------------------------------------------- Child -> Parent ----------------------------------------------
	
	/*
	 * 부모의 버퍼에 있는 메세지를 요청
	 * 
	 * HEAD_TYPE_REQUEST TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "채널번호" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "요구순서번호" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
}