package utility;

public class CircleLinkedList<T> {
	private Node<T> currentNode;
	private int count = 0;

	public void add(T element) {
		if (count == 0) {
			currentNode = new Node<T>(element);
		} else if (count == 1) {
			new Node<T>(element, currentNode, currentNode);
		} else {
			new Node<T>(element, currentNode.getPrev(), currentNode);
		}

		count++;
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
}