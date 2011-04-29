import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server_fileServer extends JFrame {
	JPanel p_logFile;
	JPanel p_clientListFile;
	
	JTextArea ta_logFile;
	JScrollPane sp_logFile;
	JScrollBar sb_logFile;
	
	JTextArea ta_clientListFile;
	JScrollPane sp_clientListFile;
	JScrollBar sb_clientListFile;
	
	ServerSocket serverSocketFile;
	Socket clientSocketFile;
	String serverIP;
	int serverPortFile;
	
	Vector<Object> clientListFile;
	Vector<String> clientIPListFile;
	
	// 생성자 - 파일 서버 UI 생성
	Server_fileServer(String serverIP, String serverPort_s) { // 생성자
		super("파일 서버 IP :: " + serverIP + " / 파일 서버 포트 :: " + serverPort_s);
		
		this.serverIP = serverIP;
		
		// 서버 포트(String형)를 int형으로 변환한 뒤, 1을 더함
		// 따라서 채팅 서버와 파일 서버의 포트는 달리 수행된다. 
		serverPortFile = Integer.parseInt(serverPort_s) + 1; 
		
		// 클라이언트 리스트를 저장하는 벡터
		clientListFile = new Vector<Object>();
		clientIPListFile = new Vector<String>();
		
		// 로그를 표시하는 패널
		p_logFile = new JPanel(new BorderLayout());
		{
			ta_logFile = new JTextArea();
			ta_logFile.setEditable(false);
			ta_logFile.setLineWrap(true);
			ta_logFile.setBackground(Color.black);
			ta_logFile.setForeground(Color.white);
			ta_logFile.setMargin(new Insets(5, 5, 5, 5));
			
			// 스크롤 패널(세로 스크롤 항상 표시, 가로스크롤 표시하지 않음)에 텍스트필드를 얹음
			sp_logFile = new JScrollPane(ta_logFile);
			sp_logFile.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_logFile.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_logFile = sp_logFile.getVerticalScrollBar();
		}
		p_logFile.add(BorderLayout.CENTER, sp_logFile);
		
		// 클라이언트 리스트를 표시하는 패널
		p_clientListFile = new JPanel(new BorderLayout());
		{
			ta_clientListFile = new JTextArea();
			ta_clientListFile.setEditable(false);
			ta_clientListFile.setLineWrap(true);
			ta_clientListFile.setBackground(Color.black);
			ta_clientListFile.setForeground(Color.white);
			ta_clientListFile.setMargin(new Insets(5, 5, 5, 5));
			
			// 스크롤 패널(세로 스크롤 항상 표시, 가로스크롤 표시하지 않음)에 텍스트필드를 얹음
			sp_clientListFile = new JScrollPane(ta_clientListFile);
			sp_clientListFile.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sp_clientListFile.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sb_clientListFile = sp_clientListFile.getVerticalScrollBar();
		}
		p_clientListFile.add(BorderLayout.CENTER, sp_clientListFile);
		
		getContentPane().add(BorderLayout.CENTER, p_logFile);
		getContentPane().add(BorderLayout.EAST, p_clientListFile);
	}
	
	public static void main(String[] args) { // 메인
		Server_fileServer myServer_fileServer = new Server_fileServer(args[0], args[1]);
		
		//창의 x 버튼을 누르면 프로그램을 종료한다.
		myServer_fileServer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myServer_fileServer.setSize(500, 300);
		myServer_fileServer.setLocation(new Point(500, 0));
		myServer_fileServer.setVisible(true);
		
		// 파일 서버 실행 가능 여부를 체크
		boolean serverState = myServer_fileServer.fileServerStart();
		
		// 파일 서버 실행이 가능하면, 클라이언트 접속을 기다림
		if(serverState) {
			myServer_fileServer.fileServerWait();
		}
		
		// 파일 서버 실행이 불가능하면, 로그를 띄움
		else {
			myServer_fileServer.dspLogFile("[Error] 파일 서버 시작에 실패하였습니다.");
		}
	}
	
	// 파일 서버를 시작하는 메쏘드
	boolean fileServerStart() {
		
		// 파일 서버 실행이 가능하면, 로그를 띄우고 true를 리턴
		try {
			serverSocketFile = new ServerSocket(serverPortFile);
			
			dspLogFile("[Success] 파일 서버가 시작되었습니다.");
			return true;
		}
		
		// 파일 서버 실행이 불가능하면, 로그를 띄우고 fasle를 리턴 
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return false;
		}
	}

	// 클라이언트의 접속을 기다리는 메쏘드
	void fileServerWait() {
		while(true) {
			try {
				// 클라이언트가 접속될 때까지 기다린다.
				clientSocketFile = serverSocketFile.accept();
				
				// 클라이언트가 접속하면, 클라이언트별로 중계 쓰레드를 시작한다.
				FileServerRelay myFileServerRelay = new FileServerRelay(clientSocketFile);
				myFileServerRelay.start();
				
				// 리스트에 해당 클라이언트<Object>를 벡터에 추가
				clientListFile.addElement(myFileServerRelay);
			}
			
			// 예외가 발생하면 로그를 띄운다.
			catch(Exception e) {
				System.out.println("[Error] 서버 소켓 생성에 실패하였습니다.");
				e.printStackTrace();
			}
		}
	}
	
	// 클라이언트를 중계하는 클래스(inner)
	class FileServerRelay extends Thread {
		Socket clientSocketFile;
		BufferedReader bufferedReader;
		PrintWriter printWriter;
		DataInputStream dis;
		DataOutputStream dos;
		
		String clientIP;
		String clientNameFile;
		
		// 생성자
		FileServerRelay(Socket clientSocketFile) {
			this.clientSocketFile = clientSocketFile;
			
			// 접속한 해당 클라이언트의 IP를 저장한다.
			clientIP = clientSocketFile.getInetAddress().getHostAddress();
		}
		
		// 클라이언트의 중계를 시작 
		public void run() {
			try {
				// 해당 클라이언트와의 스트림을 생성
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocketFile.getInputStream()));
				printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocketFile.getOutputStream())));
				
				// 해당 클라이언트가 보내는 파일명을 저장
				clientNameFile = bufferedReader.readLine();
				
				// 해당 클라이언트가 파일을 보낸다는 것을, 로그에 표시
				dspLogFile("[Info] " + " (" + clientIP + ") 가 " + clientNameFile + " 을 보냅니다.");
				
				// 해당 클라이언트의 IP를 벡터에 추가한 뒤, 리스트 표시
				clientIPListFile.addElement(clientIP);
				dspClientListFile();
				
				
				try {
					// 해당 클라이언트와의 파일용 스트림을 생성
					dis = new DataInputStream(clientSocketFile.getInputStream());
					dos = new DataOutputStream(clientSocketFile.getOutputStream());
					
					// 해당 클라이언트가 보내는 파일을 각 클라이언트에게 전송
					int i = 0;
					while((i = dis.read()) != -1 ){
						broadCastFile(i);
					}
					
					dspLogFile("[Info] " + " (" + clientIP + ") 의 " + clientNameFile + " 을 성공적으로 전송하였습니다.");
				}
				
				// 예외가 발생하면, 해당 클라이언트와의 파일용 스트림을 해제
				catch(Exception e) {
					if(dis != null) {
						try {
							dis.close();
						}
						catch(Exception ex) {
							e.printStackTrace();
						}
					}
					if(dos != null) {
						try {
							dos.close();
						}
						catch(Exception ex) {
							e.printStackTrace();
						}
					}
				}
			}
			
			// 예외가 발생하면, 해당 클라이언트의 연결이 끊어졌다는 것을 의미
			catch (Exception e) {
				// 해당 클라이언트의 연결이 끊어졌다는 것을, 로그에 표시
				dspLogFile("[Info] " + " (" + clientIP + ") 가 연결을 끊었습니다.");
				
				// 해당 클라이언트의 IP를 벡터에서 삭제
				clientIPListFile.removeElement(clientIP);
			}
			
			// 마지막으로 해당 클라이언트와의 스트림과 소켓을 해제 
			finally {
				if(bufferedReader != null) {
					try {
						bufferedReader.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(clientSocketFile != null) {
					try {
						clientSocketFile.close();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// 다른 클라이언트에게 파일을 보내는 메쏘드
		void sendFile(int clientFile) {
			try {
				dos.writeByte(clientFile);
				dos.flush();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 모든 클라이언트에게 파일을 보내기 위한 메쏘드
	void broadCastFile(int serverFile) {
		// Enumeration 인터페이스에 클라이언트 리스트가 담겨있는 벡터를 할당
		Enumeration<Object> container = clientListFile.elements();
		
		// 커서에 데이터가 없을 때까지 반복
		while(container.hasMoreElements()) {
			// 객체를 리턴하고, 커서를 뒤로 이동
			FileServerRelay myFileServerRelay = (FileServerRelay)container.nextElement();
			
			// 해당 클라이언트에게 파일을 보냄
			myFileServerRelay.sendFile(serverFile);
		}
	}
	
	// 로그를 띄우는 메쏘드
	void dspLogFile(String MSG) {
		// 스크롤바의 맨끝으로 이동한 뒤, 로그를 표시한다.
		sb_logFile.setValue(sb_logFile.getMaximum());
		ta_logFile.append(MSG + "\n");
	}
	
	// 접속자 목록을 띄우는 메쏘드
	void dspClientListFile() {
		ta_clientListFile.setText("접속자 목록 :");
		
		// 클라이언트의 갯수까지 반복
		for(int i=0 ; i<clientIPListFile.size(); i++ ) {
			// 클라이언트 IP를 표시
			ta_clientListFile.append("\n" + " (" + clientIPListFile.get(i) + ")");
		}
	}
}