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
	 * public synchronized void add(User user) { if
	 * (!names.contains(user.getName())) { names.add(user.getName());
	 * users.add(user); } }
	 */
	/*
	 * public Queue<Message> getMessageQ() { return messageQ; }
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
		log("Methode Send", message);
		if (message.getType() == TYPE.SEND){
			((Send) message).setSeq(messageSqeuence);
			messageSqeuence++;
		}
		
		if (users.size() > 1) {
			users.get(1).send(message);
		}
	}

	private void join(Join join) {
		AddSequence temp = new AddSequence(join, setID);
		changes.put(setID, temp);
		setID++; 
		// sync �ȵ� �ٸ��ɷ� ��������.. �ʿ���� ���� �ڿ��� �ƴ�.
		temp.next(join);

	}

	private void exit(Exit exit) {
		//
		if (users.size() <= 1) {
			log("����", exit);
		}
		ChangeSequence temp = new ChangeSequence(exit, setID);
		changes.put(setID, temp);
		setID++;
		temp.next(exit);
	}

	private void set(Set message) {
		//
	}

	private void success(Success message) {
		log("success",message);
		LinkSequence sequence = changes.get(message.getSequence());
		boolean isEndSequence = false;
		if (sequence != null) {
			log("success not null");
			isEndSequence = sequence.next(message);
		}
		if (isEndSequence) {
			log("success, seq is end");
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
				log("�޼��� ����, ��ť", message.getType(), message);
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
	Channel channel = this;

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
				// Channel �� �ִ� �ʵ�� �������� ����.
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
		 * 1���� ����. 0 = null �θ��� ��ġ = (�ڽ��� ��ġ)/2; �ڽ��� ��ġ = (�ڽ��� ��ġ)*2 +(0|1)
		 */
		private void first() {
			// int index = users.size();
			System.out.println(index);

			// users.add(added);
			parent = users.get(index / 2);
			log("link seq : " + id, added, parent);

			if (parent != null) {
				parent.send(new Set(name, added.getIP(), CHILD, id));
				// �ڽ� IP �˸�.
				sequence++;
			} else {
				added.send(new Set(name, Server.getIP(), PARENT, id)); 
				// ���ʻ���� �̹Ƿ� ������  �θ�
				sequence = 100;

			}
		}

		private void second(Message message) {
			if (message.getType() == TYPE.SUCCESS) {
				added.send(new Set(name, parent.getIP(), PARENT, id));
				sequence = 100;
			} else {
				// ������ �����ϴ°͸� �����ϰ���..
				// first();
			}
		}

		private void last(Message message) {

			if (message.getType() == TYPE.SUCCESS) {

				changes.remove(id);
				added.add(channel);
				send(join);
				log("join ����", added);
			} else {
				//
				// first();
			}

		}
	}

	class ChangeSequence extends LinkSequence {
		// exit �ѳ��� �θ�
		User parent;
		// ������ ����
		User moveUser;
		// exit �ѳ��� �ڽĵ�
		User child1;
		User child2;
		// moveUser�� ���� �θ�
		User parentOfMove;
		// exit�� ����
		User removedUser;
		// exit �ѳ��� �ε���
		
		// ���� �޼���
		Exit exit;

		public ChangeSequence(Exit exit, int sequence) {
			// TODO Auto-generated constructor stub
			super(sequence);

			this.exit = exit;
			removedUser = messageProcessor.getUser(exit.getNick());
			// users �� ������ �ε��� moveUser�� ������ġ
			int moveIndex = users.size() - 1;
			moveUser = users.remove(moveIndex);
			int dstIndex = getUserIndex(removedUser);
			
			
			
			users.set(dstIndex, moveUser);
			
			parentOfMove = users.get(moveIndex / 2);

			removedUser.remove(channel);

			parent = users.get(dstIndex / 2);

			if ((dstIndex * 2) < users.size()) {
				child1 = users.get(dstIndex * 2);
			}
			if ((dstIndex * 2 + 1) < users.size()) {
				child2 = users.get(dstIndex * 2 + 1);
			}
		}

		@Override
		synchronized boolean next(Message message) {
			// TODO Auto-generated method stub
			switch (sequence) {
				case 1:
					first();
					break;
				case 2:
					second(message);
					break;
				case 3:
					third(message);
					break;
				case 4:
					fourth(message);
					break;
				case 5:
					fifth(message);
					break;
				case 6:
					sixth(message);
					break;
				case 7:
					seventh(message);
					break;
				case 100:
					last(message);
					break;
				default:
					return true;
			}
			return false;
		}

		void first() {
			log("change 1");
			if(parentOfMove==moveUser)
			{
				second(new Success());
				sequence=2;
				return;
			}
			parentOfMove.send(new Set(channel.name, moveUser.getIP(), "0.0.0.0", CHILD, id));
			if (parent == null) {
				sequence = 3;
			} else {
				sequence=2;
			}
		}

		void second(Message message) {
			log("change 2");
			if (message.getType() == TYPE.SUCCESS) {
				// moveUser �� �θ�κ��� ���״� �۾�. ����
				// parent �� �ڽ��� ����.
				parent.send(new Set(channel.name, removedUser.getIP(), moveUser.getIP(), CHILD, id));
				sequence=3;
			}
		}

		void third(Message message) {
			log("change 3");
			if (message.getType() == TYPE.SUCCESS) {
				// parent �� moveUser�� �ڽ����� ���� ����
				// moveUser�� �θ��� parent�� ����
				if (parent != null) {
					moveUser.send(new Set(channel.name, parent.getIP(), PARENT, id));
				} else {
					moveUser.send(new Set(channel.name, Server.getIP(), PARENT, id));
				}
				if (child1 != null) {
					sequence=4;
				} else {
					sequence = 100;
				}
			}
		}

		void fourth(Message message) {
			log("change 4");
			if (message.getType() == TYPE.SUCCESS) {
				// child1 ���� 1
				moveUser.send(new Set(channel.name, child1.getIP(), CHILD, id));
				sequence=5;
			}
		}

		void fifth(Message message) {
			log("change 5");
			if (message.getType() == TYPE.SUCCESS) {
				// child1 ���� 2
				child1.send(new Set(channel.name, moveUser.getIP(), PARENT, id));

				if (child2 != null) {
					sequence=6;
				} else {
					sequence = 100;
				}

			}
		}

		void sixth(Message message) {
			log("change 6");
			if (message.getType() == TYPE.SUCCESS) {
				// child2 ���� 1
				moveUser.send(new Set(channel.name, child2.getIP(), CHILD, id));
				sequence=7;
			}
		}

		void seventh(Message message) {
			log("change 7");
			if (message.getType() == TYPE.SUCCESS) {
				// child2 ���� 2
				child2.send(new Set(channel.name, moveUser.getIP(), PARENT, id));
				sequence = 100;
			}
		}

		void last(Message message) {
			log("change end");
			if (message.getType() == TYPE.SUCCESS) {
				changes.remove(id);
				send(exit);
				log("exit ����", removedUser);
			}
		}

	}

	private void log(Object... logs) {
		System.out.println("CH " + name);
		for (Object log : logs)
			System.out.println(log);
		System.out.println("----------------------------");
	}
}
