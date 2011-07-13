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
 * 김성현 추가.
 * String ip 는 사용하지 않고 srcip 와 dstip를 사용하겠음.
 * src가 없고 dst만 있는 경우는 그냥 설정만함
 * dst가 없고 src만 있는 경우는 해당 srcip를 부모/자식 을 제거
 * 
 * dst와 src가 있는 경우는 자식중 dst인 것을 src로 변경
 * 부모인 경우는 있으나 없으나 관계없음
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