package server;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import util.*;

public class ChatRooms extends TreeMap<Integer,ChatRoomServer> {
	/**
       * 
       */
      private static final long serialVersionUID = 1L;
	//public int ID;
	public int putRoom(ChatRoomServer room)
	{
		int ID=1;
		Iterator<Integer> iter=this.keySet().iterator();
		while(iter.hasNext())
		{
			if(!iter.next().equals(ID))	break;
			ID++;
		}
		room.setNumber(ID);
		put(ID,room);
		return ID;
	}
	public ChatRoomServer getRoom(int ID)
	{
		return get(ID);
	}
	public synchronized void removRoom(int room)
	{
		remove(room);
	}
	public synchronized Vector<RoomData> getRoomList()
	{
		Vector<RoomData> temp=new Vector<RoomData>();
		Iterator <ChatRoomServer>iter=values().iterator();
		while(iter.hasNext())
		{
			temp.add(new RoomData(iter.next()));
		}
		//temp.add(e)
		return temp;
	}
}
