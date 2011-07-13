package util;

import static util.Logger.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import msg.Definition;
import msg.Message;
import msg.Packet;
import msg.sub.Exit;

public class User implements Runnable {
	private String name;
	private final String ip;
	private final Socket socket;
	Map<String, Integer> joinChannels;
	
	private BufferedReader in = null;
	private BufferedWriter out = null;
	
	public User(Socket socket) {
		
		this.socket = socket;
		joinChannels = new HashMap<String, Integer>();
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-16"));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-16"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ip = socket.getInetAddress().toString().substring(1);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIP() {
		return ip;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		String line = "";
		Message message = null;
		// 정상 동작
		try {
			while ((line = in.readLine()) != null) {
				//line = in.readLine();
				log("User : " + name, "run line : " + line);
				if (message == null) {
					message = Message.parsHead(line);
				} else if (message.parse(line)) {
					// server 의 messageQ에 message를 add
					log("메세지 끝", message);
					Packet.Q.offer(new Packet(message, this));
					message = null;
				}
			}
		} catch (IOException e) {
			disconnect("User" + name + " : " + e.getMessage());
			log("User" + name, e);
			return;
		}
	}
	
	public synchronized void send(Message message) {
		try {
			log("User : " + name, "메세지 전송", message);
			out.write(message.toString());
			out.flush();
		} catch (Exception e) {
			disconnect("User" + name + " : " + e.getMessage());
			log("User" + name, e);
		}
	}
	
	public synchronized void send(Packet packet) {
		try {
			log("User : " + name, "메세지 전송", packet.getMessage());
			out.write(packet.getMessage().toString());
			out.flush();
		} catch (Exception e) {
			disconnect("User" + name + " : " + e.getMessage());
			
			log("User" + name, e);
		}
	}
	
	public synchronized void disconnect(String message) {
		// TODO Auto-generated method stub
		if (socket.isClosed()) {
			return;
		}
		log("User disconnect : " + name, message);
		
		Packet.Q.offer(new Packet(new Exit(Definition.ALL, name), this));
		try {
			socket.close();
		} catch (IOException e) {
			log("User" + name, e);
		}
		
	}
	
	public Set<Entry<String, Integer>> getChannelEntrys() {
		// TODO Auto-generated method stub
		return joinChannels.entrySet();
	}
	
	public boolean setPosition(Channel channel, Integer index) {
		joinChannels.put(channel.getName(), index);
		return false;
	}
	
	public int getPosition(Channel channel) {
		return joinChannels.get(channel.getName());
	}
	
	public boolean isJoin(Channel channel) {
		return joinChannels.keySet().contains(channel.getName());
	}
	
	public void remove(Channel channel) {
		// TODO Auto-generated method stub
		joinChannels.remove(channel.getName());
		
	}
}
