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
public class Script extends Message {
	private TYPE cast = TYPE.CAST_BROAD;
	private String channel = "";
	private String nick = "";
	private String msg = "";

	public Script() {
		super(TYPE.SCRIPT);
	}

	public Script(TYPE cast, String channel, String nick, String msg) {
		super(TYPE.SCRIPT);
		
		this.cast = cast;
		this.channel = channel;
		this.nick = nick;
		this.msg = msg;
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
		case CAST:
			if (TYPE.CAST_UNI.toString().equals(value)) {
				cast = TYPE.CAST_UNI;
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

	@Override
	public String toString() {
		String format = HEAD_TYPE_SCRIPT + TOKEN_HEAD
					+ HEAD_CAST + ":" + cast.toString() + TOKEN_HEAD
					+ HEAD_CHANNEL + ":" + channel + TOKEN_HEAD
					+ HEAD_NICK + ":" + nick + TOKEN_HEAD
					+ HEAD_MSG + ":" + msg + TOKEN_HEAD + TOKEN_HEAD;

		return format;
	}
}