package util.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.msg.sub.*;

public abstract class Message {
	
	ArrayList<String> message = new ArrayList<String>();
	private int i_type;
	protected String s_type;
	protected TYPE type;
	private static final Map<String, TYPE> map = new HashMap<String, TYPE>();
	
	/**
	 * 
	 * @param line
	 *            파싱할 String
	 * @return 메세지의 끝나면 true. 메세지가 안끝났으면 false
	 */
	public abstract boolean parse(String line);

	public static void initialize() {
		for(TYPE type : TYPE.values()){
			map.put(type.toString(), type);
			//System.out.println(type);
		}
		
		/*
		 * 같은 내용
		map.put(HEAD_TYPE_SEND, SEND);
		map.put(HEAD_TYPE_EXIT, EXIT);
		map.put(HEAD_TYPE_FAIL, FAIL);
		map.put(HEAD_TYPE_JOIN, JOIN);
		map.put(HEAD_TYPE_REQUEST, REQUEST);
		map.put(HEAD_TYPE_SCRIPT, SCRIPT);
		map.put(HEAD_TYPE_SET, SET);
		map.put(HEAD_TYPE_SUCCESS, SUCCESS);
		map.put(HEAD_CAST, CAST);
		map.put(HEAD_CAST_BROAD, CAST_BROAD);
		map.put(HEAD_CAST_UNI, CAST_UNI);
		map.put(HEAD_FAMILY, FAMILY);
		map.put(HEAD_FAMILY_CHILD, FAMILY_CHILD);
		map.put(HEAD_FAMILY_PARENT, FAMILY_PARENT);
		map.put(HEAD_IP, IP);
		map.put(HEAD_MSG, MSG);
		map.put(HEAD_NICK, NICK);
		map.put(HEAD_SEQ, SEQ);
		map.put(HEAD_END, END);
		*/
	}
	
	public static Message parsType(String str) {
		Message msg;
		TYPE type = getTypeTable().get(str);
		switch (type) {
			case SEND:
				msg = new Send();
				break;
			case JOIN:
				msg = new Join();
				break;
			case EXIT:
				msg = new Exit();
				break;
			case SET:
				msg = new Set();
				break;
			case REQUEST:
				msg = new Request();
				break;
			default:
				msg = null;
		}
		return msg;
	}

	public TYPE getType() {
		return type;
	}

	/**
	 * getType() 을 이용합시다.
	 */
	@Deprecated
	public int getIntType() {
		return i_type;
	}

	/**
	 * getType().toString() 을 이용합시다.
	 */
	@Deprecated
	public String getTypeByString() {
		return s_type;
	}

	public ArrayList<String> getMessages() {
		return message;
	}

	protected static TYPE getStringToType(String str) {
		return getTypeTable().get(str);
	}

	private static Map<String, TYPE> getTypeTable() {
		if (map == null)
			initialize();
		return map;
	}
	public static void main(String[] args) {
		initialize();
	}
}
