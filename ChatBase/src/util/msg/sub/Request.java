package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;

/**
 * 
 * @author ¼­º¸·æ
 *
 */
public class Request extends Message {
	private String channel = "";
	private int seq = 0;
	
	public Request() {
		super(TYPE.REQUEST);
	}
	
	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr = token.nextToken();
		TYPE type = getStringToType(typeStr);
		String value = token.nextToken().trim();
		
		switch (type) {
			case CHANNEL:
				channel = value;
				break;
			case SEQ:
				try {
					seq = Integer.parseInt(value);
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
					isValid = false;
					return false;
				}
				break;
			case END:
				return true;
			default:
				break;
		}
		
		return false;
	}

	public String getChannel() {
		return channel;
	}

	public int getSeq() {
		return seq;
	}
}