package utility;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class IteratorCircleTest {

	@Test
	public void testIteratorCircle() {
		CircleLinkedList<Integer> list=new CircleLinkedList<Integer>();
		Iterator<Integer> iterator =list.iterator();
	}

	@Test
	public void testHasNext() {
		CircleLinkedList<Integer> list=new CircleLinkedList<Integer>();
		
		for(int i=0;i<3;i++){
			list.add(i);
		}
		int count =0;
		for(Integer i:list){
			assertTrue(count==i);
			count++;
			System.out.println(i);
		}
		
	}

	@Test
	public void testNext() {
		fail("아직 구현되지 않음");
	}

}
