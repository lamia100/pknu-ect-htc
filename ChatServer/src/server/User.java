package server;

import java.io.*;
import java.net.Socket;

import util.Definition;
import util.msg.Message;
import util.msg.sub.*;

/**
 * 
 * @author "�輺��"
 * 
 */
public class User implements Comparable<User>, Runnable {
	// ���� �ʿ��� >> �ʿ����. 
	private static MessageProcessor messageProcessor = null;
	
	private Socket socket = null;
	private String name = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;
	private boolean isRun = true;
	
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
	 * Server Ŭ���������� ȣ�� �Ұ�.
	 */
	public static void setMessageProcessor(MessageProcessor messageProcessor) {
		User.messageProcessor = messageProcessor;
	}
	
	public void initialize() {
		/*
		 * �г��� ������ �ʱ�ȭ
		 */
		String line = "";
		Message message = null;
		try {
			while (isRun) {
				line = in.readLine();
				System.out.println(line);
				if (message == null) {
					message = Message.parsType(line);
					
				} else {
					if (!(message instanceof Join)) {
						disconnect();
						return;
					}
					boolean isMessageEnd = message.parse(line);
					if (isMessageEnd) {
						Join join=(Join)message;
						if(Definition.ALL.equals(join.getChannel()))
						{
							name=join.getNick();
							messageProcessor.add(this);
						}else{
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
	public String getIP()
	{
		return socket.getInetAddress().toString();
	}
	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		
		return name.compareToIgnoreCase(o.name);
	}
	
	public synchronized void send(Message message) {
		try {
			System.out.println("user : �޼��� ����");
			System.out.println(message);
			
			out.write(message.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		
	}
	public void stop()
	{
		isRun=false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// �ʱ�ȭ
		initialize();
		// ���� ����
		String line = "";
		Message message = null;
		try {
			while (isRun) {
				line = in.readLine();
				System.out.println(line);
				if (message == null) {
					message = Message.parsType(line);
					
				} else if (message.parse(line)) {
					// server �� messageQ�� message�� add
					
					messageProcessor.enqueue(message);
					message = null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
