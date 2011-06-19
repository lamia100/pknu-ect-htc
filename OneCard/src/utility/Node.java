package utility;

public class Node<T> {
	private Node<T> next;
	private Node<T> prev;
	private T element;

	Node(T element, Node<T> prev, Node<T> next) {
		prev.setNext(this);
		this.prev = prev;
		next.setPrev(this);
		this.next = next;
		this.element = element;
	}
	Node(Node<T> next){
		this.next=next;
	}

	Node(T element) {
		this.element = element;
	}

	public T getElement() {
		return element;
	}

	public void setElement(T element) {
		this.element = element;
	}

	public Node<T> getNext() {
		return next;
	}

	public Node<T> getPrev() {
		return prev;
	}

	public void setNext(Node<T> node) {
		next = node;
	}

	public void setPrev(Node<T> node) {
		prev = node;
	}
}