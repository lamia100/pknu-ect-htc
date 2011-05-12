package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;

/**
 * 
 * @author ¼­º¸·æ
 *
 */
public class Script extends Message {
	private TYPE cast = TYPE.CAST_BROAD;
	private String channel = "";
	private String nick = "";
	private String msg = "";
	
	public Script() {
		super(TYPE.SCRIPT);
	}
	
	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr;
		String value;
		if(token.hasMoreElements()){
			typeStr = token.nextToken();
			value = token.nextToken().trim();
		}else{
			typeStr = line;
			value="";
		}
		TYPE type = getStringToType(typeStr);
		
		switch (type) {
			case CAST:
				if (TYPE.CAST_UNI.toString().equals(value)) {
					cast=TYPE.CAST_UNI;
				}
				break;
			case CHANNEL:
				channel = value;
				break;
			case NICK:
				nick = value;
				break;
			case MSG:
				msg = value;
				break;
			case END:
				return true;
			default:
				break;
		}
		
		return false;
	}

	public TYPE getCast() {
		return cast;
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
}