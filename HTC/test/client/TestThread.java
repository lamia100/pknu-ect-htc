package client;

import java.util.StringTokenizer;

public class TestThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		while (true) {
			System.out.println("쓰레드에서 while문으로 돌아가는 영역");
			
			String str = "ffsa|=|asf|=|asdf";
			
			StringTokenizer st = new StringTokenizer(str, "|=|");
			
			while(st.hasMoreTokens()) {
				System.out.println(st.nextToken());
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void print() {
		System.out.println("외부에서 호출한 메쏘드");
	}
}