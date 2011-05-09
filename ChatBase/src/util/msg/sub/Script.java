package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
import java.util.StringTokenizer;
import static util.PacketDefinition.*;

/**
 * 
 * @author 서보룡
 *
 */
public class Script extends Message {
	private TYPE cast = TYPE.CAST_BROAD;
	private String channel = "";
	private String nick = "";
	private String msg = "";
	
	public Script() {
		this.type = TYPE.SCRIPT;
		this.s_type = HEAD_TYPE_SCRIPT;
	}
	
	@Override
	public boolean parse(String line) {
		StringTokenizer token = new StringTokenizer(line, ":");
		String typeStr = token.nextToken();
		TYPE type = getStringToType(typeStr);
		
		switch (type) {
			case CAST:
				/*이부분은 따로 만드는게 좋지 않을까. c나 p 이외에 다른 문자가 들어오면 cast 에 엉뚱한게 들어있음.
				 * 
				 * enum CAST{
				 * 	BROAD,UNI; 
				 * }
				 * 
				 * private TYPE cast = TYPE.CAST_BROAD;>> private CAST cast = CAST.BROAD;
				 * 
				 * 를 추천
				 */
				
				cast = getStringToType(token.nextToken());
			case CHANNEL:
				channel = token.nextToken();
				break;
			case NICK:
				nick = token.nextToken();
				break;
			case MSG:
				msg = token.nextToken();
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

	public String getChannel() {
		return channel;
	}

	public String getNick() {
		return nick;
	}

	public String getMsg() {
		return msg;
	}
}