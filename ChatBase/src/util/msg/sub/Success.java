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
public class Success extends Message {
	private String channel = "";
	private TYPE family = null;
	private String ip = "";
	private int sequence = 0;

	public Success() {
		super(TYPE.SUCCESS);
	}

	public Success(String channel, String ip, TYPE family, int sequence) {
		super(TYPE.SUCCESS);
		
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
			value = token.nextToken().trim();
		}
		else {
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
			}
			else if (TYPE.FAMILY_CHILD.toString().equals(value)) {
				family = TYPE.FAMILY_CHILD;
			}
			else {
				isValid = false;
			}
			
			break;
		case IP:
			ip = value;
			
			break;
		case SEQ:
			try {
				sequence = Integer.parseInt(value);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
				isValid = false;
				return false;
			}
			
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

	public String getIp() {
		return ip;
	}

	public int getSequence() {
		return sequence;
	}

	@Override
	public String toString() {
		String format = HEAD_TYPE_SUCCESS + TOKEN_HEAD
					+ HEAD_CHANNEL + ":" + channel + TOKEN_HEAD
					+ HEAD_FAMILY + ":" + family.toString()	+ TOKEN_HEAD
					+ HEAD_IP + ":" + ip + TOKEN_HEAD
					+ HEAD_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD;

		return format;
	}
}