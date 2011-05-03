package server;

import java.util.*;

public class Channel implements Comparable<Channel> , Runnable {
	public ArrayList<User> users=null;
	public Queue <Message> messageQ=null;
	@Override
	public int compareTo(Channel o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
