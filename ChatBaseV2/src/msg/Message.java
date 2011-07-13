package msg;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import msg.sub.Exit;
import msg.sub.Fail;
import msg.sub.Join;
import msg.sub.Request;
import msg.sub.Script;
import msg.sub.Send;
import msg.sub.Set;
import msg.sub.Success;

public abstract class Message {

	private static final Map<String, HEAD> headTable;
	protected static final Map<String, TYPE> typeTable;
	static {
		headTable = new HashMap<String, HEAD>();
		for (HEAD head : HEAD.values()) {
			headTable.put(head.toString(), head);
		}
		typeTable = new Hashtable<String, TYPE>();
		for (TYPE type : TYPE.values()) {
			typeTable.put(type.toString(), type);
		}

	}
	

	protected String channel = "";
	protected String name = "";
	protected int sequence = 0;
	private final HEAD head;
	public static Message parsHead(String str) {
		Message message;
		if (str == null) {
			// throw new NullPointerException("Message.ParsType input is Null");
			return null;
		}
		HEAD head = headTable.get(str);
	
		if (head == null) {
			return null;
		}
	
		switch (head) {
			case SEND:
				message = new Send();
				break;
			case JOIN:
				message = new Join();
				break;
			case EXIT:
				message = new Exit();
				break;
			case SET:
				message = new Set();
				break;
			case REQUEST:
				message = new Request();
				break;
			case SUCCESS:
				message = new Success();
				break;
			case FAIL:
				message = new Fail();
				break;
			case SCRIPT:
				message = new Script();
				break;
			default:
				message = null;
		}
	
		return message;
	}
	public Message(HEAD head) {
		this.head = head;
	}
	public String getChannel() {
		return channel;
	}
	public HEAD getHead() {
		// TODO Auto-generated method stub
		return head;
	}
	public String getName() {
		return name;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public abstract boolean parse(String line);
	
	@Override
	public abstract String toString();
}
