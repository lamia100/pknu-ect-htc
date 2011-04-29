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
		sendMsg(new Pack(Pack.RoomIn, rd,new UserData(this))); //��ȭ�� ����
	}
	public void sendRoomCreate(String title)
	{
		sendMsg(new Pack(Pack.RoomCreate,new RoomData(-1,title),new UserData(this))); //��ȭ�� ������û
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
				//Pack�� code �� ���� �ٸ� �۾��� �ؾ���
				switch(p.code)
				{
					case Pack.LogIn:
						userID=p.user.userID;//���̵� �Ҵ����
						this.sendMsg(new Pack(Pack.RoomIn,new RoomData(0),new UserData(this)));//�κ� ���������� �˸�
						break;
					case Pack.LogOut:
						c.appendChat(p.user +" ���� ���� �Ͽ����ϴ�.\n",p.roomdata.number);
						c.removeListUser(p.roomdata.number, new UserData(this));
						c.defaultListModel.removeElement(p.user.toString());//�ش� ������ �α׾ƿ������� �˸�
						break;
					case Pack.RoomCreate://��ȭ�� ���������� ���޹���
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
							//�ش� ��ȭ�濡 ����޼���
						}
						c.appendChat(p.user + " ���� ���� �Ͽ����ϴ�.\n",p.roomdata.number);
						c.addListUser(p.roomdata.number, p.user);
						break;
					case Pack.RoomOut:
						if(p.roomdata.number>0){
							sendRoomOut(p.roomdata);
							//�ش� ��ȭ�濡 ���� �޼���
						}
						c.removeListUser(p.roomdata.number,p.user);
						break;
					case Pack.RoomMsg:
							c.appendChat("["+p.user+"] \n  "+(String) p.data+	"\n",p.roomdata.number);
						break;
					case Pack.SendMsg:
						//���� ���ױ� ����
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
