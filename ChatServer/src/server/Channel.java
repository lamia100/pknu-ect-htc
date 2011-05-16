package server;

import java.util.ArrayList;
import java.util.HashMap;
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
	private final Map<String, Integer> names;
	private final BlockingQueue<Message> messageQ;
	private final Map<Integer, LinkSequence> changes;
	private String name = "";
	private boolean isRun = true;
	private int messageSqeuence = 0;
	public int setID = 0;
	
	public Channel(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		users = new ArrayList<User>();
		users.add(null);
		names = new HashMap<String, Integer>();
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
	 * 
	 * @param messageProcessor
	 *            �������� ����ϴ� ������ MessageProcessor
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
		log("Send",message);
		Send send;
		if (message.getType() == TYPE.SEND)
			send = (Send) message;
		else
			return;
		
		if (users.size() > 1){
			send.setSeq(messageSqeuence++);
			users.get(1).send(send);
		}
	}
	
	private void join(Join message) {
		AddSequence temp = new AddSequence(message, setID);
		changes.put(setID, temp);
		setID++; // sync �ȵ� �ٸ��ɷ� ��������.
		temp.next(message);
		
	}
	
	private void exit(Exit message) {
		//
		
		if (users.size() <= 1) {
			log("����",message);
		}
	}
	
	private void set(Set message) {
		//
	}
	
	private void success(Success message) {
		LinkSequence sequence = changes.get(message.getSequence());
		boolean isEndSequence = false;
		if (sequence != null) {
			isEndSequence = sequence.next(message);
		}
		if (isEndSequence) {
			changes.remove(message.getSequence());
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message message;
		while (isRun) {
			message = null;
			log("������");
			try {
				message = messageQ.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (message != null) {
				log("�޼��� ����, ��ť",message.getType() , message);
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
						success((Success) message);
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
	Channel channel=this;
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
	
	private int getUserIndex(User user) {
		
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i) == user)
				return i;
		}
		return -1;
	}
	
	@SuppressWarnings("unused")
	private int getUserIndex(String name) {
		return getUserIndex(messageProcessor.getUser(name));
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
			if (!names.keySet().contains(added.getName())) {
				//Channel �� �ִ� �ʵ�� �������� ����.
				index = users.size();
				users.add(added);
				names.put(added.getName(), index);
			} else {
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
		private void first() {
			//int index = users.size();
			System.out.println(index);
			
			//users.add(added);
			parent = users.get(index / 2);
			log("link seq : "+id,added,parent);
			
			if (parent != null) {
				parent.send(new Set(name, added.getIP(), CHILD, id));//�ڽ� IP �˸�.
				sequence++;
			} else {
				added.send(new Set(name, Server.getIP(), PARENT, id)); // ���ʻ���� �̹Ƿ� ������ �θ�
				sequence = 100;
				
			}
		}
		
		private void second(Message message) {
			if (message.getType() == TYPE.SUCCESS) {
				added.send(new Set(name, parent.getIP(), PARENT, id));
				sequence = 100;
			} else {
				//������ �����ϴ°͸� �����ϰ���..
				//first();
			}
		}
		
		private void last(Message message) {
			
			if (message.getType() == TYPE.SUCCESS) {
				
				changes.remove(id);
				added.add(channel);
				send(join);
				log("join ����",added);
			} else {
				//
				//first();
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
	
	private void log(Object... logs) {
		System.out.println("CH " + name);
		for(Object log:logs)
			System.out.println(log);
		System.out.println("----------------------------");
	}
}
