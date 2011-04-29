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
				//Pack의 code 에 따라 다른 작업을 해야함
				ChatRoomServer room=null;
				switch(p.code)
				{
					case Pack.LogIn:
						sendMsg(new Pack(Pack.RoomUser, 0, Server.lobby.getUserList(p.user.userID)));
						Server.lobby.sendMsgAll(p);
						this.name=p.user.name;
						break;
					case Pack.LogOut:
						Server.lobby.removUser(new UserData(this));//로비에서 유저 제거
						//Server.lobby.sendMsgAll(p);
						/*
						 * roomNums 에 저장되어있는 모든 채팅방에 대해서 로그아웃해야함
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
					case Pack.RoomIn: //대화방에 입장
						System.out.println("대화방 입장  "+p.roomdata.number);
						if(p.roomdata.number==0) // 0번 대화방은 로비
						{
							this.name=p.user.name; //클라이언트로부터 사용자의 이름 입력
							sendMsg(new Pack(Pack.RoomUser, 0, Server.lobby.getUserList(p.user.userID)));//대화방에 있는 사용자 목록 전송
							//System.out.println(p.user);
							Server.lobby.sendMsgAll(p);//입장했다는 메세지를 전달
						}else{
							ChatRoomServer temp =Server.rooms.getRoom(p.roomdata.number);//roomNum 번의 대화방을 찾음
							if(temp!=null)//대화방이 존재하면
							{
								temp.putUser(this); //사용자를 대화방에 집어넣음
								roomNums.add(p.roomdata.number);//this가 입장해 있는 방들을 설정
								temp.sendMsgAll(p);//해당 대화방에 입장메세지
								System.out.println("대화방 입장  "+p.roomdata.number);
								sendMsg(new Pack(Pack.RoomUser,p.roomdata.number, temp.getUserList(p.user.userID))); //대화방의 사용자 목록 전송
								
							}
						}

						break;
					case Pack.RoomOut:
						Server.rooms.get(p.roomdata.number).remove(userID);//대화방에서 사용자 정보 삭제
						Server.rooms.get(p.roomdata.number).sendMsgAll(p);//퇴장 메세지
						this.roomNums.remove(p.roomdata.number);//대화방에 입장되어있다는 정보를 삭제
						if(Server.rooms.size()<1)
						{
							Server.rooms.removRoom(p.roomdata.number);
						}
						break;
					case Pack.RoomMsg://대화방에 메세지 전송
						if( (p.data==null || p.data.equals(""))) break;	
						if(p.roomdata.number==0)//0번일 경우 로비
						{
							Server.lobby.sendMsgAll(p);
						}else{//아닐경우 해당 대화방에 메세지 전송
							if(Server.rooms.get(p.roomdata.number)!=null)
								Server.rooms.get(p.roomdata.number).sendMsgAll(p);
						}
						break;
					case Pack.SendMsg://쪽지 기능구현예정
						if(p.data!=null && !p.data.equals(""))	Server.lobby.sendMsgAll(p);
						break;
					case Pack.RoomList:
						sendMsg(new Pack(Pack.RoomList,Server.rooms.getRoomList()));
						System.out.println("RoomList 요청");
						break;
				}
			}

			//ChatServer.room.removeChatRunner(this);
		}catch (java.net.SocketException e){
			stop();//소켓에서 에러가 나면 this를 정지 삭제 함
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
