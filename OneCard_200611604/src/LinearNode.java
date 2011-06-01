// 하나의 노드를 구성하는 클래스
/* 게임진행순서를 위해 Ring(순방향 순환, 역방향 순환) 방식의 Linked 구조를 사용해야하기 때문에 
 * 하나의 노드는 next는 물론 prev 노드까지 알고있어야 함
 */
public class LinearNode<T> {
	private LinearNode<T> prev;
	private LinearNode<T> next;
	private T element;
	
	// 아직까지 다른 데서 딱히 사용치 않음
	public LinearNode() {
		prev = null;
		next = null;
		element = null;
	}
	
	public LinearNode(T element) {
		prev = null;
		next = null;
		this.element = element;
	}
	
	public LinearNode<T> getPrev() {
		return prev;
	}
	
	public LinearNode<T> getNext() {
		return next;
	}
	
	public T getElement() {
		return element;
	}
	
	public void setPrev(LinearNode<T> node) {
		prev = node;
	}
	
	public void setNext(LinearNode<T> node) {
		next = node;
	}
	
	public void setElement(T element) {
		this.element = element;
	}
}