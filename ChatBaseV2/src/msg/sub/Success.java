package msg.sub;

import java.util.StringTokenizer;

import util.Logger;

import msg.HEAD;
import msg.Message;
import msg.TYPE;
import static msg.Definition.*;

/**
 * 
 * @author inter6
 * 
 */
public class Success extends Message {
	private TYPE family = null;
	private String ip = "";
	
	public Success() {
		super(HEAD.SUCCESS);
	}
	
	public Success(String channel, String name, int sequence) {
		super(HEAD.SUCCESS);
		this.channel = channel;
		this.name = name;
		this.sequence = sequence;
		Logger.log("success", "name : " + name, "channel : " + channel, "sequence : " + sequence);
	}
	//abcdefg
	//가나다라마바사
	//
	public int a;
	public Success(String channel, String ip, TYPE family, int sequence) {
		super(HEAD.SUCCESS);
		
		this.channel = channel;
		this.family = family;
		this.ip = ip;
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
		} else {
			value = "";
		}
		TYPE type = typeTable.get(typeStr);
		System.out.println("Success Message Pars Type : " + type.name());
		switch (type) {
			case CHANNEL:
				channel = value;
				break;
			case FAMILY:
				if (TYPE.FAMILY_PARENT.toString().equals(value)) {
					family = TYPE.FAMILY_PARENT;
				} else if (TYPE.FAMILY_CHILD1.toString().equals(value)) {
					family = TYPE.FAMILY_CHILD1;
				} else {
					//				isValid = false;
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
					//				isValid = false;
					return false;
				}
				break;
			case END:
				return true;
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
	
	@Override
	public String toString() {
		String format = HEAD_SUCCESS + TOKEN_HEAD + TYPE_CHANNEL + ":" + channel + TOKEN_HEAD + TYPE_NICK + ":" + name + TOKEN_HEAD
				+ TYPE_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD;
		
		return format;
	}
}