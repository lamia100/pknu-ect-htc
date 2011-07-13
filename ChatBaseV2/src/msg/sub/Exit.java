package msg.sub;

import java.util.StringTokenizer;

import msg.HEAD;
import msg.Message;
import msg.TYPE;
import static msg.Definition.*;

/**
 * 
 * @author inter6
 * 
 */
public class Exit extends Message {

	public Exit() {
		super(HEAD.EXIT);
	}

	public Exit(String channel, String name) {
		super(HEAD.EXIT);

		this.channel = channel;
		this.name = name;
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
			case NICK:
				name = value;
				break;
			case END:
				return true;
		}

		return false;
	}

	@Override
	public String toString() {
		String format = HEAD_EXIT + TOKEN_HEAD + TYPE_CHANNEL + ":" + channel + TOKEN_HEAD + TYPE_NICK + ":" + name + TOKEN_HEAD
				+ TOKEN_HEAD;

		return format;
	}
}