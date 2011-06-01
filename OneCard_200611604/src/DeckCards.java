// Deck�� ������ pop�� ��
public class DeckCards {
	private SetADT<Card> deckCards;
	
	public DeckCards() {
		deckCards = new LinkedArray<Card>();
		
		// == [Rule 1] ��Ŀ 2���� ������ �÷���ī�� 54���� ����Ѵ�. ==
		// 13 x 4���� �Ϲ� ī�� ���� �� Deck�� �߰�
		for (int i=1; i<=4; i++) {
			for (int j=1; j<=13; j++) {
				deckCards.add(new Card(i, j));
			}
		}
		
		// 2���� ��Ŀ ī�� ���� �� Deck�� �߰�
		deckCards.add(new Card(5, 14));
		deckCards.add(new Card(5, 15));
		// == [Rule 1] �� ==
	}
	
	// Deck���� ī�� ������ ��
	public Card pop() {
		if ( !(isEmpty()) ) {
			// Deck�� ī����� ������� add���ױ� ������ random���� ������ �����
			return deckCards.removeRandom();
		}
		else {
			System.out.println("[Exception::DeckCards::pop()] Deck�� ī�� ����, isEmpty()�� �˻��� �� ��� ���");
			return null;
		}
	}
	
	// Deck�� ����� ��, Drawn�� Deck�� �ٽ� ����
	public void reset(DrawnCards drawnCards) {
		 if (isEmpty()) {
			deckCards = drawnCards.showAll();
			// ���� �ִ� ī��� Drawn�Ǿ��� ������ ���ߵ� 
			deckCards.remove(deckCards.getCurrent());
		}
		else {
			System.out.println("[Exception::DeckCards::reset(DrawnCards)] Deck�� ī�� ���� ��������, isEmpty()�� �˻��� �� ��� ���");
		}
	}
	
	// �������� �ٸ� ���� ���� ���ġ ����
	public Card getTop() {
		return deckCards.getCurrent();
	}
	
	// �������� �ٸ� ���� ���� ���ġ ����
	public int size() {
		return deckCards.size();
	}
	
	public boolean isEmpty() {
		return deckCards.isEmpty();
	}
}