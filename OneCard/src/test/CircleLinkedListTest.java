package test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Random;

import org.junit.Test;

import utility.CircleLinkedList;

public class CircleLinkedListTest {

	@Test
	public void testCircleLinkedList() {
		CircleLinkedList<Integer> cl = new CircleLinkedList<Integer>();
		
		assertEquals(cl.size(), 0);
	}
	
	@Test
	public void testAdd() {
		CircleLinkedList<Integer> cl = new CircleLinkedList<Integer>();

		for (int i = 1; i <= 10; i++) {
			cl.add(i);
			assertEquals(cl.size(), i);
		}

		assertEquals(cl.size(), 10);
	}

	@Test
	public void testGetPrev() {
		CircleLinkedList<Integer> cl = new CircleLinkedList<Integer>();

		for (int i = 1; i <= 10; i++) {
			cl.add(i);
		}

		for (int i = 10; i >= 1; i--) {
			assertTrue(cl.getPrev() == i);
		}
	}
	
	@Test
	public void testGetNext() {
		CircleLinkedList<Integer> cl = new CircleLinkedList<Integer>();

		for (int i = 1; i <= 10; i++) {
			cl.add(i);
		}

		for (int i = 1; i <= 10; i++) {
			assertTrue(cl.getElement() == i);
			cl.getNext();
		}
	}
	
	@Test
	public void testRemove() {
		CircleLinkedList<Integer> cl = new CircleLinkedList<Integer>();

		for (int i = 1; i <= 10; i++) {
			cl.add(i);
		}

		for (int i = 1; i <= 5; i++) {
			assertTrue(cl.remove() == i);
			cl.getNext();
		}
		
		for (int i = 6; i <= 10; i++) {
			assertTrue(cl.remove() == i);
			
			System.out.println(i);
			
			cl.getNext();
		}
	}

	@Test
	public void testSize() {
		Random rand = new Random();
		int count = rand.nextInt(100);
		
		CircleLinkedList<Integer> cl = new CircleLinkedList<Integer>();

		for (int i = 1; i <= count; i++) {
			cl.add(i);
		}

		assertEquals(cl.size(), count);
	}
	
	@Test
	public void testIteratorCircle() {
		CircleLinkedList<Integer> list = new CircleLinkedList<Integer>();
		@SuppressWarnings("unused")
		Iterator<Integer> iterator = list.iterator();
	}

	@Test
	public void testIteratorx() {
		CircleLinkedList<Integer> list = new CircleLinkedList<Integer>();

		for (int i = 0; i < 10; i++) {
			list.add(i);
		}
		
		int count = 0;
		for (Integer i : list) {
			assertTrue(count == i);
			count++;
		}
	}
}