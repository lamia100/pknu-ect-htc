package util.msg;

import java.util.StringTokenizer;

public class Send extends Message {

	int seq=0;
	String channel="";
	String nick="";
	String msg="";
	@Override
	public boolean parse(String line) {
		// TODO Auto-generated method stub
		StringTokenizer token=new StringTokenizer(line, ":");
		String typeStr=token.nextToken();
		TYPE type=map.get(typeStr);
		switch(type)
		{
			case SEQ:
				seq=Integer.parseInt(token.nextToken().trim());
				break;
			case CHANNEL:
				channel=token.nextToken();
				break;
			case NICK:
				nick=token.nextToken();
				break;
			case MSG:
				msg=token.nextToken();
				break;
			default:
				return true;
			
		}
		return false;
	}
	
}
