import java.io.*;
import java.net.*;

public class Client_receiveFile {
	String serverIP;
	int serverPort;
	String savePathFile;
	
	ServerSocket serverSaveSocket;
	
	// ������
	Client_receiveFile(String serverIP, String serverPort_s, String savePathFile) {
		this.serverIP = serverIP;
		serverPort = Integer.parseInt(serverPort_s) + 1;
		this.savePathFile = savePathFile;
		
		// ������ �ޱ� ���� ���� ����
		try {
			serverSaveSocket = new ServerSocket(serverPort);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ���� �ޱ� ����
		ReceiveFile myReceiveFile = new ReceiveFile();
		myReceiveFile.start();
	}

	public static void main(String[] args) {
		Client_receiveFile myClient_receiveFile = new Client_receiveFile(args[0], args[1], args[2]);
	}
	
	class ReceiveFile extends Thread {
		public void run() {
			// 
			try{
				Socket clientSaveSocket = serverSaveSocket.accept();
				
				// ��Ʈ�� ����
				InputStream is = clientSaveSocket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				// �����κ��� ���� ������ �̸��� ����
				String nameFile = br.readLine();
				
				// ������ ������ �ް�, ���Ϸ� ����
				File f = new File(savePathFile, nameFile);
				FileOutputStream out = new FileOutputStream(f);
				
				int i=0;
				while((i=is.read())!=-1){
					out.write((char)i);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}