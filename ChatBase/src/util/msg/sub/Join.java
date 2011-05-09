package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;

/**
 * 
 * @author ¼­º¸·æ
 *
 */
public class Join extends Message {
	private String channel = "";
	private String nick = "";
	
	public Join() {
		this.type = TYPE.JOIN;
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
			case NICK:
				nick = token.nextToken();
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

	public String getNick() {
		return nick;
	}
}