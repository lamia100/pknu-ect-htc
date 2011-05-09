package util.msg.sub;

import util.msg.Message;
import util.msg.TYPE;
public class Exit extends Message {

	public Exit() {
		// TODO Auto-generated constructor stub
		this.type=TYPE.EXIT;
		this.s_type=type.toString();
	}
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
