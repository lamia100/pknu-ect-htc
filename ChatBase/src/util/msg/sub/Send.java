package util.msg.sub;

import java.util.StringTokenizer;

import util.msg.Message;
import util.msg.TYPE;

public class Send extends Message {
	
	int seq = 0;
	String channel = "";
	String nick = "";
	String msg = "";
	
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr = token.nextToken();
		TYPE type = getTypeTable().get(typeStr);
		switch (type) {
			case SEQ:
				seq = Integer.parseInt(token.nextToken().trim());
				break;
			case CHANNEL:
				channel = token.nextToken();
				break;
			case NICK:
				nick = token.nextToken();
				break;
			case MSG:
				msg = token.nextToken();
				break;
			case END:
				return true;
			default:
				break;
			
		}
		return false;
	}
}
