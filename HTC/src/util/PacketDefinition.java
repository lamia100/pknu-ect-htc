package util;

public class PacketDefinition {
	// ---------------------------------------------- NEW Packet Definition ----------------------------------------------
	
	public final static String TOKEN_MEAN = ":";
	public final static String TOKEN_HEAD = "|=|";
	
	// public final static String HEAD_TYPE = "type";
	public final static String HEAD_TYPE_SEND = "send";
	public final static String HEAD_TYPE_REQUEST = "req";
	public final static String HEAD_TYPE_SET = "set";
	public final static String HEAD_TYPE_SUCCESS = "suc";
	public final static String HEAD_TYPE_FAIL = "fail";
	public final static String HEAD_TYPE_JOIN = "join";
	public final static String HEAD_TYPE_EXIT = "exit";
	public final static String HEAD_TYPE_SCRIPT = "sc";
	
	public final static String HEAD_CAST = "cast";
	public final static String HEAD_CAST_BROAD = "broad";
	public final static String HEAD_CAST_UNI = "uni";
	
	public final static String HEAD_CHANNEL = "ch";
	
	public final static String HEAD_SEQ = "seq";
	
	public final static String HEAD_NICK = "nick";
	
	public final static String HEAD_FAMILY = "fam";
	public final static String HEAD_FAMILY_PARENT = "p";
	public final static String HEAD_FAMILY_CHILD = "c";
	
	public final static String HEAD_MSG = "msg";
	
	/*
	 * ex) SEND ����
	 * 
	 * HEAD_TYPE_SEND + TOKEN_HEAD
	 * + HEAD_CHANNEL + ":" + "3245" + TOKEN_HEAD
	 * + HEAD_SEQ + ":" + "123423" + TOKEN_HEAD
	 * + HEAD_NICK + ":" + "�г���" + TOKEN_HEAD
	 * + HEAD_MSG + ":" "�޼���" + TOKEN_HEAD + "\n"
	 */
	
	
	// ---------------------------------------------- OLD Packet Definition ----------------------------------------------
	
	// ---------------------------------------------- C L I E N T -> S E R V E R ---------------------------------------------- 
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ��û / ä�ο� �����ϰ����� ��
	 * ��Ŷ ���� : JOIN ä�ι�ȣ �г���
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 100 1 inter6
	 */
	public final static String JOIN = "100";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ��û / �޼����� ���� ��
	 * ��Ŷ ���� : SEND_MSG ä�ι�ȣ �г��� �޼���
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 101 1 inter6 Hello
	 */
	public final static String REQ_MSG = "101";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ��û / Ư�� Ŭ���̾�Ʈ�� IP�� ��û�� ��
	 * ��Ŷ ���� : REQUEST_IP Ư���г���
	 * ex) 102 inter6
	 */
	public final static String GET_IP = "102";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : �˸� / Ŭ���̾�Ʈ�� Ư�� ä�ο��� ������ ������ ��
	 * ��Ŷ ���� : EXIT ä�ι�ȣ �г���
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��(��� ä�ο��� ���� ����)
	 * ex) 103 1 inter6
	 */
	public final static String EXIT = "103";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : �˸� / Ŭ���̾�Ʈ�� �θ� ������ ������ ��
	 * ��Ŷ ���� : DISCONNECT_PARENT ä�ι�ȣ �θ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 104 1 123.213.123.32
	 */
	public final static String DISCONNECT_PARENT = "104";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : �˸� / Ŭ���̾�Ʈ�� ���� �ڽ��� ������ ������ ��
	 * ��Ŷ ���� : DISCONNECT_LEFT_SON ä�ι�ȣ �θ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 105 1 123.213.123.32
	 */
	public final static String DISCONNECT_CHILD = "105";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : �˸� / Ŭ���̾�Ʈ�� �θ� ���������� �������� ��
	 * ��Ŷ ���� : ACK_APPLY_PARENT ä�ι�ȣ �θ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 107 1 123.213.123.32
	 */
	public final static String ACK_APPLY_PARENT = "107";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ���ο� �ڽ��� ���������� �������� ��
	 * ��Ŷ ���� : ACK_APPLY_LEFT_SON ä�ι�ȣ �����ڽ�IP ���ο��ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 108 1 123.213.123.32 123.213.123.33
	 */
	public final static String ACK_APPLY_CHILD = "108";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� �θ� �������� ������ ��
	 * ��Ŷ ���� : NAK_APPLY_PARENT ä�ι�ȣ �θ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 110 1 123.213.123.32
	 */
	public final static String NAK_APPLY_PARENT = "110";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ���ο� �ڽ��� �������� ������ ��
	 * ��Ŷ ���� : NAK_APPLY_LEFT_SON ä�ι�ȣ �����ڽ�IP ���ο��ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 111 1 123.213.123.32
	 */
	public final static String NAK_APPLY_CHILD = "111";
	
	
	// ---------------------------------------------- S E R V E R -> C L I E N T ----------------------------------------------
	
	/*
	 * �з� : ���� -> Ŭ���̾�Ʈ
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ��û�� Ư�� Ŭ���̾�Ʈ�� IP�� ����
	 * ��Ŷ ���� : RESPONSE_IP Ư���г��� Ư��IP
	 * ex) 200 inter6 123.213.123.32
	 */
	public final static String RESPONSE_IP = "200";
	
	/*
	 * �з� : ���� -> Ŭ���̾�Ʈ
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� �θ� ��������
	 * ��Ŷ ���� : APPLY_PARENT ä�ι�ȣ �θ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 201 1 123.213.123.32
	 */
	public final static String APPLY_PARENT = "201";
	
	/*
	 * �з� : ���� -> Ŭ���̾�Ʈ
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ���� �ڽ��� ��������
	 * ��Ŷ ���� : APPLY_LEFT_SON ä�ι�ȣ �����ڽ�IP ���ο��ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 202 1 123.213.123.32
	 */
	public final static String APPLY_CHILD = "202";
	
	
	// ---------------------------------------------- C H I L D -> P A R E N T ----------------------------------------------
	
	/*
	 * �з� : �ڽ� -> �θ�
	 * �뵵 : �䱸 / �θ� �������ִ� ������ ���۰� �� ��ȣ�� �䱸
	 * ��Ŷ ���� : GET_SEQ ä�ι�ȣ
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 300 1
	 */
	// public final static String GET_SEQ = "300";
	
	/*
	 * �з� : �ڽ� -> �θ�
	 * �뵵 : �䱸 / �θ� �������ִ� ������ ���۰� ����ȣ�� ���� �޼����� �䱸
	 * ��Ŷ ���� : GET_MSG ä�ι�ȣ ���۹�ȣ ����ȣ
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 301 1 2348 2458
	 */
	// public final static String GET_MSG = "301";
	
	/*
	 * �з� : �ڽ� -> �θ�
	 * �뵵 : �䱸 / �θ� �������ִ� ���� �޼����� �䱸
	 * ��Ŷ ���� : REQ_SEQ_MSG ä�ι�ȣ �䱸�޼�����ȣ
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 301 1 2348
	 */
	public final static String REQ_SEQ_MSG = "302";	
	
	// ---------------------------------------------- P A R E N T -> C H I L D ----------------------------------------------
	
	/*
	 * �з� : �θ� -> �ڽ�
	 * �뵵 : ���� / �θ�(��)�� �������ִ� ������ ���۰� ����ȣ�� �˷���
	 * ��Ŷ ���� : RES_SEQ ä�ι�ȣ ���۹�ȣ ����ȣ
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 401 1 2348 2458
	 */
	// public final static String RES_SEQ = "401";
	
	/*
	 * �з� : �θ� -> �ڽ�
	 * �뵵 : ���� / �θ�(��)�� �������ִ� �޼����� ����
	 * ��Ŷ ���� : RES_SEQ ä�ι�ȣ �޼�����ȣ �г��� �޼���
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 402 1 2348 inter6 Hello
	 */
	public final static String SEND_MSG = "402";
}