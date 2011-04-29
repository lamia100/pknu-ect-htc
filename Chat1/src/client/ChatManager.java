package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import util.*;

public class ChatManager implements Runnable{
	private ObjectOutputStream oos=null;
	private ObjectInputStream ois=null;
	private boolean flag=false;
	private Socket socket=null;
	private ChatClient c=null;
	private Vector<Integer> roomNums=new Vector <Integer>();  //  @jve:decl-index=0:
	public String name;
	public int userID;
	public  ChatManager(ChatClient c)
	{
		this.c=c;
		name=c.name;
		try {
			socket=new Socket("112.162.198.121", 30000);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendMsg(Pack p)
	{
		try {
			oos.writeObject(p);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendLogOut()
	{
		//System.out.println(userID);
		sendMsg(new Pack(Pack.LogOut,new UserData(this)));
	}
	public void sendRoomIn(RoomData rd)
	{
		sendMsg(new Pack(Pack.RoomIn, rd,new UserData(this))); //대화방 입장
	}
	public void sendRoomCreate(String title)
	{
		sendMsg(new Pack(Pack.RoomCreate,new RoomData(-1,title),new UserData(this))); //대화방 생성요청
	}
	public void sendRoomOut(RoomData rd)
	{
		
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try {
			while(!this.flag)
			{
				//String msg;
				Pack p=(Pack)ois.readObject();
				//Pack의 code 에 따라 다른 작업을 해야함
				switch(p.code)
				{
					case Pack.LogIn:
						userID=p.user.userID;//아이디를 할당받음
						this.sendMsg(new Pack(Pack.RoomIn,new RoomData(0),new UserData(this)));//로비에 입장했음을 알림
						break;
					case Pack.LogOut:
						c.appendChat(p.user +" 님이 종료 하였습니다.\n",p.roomdata.number);
						c.removeListUser(p.roomdata.number, new UserData(this));
						c.defaultListModel.removeElement(p.user.toString());//해당 유저가 로그아웃했음을 알림
						break;
					case Pack.RoomCreate://대화방 생성됬음을 전달받음
						//c.addRoom(p.roomdata);
						
						sendRoomIn(p.roomdata);
						break;
					case Pack.RoomIn:
						if(p.roomdata.number==0)
						{
							
						}else if(p.roomdata.number>0){
							if(p.user.userID==this.userID)
							{
								roomNums.add(p.roomdata.number);
								c.addRoom(p.roomdata);
							}
							//해당 대화방에 입장메세지
						}
						c.appendChat(p.user + " 님이 입장 하였습니다.\n",p.roomdata.number);
						c.addListUser(p.roomdata.number, p.user);
						break;
					case Pack.RoomOut:
						if(p.roomdata.number>0){
							sendRoomOut(p.roomdata);
							//해당 대화방에 퇴장 메세지
						}
						c.removeListUser(p.roomdata.number,p.user);
						break;
					case Pack.RoomMsg:
							c.appendChat("["+p.user+"] \n  "+(String) p.data+	"\n",p.roomdata.number);
						break;
					case Pack.SendMsg:
						//쪽지 보네기 예정
						c.appendChat("["+p.user+"]\n"+(String)p.data+"\n",p.roomdata.number);
						break;
					/*case Pack.LobbyUser:
						setList(p);
						break;*/
					case Pack.RoomUser:
						c.setList(p);
						//setList(p);
						break;
					case Pack.RoomList:
						c.setRoomList((Vector<RoomData>) (p.data));
						break;
				}
			}

			//ChatServer.room.removeChatRunner(this);
		}catch (EOFException e){

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "unused", "rawtypes" })
	private void setList(Pack p)
	{
		Vector temp=(Vector)p.data;
		for(int i=0;i<temp.size();i++)
		{
			//System.out.println(temp.get(i));
			c.addListUser(p.roomdata.number, (UserData) temp.get(i));

			//System.out.println(c.defaultListModel.get());
		}
	}
	public void sendGetRoomList() {
	      // TODO Auto-generated method stub
	      sendMsg(new Pack(Pack.RoomList));
      }
}
