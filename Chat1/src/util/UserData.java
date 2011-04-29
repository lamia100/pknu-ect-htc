package util;

import java.io.Serializable;

import server.User;
import client.ChatManager;

public class UserData implements Serializable {

      private static final long serialVersionUID = 1L;
	public int userID;
	public String name;
	public UserData()
	{
		
	}
	public UserData(ChatManager cm)
	{
		this.userID=cm.userID;
		this.name=cm.name;
	}
	public UserData(User user)
	{
		this.userID=user.userID;
		this.name=user.name;
	}
	public String toString()
	{
		return name+"("+Integer.toString(userID)+")";
	}
	
}
