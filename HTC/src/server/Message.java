package server;

import java.util.ArrayList;

public class Message {
	ArrayList<String> message=new ArrayList<String>();
	private int type;
	
	/**
	 * 
	 * @param line �Ľ��� String
	 * @return �޼����� ������ true. �޼����� �ȳ������� false
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
