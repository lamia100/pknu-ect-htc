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
public class Request extends Message {
	private String channel = "";
	private int seq = 0;

	public Request() {
		super(TYPE.REQUEST);
	}

	public Request(String channel, int seq) {
		super(TYPE.REQUEST);
		
		this.channel = channel;
		this.seq = seq;
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
		}

		return false;
	}

	public String getChannel() {
		return channel;
	}

	public int getSeq() {
		return seq;
	}

	@Override
	public String toString() {
		String format = HEAD_TYPE_REQUEST + TOKEN_HEAD
					+ HEAD_CHANNEL + ":" + channel + TOKEN_HEAD
					+ HEAD_SEQ + ":" + seq + TOKEN_HEAD + TOKEN_HEAD;

		return format;
	}
}