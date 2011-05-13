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
 * @author "�輺��"
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
	 * Server Ŭ���������� ȣ�� �Ұ�.
	 * @param messageProcessor �������� ����ϴ� ������ MessageProcessor
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
		setID++; // sync �ȵ� �ٸ��ɷ� ��������.
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
			System.out.println("CH ������");
			try {
				message = messageQ.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("CH ������");
			if (message != null) {
				System.out.println("CH : �޼��� ����");
				System.out.println("ü�� ��ť \n ���� : " + message);
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
		 * 1���� ����. 0 = null
		 * �θ��� ��ġ = (�ڽ��� ��ġ)/2;
		 * �ڽ��� ��ġ = (�ڽ��� ��ġ)*2 +(0|1)
		 */
		private synchronized void first() {
			//int index = users.size();
			System.out.println(index);
			
			//users.add(added);
			parent = users.get(index / 2);
			System.out.println(added.getName());
			System.out.println(parent);

			if (parent != null) {
				parent.send(new Set(name, added.getIP(), CHILD, id));//�ڽ� IP �˸�.
				sequence++;
			} else {
				added.send(new Set(name, Server.getIP(), PARENT, id)); // ���ʻ���� �̹Ƿ� ������ �θ�
				sequence = 100;
				
			}
		}
		
		private synchronized void second(Message message) {
			if (message.getType() == TYPE.SUCCESS) {
				added.send(new Set(name, parent.getIP(), PARENT, id));
				sequence = 100;
			} else {
				//������ �����ϴ°͸� �����ϰ���..
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
