// Ring Linked Array�� ǥ���ϰ� �������ִ� Ŭ����
/* �� Ŭ������ Single Linked Array(class LinkedArray)�� ������� �ʰ� ������ �����δ�. 
 * �ֳ��ϸ� Ring�� ����������������� ���̸�, ���� / ���� / ���� ��常 �������ָ� ���̱� ������
 * ���� ���񽺰� ����(=���ſ�) LinkedArray�� ����� �ʿ䰡 ����. 
 */
public class RingLinkedArray<T> {
	private LinearNode<T> contents;
	private LinearNode<T> loopContents;
	private LinearNode<T> firstNode;
	private int count;
	
	public RingLinkedArray() {
		contents = null;
		loopContents = null;
		firstNode = null;
		count = 0;
	}
	
	// �ϳ��� ��带 �߰����ִ� ����
	public void add(T element) {
		LinearNode<T> node = new LinearNode<T>(element);
		node.setNext(contents); // 1. ���� ������ ��忡�� ���� ��尡 ���� ����Ű�� �ִ� ���(contents)��� ����
		if (count == 0) {
			firstNode = node; // ���� ���� ������ ��尡 ù ����� ���, �� ��带 ���� ����ص�.
			/* �ֳ��ϸ� ������ ���� ������ ��忡�� ���� ��尡 ù ����� �����ϰ� 
			 * ù ��忡�� ���� ��尡 ���� ������ ����� �����ؾ� Ring�� �Ǳ� ����.
			 */
		}
		else {
			contents.setPrev(node); // 2. ���� ����Ű�� �ִ� ��忡�� ���� ��尡 ���� ������ ����� ����
			firstNode.setNext(node); // 3. ù ��忡�� ���� ��带 ���� ������ ����� ����
		}
		contents = node; // 4. ���� ������ ��带 ������  
		loopContents = node; // 5. getPrev() / getNext() ���񽺸� ���� ���� ������ ��带 �߰��� ������ 
		contents.setPrev(firstNode); // 6. ���� ����Ű�� �ִ� ���(=���� ������ ���)���� ���� ��尡 ù ����� ����  
		
		count++;
	}
	
	// ���� ��带 �˷��� ����, count��ŭ ���� ��带 �������ϴ� ����  
	public T getPrev(int prevCount) {
		T result = null;
		
		if (!isEmpty()) {
			result = loopContents.getElement();
			
			for (int i=1; i<=prevCount; i++) {
				loopContents = loopContents.getPrev();
			}
		}
		else {
			System.out.println("[Exception::RingLinkedArray::getPrev(int)] ��尡 �ϳ��� ����, isEmpty()�� �˻��� �� ��� ���");
		}
		
		return result;
	}
	
	// ���� ��带 �˷��� ����, count��ŭ ���� ��带 �������ϴ� ����
	public T getNext(int nextCount) {
		T result = null;
		
		if (!isEmpty()) {
			result = loopContents.getElement();
			
			for (int i=1; i<=nextCount; i++) {
				loopContents = loopContents.getNext();
			}
		}
		else {
			System.out.println("[Exception::RingLinkedArray::getNext(int)] ��尡 �ϳ��� ����, isEmpty()�� �˻��� �� ��� ���");
		}
		
		return result;
	}
	
	// ���� ��带 �˷��ִ� ����
	public T getCurrent() {
		return loopContents.getElement();
	}
	
	public boolean isEmpty() {
		return (count == 0);
	}
	
	// �������� �ٸ� ���� ���� ���ġ ����
	public int size() {
		return count;
	}
}
