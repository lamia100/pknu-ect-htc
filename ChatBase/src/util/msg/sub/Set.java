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
public class Set extends Message {
	private String channel = "";
	private TYPE family = null;
	@Deprecated
	private String ip = "";
	private String dstip = "";
	private String srcip = "";
	private int sequence = 0;
	
	public Set() {
		super(TYPE.SET);
	}
	
	public Set(String channel, String ip, TYPE family, int sequence) {
		super(TYPE.SET);
		
		this.channel = channel;
		this.family = family;
		this.dstip = ip;
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
			case IP:
				ip = value;
				break;
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
	 * getDstip·Î º¯°æ
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
		String format = HEAD_TYPE_SET + TOKEN_HEAD +
		HEAD_CHANNEL + ":" + channel + TOKEN_HEAD +
		HEAD_FAMILY + ":"+ family.toString() + TOKEN_HEAD + 
		HEAD_IP + ":" + ip + TOKEN_HEAD + 
		HEAD_DSTIP +":"+ dstip + TOKEN_HEAD+
		HEAD_SRCIP + ":" + srcip + TOKEN_HEAD+
		HEAD_SEQ + ":" + sequence+ TOKEN_HEAD + 
		TOKEN_HEAD;
		
		return format;
	}
}