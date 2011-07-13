package util;

import static util.Logger.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import msg.Packet;

/*
 * 자식은 자신*2 + (1|2) (ex. 1 - (3,4), 2 - (5,6), 3 - (7,8), 4 - (9,10)
 * 부모는 (자신-1) / 2 (ex. 1 - 0, 2 - 0, 3 - 1, 4 - 1, 5 - 2, 6 - 2...)
 * 항상 users[1],users[2]에 전송. 
 */

public abstract class Channel implements Runnable {
	private String name;
	protected List<User> users;
	private BlockingQueue<Packet> packetQ = new LinkedBlockingQueue<Packet>();

	public Channel(String name) {
		this.name = name;
		users = new ArrayList<User>();
		users.add(null);
	}

	public synchronized void enqueue(Packet packet) {
		log("Channel : " + name + " Enqueue", packet.getMessage());
		packetQ.offer(packet);
	}

	protected abstract void send(Packet packet);

	protected abstract void join(Packet packet);

	protected abstract void exit(Packet packet);

	protected abstract void set(Packet packet);

	protected abstract void success(Packet packet);

	protected abstract void fail(Packet packet);

	protected abstract void request(Packet packet);

	protected abstract void script(Packet packet);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			Packet packet;
			log("Channel : " + name, "동작중");
			try {
				packet = packetQ.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log("Channel "+name,e);
				continue;
			}
			log("Channel"+getName(),"메세지  디큐", packet);
			if (packet != null) {
				log("Channel"+getName(),"메세지 정상처리", packet.getHead(), packet.getMessage());
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
			//사람수가 0이면 break;
		}
	}

	public String getName() {
		return name;
	}

}
