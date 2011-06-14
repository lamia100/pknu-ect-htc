package pda;

import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {
	private Node<T> contents;
	private int count;
	
	public MyLinkedList() {
		contents = null;
		count = 0;
	}
	
	public void add(T addElement) {
		if (!contains(addElement)) {
			Node<T> node = new Node<T>(addElement);
			node.next = contents;
			contents = node;
			count++;
		}
	}

	public void remove(T targetElement) {
		boolean found = false;
		Node<T> prev, current;
		
		if (isEmpty()) {
			return;
		}
		
		if (contents.element.equals(targetElement)) {
			contents = contents.next;
		}
		else {
			prev = contents;
			current = contents.next;
			
			for (int look = 0; look < count && !found; look++) {
				if (current.element.equals(targetElement)) {
					found = true;
				}
				else {
					prev = current;
					current = current.next;
				}
			}
			
			if (!found) {
				return;
			}
			
			prev.next = current.next;
		}
		
		count--;
	}
	
	private boolean contains(T targetElement) {
		boolean found = false;
		Node<T> current = contents;
		
		for (int look = 0; look < count && !found; look++) {
			if (current.element.equals(targetElement)) {
				found = true;
			}
			else {
				current = current.next;
			}
		}
		
		return found;
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return (size() == 0);
	}

	@Override
	public Iterator<T> iterator() {
		return new IteratorLinkedList(contents, count);
	}

	public class IteratorLinkedList implements Iterator<T> {
		private Node<T> current;
		
		public IteratorLinkedList(Node<T> contents, int size) {
			current = contents;
		}

		@Override
		public boolean hasNext() {
			return (current != null);
		}

		@Override
		public T next() {
			if (!hasNext()) {
				return null;
			}
			
			T result = current.element;
			current = current.next;
			
			return result;
		}

		@Override
		public void remove() {
						
		}
	}
}