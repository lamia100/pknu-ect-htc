package util.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.msg.sub.*;

import static util.PacketDefinition.*;

public abstract class Message {
	
	ArrayList<String> message = new ArrayList<String>();
	private int i_type;
	protected String s_type;
	protected TYPE type;
	private static final Map<String, TYPE> map = new HashMap<String, TYPE>();
	
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
		map.put(HEAD_END, TYPE.END);
	}
	
	protected static Map<String, TYPE> getTypeTable() {
		if(map==null)
			initialize();
		return map;
	}
	
	public static Message parsType(String str) {
		Message msg;
		TYPE type = map.get(str);
		switch (type) {
			case SEND:
				msg = new Send();
				break;
			case JOIN:
				msg = new Join();
				break;
			case EXIT:
				msg= new Exit();
				break;
			case SET:
				msg= new Set();
				break;
			case REQUEST:
				msg=new Request();
				break;
			default:
				msg = null;
		}
		return msg;
	}
	
	/**
	 * 
	 * @param line
	 *            파싱할 String
	 * @return 메세지의 끝나면 true. 메세지가 안끝났으면 false
	 */
	public abstract boolean parse(String line);

	/**
	 *  getType() 을 이용합시다.
	 */
	@Deprecated
	public int getIntType() {
		return i_type;
	}
	
	public TYPE getType() {
		return type;
	}

	/**
	 *  getType().toString() 을 이용합시다.
	 */
	@Deprecated
	public String getTypeByString() {
		return s_type;
	}
	
	public ArrayList<String> getMessages() {
		return message;
	}
}
