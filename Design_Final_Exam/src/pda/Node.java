package pda;

public class Node<T> {
	public T element;
	public Node<T> next;
	public Node<T> prev;
	
	public Node(T ele) {
		this.element = ele;
		next = prev = null;
	}
}
