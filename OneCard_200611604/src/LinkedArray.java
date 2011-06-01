import java.util.Iterator;
import java.util.Random;

// Single Linked Array를 표현하고 서비스해주는 클래스
public class LinkedArray<T> implements SetADT<T>, Iterable<T> {
	private LinearNode<T> contents;
	private int count;
	private static Random rand = new Random();
	
	// 아직까지 다른 데서 딱히 사용치 않음
	public LinkedArray() {
		contents = null;
		count = 0;
	}
	
	// 하나의 노드를 추가해주는 서비스
	public void add(T element) {
		LinearNode<T> node = new LinearNode<T>(element);
		node.setNext(contents);
		contents = node;
		count++;
	}
	
	// 지정한 노드를 찾아서 제거해주는 서비스
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
			System.out.println("[Exception::LinkedArray::remove(T)] 노드가 하나도 없음, isEmpty()로 검사한 뒤 사용 요망");
		}		
	}
	
	// 랜덤으로 노드 하나를 제거하고, 어떤 노드를 제거했는지 알려주는 서비스
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
			System.out.println("[Exception::LinkedArray::removeRandom()] 노드가 하나도 없음, isEmpty()로 검사한 뒤 사용 요망");
		}
				
		return result;
	}

	// 지정한 노드가 있는지 검사해주는 서비스 
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
	
	// 최근에 추가한 노드를 보여주는 서비스
	public T getCurrent() {
		return contents.getElement();
	}
	
	public boolean isEmpty() {
		return (count == 0);
	}

	// 딱히 사용할 필요는 없는데, 일단 다른데서 참고하고는 있음
	public int size() {
		return count;
	}

	public Iterator<T> iterator() {
		return new LinkedIterator<T>(contents);
	}
}