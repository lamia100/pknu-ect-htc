package client;

public class TestThread implements Runnable {

	public String shareData;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		while (true) {
			shareData = "�����忡�� while";
			
			System.out.println(shareData + "������ ���ư��� ����");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void print() {
		System.out.println(shareData + "���� ȣ���� �޽��");
	}
}