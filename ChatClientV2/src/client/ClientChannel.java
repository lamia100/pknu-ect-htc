package client;

import static util.Logger.log;
import gui.ChannelPanel;

import java.util.Arrays;

import msg.Packet;
import msg.TYPE;
import msg.sub.Script;
import msg.sub.Send;
import msg.sub.Set;
import msg.sub.Success;
import util.Channel;
import util.User;

public class ClientChannel extends Channel {
	static ClientManager manager = ClientManager.getInstance();
	
	private ChannelPanel panel;
	
	public void setPanel(ChannelPanel panel) {
		this.panel = panel;
	}
	
	public ClientChannel(String name) {
		super(name);
		users = Arrays.asList(new User[3]);
	}
	
	private void packetSend(Packet packet) {
		if (packet.getUser() == users.get(0)) {
			log("Channel : " + getName(), packet.getMessage());
			User user = users.get(1);
			if (user != null) {
				log("Channel : " + getName(), "전송 1");
				user.send(packet);
			}
			user = users.get(2);
			if (user != null) {
				log("Channel : " + getName(), " 전송 2");
				user.send(packet);
			}
		} else {
			log("Channel : " + getName(), "이상한놈이 페킷보넴", packet.getMessage());
		}
		
	}
	
	@Override
	protected void send(Packet packet) {
		Send send = (Send) packet.getMessage();
		log(getName() + " : " + send.getMsg());
		panel.append('<' + send.getName() + "> " + send.getMsg());
		packetSend(packet);
	}
	
	@Override
	protected void join(Packet packet) {
		// TODO Auto-generated method stub
		panel.append('<' + packet.getMessage().getName() + "> 님이 입장 하셨습니다.\n");
		packetSend(packet);
	}
	
	@Override
	protected void exit(Packet packet) {
		// TODO Auto-generated method stub
		panel.append('<' + packet.getMessage().getName() + "> 님이 퇴장 하셨습니다.\n");
		packetSend(packet);
	}
	
	@Override
	protected void set(Packet packet) {
		// TODO Auto-generated method stub
		Set set = (Set) packet.getMessage();
		switch (set.getFamily()) {
			case FAMILY_PARENT:
				setParent(packet);
				manager.upload(new Success(getName(), manager.getName(), set.getSequence()));
				break;
			default:
				setChild(packet);
				break;
		}
		
	}
	
	private void setParent(Packet packet) {
		User user = packet.getUser();
		users.set(0, user);
		
	}
	
	private void setChild(Packet packet) {
		Set set = (Set) packet.getMessage();
		if (set.getFamily() == TYPE.FAMILY_CHILD1) {
			users.set(1, packet.getUser());
		} else if (set.getFamily() == TYPE.FAMILY_CHILD2) {
			users.set(2, packet.getUser());
		} else {
			log("Channel : " + getName(), "잘못된 FAMILY TYPE : " + set.getFamily());
		}
	}
	
	//--------------------------------------미구현----------------------------------------//
	@Override
	protected void success(Packet packet) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void fail(Packet packet) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void request(Packet packet) {
		// TODO Auto-generated method stub
		
	}
	
	int scriptcount=0;
	@Override
	protected void script(Packet packet) {
		// TODO Auto-generated method stub
		scriptcount++;
		panel.append(scriptcount+"\n");
		packetSend(packet);
		if(scriptcount%50==0){
			manager.upload(new Script(TYPE.CAST_UNI,getName(),manager.getName(),"end"));
		}
	}
	
}
