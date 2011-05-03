package client;

import java.net.ServerSocket;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unused")
public class ConnectServerTest {
	private final static int serverPort = 3244;
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serverSocket = new ServerSocket(serverPort);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() {
		ConnectServer client = new ConnectServer("127.0.0.1", serverPort, "inter6");
		
		while(true) {
			try {
				clientSocket = serverSocket.accept();
				
				ServerRelay relay = new ServerRelay(clientSocket);
				new Thread(relay).start();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class ServerRelay implements Runnable {
	private Socket clientSocket;
	
	public ServerRelay(Socket clientSocket) {
		this.clientSocket = clientSocket; 
	}
	
	@Override
	public void run() {
		System.out.println(clientSocket.getInetAddress().getHostAddress());
	}
}