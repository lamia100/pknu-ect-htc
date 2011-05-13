package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;
import static util.Definition.*;
/**
 * 
 * @author ¼­º¸·æ
 *
 */
public class Join extends Message {
	private String channel = "";
	private String nick = "";
	
	public Join() {
		super(TYPE.JOIN);
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
			case CHANNEL:
				channel = value;
				break;
			case NICK:
				nick = value;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String temp=HEAD_TYPE_JOIN + TOKEN_HEAD + 
		 HEAD_CHANNEL+ ":"+ channel+ TOKEN_HEAD + 
		 HEAD_NICK +":"+ nick+ TOKEN_HEAD + 
		 TOKEN_HEAD;
		return temp;
	}
}