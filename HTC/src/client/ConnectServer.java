package client;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConnectServer  implements Runnable {
	private String serverIP;
	private int serverPort;
	
	private Socket toServerSocket;
	private BufferedReader fromServerMsg;
	private BufferedWriter toServerMsg;
	
	private String nickName;
	
	public ConnectServer(String serverIP, int serverPort, String nickName) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.nickName = nickName;
	}
	
	public boolean login(String nickName) {
		try {
			toServerSocket = new Socket(serverIP, serverPort);
			
			fromServerMsg = new BufferedReader(new InputStreamReader(toServerSocket.getInputStream()));
			toServerMsg = new BufferedWriter(new OutputStreamWriter(toServerSocket.getOutputStream()));
			
			toServerMsg.write(nickName);
			toServerMsg.flush();
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reqeustJoinChannel(int channel) {
		try {
			toServerMsg.write("/join " + channel);
			toServerMsg.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestMsg(int channel, String msg) {
		try {
			toServerMsg.write("/sendMsg " + channel + " " + msg);
			toServerMsg.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean requestCertainClientIP(String certainNick) {
		try {
			toServerMsg.write("/getIP " + certainNick);
			toServerMsg.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean reportImExit(int channel) {
		try {
			toServerMsg.write("/exit " + channel);
			toServerMsg.flush();
			
			if (channel == 0) {
				toServerMsg.close();
				fromServerMsg.close();
				toServerSocket.close();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean reportMyFamilyDisconnect(int channel, String who, String ip) {
		try {
			toServerMsg.write("/disconnet " + channel + " " + who + " " + ip);
			toServerMsg.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveMyFamilyInfo(int channel, String who, String ip) {
		try {
			boolean apply = false;
			
			if ("parent".equals(who)) {
				apply = true; // 나의 부모를 바꾸고
			}
			else if ("leftSon".equals(who)) {
				apply = true; // 나의 왼쪽 자식을 바꾸고
			}
			else if ("rightSon".equals(who)) {
				apply = true; // 나의 오른쪽 자식을 바꾸고
			}
			
			if (apply) {
				toServerMsg.write("/ack " + channel + " " + who + " " + ip);
			}
			else {
				toServerMsg.write("/nak " + channel + " " + who + " " + ip);
			}
			
			toServerMsg.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean receiveOtherClientIP(String nick, String ip) {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			login(nickName);
			
			while (toServerSocket.isConnected()) {
				String fromServer = fromServerMsg.readLine();
				
				while (fromServer != null) {
					StringTokenizer fromServerToken = new StringTokenizer(" ");
					ArrayList<String> parseFromServerMsg = new ArrayList<String>();
					
					while (fromServerToken.hasMoreTokens()) {
						parseFromServerMsg.add(fromServerToken.nextToken());
					}
					
					if ("/yourParentIs".equals(parseFromServerMsg.get(0))) {
						receiveMyFamilyInfo("parent", parseFromServerMsg.get(1));
					}
					else if ("/yourLeftSonIs".equals(parseFromServerMsg.get(0))) {
						receiveMyFamilyInfo("leftSon", parseFromServerMsg.get(1));
					}
					else if ("/yourRightSonIs".equals(parseFromServerMsg.get(0))) {
						receiveMyFamilyInfo("rightSon", parseFromServerMsg.get(1));
					}
					else if ("/responseOtherClientIP".equals(parseFromServerMsg.get(0))) {
						receiveMyFamilyInfo("parent", parseFromServerMsg.get(1));
					}
					else {
						
					}
					
					fromServer = fromServerMsg.readLine();
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}