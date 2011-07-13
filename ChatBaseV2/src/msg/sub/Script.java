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
public class Script extends Message {
	private TYPE cast = TYPE.CAST_BROAD;
	private String msg = "";

	public Script() {
		super(HEAD.SCRIPT);
	}

	public Script(TYPE cast, String channel, String name, String msg) {
		super(HEAD.SCRIPT);

		this.cast = cast;
		this.channel = channel;
		this.name = name;
		this.msg = msg;
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
			case CAST:
				if (TYPE.CAST_UNI.toString().equals(value)) {
					cast = TYPE.CAST_UNI;
				}
				break;
			case CHANNEL:
				channel = value;
				break;
			case NICK:
				name = value;
				break;
			case MSG:
				msg += value;
				break;
			case END:
				return true;
			default:
				break;

		}
		return false;
	}

	public TYPE getCast() {
		return cast;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		String format = HEAD_SCRIPT + TOKEN_HEAD + TYPE_CAST + ":" + cast.toString() + TOKEN_HEAD + TYPE_CHANNEL + ":" + channel
				+ TOKEN_HEAD + TYPE_NICK + ":" + name + TOKEN_HEAD + TYPE_MSG + ":" + msg + TOKEN_HEAD + TOKEN_HEAD;

		return format;
	}
}