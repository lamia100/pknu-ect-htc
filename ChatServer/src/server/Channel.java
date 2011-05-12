package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;

import util.msg.Message;
import util.msg.TYPE;
import util.msg.sub.Exit;
import util.msg.sub.Join;
import util.msg.sub.Set;
import util.msg.sub.Success;

/**
 * 
 * @author "�輺��"
 * 
 */
public class Channel implements Comparable<Channel>, Runnable {
	private static MessageProcessor messageProcessor = null;
	
	private ArrayList<User> users = null;
	private java.util.Set<String> names = null;
	private Queue<Message> messageQ = null;
	private Map<Integer, LinkSequence> changes = null;
	private String name = "";
	private boolean isRun = true;
	
	public int setID = 0;
	
	public Channel(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		users = new ArrayList<User>();
		users.add(null);
		names = new HashSet<String>();
		changes = new HashMap<Integer, Channel.LinkSequence>();
	}
	
	public synchronized void enqueue(Message message) {
		messageQ.offer(message);
	}
	
	private synchronized Message dequeue() {
		return messageQ.poll();
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	/**
	 * Server Ŭ���������� ȣ�� �Ұ�.
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
		users.get(0).send(message);
	}
	
	private void join(Join message) {
		AddSequence temp = new AddSequence(message, setID);
		changes.put(setID, temp);
		setID++; // sync �ȵ� �ٸ��ɷ� ��������.
		temp.next(message);
		
	}
	
	private void exit(Exit message) {
		//
	}
	
	private void set(Set message) {
		//
	}
	private void success(Success message)
	{
		//
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			if (!messageQ.isEmpty()) {
				Message message = dequeue();
				switch (message.getType()) {
					case SEND:
						send(message);
						break;
					case JOIN:
						join((Join) message);
						break;
					case EXIT:
						exit((Exit) message);
						break;
					case SET:
						set((Set) message);
						break;
					case SUCCESS:
						success((Success)message);
						break;
					default:
						break;
				}
			}
		}
	}
	
	
	
	/*-----------------------------LinkSequence---------------------------------*/
	static final TYPE CHILD = TYPE.FAMILY_CHILD;
	static final TYPE PARENT = TYPE.FAMILY_PARENT;
	
	abstract class LinkSequence {
		/** ���������� �����ϱ� ���� �۾� ��ȣ. */
		int sequence = 0;
		/** ������ ������Ʈ�� �ĺ��� */
		int id = 0;
		
		/**
		 * User �� �θ� �ڽ� ����.
		 * 
		 * @param message
		 *            SET, SUCCESS, FAIL �޼����� �޴´�.
		 * @return �������� ����� true
		 */
		abstract boolean next(Message message);
		
		public LinkSequence(int id) {
			// TODO Auto-generated constructor stub
			this.id = id;
			sequence=1;
		}
	}
	
	class AddSequence extends LinkSequence {
		
		User parent;
		User added;
		Join join;
		int index=0;
		AddSequence(Join join, int id) {
			// TODO Auto-generated constructor stub
			super(id);
			this.join = join;
			User user=messageProcessor.getUser(join.getNick());
			index=users.size();
			users.add(user);
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
		 * 1���� ����. 0 = null
		 * �θ��� ��ġ = (�ڽ��� ��ġ)/2;
		 * �ڽ��� ��ġ = (�ڽ��� ��ġ)*2 +(0|1)
		 */
		private synchronized void first() {
			int index = users.size();
			users.add(added);
			parent = users.get(index / 2);
			if (parent != null) {
				parent.send(new Set(name, added.getIP(), CHILD, id));//�ڽ� IP �˸�.
				sequence++;
			} else {
				added.send(new Set(name, Server.getIP(), PARENT, id)); // ���ʻ���� �̹Ƿ� ������ �θ�
				sequence=100;
				
			}
		}
		
		private synchronized void second(Message message)
		{
			if(message.getType()==TYPE.SUCCESS)
			{
				added.send(new Set(name, parent.getIP(), PARENT, id));
				sequence=100;
			}else{
				//������ �����ϴ°͸� �����ϰ���..
				first();
			}
		}
		
		private synchronized void last(Message message) {
			
			if(message.getType()==TYPE.SUCCESS){
				changes.remove(id);
				send(join);
			}else{
				//
				first();
			}
			
		}
	}
	
	/**
	 * ���߿� �����. ������ �ٲٴ°� ������������.
	 * 
	 * @author "�輺��"
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
