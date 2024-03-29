package util.msg.sub;

import java.util.StringTokenizer;
import util.msg.Message;
import util.msg.TYPE;
import static util.Definition.*;

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
	private String channel = "";
	private TYPE family = null;
//	@Deprecated
//	private String ip = "";
	private String dstip = "";
	private String srcip = "";
	private int sequence = 0;
	
	public Set() {
		super(TYPE.SET);
	}
	
	public Set(String channel, String dstip, TYPE family, int sequence) {
		super(TYPE.SET);
		
		this.channel = channel;
		this.family = family;
		this.dstip = dstip;
		this.sequence = sequence;
	}
	public Set(String channel, String srcip,String dstip, TYPE family, int sequence) {
		super(TYPE.SET);
		
		this.channel = channel;
		this.family = family;
		this.dstip = dstip;
		this.srcip = srcip;
		this.sequence = sequence;
	}
	
	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr;
		String value;
		if (token.hasMoreElements()) {
			typeStr = token.nextToken();
			value = token.nextToken().trim();
		} else {
			typeStr = line;
			value = "";
		}
		
		TYPE type = getStringToType(typeStr);
		
		switch (type) {
			case CHANNEL:
				channel = value;
				
				break;
			case FAMILY:
				if (TYPE.FAMILY_PARENT.toString().equals(value)) {
					family = TYPE.FAMILY_PARENT;
				} else if (TYPE.FAMILY_CHILD.toString().equals(value)) {
					family = TYPE.FAMILY_CHILD;
				} else {
					isValid = false;
				}
				
				break;
//			case IP:
//				ip = value;
//				break;
			case SEQ:
				try {
					sequence = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					isValid = false;
					return false;
				}
				
				break;
			case DSTIP:
				dstip = value;
				break;
			
			case SRCIP:
				srcip = value;
				break;
			case END:
				return true;
		}
		
		return false;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public TYPE getFamily() {
		return family;
	}
	/**
	 * getDstip로 변경
	 * @return dstip
	 */
	@Deprecated 
	public String getIp() {
		return dstip;
	}
	
	public String getSrcip() {
		return srcip;
	}
	
	public String getDstip() {
		return dstip;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	@Override
	public String toString() {
		String format = "";
		
		if ("".equals(srcip)) {
			format = HEAD_TYPE_SET + TOKEN_HEAD +
			HEAD_CHANNEL + ":" + channel + TOKEN_HEAD +
			HEAD_FAMILY + ":"+ family.toString() + TOKEN_HEAD + 
			HEAD_DSTIP +":"+ dstip + TOKEN_HEAD+
			HEAD_SEQ + ":" + sequence+ TOKEN_HEAD + 
			TOKEN_HEAD;
		}
		else {
			format = HEAD_TYPE_SET + TOKEN_HEAD +
			HEAD_CHANNEL + ":" + channel + TOKEN_HEAD +
			HEAD_FAMILY + ":"+ family.toString() + TOKEN_HEAD + 
			HEAD_DSTIP +":"+ dstip + TOKEN_HEAD+
			HEAD_SRCIP + ":" + srcip + TOKEN_HEAD+
			HEAD_SEQ + ":" + sequence+ TOKEN_HEAD + 
			TOKEN_HEAD;
		}
		
		return format;
	}
}