package msg.sub;

import java.util.StringTokenizer;

import msg.HEAD;
import msg.Message;
import msg.TYPE;
import static msg.Definition.*;
import static util.Logger.log;

/**
 * 
 * @author inter6
 * 
 */
/*
 * �輺�� �߰�.
 * String ip �� ������� �ʰ� srcip �� dstip�� ����ϰ���.
 * src�� ���� dst�� �ִ� ���� �׳� ��������
 * dst�� ���� src�� �ִ� ���� �ش� srcip�� �θ�/�ڽ� �� ����
 * 
 * dst�� src�� �ִ� ���� �ڽ��� dst�� ���� src�� ����
 * �θ��� ���� ������ ������ �������
 * 
 */
public class Set extends Message {
	private TYPE family = null;
	//	@Deprecated
	//	private String ip = "";
	private String ip = "";
	
	//	private String srcip = "";
	
	public Set() {
		super(HEAD.SET);
	}
	
	public Set(String name, String channel, String ip, TYPE family, int sequence) {
		super(HEAD.SET);
		this.name = name;
		this.channel = channel;
		this.family = family;
		this.ip = ip;
		this.sequence = sequence;
	}
	

	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr;
		String value;
		if (token.hasMoreElements()) {
			typeStr = token.nextToken();
		} else {
			typeStr = line;
		}
		
		if (token.hasMoreElements()) {
			value = token.nextToken().trim();
		} else {
			value = "";
		}
		//System.out.println("set typestr=" + typeStr);
		TYPE type = typeTable.get(typeStr);
		
		switch (type) {
			case CHANNEL:
				channel = value;
				
				break;
			case FAMILY:
				if (TYPE.FAMILY_PARENT.toString().equals(value)) {
					family = TYPE.FAMILY_PARENT;
				} else if (TYPE.FAMILY_CHILD1.toString().equals(value)) {
					family = TYPE.FAMILY_CHILD1;
				} else if (TYPE.FAMILY_CHILD2.toString().equals(value)) {
					family = TYPE.FAMILY_CHILD2;
				}
				
				break;
			case SEQ:
				try {
					sequence = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					log("Set",e);
					return false;
				}
				
				break;
			case IP:
				ip = value;
				break;
			case NICK:
				name = value;
				break;
			case END:
				return true;
		}
		
		return false;
	}
	
	public TYPE getFamily() {
		return family;
	}
	
	public String getIP() {
		return ip;
	}
	
	@Override
	public String toString() {
		String format = "";
		format = HEAD_SET + TOKEN_HEAD + TYPE_CHANNEL + ":" + channel + TOKEN_HEAD + TYPE_FAMILY + ":" + family.toString() + TOKEN_HEAD
				+ TYPE_NICK + ":" + name + TOKEN_HEAD + TYPE_IP + ":" + ip + TOKEN_HEAD + TYPE_SEQ + ":" + sequence + TOKEN_HEAD
				+ TOKEN_HEAD;
		
		return format;
	}
}