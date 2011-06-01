// �ϳ��� ��带 �����ϴ� Ŭ����
/* ������������� ���� Ring(������ ��ȯ, ������ ��ȯ) ����� Linked ������ ����ؾ��ϱ� ������ 
 * �ϳ��� ���� next�� ���� prev ������ �˰��־�� ��
 */
public class LinearNode<T> {
	private LinearNode<T> prev;
	private LinearNode<T> next;
	private T element;
	
	// �������� �ٸ� ���� ���� ���ġ ����
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