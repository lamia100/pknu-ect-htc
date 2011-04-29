import java.io.*;
import java.net.*;

public class Client_receiveFile {
	String serverIP;
	int serverPort;
	String savePathFile;
	
	ServerSocket serverSaveSocket;
	
	// 생성자
	Client_receiveFile(String serverIP, String serverPort_s, String savePathFile) {
		this.serverIP = serverIP;
		serverPort = Integer.parseInt(serverPort_s) + 1;
		this.savePathFile = savePathFile;
		
		// 파일을 받기 위한 소켓 생성
		try {
			serverSaveSocket = new ServerSocket(serverPort);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// 파일 받기 시작
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
				
				// 스트림 생성
				InputStream is = clientSaveSocket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				// 서버로부터 받을 파일의 이름을 받음
				String nameFile = br.readLine();
				
				// 저장할 파일을 받고, 파일로 저장
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