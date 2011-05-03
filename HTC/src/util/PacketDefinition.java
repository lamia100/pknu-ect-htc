package util;

public class PacketDefinition {
	public final static String TOKEN = "|=|";
	
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
	public final static String SEND_MSG = "101";
	
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
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
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
	public final static String DISCONNECT_LEFT_SON = "105";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : �˸� / Ŭ���̾�Ʈ�� ������ �ڽ��� ������ ������ ��
	 * ��Ŷ ���� : DISCONNECT_LEFT_SON ä�ι�ȣ �θ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 106 1 123.213.123.32
	 */
	public final static String DISCONNECT_RIGHT_SON = "106";
	
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
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ���� �ڽ��� ���������� �������� ��
	 * ��Ŷ ���� : ACK_APPLY_LEFT_SON ä�ι�ȣ �����ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 108 1 123.213.123.32
	 */
	public final static String ACK_APPLY_LEFT_SON = "108";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ������ �ڽ��� ���������� �������� ��
	 * ��Ŷ ���� : ACK_APPLY_RIGHT_SON ä�ι�ȣ �������ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 109 1 123.213.123.32
	 */
	public final static String ACK_APPLY_RIGHT_SON = "109";
	
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
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ���� �ڽ��� �������� ������ ��
	 * ��Ŷ ���� : NAK_APPLY_LEFT_SON ä�ι�ȣ �����ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 111 1 123.213.123.32
	 */
	public final static String NAK_APPLY_LEFT_SON = "111";
	
	/*
	 * �з� : Ŭ���̾�Ʈ -> ����
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ������ �ڽ��� �������� ������ ��
	 * ��Ŷ ���� : NAK_APPLY_RIGHT_SON ä�ι�ȣ �������ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 112 1 123.213.123.32
	 */
	public final static String NAK_APPLY_RIGHT_SON = "112";
	
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
	 * ��Ŷ ���� : APPLY_LEFT_SON ä�ι�ȣ �����ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 202 1 123.213.123.32
	 */
	public final static String APPLY_LEFT_SON = "202";
	
	/*
	 * �з� : ���� -> Ŭ���̾�Ʈ
	 * �뵵 : ���� / Ŭ���̾�Ʈ�� ������ �ڽĸ� ��������
	 * ��Ŷ ���� : APPLY_RIGHT_SON ä�ι�ȣ �������ڽ�IP
	 * -- �� �� ä�ι�ȣ�� 0�̸� ��ü ä��
	 * ex) 203 1 123.213.123.32
	 */
	public final static String APPLY_RIGHT_SON = "203";
}