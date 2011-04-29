package util;

import java.io.Serializable;
public class Pack implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int LogIn=1;
	public static final int LogOut=2;
	public static final int RoomIn=3;
	public static final int RoomOut=4;
	public static final int RoomMsg=5;
	public static final int SendMsg=6;
	//public static final int LobbyUser=7;
	public static final int RoomUser=8;
	public static final int RoomList=9;
	public static final int RoomCreate=10;

	public int code;
	//public int roomNum;
	public RoomData roomdata=new RoomData();
	public UserData user=new UserData();
	public Object data=null;

	//////////////////////////////////////////////////////////////////////////////////////////

	public Pack(int code,Object data)
	{
		this.code=code;
		this.data=data;
	}
	public Pack(int code,int num,Object data) // 대화방에 있는 사용자 목록 읽어오기
	{
		this.code=code;
		this.data=data;
		roomdata.number=num;
	}
	public Pack(int code,int num	)
	{
		this.code=code;
		switch(code)
		{
			case Pack.LogIn: //로그인 했을때 서버로 부터 아이디 발급
				this.user.userID=num;
				break;
			case Pack.RoomIn: //해당 대화방(roomnum)으로 입장
				roomdata.number=num;
				break;

		}
	}
	public Pack(int code,UserData user) //퇴장할때 필요한 정보 전송
	{
		this.code=code;
		this.user=user;
	}
	public Pack(int code,int roomNum,UserData user,String msg) //해당 대화방에 메세지 전송
	{
		this.code=code;
		roomdata.number=roomNum;
		this.user=user;
		this.data=msg;

	}
	public Pack(int code,int roomNum,UserData user) //해당 대화방에 사용자 정보 전송 //대화방 퇴실
	{
		this.code=code;
		roomdata.number=roomNum;
		this.user=user;
	}
	public Pack(int code,RoomData roomdata,UserData user) //해당 대화방에 사용자 정보 전송 //대화방 퇴실
	{
		this.code=code;
		this.roomdata=roomdata;
		this.user=user;
	}
	public Pack(int code,RoomData roomdata)
	{
		this.code=code;
		this.roomdata=roomdata;
	}
	public Pack(int code)
	{
		this.code=code;
	}
	public String toString()
	{
		return (String) data;
	}
}
