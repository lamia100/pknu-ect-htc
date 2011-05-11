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
			gui.dspInfo("서버와 연결 설정(3way Handshake)에 성공하였습니다.");
			
			result = connectServer.joinChannel(ALL, nickName);
			
			if (result) {
				gui.dspInfo("서버와 연결(ALL)에 성공하였습니다.");
				
				connectChilds = new Childs(this, DEFAULT_PORT);
				new Thread(connectChilds).start();
				
				gui.dspInfo("자식을 받을 준비가 되었습니다.");
			}
			else {
				gui.dspInfo("서버와 연결(ALL)에 실패하였습니다. 다른 닉네임을 사용하세요.");
			}
		}
		else {
			gui.dspInfo("서버와 연결 설정(3way Handshake)에 실패하였습니다.");
		}
		
		return result;
	}
	
	public boolean disconnectServer(String nickName) {
		connectServer.exitChannel(ALL, nickName);		
		connectParent.logoutParent();
		connectChilds.closeAllChild();
		connectServer.logoutServer();
		
		gui.dspInfo("모든 연결을 끊었습니다.");
		
		return true;
	}
	
	public boolean joinChannel(String channel, String nickName) {
		boolean result = connectServer.joinChannel(channel, nickName);
		
		if (result) {
			gui.dspInfo("서버에 채널 입장 메세지를 보냈습니다.");
		}
		else {
			gui.dspInfo("서버에 채널 입장 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		return result;
	}
	
	public boolean exitChannel(String channel, String nickName) {
		boolean result = connectServer.exitChannel(channel, nickName);
		
		if (result) {
			gui.dspInfo("서버에 채널 퇴장 메세지를 보냈습니다.");
		}
		else {
			gui.dspInfo("서버에 채널 퇴장 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
		}
		
		return result;
	}
	
	public boolean sendMsg(String channel, String nickName, String msg) {
		boolean result = connectServer.sendMsgToServer(channel, nickName, msg);
		
		if (result) {
			gui.dspInfo("서버에 메세지를 보냈습니다.");
		}
		else {
			gui.dspInfo("서버에 메세지를 보내지 못했습니다. 이전에 서버와 연결이 끊겼습니다.");
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
			gui.dspInfo(join.getChannel() + " 채널에 " + join.getNick() + "이(가) 접속하였습니다.");
		case EXIT:
			Exit exit = (Exit)msg;
			gui.dspInfo(exit.getChannel() + " 채널에서 " + exit.getNick() + "이(가) 나가셨습니다.");
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