package server;

import java.io.IOException;
import java.net.*;


public class Server {
	ServerSocket sSocket=null;
	public static ChatLobby lobby=new ChatLobby();
	public static ChatRooms rooms=new ChatRooms();
	public static final int port=30000;
	public static void main(String args[])
	{
		
		try {
	            ServerSocket sSocket=new ServerSocket(port);
	            System.out.println("채팅서버 시작..."+sSocket.getLocalPort());
	            while(true)
	            {
	            	Socket socket =  sSocket.accept();
	            	System.out.println("입장 : "+socket );
	            	User user=new User(socket);
	            	lobby.putUser(user);
	            	new Thread(user).start();	
	            }
            } catch (NumberFormatException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
	}
}
