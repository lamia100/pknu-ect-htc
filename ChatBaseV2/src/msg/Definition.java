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
	 * Ŭ���̾�Ʈ�� ������ ���ʷ� �������� ��
	 * 
	 * HEAD_TYPE_JOIN TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" ALL TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ�� ä�ο� �����ҷ��� �� ��
	 * 
	 * HEAD_TYPE_JOIN TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ�� ä�ο��� �������� �� ��
	 * 
	 * HEAD_TYPE_EXIT TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD +
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ�� ������ �޼��� ��ε�ĳ��Ʈ�� �䱸�� ��
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * (HEAD_CAST ":" HEAD_CAST_BROAD TOKEN_HEAD) + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * HEAD_MSG ":" "�޼���" TOKEN_HEAD +
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ�� ������ ��ũ��Ʈ�� �䱸�� ��
	 * 
	 * HEAD_TYPE_SCRIPT TOKEN_HEAD + 
	 * HEAD_CAST ":" (HEAD_CAST_BROAD)/HEAD_CAST_UNI TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * HEAD_MSG ":" "�޼���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * �ڽĿ� ���� ������ ���������� ������ �� 
	 * 
	 * HEAD_TYPE_SUC TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_CHILD TOKEN_HEAD + 
	 * HEAD_IP ":" "�ڽ�IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * �ڽĿ� ���� ������ ���������� ��
	 * 
	 * HEAD_TYPE_FAIL TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_CHILD TOKEN_HEAD + 
	 * HEAD_IP ":" "�ڽ�IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * �θ� ���������� ����Ǿ��� ��
	 * 
	 * HEAD_TYPE_SUC TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_PARENT TOKEN_HEAD + 
	 * HEAD_IP ":" "�θ�IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * �θ� ������� ������ ��
	 * 
	 * HEAD_TYPE_FAIL TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_PARENT TOKEN_HEAD + 
	 * HEAD_IP ":" "�θ�IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * ������ ���ۿ� �ִ� �޼����� ��û
	 * 
	 * HEAD_TYPE_REQUEST TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "�䱸������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * IP ��û 
	 */
	
	
	// ---------------------------------------------- Server -> Client ----------------------------------------------
	
	/*
	 * �ڽĵ鿡�� �޼����� ��ε�ĳ��Ʈ�� ��
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * (HEAD_CAST ":" HEAD_CAST_BROAD TOKEN_HEAD) + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * HEAD_MSG ":" "�޼���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ���� �޼����� ����ĳ��Ʈ�� ��
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * HEAD_CAST ":" HEAD_CAST_UNI TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * HEAD_MSG ":" "�޼���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ���� �θ� IP�� �˷��� ��
	 * 
	 * HEAD_TYPE_SET TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_PARENT TOKEN_HEAD + 
	 * HEAD_IP ":" "�θ�IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * Ŭ���̾�Ʈ���� �ڽ� IP�� �˷��� ��
	 * 
	 * HEAD_TYPE_SET TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_FAMILY ":" HEAD_FAMILY_CHILD TOKEN_HEAD + 
	 * HEAD_IP ":" "�ڽ�IP" TOKEN_HEAD TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	
	// ---------------------------------------------- Parent -> Child ----------------------------------------------
	
	/*
	 * � Ŭ���̾�Ʈ�� ä�ο� �������� ��
	 * 
	 * HEAD_TYPE_JOIN TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * � Ŭ���̾�Ʈ�� ä�ο��� ������ ��
	 * 
	 * HEAD_TYPE_EXIT TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	/*
	 * �ڽĵ鿡�� �޼����� ��ε�ĳ��Ʈ�� ��
	 * 
	 * HEAD_TYPE_SEND TOKEN_HEAD + 
	 * (HEAD_CAST ":" HEAD_CAST_BROAD TOKEN_HEAD) + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "������ȣ" TOKEN_HEAD + 
	 * HEAD_NICK ":" "�г���" TOKEN_HEAD + 
	 * HEAD_MSG ":" "�޼���" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
	
	
	// ---------------------------------------------- Child -> Parent ----------------------------------------------
	
	/*
	 * �θ��� ���ۿ� �ִ� �޼����� ��û
	 * 
	 * HEAD_TYPE_REQUEST TOKEN_HEAD + 
	 * HEAD_CHANNEL ":" "ä�ι�ȣ" TOKEN_HEAD + 
	 * HEAD_SEQ ":" "�䱸������ȣ" TOKEN_HEAD + 
	 * TOKEN_HEAD
	 */
}