package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import util.*;



public class User implements Runnable{
	public String name="";
	private Socket socket=null;
	boolean flag = false;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;
	private Vector<Integer> roomNums=new Vector <Integer>();
	public int userID;
	public User(Socket socket)
	{
		this.socket=socket;
		try {
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			//roomNums.add(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated catch block
	}
	public void setID(int ID)
	{
		try {
			this.userID=ID;
			oos.writeObject(new Pack(Pack.LogIn,ID));
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void run()
	{
		try {
			while(!this.flag)
			{
				//String msg;
				Pack p=(Pack)ois.readObject();
				//Pack�� code �� ���� �ٸ� �۾��� �ؾ���
				ChatRoomServer room=null;
				switch(p.code)
				{
					case Pack.LogIn:
						sendMsg(new Pack(Pack.RoomUser, 0, Server.lobby.getUserList(p.user.userID)));
						Server.lobby.sendMsgAll(p);
						this.name=p.user.name;
						break;
					case Pack.LogOut:
						Server.lobby.removUser(new UserData(this));//�κ񿡼� ���� ����
						//Server.lobby.sendMsgAll(p);
						/*
						 * roomNums �� ����Ǿ��ִ� ��� ä�ù濡 ���ؼ� �α׾ƿ��ؾ���
						 */
						logOut();
						oos.close();
						ois.close();
						socket.close();
						flag=true;
						break;
					case Pack.RoomCreate:
						room = new ChatRoomServer(p.roomdata.title);
						roomNums.add(Server.rooms.putRoom(room)); 
						this.sendMsg(new Pack(Pack.RoomCreate, new RoomData(room) ) );
						break;
					case Pack.RoomIn: //��ȭ�濡 ����
						System.out.println("��ȭ�� ����  "+p.roomdata.number);
						if(p.roomdata.number==0) // 0�� ��ȭ���� �κ�
						{
							this.name=p.user.name; //Ŭ���̾�Ʈ�κ��� ������� �̸� �Է�
							sendMsg(new Pack(Pack.RoomUser, 0, Server.lobby.getUserList(p.user.userID)));//��ȭ�濡 �ִ� ����� ��� ����
							//System.out.println(p.user);
							Server.lobby.sendMsgAll(p);//�����ߴٴ� �޼����� ����
						}else{
							ChatRoomServer temp =Server.rooms.getRoom(p.roomdata.number);//roomNum ���� ��ȭ���� ã��
							if(temp!=null)//��ȭ���� �����ϸ�
							{
								temp.putUser(this); //����ڸ� ��ȭ�濡 �������
								roomNums.add(p.roomdata.number);//this�� ������ �ִ� ����� ����
								temp.sendMsgAll(p);//�ش� ��ȭ�濡 ����޼���
								System.out.println("��ȭ�� ����  "+p.roomdata.number);
								sendMsg(new Pack(Pack.RoomUser,p.roomdata.number, temp.getUserList(p.user.userID))); //��ȭ���� ����� ��� ����
								
							}
						}

						break;
					case Pack.RoomOut:
						Server.rooms.get(p.roomdata.number).remove(userID);//��ȭ�濡�� ����� ���� ����
						Server.rooms.get(p.roomdata.number).sendMsgAll(p);//���� �޼���
						this.roomNums.remove(p.roomdata.number);//��ȭ�濡 ����Ǿ��ִٴ� ������ ����
						if(Server.rooms.size()<1)
						{
							Server.rooms.removRoom(p.roomdata.number);
						}
						break;
					case Pack.RoomMsg://��ȭ�濡 �޼��� ����
						if( (p.data==null || p.data.equals(""))) break;	
						if(p.roomdata.number==0)//0���� ��� �κ�
						{
							Server.lobby.sendMsgAll(p);
						}else{//�ƴҰ�� �ش� ��ȭ�濡 �޼��� ����
							if(Server.rooms.get(p.roomdata.number)!=null)
								Server.rooms.get(p.roomdata.number).sendMsgAll(p);
						}
						break;
					case Pack.SendMsg://���� ��ɱ�������
						if(p.data!=null && !p.data.equals(""))	Server.lobby.sendMsgAll(p);
						break;
					case Pack.RoomList:
						sendMsg(new Pack(Pack.RoomList,Server.rooms.getRoomList()));
						System.out.println("RoomList ��û");
						break;
				}
			}

			//ChatServer.room.removeChatRunner(this);
		}catch (java.net.SocketException e){
			stop();//���Ͽ��� ������ ���� this�� ���� ���� ��
		}catch (EOFException e){

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void stop()
	{
		try {
			Server.lobby.remove(userID);
			Server.lobby.sendMsgAll(new Pack(Pack.LogOut,new UserData(this)));
			oos.close();
			ois.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flag=true;
	}
	public void sendMsg(Pack p) throws IOException
	{
		oos.writeObject(p);
		oos.flush();
	}
	public String toString()
	{
		return socket.toString();
	}

	public void logOut()
	{
		for(int i=0;i<roomNums.size();i++)
		{
			Server.rooms.get(roomNums.get(i)).removUser(new UserData(this));
			Server.rooms.get(roomNums.get(i)).sendRoomOut(new UserData(this));
		}
	}
	public void roomOut(Pack p)
	{
		Server.rooms.get(p.roomdata.number).sendRoomOut(new UserData(this));
	}

}
