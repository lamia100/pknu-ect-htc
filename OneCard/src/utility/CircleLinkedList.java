package utility;

import java.util.Iterator;

import org.apache.log4j.*;

public class CircleLinkedList<T> implements Iterable<T> {
	private static Logger logger = Logger.getLogger(CircleLinkedList.class);

	private Node<T> currentNode;
	private int count = 0;

	public CircleLinkedList() {
		currentNode = null;
		count = 0;
	}

	public void add(T element) {
		if (count == 0) {
			currentNode = new Node<T>(element);
		} else if (count == 1) {
			new Node<T>(element, currentNode, currentNode);
		} else {
			new Node<T>(element, currentNode.getPrev(), currentNode);
		}

		count++;

		logger.info(element + "가 추가되었습니다.");
	}

	public T remove() {
		if (currentNode == null) {
			return null;
		}

		count--;

		Node<T> prev = currentNode.getPrev();
		Node<T> next = currentNode.getNext();
		T result = currentNode.getElement();

		if (prev == currentNode) {
			currentNode = null;
			return result;
		}

		prev.setNext(next);
		next.setPrev(prev);
		//currentNode = next;

		logger.info(result + " 가 삭제되었습니다.");

		return result;
	}

	@Deprecated
	public Node<T> remove(T e) {
		return null;
	}

	public Node<T> getHead() {
		return currentNode;
	}

	public T getElement() {
		return currentNode.getElement();
	}

	public T getNext() {
		if (currentNode == null) {
			return null;
		}
		
		currentNode = currentNode.getNext();
		return currentNode.getElement();
	}

	public T getPrev() {
		currentNode = currentNode.getPrev();
		return currentNode.getElement();
	}

	public int size() {
		return count;
	}

	@Override
	public Iterator<T> iterator() {
		return new IteratorCircle();
	}

	private class IteratorCircle implements Iterator<T> {
		Node<T> end = null;
		Node<T> current = null;

		public IteratorCircle() {
			current=end=currentNode;
			
			if (end != null) {
				end = end.getPrev();
				current = new Node<T>(currentNode);
			} 
		}

		@Override
		public boolean hasNext() {
			return current != end;
		}

		@Override
		public T next() {
			current = current.getNext();
			return current.getElement();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	class Node<T1> {
		private Node<T1> next;
		private Node<T1> prev;
		private T1 element;

		Node(T1 element, Node<T1> prev, Node<T1> next) {
			prev.setNext(this);
			this.prev = prev;
			next.setPrev(this);
			this.next = next;
			this.element = element;
		}
		Node(Node<T1> next){
			this.next=next;
		}

		Node(T1 element) {
			this.element = element;
		}

		public T1 getElement() {
			return element;
		}

		public void setElement(T1 element) {
			this.element = element;
		}

		public Node<T1> getNext() {
			return next;
		}

		public Node<T1> getPrev() {
			return prev;
		}

		public void setNext(Node<T1> node) {
			next = node;
		}

		public void setPrev(Node<T1> node) {
			prev = node;
		}
	}
}