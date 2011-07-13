package util;

import java.util.HashMap;
import java.util.Map;

import msg.Packet;
import static util.Logger.*;

public abstract class Manager implements Runnable {
	
	private final Map<String, User> users;
	protected final Map<String, Channel> channels;
	
	public Manager() {
		channels = new HashMap<String, Channel>();
		users = new HashMap<String, User>();
	}
	
	public Channel getChannel(String name) {
		return channels.get(name);
	}
	
	public User getUser(String name) {
		// TODO Auto-generated method stub
		return users.get(name);
	}
	
	public synchronized boolean add(User user) {
		if (users.containsKey(user.getName())) {
			user.disconnect("같은 이름이 있습니다.");
			return false;
		} else {
			users.put(user.getName(), user);
			return true;
		}
	}
	
	/*	
	public synchronized Channel add(Channel channel) {
		Channel temp=channels.get(channel.getName());
		if (temp==null) {
			channels.put(channel.getName(), channel);
			return channel;
		} else {
			return temp;
		}
	}
	*/

	protected void remove(User user) {
		log("remove user : " + users.remove(user.getName()));
	}
	
	protected void remove(Channel channel) {
		channels.remove(channel.getName());
	}
	
	public synchronized void enqueue(Packet packet) {
		log("Manager Enqueue", packet.getMessage());
		Packet.Q.offer(packet);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			log("Manager 동작중");
			Packet packet = null;
			try {
				packet = Packet.Q.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log("Manager",e);
			}
			
			if (packet != null) {
				log("메세지 있음", packet.getMessage());
			} else {
				log("메세지 없음");
			}
			
			if (packet != null) {
				switch (packet.getHead()) {
					case SEND:
						send(packet);
						break;
					case JOIN:
						join(packet);
						break;
					case EXIT:
						exit(packet);
						break;
					case SET:
						set(packet);
						break;
					case SUCCESS:
						success(packet);
						break;
					case FAIL:
						fail(packet);
						break;
					case REQUEST:
						request(packet);
						break;
					case SCRIPT:
						script(packet);
						break;
					default:
						break;
				}
			}
			
		}
	}
	
	protected abstract void send(Packet packet);
	
	protected abstract void join(Packet packet);
	
	protected abstract void exit(Packet packet);
	
	protected abstract void set(Packet packet);
	
	protected abstract void success(Packet packet);
	
	protected abstract void fail(Packet packet);
	
	protected abstract void request(Packet packet);
	
	protected abstract void script(Packet packet);
	
}
