import java.util.Iterator;

public class LinkedIterator<T> implements Iterator<T> {
	private LinearNode<T> current;
	
	public LinkedIterator(LinearNode<T> collection) {
		current = collection;
	}
	
	public boolean hasNext() {
		return (current != null);
	}
	
	public T next()  {
		T result = null;
		
		if (hasNext()) {
			result = current.getElement();
			current = current.getNext();
		}
		else {
			System.out.println("[Exception::LinkedIretator::next()] ���� ��尡 ����, hasNext()�� �˻��� �� ��� ���");
		}
		
		return result;
	}

	public void remove() {}
}