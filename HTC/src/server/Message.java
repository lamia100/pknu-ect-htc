package server;

import java.util.ArrayList;

public class Message {
	ArrayList<String> message=new ArrayList<String>();
	private int type;
	
	/**
	 * 
	 * @param line 파싱할 String
	 * @return 메세지의 끝나면 true. 메세지가 안끝났으면 false
	 */
	public boolean parse(String line)
	{
		line.trim();
		if(line!=null && "".equals(line))
		{
			message.add(line);
		}
		return true;
	}
	public int getType()
	{
		return type;
	}
	public ArrayList<String> getMessages()
	{
		return message;
	}
}
