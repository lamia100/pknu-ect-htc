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
	public Pack(int code,int num,Object data) // ��ȭ�濡 �ִ� ����� ��� �о����
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
			case Pack.LogIn: //�α��� ������ ������ ���� ���̵� �߱�
				this.user.userID=num;
				break;
			case Pack.RoomIn: //�ش� ��ȭ��(roomnum)���� ����
				roomdata.number=num;
				break;

		}
	}
	public Pack(int code,UserData user) //�����Ҷ� �ʿ��� ���� ����
	{
		this.code=code;
		this.user=user;
	}
	public Pack(int code,int roomNum,UserData user,String msg) //�ش� ��ȭ�濡 �޼��� ����
	{
		this.code=code;
		roomdata.number=roomNum;
		this.user=user;
		this.data=msg;

	}
	public Pack(int code,int roomNum,UserData user) //�ش� ��ȭ�濡 ����� ���� ���� //��ȭ�� ���
	{
		this.code=code;
		roomdata.number=roomNum;
		this.user=user;
	}
	public Pack(int code,RoomData roomdata,UserData user) //�ش� ��ȭ�濡 ����� ���� ���� //��ȭ�� ���
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
