package test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import utility.CircleLinkedList;

public class IteratorCircleTest {

	@Test
	public void testIteratorCircle() {
		CircleLinkedList<Integer> list = new CircleLinkedList<Integer>();
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