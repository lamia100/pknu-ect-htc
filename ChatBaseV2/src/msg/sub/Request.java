package msg.sub;

import java.util.StringTokenizer;

import msg.HEAD;
import msg.Message;
import msg.TYPE;
import static msg.Definition.*;
/**
 * 
 * @author inter6
 * 
 */
public class Request extends Message {

	public Request() {
		super(HEAD.REQUEST);
	}

	public Request(String channel, int sequence) {
		super(HEAD.REQUEST);
		
		this.channel = channel;
		this.sequence = sequence;
	}
	
	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr;
		String value;
		
		if (token.hasMoreElements()) {
			typeStr = token.nextToken();
		} else {
			typeStr = line;
		}
		
		if (token.hasMoreElements()) {
			value = token.nextToken().trim();
		}else{
			value="";
		}
		
		TYPE type = typeTable.get(typeStr);

		switch (type) {
		case CHANNEL:
			channel = value;
			
			break;
		case SEQ:
			try {
				sequence = Integer.parseInt(value);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
				return false;
			}
			
			break;
		case END:
			return true;
		}

		return false;
	}


	@Override
	public String toString() {
		String format = HEAD_REQUEST + TOKEN_HEAD
					+ TYPE_CHANNEL + ":" + channel + TOKEN_HEAD
					+ TYPE_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD;

		return format;
	}
}