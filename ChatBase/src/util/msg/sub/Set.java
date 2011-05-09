package util.msg.sub;

import java.util.StringTokenizer;

import util.msg.Message;
import util.msg.TYPE;

/**
 * 
 * @author malloc
 * 
 */
public class Set extends Message {
	
	enum Family {
		PARENT, CHILD;
	}
	
	Family family = null;
	String ip = "";
	int sequence = 0;
	
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		StringTokenizer token = new StringTokenizer(line, ":");
		TYPE type = getStringToType(token.nextToken().trim());
		String value = token.nextToken().trim();
		switch (type) {
			case IP:
				ip = value;
				break;
			default:
				break;
		}
		return false;
	}
	
}
