package client;

import static util.Logger.log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Map;

import msg.Definition;
import msg.Message;
import msg.Packet;
import msg.TYPE;
import msg.sub.Set;
import msg.sub.Success;
import util.Channel;
import util.Manager;
import util.User;

public class ClientManager extends Manager {
	
	private static final ClientManager manager = new ClientManager();
	
	public static ClientManager getInstance() {
		return manager;
	}
	
	private User server;
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setServer(User server) {
		this.server = server;
	}
	
	public boolean isConnected() {
		return server != null;
	}
	
	public boolean isClosed() {
		return server == null;
	}
	
	MakeUser makeUser;
	
	public ClientManager() {
		super();
		//User server 를 초기화 할것.
		makeUser = new MakeUser();
		makeUser.start();
	}
	
	public synchronized void add(Channel channel) {
		if (getChannel(channel.getName()) == null) {
			channels.put(channel.getName(), channel);
			new Thread(channel).start();
		}
	}
	
	private void packetProcess(Packet packet) {
		Channel channel = getChannel(packet.getMessage().getChannel());
		if (channel != null) {
			channel.enqueue(packet);
		} else {
			log(packet.getMessage().getChannel() + "is null");
		}
	}
	
	@Override
	protected void send(Packet packet) {
		packetProcess(packet);
	}
	
	@Override
	protected void join(Packet packet) {
		packetProcess(packet);
		
	}
	
	@Override
	protected void exit(Packet packet) {
		if (Definition.ALL.equals(packet.getMessage().getChannel())) {
			return;
		}
		packetProcess(packet);
	}
	
	@Override
	protected void set(Packet packet) {
		//메니저에서 처리.
		//User 를 packet에 설정해주고 Channel에 전달.
		if (packet.getUser() == server) {
			Set set = (Set) packet.getMessage();
			if (set.getFamily() == TYPE.FAMILY_PARENT) {
				if ("".equals(set.getName())) {
					manager.getChannel(set.getChannel()).enqueue(packet);
					return;
				}
				
				User user = getUser(set.getName());
				if (user == null) {
					try {
						Socket socket = new Socket(set.getIP(), Definition.DEFAULT_PORT);
						user = new User(socket);
						new Thread(user).start();
						user.setName(set.getName());
						manager.add(user);
					} catch (UnknownHostException e) {
						log("ClientManager", e);
						return;
					} catch (IOException e) {
						log("ClientManager", e);
						return;
					}
				}
				
				packet.setUser(user);
				manager.getChannel(set.getChannel()).enqueue(packet);
			} else {
				//isChild
				if ("".equals(set.getName())) {
					packet.setUser(null);
					manager.getChannel(set.getChannel()).enqueue(packet);
				} else {
					makeUser.add(packet);
					manager.upload(new Success(set.getChannel(), manager.getName(), set.getSequence()));
				}
			}
		}
	}
	
	//--------------------------------------미구현----------------------------------------//
	@Override
	protected void success(Packet packet) {
		packetProcess(packet);
	}
	@Override
	protected void script(Packet packet) {
		packetProcess(packet);
	}
	@Override
	protected void fail(Packet packet) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void request(Packet packet) {
		// TODO Auto-generated method stub
	}
	
	
	
	//--------------------------------------미구현----------------------------------------//
	
	class MakeUser extends Thread {
		private Map<String, Packet> packetTable;
		private ServerSocket serverSocket;
		
		public MakeUser() {
			packetTable = new Hashtable<String, Packet>();
			// TODO Auto-generated constructor stub
			try {
				serverSocket = new ServerSocket(Definition.DEFAULT_PORT);
			} catch (IOException e) {
				log("ClientManager", e);
				System.exit(1);
			}
		}
		
		public void add(Packet packet) {
			Set set = (Set) packet.getMessage();
			packetTable.put(set.getIP(), packet);
		}
		
		@Override
		public void run() {
			while (true) {
				Socket socket;
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					log("ClientManager", e);
					continue;
				}
				String ip = socket.getInetAddress().toString().substring(1);
				Packet packet = packetTable.get(ip);
				
				if (packet != null) {
					User user = new User(socket);
					new Thread(user).start();
					//페킷으로부터 user의 정보를 입력할 필요가 있음.
					user.setName(packet.getMessage().getName());
					manager.add(user);
					packet.setUser(user);
					Message message = packet.getMessage();
					Channel channel = getChannel(message.getChannel());
					if (channel != null) {
						channel.enqueue(packet);
					} else {
						log("Make User. Channel : " + message.getChannel() + "is null");
					}
					
				} else {
					try {
						socket.close();
					} catch (IOException e) {
						log("ClientManager", e);
					}
				}
				
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void upload(String string) {
		
	}
	
	public void upload(Message message) {
		// TODO Auto-generated method stub
		System.out.println(message);
		if (server != null)
			server.send(message);
		
	}
	
}
