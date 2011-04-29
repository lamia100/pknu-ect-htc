package util;

import java.io.Serializable;
import java.util.Vector;

import server.ChatRoomServer;

public class RoomData implements Serializable {

      private static final long serialVersionUID = 1L;
	public int number;
	public String title;
	public RoomData(int number,String title)
	{
		this.number=number;
		this.title=title;
	}
	public RoomData(ChatRoomServer cms)
	{
		this.number=cms.number;
		this.title=cms.title;
	}
	public RoomData(int number)
	{
		this.number=number;
	}
	public RoomData() {
	      // TODO Auto-generated constructor stub
      }
	
	public Vector<String> rowDate()
	{
		Vector <String> row=new Vector<String>();
		row.add(Integer.toString(number));
		row.add(title);
		return row;
	}
	
}
