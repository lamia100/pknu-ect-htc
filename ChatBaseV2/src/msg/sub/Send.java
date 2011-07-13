package msg.sub;

import java.util.StringTokenizer;

import msg.HEAD;
import msg.Message;
import msg.TYPE;
import static msg.Definition.*;

/**
 * 
 * @author malloc
 * 
 */
public class Send extends Message {
	private TYPE cast = TYPE.CAST_BROAD;
	private String msg = "";

	public Send() {
		// TODO Auto-generated constructor stub
		super(HEAD.SEND);
	}
	public Send(String channel,String name ,String message) {
		super(HEAD.SEND);
		this.channel=channel;
		this.name=name;
		msg=message;
		
	}
	public TYPE getCast() {
		return cast;
	}

	public String getMsg() {
		return msg;
	}
	
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
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

		switch (type) {
			case CAST:
				if (TYPE.CAST_UNI.toString().equals(value)) {
					cast = TYPE.CAST_UNI;
				}
				break;
			case SEQ:
				try {
					sequence = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
				break;
			case CHANNEL:
				channel = value;
				break;
			case NICK:
				name = value;
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

	/*
	public Send getClone(int sequence) {
		Send send = new Send();
		send.cast = cast;
		send.channel=channel;
		send.seq = sequence;
		send.nick = nick;
		send.msg = msg;
		
		return send;
	}
	*/
	@Override
	public String toString() {
		// TODO Auto-generated method stub

		String temp = HEAD_SEND + TOKEN_HEAD + TYPE_CAST + ":" + cast.toString() + TOKEN_HEAD + TYPE_SEQ + ":" + sequence + TOKEN_HEAD
				+ TYPE_CHANNEL + ":" + channel + TOKEN_HEAD + TYPE_NICK + ":" + name + TOKEN_HEAD + TYPE_MSG + ":" + msg + TOKEN_HEAD
				+ TOKEN_HEAD;
		return temp;
	}
}
