package server;

import java.util.HashMap;
import java.util.Map;

import msg.*;
import msg.sub.*;
import util.*;
import static util.Logger.*;

public class ServerChannel extends Channel {
	
	//	������
	private int messageSqeuenceNumber = 0;
	private int controlSequenceNumber = 0;
	private final Map<Integer, LinkSequence> changes;
	
	private Manager manager;
	
	final Script scr;
	
	public ServerChannel(String name) {
		super(name);
		changes = new HashMap<Integer, LinkSequence>();
		manager = ServerManager.getInstance();
		scr = new Script(TYPE.CAST_BROAD, getName(), "", BENCH);
	}
	
	@Override
	protected void send(Packet packet) {
		if (packet.getHead() == HEAD.SEND) {
			packet.getMessage().setSequence(messageSqeuenceNumber);
			messageSqeuenceNumber++;
		}
		for (int i = 1; i < 3 && i < users.size(); i++) {
			User user = users.get(i);
			if (user != null) {
				user.send(packet);
			}
		}
	}
	
	@Override
	protected void join(Packet packet) {
		Join join = (Join) packet.getMessage();
		AddSequence temp = new AddSequence(join, controlSequenceNumber, packet.getUser());
		changes.put(controlSequenceNumber, temp);
		controlSequenceNumber++;
		// sync �ȵ� �ٸ��ɷ� ��������.. �ʿ���� ���� �ڿ��� �ƴ�.
		temp.next(join);
		
	}
	
	@Override
	protected void exit(Packet packet) {
		if (users.size() > 2) {
			Exit exit = (Exit) packet.getMessage();
			log("����", exit);
			ChangeSequence temp = new ChangeSequence(exit, controlSequenceNumber, packet.getUser());
			changes.put(controlSequenceNumber, temp);
			controlSequenceNumber++;
			temp.next(exit);
		} else if (users.size() == 2) {
			if (users.get(1) == packet.getUser()) {
				users.remove(1);
			}
		} else {
			return;
		}
	}
	
	@Override
	protected void success(Packet packet) {
		Success success = (Success) packet.getMessage();
		log("success", success);
		LinkSequence sequence = changes.get(success.getSequence());
		boolean isEndSequence = false;
		if (sequence != null) {
			log("success not null");
			isEndSequence = sequence.next(success);
		}
		if (isEndSequence) {
			log("success, seq is end");
			changes.remove(success.getSequence());
		}
		
	}
	
	@Override
	protected void set(Packet packet) {
		//		throw new UnsupportedOperationException("���� �����ȵ�");
		
	}
	
	@Override
	protected void fail(Packet packet) {
		//		throw new UnsupportedOperationException("���� �����ȵ�");
		
	}
	
	@Override
	protected void request(Packet packet) {
		//		throw new UnsupportedOperationException("���� �����ȵ�");
		
	}
	
	long time;
	
	@Override
	protected void script(Packet packet) {
		Script script = (Script) packet.getMessage();
		log("script", script);
		if ("bench".equals(script.getMsg())) {
			log("bench start");
			time = System.currentTimeMillis();
			new Thread(new Bench()).start();
		}
		if ("end".equals(script.getMsg())) {
			send(new Packet(new Send(getName(), "", ("time>>" + Long.toString((System.currentTimeMillis() - time)) + "ms")), null));
		}
	}
	
	class Bench implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (int i = 0; i < 50; i++) {
				System.out.println(">>>>>>>>>>>>>>" + i);
				send(new Packet(scr, null));
			}
		}
	}
	
	static final String BENCH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	/*-----------------------------LinkSequence---------------------------------*/

	static final TYPE CHILD1 = TYPE.FAMILY_CHILD1;
	static final TYPE CHILD2 = TYPE.FAMILY_CHILD2;
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
	
	/*private int getUserIndex(User user) {
		
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i) == user)
				return i;
		}
		return -1;
	}
	
	@SuppressWarnings("unused")
	private int getUserIndex(String name) {
		return getUserIndex(manager.getUser(name));
	}
	
	*/

	class AddSequence extends LinkSequence {
		
		User parent;
		User added;
		Join join;
		int index = 0;
		int iParent = 0;
		
		AddSequence(Join join, int id, User user) {
			// TODO Auto-generated constructor stub
			super(id);
			this.join = join;
			added = user;
			if (added.isJoin(channel)) {
				sequence = -1;
				log("Channel : " + channel.getName(), "�̹� ������ �ִ�.");
			} else {
				index = users.size();
				users.add(added);
				added.setPosition(channel, index);
				iParent = (index - 1) / 2;
				parent = users.get(iParent);
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
		 * �ڽ��� �ڽ�*2 + (1|2) (ex. 1 - (3,4), 2 - (5,6), 3 - (7,8), 4 - (9,10)
		 * �θ�� (�ڽ�-1) / 2 (ex. 1 - 0, 2 - 0, 3 - 1, 4 - 1, 5 - 2, 6 - 2...)
		 * �׻� users[1],users[2]�� ����. 
		 */
		private void first() {
			// int index = users.size();
			System.out.println(index);
			
			log("Channel" + getName(), "add 1");
			// users.add(added);
			
			log("link seq : " + id, added, parent);
			
			if (parent != null) {
				TYPE child;
				if ((index - (2 * iParent)) == 1) {
					child = CHILD1;
				} else {
					child = CHILD2;
				}
				parent.send(new Set(added.getName(), getName(), added.getIP(), child, id));
				// �ڽ� IP �˸�.
				sequence++;
			} else {
				added.send(new Set(" ", getName(), " ", PARENT, id));
				// ���ʻ���� �̹Ƿ� ������  �θ�
				sequence = 100;
				
			}
		}
		
		private void second(Message message) {
			log("Channel" + getName(), "add 2");
			if (message.getHead() == HEAD.SUCCESS) {
				added.send(new Set(parent.getName(), getName(), parent.getIP(), PARENT, id));
				sequence = 100;
			} else {
				// ������ �����ϴ°͸� �����ϰ���..
				// first();
			}
		}
		
		private void last(Message message) {
			
			if (message.getHead() == HEAD.SUCCESS) {
				
				changes.remove(id);
				//added.add(channel);
				send(new Packet(join, added));
				log("Channel" + getName(), "add last", "join ����", added);
			} else {
				//
				// first();
				log("Channel" + getName(), "add last", "join ����", added);
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
		User removeUser;
		// exit �ѳ��� �ε���
		
		// ���� �޼���
		Exit exit;
		int moveIndex;
		int removeIndex;
		
		public ChangeSequence(Exit exit, int sequence, User user) {
			// TODO Auto-generated constructor stub
			super(sequence);
			
			this.exit = exit;
			removeUser = user;
			// users �� ������ �ε��� moveUser�� ������ġ
			moveIndex = users.size() - 1;
			
			moveUser = users.remove(moveIndex);
			removeIndex = user.getPosition(channel);
			if (removeIndex < users.size()) {
				users.set(removeIndex, moveUser);
				moveUser.setPosition(channel, removeIndex);
			}
			
			parentOfMove = users.get((moveIndex - 1) / 2);
			removeUser.remove(channel);
			parent = users.get((removeIndex - 1) / 2);
			
			if ((removeIndex * 2 + 1) < users.size()) {
				child1 = users.get(removeIndex * 2 + 1);
			}
			if ((removeIndex * 2 + 2) < users.size()) {
				child2 = users.get(removeIndex * 2 + 2);
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
			if (parentOfMove == null) {
				last(new Success());
				return;
			}
			if (parentOfMove == moveUser) {
				second(new Success());
				sequence = 2;
				return;
			}
			TYPE child;
			int iParent = parentOfMove.getPosition(channel);
			int iChild = moveIndex;
			if (iChild - 2 * iParent == 1) {
				child = CHILD1;
			} else {
				child = CHILD2;
			}
			
			parentOfMove.send(new Set(" ", getName(), " ", child, id));
			
			if(removeIndex>=users.size()){
				last(new Success());
				return;
			}
			
			if (parent == null) {
				sequence = 3;
			} else {
				sequence = 2;
			}
		}
		
		void second(Message message) {
			log("change 2");
			if (parent == null) {
				third(message);
				return;
			}
			if (message.getHead() == HEAD.SUCCESS) {
				// moveUser �� �θ�κ��� ���״� �۾� ����
				// parent �� �ڽ��� ����.
				TYPE child;
				int iParent = parent.getPosition(channel);
				int iChild = removeIndex;
				if (iChild - 2 * iParent == 1) {
					child = CHILD1;
				} else {
					child = CHILD2;
				}
				parent.send(new Set(moveUser.getName(), getName(), moveUser.getIP(), child, id));
				sequence = 3;
			}
		}
		
		void third(Message message) {
			log("change 3");
			if (message.getHead() == HEAD.SUCCESS) {
				// parent �� moveUser�� �ڽ����� ���� ����
				// moveUser�� �θ��� parent�� ����
				if (parent != null) {
					moveUser.send(new Set(parent.getName(), getName(), parent.getIP(), PARENT, id));
				} else {
					moveUser.send(new Set(" ", getName(), Server.getIP(), PARENT, id));
				}
				if (child1 != null) {
					sequence = 4;
				} else {
					sequence = 100;
				}
			}
		}
		
		void fourth(Message message) {
			log("change 4");
			if (message.getHead() == HEAD.SUCCESS) {
				// child1 ���� 1
				moveUser.send(new Set(child1.getName(), getName(), child1.getIP(), CHILD1, id));
				sequence = 5;
			}
		}
		
		void fifth(Message message) {
			log("change 5");
			if (message.getHead() == HEAD.SUCCESS) {
				// child1 ���� 2
				child1.send(new Set(moveUser.getName(), getName(), moveUser.getIP(), PARENT, id));
				
				if (child2 != null) {
					sequence = 6;
				} else {
					sequence = 100;
				}
				
			}
		}
		
		void sixth(Message message) {
			log("change 6");
			if (message.getHead() == HEAD.SUCCESS) {
				// child2 ���� 1
				moveUser.send(new Set(child2.getName(), getName(), child2.getIP(), CHILD2, id));
				sequence = 7;
			}
		}
		
		void seventh(Message message) {
			log("change 7");
			if (message.getHead() == HEAD.SUCCESS) {
				// child2 ���� 2
				child2.send(new Set(moveUser.getName(), getName(), moveUser.getIP(), PARENT, id));
				sequence = 100;
			}
		}
		
		void last(Message message) {
			log("change end");
			if (message.getHead() == HEAD.SUCCESS) {
				changes.remove(id);
				send(new Packet(exit, removeUser));
				log("exit ����", removeUser);
			}
		}
		
	}
	
}
