// Ring Linked Array를 표현하고 서비스해주는 클래스
/* 이 클래스는 Single Linked Array(class LinkedArray)를 상속하지 않고 별개로 움직인다. 
 * 왜냐하면 Ring은 게임진행순서에서만 쓰이며, 이전 / 현재 / 다음 노드만 가르쳐주면 끝이기 때문에
 * 궃이 서비스가 많은(=무거운) LinkedArray를 상속할 필요가 없다. 
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
	
	// 하나의 노드를 추가해주는 서비스
	public void add(T element) {
		LinearNode<T> node = new LinearNode<T>(element);
		node.setNext(contents); // 1. 새로 생성한 노드에서 다음 노드가 현재 가리키고 있는 노드(contents)라고 지정
		if (count == 0) {
			firstNode = node; // 만약 새로 생성한 노드가 첫 노드일 경우, 이 노드를 따로 기억해둠.
			/* 왜냐하면 다음에 새로 생성할 노드에서 이전 노드가 첫 노드라고 지정하고 
			 * 첫 노드에서 다음 노드가 새로 생성한 노드라고 지정해야 Ring이 되기 때문.
			 */
		}
		else {
			contents.setPrev(node); // 2. 현재 가리키고 있는 노드에서 이전 노드가 새로 생성한 노드라고 지정
			firstNode.setNext(node); // 3. 첫 노드에서 다음 노드를 새로 생성한 노드라고 지정
		}
		contents = node; // 4. 새로 생성한 노드를 포인팅  
		loopContents = node; // 5. getPrev() / getNext() 서비스를 위해 새로 생성한 노드를 추가로 포인팅 
		contents.setPrev(firstNode); // 6. 현재 가리키고 있는 노드(=새로 생성한 노드)에서 이전 노드가 첫 노드라고 지정  
		
		count++;
	}
	
	// 현재 노드를 알려준 다음, count만큼 이전 노드를 포인팅하는 서비스  
	public T getPrev(int prevCount) {
		T result = null;
		
		if (!isEmpty()) {
			result = loopContents.getElement();
			
			for (int i=1; i<=prevCount; i++) {
				loopContents = loopContents.getPrev();
			}
		}
		else {
			System.out.println("[Exception::RingLinkedArray::getPrev(int)] 노드가 하나도 없음, isEmpty()로 검사한 뒤 사용 요망");
		}
		
		return result;
	}
	
	// 현재 노드를 알려준 다음, count만큼 다음 노드를 포인팅하는 서비스
	public T getNext(int nextCount) {
		T result = null;
		
		if (!isEmpty()) {
			result = loopContents.getElement();
			
			for (int i=1; i<=nextCount; i++) {
				loopContents = loopContents.getNext();
			}
		}
		else {
			System.out.println("[Exception::RingLinkedArray::getNext(int)] 노드가 하나도 없음, isEmpty()로 검사한 뒤 사용 요망");
		}
		
		return result;
	}
	
	// 현재 노드를 알려주는 서비스
	public T getCurrent() {
		return loopContents.getElement();
	}
	
	public boolean isEmpty() {
		return (count == 0);
	}
	
	// 아직까지 다른 데서 딱히 사용치 않음
	public int size() {
		return count;
	}
}
