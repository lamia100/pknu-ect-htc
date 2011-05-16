package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collection;

import util.Definition;
import util.msg.Message;
import util.msg.sub.Exit;
import util.msg.sub.Join;

/**
 * 
 * @author "김성현"
 * 
 */
public class User implements Comparable<User>, Runnable {
	// 서버 필요함 >> 필요없음. 
	private static MessageProcessor messageProcessor = null;
	private Socket socket = null;
	private String name = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;
	private boolean isRun = true;
	private java.util.Set<Channel> joinChannels = null;
	
	public String getName() {
		return name;
	}
	
	public User(Socket socket) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Server 클레스에서만 호출 할것.
	 */
	public static void setMessageProcessor(MessageProcessor messageProcessor) {
		User.messageProcessor = messageProcessor;
	}
	
	public Collection<Channel> getChannels() {
		return joinChannels;
	}
	
	public void add(Channel channel) {
		joinChannels.add(channel);
	}
	
	public String getIP() {
		return socket.getInetAddress().toString().substring(1);
	}
	
	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		
		return name.compareToIgnoreCase(o.name);
	}
	
	public synchronized void send(Message message) {
		try {
			log("메세지 전송");
			log(message.toString());
			
			out.write(message.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			User t = messageProcessor.getUser(name);
			if (t == this) {
				messageProcessor.remove(this);
			}
			if (socket.isConnected())
				socket.close();
			messageProcessor.enqueue(new Exit("*", name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		isRun = false;
	}
	
	public void initialize() {
		/*
		 * 닉네임 설정등 초기화
		 */
		String line = "";
		Message message = null;
		try {
			while (isRun && (line = in.readLine()) != null) {
				
				log("initialize line : " + line);
				if (message == null) {
					message = Message.parsType(line);
					
				} else {
					if (!(message instanceof Join)) {
						disconnect();
						return;
					}
					boolean isMessageEnd = message.parse(line);
					if (isMessageEnd) {
						Join join = (Join) message;
						if (Definition.ALL.equals(join.getChannel())) {
							name = join.getNick();
							messageProcessor.add(this);
						} else {
							disconnect();
						}
						return;
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageProcessor.add(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 초기화
		initialize();
		// 정상 동작
		String line = "";
		Message message = null;
		try {
			while (isRun && (line = in.readLine()) != null) {
				//line = in.readLine();
				log("run line : " + line);
				if (message == null) {
					try {
						message = Message.parsType(line);
					} catch (NullPointerException e) {
						e.printStackTrace();
						
						this.disconnect();
						this.stop();
					}
				} else if (message.parse(line)) {
					// server 의 messageQ에 message를 add
					
					messageProcessor.enqueue(message);
					message = null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}
	
	private void log(Object...  logs) {
		// TODO Auto-generated method stub
		System.out.println("User " + name);
		for(Object log:logs)
			System.out.println(log);
		System.out.println("----------------------------");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+socket.toString();
	}
	
}
