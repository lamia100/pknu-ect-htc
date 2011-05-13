package util.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.msg.sub.*;

public abstract class Message {
	private static final Map<String, TYPE> map = new HashMap<String, TYPE>();
	private ArrayList<String> message = new ArrayList<String>();
	private final TYPE type;
	protected boolean isValid = true;

	public Message(TYPE type) {
		this.type = type;
	}

	/**
	 * 라인 단위로 파싱
	 * @param line 파싱할 String
	 * @return 메세지의 끝나면 true. 메세지가 안끝났으면 false
	 */
	public abstract boolean parse(String line);

	private static void initialize() {
		for (TYPE type : TYPE.values()) {
			map.put(type.toString(), type);
		}
	}

	public static Message parsType(String str) {
		Message msg;
		if (str==null)
		{
			//throw new NullPointerException("Message.ParsType input is Null");
			//return null;
		}
		TYPE type = getTypeTable().get(str);
		
		if (type == null) {
			return null;
		}
		
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

	public ArrayList<String> getMessages() {
		return message;
	}

	protected static TYPE getStringToType(String str) {
		return getTypeTable().get(str);
	}

	/**
	 * 바로 map 를 사용하지 말것. 초기화 문제.
	 * @return {@code Message.map
	 */
	private static Map<String, TYPE> getTypeTable() {
		if (map.isEmpty()) {
			initialize();
		}
		
		return map;
	}

	public static void main(String[] args) {
		System.out.println(Message.getStringToType("p").name());
	}

	public boolean isValid() {
		return isValid;
	}

	public abstract String toString();
}