package util.msg.sub;

import java.util.StringTokenizer;

import util.msg.Message;
import util.msg.TYPE;
import static util.Definition.*;

/**
 * 
 * @author malloc
 * 
 */
public class Send extends Message {
	private TYPE cast = TYPE.CAST_BROAD;
	private String channel = "";
	private int seq = 0;
	private String nick = "";
	private String msg = "";
	
	public Send() {
		// TODO Auto-generated constructor stub
		super(TYPE.SEND);
	}
	
	public TYPE getCast() {
		return cast;
	}
	
	public int getSeq() {
		return seq;
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
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr;
		String value;
		if (token.hasMoreElements()) {
			typeStr = token.nextToken();
			value = token.nextToken().trim();
		} else {
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
			case SEQ:
				try {
					seq = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					isValid = false;
					return false;
				}
				break;
			case CHANNEL:
				channel = value;
				break;
			case NICK:
				nick = value;
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
	
	/**
	 * 사용자로부터 받은 메세지에 시퀀스를 붙이는 작업.
	 * 
	 * @param sequence
	 * @return
	 */
	public Send getClone(int sequence) {
		Send send = new Send();
		send.cast = cast;
		send.channel=channel;
		send.seq = sequence;
		send.nick = nick;
		send.msg = msg;
		
		return send;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String temp = 
		HEAD_TYPE_SEND + TOKEN_HEAD +
		HEAD_CAST + ":" + cast.toString() + TOKEN_HEAD + 
		HEAD_SEQ + ":" + seq + TOKEN_HEAD + 
		HEAD_CHANNEL + ":"+ channel + TOKEN_HEAD + 
		HEAD_NICK + ":" + nick + TOKEN_HEAD + 
		HEAD_MSG + ":" + msg + TOKEN_HEAD + 
		TOKEN_HEAD;
		return temp;
	}
}
