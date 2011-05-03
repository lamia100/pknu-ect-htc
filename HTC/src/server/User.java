package server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class User implements Comparable<User> , Runnable{

	private Socket socket=null;
	private String name=null;
	private Scanner in=null;
	private PrintWriter out=null;
	
	public User(Socket socket)
	{
		this.socket=socket;
		try {
			in=new Scanner(socket.getInputStream());
			out=new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialize()
	{
		
	}
	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		
		return name.compareToIgnoreCase(o.name);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//초기화
		initialize();
		//정상 동작
		while(true)
		{
			if(in.hasNextLine()){
				Message m=new Message();
				while(in.hasNextLine()){
					String line=in.nextLine();
					m.pars(line);
				}
			}
		}
	}

}
