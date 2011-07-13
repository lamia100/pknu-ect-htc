package server;

import java.util.Map.Entry;

import msg.*;
import msg.sub.*;
import util.*;
import static util.Logger.*;

public class ServerManager extends Manager {
	
	private static final Manager manager = new ServerManager();
	
	public static Manager getInstance() {
		return manager;
	}
	
	@Override
	protected void send(Packet packet) {
		Message message = packet.getMessage();
		Channel channel = getChannel(message.getChannel());
		if (channel != null) {
			log("메세지 받았음");
			channel.enqueue(packet);
		} else {
			log(message.getChannel() + "체널이 없음");
			
		}
		
	}
	
	@Override
	protected void join(Packet packet) {
		Join join = (Join) packet.getMessage();
		
		if (Definition.ALL.equals(join.getChannel())) {
			//로그인
			User user = packet.getUser();
			String name = join.getName();
			user.setName(name);
			add(user);
		} else {
			if (packet.getUser().getName() == null) {
				packet.getUser().disconnect("로그인부터 하세요");
				return;
			}
			
			Channel channel = getChannel(join.getChannel());
			if (channel == null) {
				channel = new ServerChannel(join.getChannel());
				new Thread(channel).start();
				channels.put(join.getChannel(), channel);
			}
			channel.enqueue(packet);
		}
	}
	
	@Override
	protected void exit(Packet packet) {
		Exit exit = (Exit) packet.getMessage();
		if (Definition.ALL.equals(exit.getChannel())) {
			User user = packet.getUser();
			if (user != null)
				for (Entry<String, Integer> name : user.getChannelEntrys()) {
					Channel channel = getChannel(name.getKey());
					Packet temp = new Packet(new Exit(channel.getName(), exit.getName()), packet.getUser());
					channel.enqueue(temp);
				}
			remove(user);
		} else {
			Channel channel = getChannel(exit.getChannel());
			if (channel != null) {
				channel.enqueue(packet);
			} else {
				log("Exit. Channel : " + exit.getChannel() + "is null");
			}
		}
		
	}
	
	@Override
	protected void success(Packet packet) {
		Success success = (Success) packet.getMessage();
		log("success", success);
		Channel channel = getChannel(success.getChannel());
		channel.enqueue(packet);
	}
	
	@Override
	protected void script(Packet packet) {
		Script script = (Script) packet.getMessage();
		log("script", script);
		Channel channel = getChannel(script.getChannel());
		channel.enqueue(packet);
	}
	
	@Override
	protected void set(Packet packet) {
		
		//throw new UnsupportedOperationException("아직 구현안됨");
	}
	
	@Override
	protected void fail(Packet packet) {
		//throw new UnsupportedOperationException("아직 구현안됨");
	}
	
	@Override
	protected void request(Packet packet) {
		//throw new UnsupportedOperationException("아직 구현안됨");
	}
	
}