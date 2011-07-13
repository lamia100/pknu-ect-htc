package msg.sub;

import static msg.Definition.HEAD_FAIL;
import static msg.Definition.TOKEN_HEAD;
import static msg.Definition.TYPE_CHANNEL;
import static msg.Definition.TYPE_FAMILY;
import static msg.Definition.TYPE_IP;
import static msg.Definition.TYPE_SEQ;

import java.util.StringTokenizer;

import msg.HEAD;
import msg.Message;
import msg.TYPE;

/**
 * 
 * @author inter6
 * 
 */
public class Fail extends Message {
	private TYPE family = null;
	private String ip = "";

	public Fail() {
		super(HEAD.FAIL);
	}

	public Fail(String channel, String ip, TYPE family, int sequence) {
		super(HEAD.FAIL);

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

		switch (type) {
			case CHANNEL:
				channel = value;

				break;
			case FAMILY:
				if (TYPE.FAMILY_PARENT.toString().equals(value)) {
					family = TYPE.FAMILY_PARENT;
				} else if (TYPE.FAMILY_CHILD1.toString().equals(value)) {
					family = TYPE.FAMILY_CHILD1;
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

	@Override
	public String toString() {
		String format = HEAD_FAIL + TOKEN_HEAD + TYPE_CHANNEL + ":" + channel + TOKEN_HEAD + TYPE_FAMILY + ":" + family.toString()
				+ TOKEN_HEAD + TYPE_IP + ":" + ip + TOKEN_HEAD + TYPE_SEQ + ":" + sequence + TOKEN_HEAD + TOKEN_HEAD;

		return format;
	}
}