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
		currentNode = next;

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
			// TODO Auto-generated constructor stub
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
			// TODO Auto-generated method stub
			current = current.getNext();
			return current.getElement();
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}
	}
}