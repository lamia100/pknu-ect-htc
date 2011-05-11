package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import util.msg.Message;
import util.msg.sub.*;
import static util.Definition.*;
import test.GUI;

public class Manager implements Runnable {
	private final static int MAX_CHILD = 2;
	
	private Queue<Packet> packetQueue;
	private ArrayList<Send> msgList;
	private int msgOffset;
	
	private Server connectServer;
	private Parent connectParent;
	private Childs connectChilds;
	
	private GUI gui;
	private boolean isFirstConnect;
	
	public Manager(GUI gui) {
		packetQueue = new LinkedList<Packet>();
		msgList = new ArrayList<Send>();
		msgOffset = 0;
		
		this.gui = gui;
		isFirstConnect = true;
	}
	
	public boolean addPacket(Packet addPacket) {
		return packetQueue.offer(addPacket);
	}
	
	private int getLastSequence() {
		return msgOffset + msgList.size() - 1;
	}
	
	private Send getMsg(int sequence) {
		return msgList.get(sequence - msgOffset - 1);
	}
	
	public void debug(String msg) {
		System.out.println(msg);
	}
	
	// ------------------------------------------------- GUI input -------------------------------------------------
	
	public boolean connectServer(String serverIP, int serverPort, String nickName) {
		connectServer = new Server(this, serverIP, serverPort);
		boolean result = connectServer.loginServer();
		
		if (result) {
			gui.dspInfo("������ ���� ����(3way Handshake)�� �����Ͽ����ϴ�.");
			
			result = connectServer.joinChannel(ALL, nickName);
			
			if (result) {
				gui.dspInfo("������ ����(ALL)�� �����Ͽ����ϴ�.");
				
				connectChilds = new Childs(this, DEFAULT_PORT);
				new Thread(connectChilds).start();
				
				gui.dspInfo("�ڽ��� ���� �غ� �Ǿ����ϴ�.");
			}
			else {
				gui.dspInfo("������ ����(ALL)�� �����Ͽ����ϴ�. �ٸ� �г����� ����ϼ���.");
			}
		}
		else {
			gui.dspInfo("������ ���� ����(3way Handshake)�� �����Ͽ����ϴ�.");
		}
		
		return result;
	}
	
	public boolean disconnectServer(String nickName) {
		connectServer.exitChannel(ALL, nickName);		
		connectParent.logoutParent();
		connectChilds.closeAllChild();
		connectServer.logoutServer();
		
		gui.dspInfo("��� ������ �������ϴ�.");
		
		return true;
	}
	
	public boolean joinChannel(String channel, String nickName) {
		boolean result = connectServer.joinChannel(channel, nickName);
		
		if (result) {
			gui.dspInfo("������ ä�� ���� �޼����� ���½��ϴ�.");
		}
		else {
			gui.dspInfo("������ ä�� ���� �޼����� ������ ���߽��ϴ�. ������ ������ ������ ������ϴ�.");
		}
		
		return result;
	}
	
	public boolean exitChannel(String channel, String nickName) {
		boolean result = connectServer.exitChannel(channel, nickName);
		
		if (result) {
			gui.dspInfo("������ ä�� ���� �޼����� ���½��ϴ�.");
		}
		else {
			gui.dspInfo("������ ä�� ���� �޼����� ������ ���߽��ϴ�. ������ ������ ������ ������ϴ�.");
		}
		
		return result;
	}
	
	public boolean sendMsg(String channel, String nickName, String msg) {
		boolean result = connectServer.sendMsgToServer(channel, nickName, msg);
		
		if (result) {
			gui.dspInfo("������ �޼����� ���½��ϴ�.");
		}
		else {
			gui.dspInfo("������ �޼����� ������ ���߽��ϴ�. ������ ������ ������ ������ϴ�.");
		}
		
		return result;
	}
	
	
	// ------------------------------------------------- GUI output -------------------------------------------------
	
	private void display(Message msg) {
		switch (msg.getType()) {
		case SEND:
			Send send = (Send)msg;
			msgList.add(send);
			gui.dspMsg(send.getNick() + " : " + send.getMsg());
		case JOIN:
			Join join = (Join)msg;
			gui.dspInfo(join.getChannel() + " ä�ο� " + join.getNick() + "��(��) �����Ͽ����ϴ�.");
		case EXIT:
			Exit exit = (Exit)msg;
			gui.dspInfo(exit.getChannel() + " ä�ο��� " + exit.getNick() + "��(��) �����̽��ϴ�.");
		}
	}
	
	// ------------------------------------------------- P E R F O R M -------------------------------------------------
	
	@Override
	public void run() {
		while (true) {
			Packet packet = null;
			
			if ((packet = packetQueue.poll()) != null) {
				if (packet.getMessage().isValid()) {
					performService(packet);
				}
			}
		}
	}
	
	private boolean performService(Packet packet) {
		boolean result = false;
		
		switch (packet.getMessage().getType()) {
		case SEND:
			Send send = (Send)packet.getMessage();
			
			switch (send.getCast()) {
			case CAST_BROAD:
				if (isFirstConnect) {
					msgOffset = send.getSeq();
					isFirstConnect = false;
					
					display(send);
					result = connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
				}
				else {
					if (send.getSeq() == getLastSequence() - 1) {
						display(send);
						result = connectChilds.sendMsgToAllChild(send.getChannel(), send.getSeq(), send.getNick(), send.getMsg());
					}
					else {
						result = connectParent.requestMsgToParent(send.getChannel(), Integer.toString(getLastSequence() + 1));
					}
				}
				
				break;
			case CAST_UNI:
				display(send);
				result = true;
				
				break;
			default:
			}
			
			break;
		case REQUEST:
			Request request = (Request)packet.getMessage();
			
			int startSequence = request.getSeq();
			boolean sendResult = true;
			
			if (startSequence > getLastSequence()) {
				sendResult = connectServer.requestMsgToServer(request.getChannel(), startSequence);
			}
			else {
				while (startSequence <= getLastSequence() && sendResult) {
					Send msg = getMsg(startSequence++);
					sendResult = connectChilds.sendMsgToSomeChild(packet.getFromIP(), msg.getChannel(), msg.getSeq(), msg.getNick(), msg.getMsg());
				}
			}
			
			result = sendResult;
			
			break;
		case SET:
			Set set = ((Set)packet.getMessage());
			
			switch (set.getFamily()) {
			case FAMILY_PARENT:
				Parent newParent = new Parent(this, set.getIp(), DEFAULT_PORT);
				
				if (newParent.loginParent()) {
					connectParent = newParent;
					result = connectServer.successConnectToParent(set.getChannel(), set.getIp());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp());
				}
				
				break;
			case FAMILY_CHILD:
				if (connectChilds.getChildSize() < MAX_CHILD) {
					result = connectServer.successOpenSocketForChild(set.getChannel(), set.getIp());
					
					Send msg = msgList.get(msgList.size() - 1);
					connectChilds.sendMsgToSomeChild(set.getIp(), msg.getChannel(), msg.getSeq(), msg.getNick(), msg.getMsg());
				}
				else {
					result = connectServer.failOpenSocketForChild(set.getChannel(), set.getIp());
				}
				
				break;
			default:	
			}
			
			break;
		case JOIN:
			Join join = (Join)packet.getMessage();
			
			display(join);
			result = connectChilds.whoJoinToAllChild(join.getChannel(), join.getNick());
			break;
		case EXIT:
			Exit exit = (Exit)packet.getMessage(); 
			
			display(exit);
			result = connectChilds.whoExitToAllChild(exit.getChannel(), exit.getNick());
			break;
		}
		
		return result;
	}
}