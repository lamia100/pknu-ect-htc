package util.msg.sub;

import java.util.StringTokenizer;

import util.msg.Message;
import util.msg.TYPE;

/**
 * 
 * @author malloc
 *
 */
public class Send extends Message {
	
	int seq = 0;
	String channel = "";
	String nick = "";
	String msg = "";
	
	public int getSeq() {
		return seq;
	}

	public String getChannel() {
		return channel;
	}

	public String getNick() {
		return nick;
	}

	public String getMsg() {
		return msg;
	}

	public Send() {
		// TODO Auto-generated constructor stub
		this.type = TYPE.SEND;
	}
	
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr = token.nextToken().trim();
		TYPE type = getStringToType(typeStr);
		String value = token.nextToken().trim();
		switch (type) {
			case SEQ:
				try {
					seq = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					isValid=false;
					return false;
				}
				break;
			case CHANNEL:
				channel = value;
				break;
			case NICK:
				nick = value;
				break;
			case MSG:
				msg += value + '\n';
				break;
			case END:
				return true;
			default:
				break;
			
		}
		return false;
	}
}
