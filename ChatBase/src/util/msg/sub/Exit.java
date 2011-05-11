package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;

/**
 * 
 * @author ¼­º¸·æ
 *
 */
public class Exit extends Message {
	private String channel = "";
	private String nick = "";
	
	public Exit() {
		super(TYPE.EXIT);
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
	
	public Exit getClone(String channel)
	{
		Exit exit=new Exit();
		exit.channel=channel;
		exit.nick=nick;
		return exit;
	}
	
	public String getChannel() {
		return channel;
	}

	public String getNick() {
		return nick;
	}
}