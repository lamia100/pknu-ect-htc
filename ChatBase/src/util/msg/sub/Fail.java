package util.msg.sub;

import java.util.StringTokenizer;

import util.msg.Message;
import util.msg.TYPE;

public class Fail extends Message {
	private TYPE family = null;
	private String ip = "";
	private int sequence = 0;
	
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		StringTokenizer token = new StringTokenizer(line, ":");
		TYPE type = getStringToType(token.nextToken().trim());
		String value = token.nextToken().trim();
		
		switch (type) {
			case FAMILY:
				if (TYPE.FAMILY_PARENT.toString().equals(value)) {
					family = TYPE.FAMILY_PARENT;
				}
				else if (TYPE.FAMILY_CHILD.toString().equals(value)) {
					family = TYPE.FAMILY_CHILD;
				}
				else {
					isValid = false;
				}
				break;
			case IP:
				ip = value;
				break;
			case SEQ:
				try {
					sequence = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					isValid=false;
					return false;
				}
				break;
			default:
				break;
		}
		return false;
	}

	public TYPE getFamily() {
		return family;
	}

	public String getIp() {
		return ip;
	}

	public int getSequence() {
		return sequence;
	}
}
