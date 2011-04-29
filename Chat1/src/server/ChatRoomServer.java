package server;

import java.util.Iterator;
import java.util.Vector;

import util.*;

public class ChatRoomServer extends ChatLobby {
	/*
       * 
       */
      private static final long serialVersionUID = 1L;
	public String title;
	public int number;
	public synchronized void putUser(User user)
	{
		put(user.userID,user);
	}
	public void setNumber(int number)
	{
		this.number=number;
	}
	public synchronized void removUser(UserData user)	
	{
		System.out.println(user.userID+"111  "+get(user.userID));
		this.remove(user.userID);
		//this.sendMsgAll(new Pack(Pack.LogOut,user));
	}
	public ChatRoomServer()
	{
		
	}
	public ChatRoomServer(String title)
	{
		this.title=title;
	}
	public void sendRoomOut(UserData user)
	{
		sendMsgAll(new Pack(Pack.RoomOut,new RoomData(this),user));
	}
	public Vector<UserData> getUserList(int id)
	{
		Iterator <User>iter=this.values().iterator();
		Vector <UserData>temp=new Vector<UserData>();
		while(iter.hasNext())
		{
			UserData t=new UserData(iter.next());
			if(t.userID!=id){
				temp.add(t);
				System.out.println(t.toString());
			}
			//System.out.println(temp.lastElement());
		}
		return temp;
	}
	
}
