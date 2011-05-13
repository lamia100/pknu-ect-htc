package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.msg.Message;
import util.msg.TYPE;
import util.msg.sub.Exit;
import util.msg.sub.Join;
import util.msg.sub.Send;
import util.msg.sub.Set;
import util.msg.sub.Success;

/**
 * 
 * @author "김성현"
 * 
 */
public class Channel implements Comparable<Channel>, Runnable {
	private static MessageProcessor messageProcessor = null;
	
	private final ArrayList<User> users;
	private final java.util.Set<String> names;
	private final BlockingQueue<Message> messageQ;
	private final Map<Integer, LinkSequence> changes;
	private String name = "";
	private boolean isRun = true;
	private int messageSqeuence=0;
	public int setID = 0;
	
	public Channel(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		users = new ArrayList<User>();
		users.add(null);
		names = new HashSet<String>();
		changes = new HashMap<Integer, Channel.LinkSequence>();
		messageQ = new LinkedBlockingQueue<Message>();
	}
	
	public synchronized void enqueue(Message message) {
		messageQ.offer(message);
	}
	

	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	/**
	 * Server 클레스에서만 호출 할것.
	 * @param messageProcessor 서버에서 사용하는 유일한 MessageProcessor
	 */
	public static void setMessageProcessor(MessageProcessor messageProcessor) {
		Channel.messageProcessor = messageProcessor;
	}
	
	/*	
	public synchronized void add(User user) {
		if (!names.contains(user.getName())) {
			names.add(user.getName());
			users.add(user);
		}
	}
	*/
	/*
	public Queue<Message> getMessageQ() {
		return messageQ;
	}
	*/

	public String getName() {
		return name;
	}
	
	@Override
	public int compareTo(Channel o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void send(Message message) {
		System.out.println("CH : Send");
		Send send;
		if(message.getType()==TYPE.SEND)
			send=(Send)message;
		else
			return;
		
		if(users.size()>1)
			users.get(1).send(send.getClone(messageSqeuence++));
	}
	
	private void join(Join message) {
		AddSequence temp = new AddSequence(message, setID);
		changes.put(setID, temp);
		setID++; // sync 안됨 다른걸로 수정요함.
		temp.next(message);
		
	}
	
	private void exit(Exit message) {
		//
	}
	
	private void set(Set message) {
		//
	}
	
	private void success(Success message) {
		//
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			Message message=null;
			System.out.println("CH 동작중");
			try {
				message = messageQ.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("CH 동작중");
			if (message != null) {
				System.out.println("CH : 메세지 있음");
				System.out.println("체널 디큐 \n 내용 : " + message);
				switch (message.getType()) {
					case SEND:
						System.out.println("CH CASE SEND");
						send(message);
						break;
					case JOIN:
						System.out.println("CH CASE JOIN");
						join((Join) message);
						break;
					case EXIT:
						System.out.println("CH CASE EXIT");
						exit((Exit) message);
						break;
					case SET:
						System.out.println("CH CASE SET");
						set((Set) message);
						break;
					case SUCCESS:
						System.out.println("CH CASE SUCCESS");
						success((Success) message);
						break;
					default:
						System.out.println("CH CASE DEFAULT");
						break;
				}
			}
		}
	}
	
	/*-----------------------------LinkSequence---------------------------------*/
	static final TYPE CHILD = TYPE.FAMILY_CHILD;
	static final TYPE PARENT = TYPE.FAMILY_PARENT;
	
	abstract class LinkSequence {
		/** 순차적으로 진행하기 위한 작업 번호. */
		int sequence = 0;
		/** 생성된 오브젝트의 식별자 */
		int id = 0;
		
		/**
		 * User 의 부모 자식 설정.
		 * 
		 * @param message
		 *            SET, SUCCESS, FAIL 메세지만 받는다.
		 * @return 시퀀스의 종료시 true
		 */
		abstract boolean next(Message message);
		
		public LinkSequence(int id) {
			// TODO Auto-generated constructor stub
			this.id = id;
			sequence = 1;
		}
	}
	
	class AddSequence extends LinkSequence {
		
		User parent;
		User added;
		Join join;
		int index = 0;
		
		AddSequence(Join join, int id) {
			// TODO Auto-generated constructor stub
			super(id);
			this.join = join;
			added = messageProcessor.getUser(join.getNick());
			if (!names.contains(added.getName())) {
				names.add(added.getName());
				index = users.size();
				users.add(added);
			}else{
				sequence = -1;
			}
			
		}
		
		@Override
		boolean next(Message message) {
			// TODO Auto-generated method stub
			switch (sequence) {
				case 1:
					first();
					break;
				case 2:
					second(message);
					break;
				case 100:
					last(message);
					break;
				default:
					return true;
			}
			return false;
		}
		
		/*
		 * 1부터 시작. 0 = null
		 * 부모의 위치 = (자신의 위치)/2;
		 * 자식의 위치 = (자신의 위치)*2 +(0|1)
		 */
		private synchronized void first() {
			//int index = users.size();
			System.out.println(index);
			
			//users.add(added);
			parent = users.get(index / 2);
			System.out.println(added.getName());
			System.out.println(parent);

			if (parent != null) {
				parent.send(new Set(name, added.getIP(), CHILD, id));//자식 IP 알림.
				sequence++;
			} else {
				added.send(new Set(name, Server.getIP(), PARENT, id)); // 최초사용자 이므로 서버가 부모
				sequence = 100;
				
			}
		}
		
		private synchronized void second(Message message) {
			if (message.getType() == TYPE.SUCCESS) {
				added.send(new Set(name, parent.getIP(), PARENT, id));
				sequence = 100;
			} else {
				//현제는 성공하는것만 가정하겠음..
				first();
			}
		}
		
		private synchronized void last(Message message) {
			
			if (message.getType() == TYPE.SUCCESS) {
				changes.remove(id);
				send(join);
			} else {
				//
				first();
			}
			
		}
	}
	
	/**
	 * 나중에 사용함. 지금은 바꾸는거 생각하지말자.
	 * 
	 * @author "김성현"
	 * 
	 */
	class ChangeSequence extends LinkSequence {
		
		User parent;
		User change;
		User child1;
		User child2;
		Message message;
		
		public ChangeSequence(Message message, int sequence) {
			// TODO Auto-generated constructor stub
			super(sequence);
			this.message = message;
		}
		
		@Override
		synchronized boolean next(Message message) {
			// TODO Auto-generated method stub
			switch (sequence) {
				case 0:
					break;
				default:
					return true;
			}
			return false;
		}
		
		void first() {
			//change=
		}
		
	}
	
}
