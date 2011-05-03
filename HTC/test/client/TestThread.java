package client;

public class TestThread implements Runnable {

	public String shareData;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		while (true) {
			shareData = "쓰레드에서 while";
			
			System.out.println(shareData + "문으로 돌아가는 영역");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void print() {
		System.out.println(shareData + "에서 호출한 메쏘드");
	}
}