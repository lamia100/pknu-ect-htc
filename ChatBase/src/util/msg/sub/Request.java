package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;

/**
 * 
 * @author ������
 *
 */
public class Request extends Message {
	private String channel = "";
	private int seq = 0;
	
	public Request() {
		this.type = TYPE.REQUEST;
	}
	
	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr = token.nextToken();
		TYPE type = getStringToType(typeStr);
		
		switch (type) {
			case CHANNEL:
				channel = token.nextToken();
				break;
			case SEQ:
				seq = Integer.parseInt(token.nextToken().trim());
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