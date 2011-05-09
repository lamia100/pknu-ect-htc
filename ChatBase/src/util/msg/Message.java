package util.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static util.PacketDefinition.*;

public abstract class Message {	
	protected enum TYPE {
		SEND, REQUEST, SET, SUCCESS, FAIL, JOIN, EXIT, SCRIPT,
		CAST, CAST_BROAD, CAST_UNI,
		CHANNEL,
		SEQ,
		NICK,
		FAMILY, FAMILY_PARENT, FAMILY_CHILD,
		IP, 
		MSG
	}
	
	ArrayList<String> message = new ArrayList<String>();
	private int i_type;
	private String s_type;
	protected static final Map<String, TYPE> map = new HashMap<String, TYPE>();
	
	/**
	 * 
	 * @param line
	 *            파싱할 String
	 * @return 메세지의 끝나면 true. 메세지가 안끝났으면 false
	 */
	public static void initialize() {
		map.put(HEAD_TYPE_SEND, TYPE.SEND);
		map.put(HEAD_TYPE_EXIT, TYPE.EXIT);
		map.put(HEAD_TYPE_FAIL, TYPE.FAIL);
		map.put(HEAD_TYPE_JOIN, TYPE.JOIN);
		map.put(HEAD_TYPE_REQUEST, TYPE.REQUEST);
		map.put(HEAD_TYPE_SCRIPT, TYPE.SCRIPT);
		map.put(HEAD_TYPE_SET, TYPE.SET);
		map.put(HEAD_TYPE_SUCCESS, TYPE.SUCCESS);
		map.put(HEAD_CAST, TYPE.CAST);
		map.put(HEAD_CAST_BROAD, TYPE.CAST_BROAD);
		map.put(HEAD_CAST_UNI, TYPE.CAST_UNI);
		map.put(HEAD_FAMILY, TYPE.FAMILY);
		map.put(HEAD_FAMILY_CHILD, TYPE.FAMILY_CHILD);
		map.put(HEAD_FAMILY_PARENT, TYPE.FAMILY_PARENT);
		map.put(HEAD_IP, TYPE.IP);
		map.put(HEAD_MSG, TYPE.MSG);
		map.put(HEAD_NICK, TYPE.NICK);
		map.put(HEAD_SEQ, TYPE.SEQ);
	}
	
	public static Message parsType(String str) {
		Message msg;
		TYPE type = map.get(str);
		switch (type) {
			case SEND:
				msg = new Send();
				msg.s_type = HEAD_TYPE_SEND;
				break;
			default:
				msg = null;
		}
		return msg;
	}
	
	public abstract boolean parse(String line);
	
	public int getType() {
		return i_type;
	}
	
	public String getTypeByString() {
		return s_type;
	}
	
	public ArrayList<String> getMessages() {
		return message;
	}
}
