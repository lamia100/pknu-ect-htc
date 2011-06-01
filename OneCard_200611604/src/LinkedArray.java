import java.util.Iterator;
import java.util.Random;

// Single Linked Array�� ǥ���ϰ� �������ִ� Ŭ����
public class LinkedArray<T> implements SetADT<T>, Iterable<T> {
	private LinearNode<T> contents;
	private int count;
	private static Random rand = new Random();
	
	// �������� �ٸ� ���� ���� ���ġ ����
	public LinkedArray() {
		contents = null;
		count = 0;
	}
	
	// �ϳ��� ��带 �߰����ִ� ����
	public void add(T element) {
		LinearNode<T> node = new LinearNode<T>(element);
		node.setNext(contents);
		contents = node;
		count++;
	}
	
	// ������ ��带 ã�Ƽ� �������ִ� ����
	public void remove(T target) {
		LinearNode<T> previous;
		LinearNode<T> current;
		boolean found = false;		
		
		if (!isEmpty()) {
			if (contents.getElement().equals(target)) {
				contents = contents.getNext();
			}
			else {
				previous = contents;
				current = contents.getNext();
				
				for (int look=0; (look < count) && (!found); look++) {
					if (current.getElement().equals(target)) {
						found = true;
					}
					else {
						previous = current;
						current = current.getNext();
					}
				}
				
				previous.setNext(current.getNext());
			}
			
			count--;			
		}
		else {
			System.out.println("[Exception::LinkedArray::remove(T)] ��尡 �ϳ��� ����, isEmpty()�� �˻��� �� ��� ���");
		}		
	}
	
	// �������� ��� �ϳ��� �����ϰ�, � ��带 �����ߴ��� �˷��ִ� ����
	public T removeRandom()  {		
		LinearNode<T> previous;
		LinearNode<T> current;
		T result = null;
		
		if (!isEmpty()) {
			int choice = rand.nextInt(count) + 1;
			
			if (choice == 1) {
				result = contents.getElement();
				contents = contents.getNext();
			}
			else {
				previous = contents;
				
				for (int skip=2; skip < choice; skip++) {
					previous = previous.getNext();
				}
				
				current = previous.getNext();
				result = current.getElement();
				previous.setNext(current.getNext());
			}
			
			count--;
		}
		else {
			System.out.println("[Exception::LinkedArray::removeRandom()] ��尡 �ϳ��� ����, isEmpty()�� �˻��� �� ��� ���");
		}
				
		return result;
	}

	// ������ ��尡 �ִ��� �˻����ִ� ���� 
	public boolean find(T target) {
		LinearNode<T> current = contents;
		boolean found = false;
		
		for (int look=0; (look < count) && (!found); look++) {
			if (current.getElement().equals(target)) {
				found = true;
			}
			else {
				current = current.getNext();
			}
		}
		
		return found;
	}
	
	// �ֱٿ� �߰��� ��带 �����ִ� ����
	public T getCurrent() {
		return contents.getElement();
	}
	
	public boolean isEmpty() {
		return (count == 0);
	}

	// ���� ����� �ʿ�� ���µ�, �ϴ� �ٸ����� �����ϰ�� ����
	public int size() {
		return count;
	}

	public Iterator<T> iterator() {
		return new LinkedIterator<T>(contents);
	}
}