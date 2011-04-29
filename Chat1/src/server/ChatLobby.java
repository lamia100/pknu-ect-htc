package server;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import util.Pack;
import util.UserData;



public class ChatLobby extends TreeMap<Integer,User>{
	
	/**
       * 
       */
      private static final long serialVersionUID = 1L;
	public synchronized void putUser(User user)
	{
		int ID=1;
		Iterator<Integer> iter=this.keySet().iterator();
		while(iter.hasNext())
		{
			if(!iter.next().equals(ID))	break;
			ID++;
		}
		put(ID,user);
		user.setID(ID);
	}
	public User getUser(int ID)
	{
		return get(ID);
	}
	public synchronized void removUser(UserData user)	
	{
		this.remove(user.userID);
		this.sendMsgAll(new Pack(Pack.LogOut,user));
	}
	public synchronized void sendMsgAll(Pack p) {
		Iterator <User>iter=this.values().iterator();
		while(iter.hasNext())
		{
			try {
	                  iter.next().sendMsg(p);
                  } catch (IOException e) {
	                  // TODO Auto-generated catch block
	                  e.printStackTrace();
                  }
		}
	      // TODO Auto-generated method stub
	      
	}
	public Vector<UserData> getUserList(int id)
	{
		Iterator <User>iter=this.values().iterator();
		Vector <UserData>temp=new Vector<UserData>();
		while(iter.hasNext())
		{
			UserData t=new UserData(iter.next());
			if(t.userID!=id)	temp.add(t);
			//System.out.println(temp.lastElement());
		}
		return temp;
	}
}
