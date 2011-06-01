// Deck은 무조건 pop만 함
public class DeckCards {
	private SetADT<Card> deckCards;
	
	public DeckCards() {
		deckCards = new LinkedArray<Card>();
		
		// == [Rule 1] 조커 2개를 포함한 플레잉카드 54장을 사용한다. ==
		// 13 x 4개의 일반 카드 생성 및 Deck에 추가
		for (int i=1; i<=4; i++) {
			for (int j=1; j<=13; j++) {
				deckCards.add(new Card(i, j));
			}
		}
		
		// 2개의 조커 카드 생성 및 Deck에 추가
		deckCards.add(new Card(5, 14));
		deckCards.add(new Card(5, 15));
		// == [Rule 1] 끝 ==
	}
	
	// Deck에서 카드 한장을 줌
	public Card pop() {
		if ( !(isEmpty()) ) {
			// Deck에 카드들을 순서대로 add시켰기 때문에 random으로 한장을 줘야함
			return deckCards.removeRandom();
		}
		else {
			System.out.println("[Exception::DeckCards::pop()] Deck에 카드 없음, isEmpty()로 검사한 뒤 사용 요망");
			return null;
		}
	}
	
	// Deck이 비었을 때, Drawn을 Deck에 다시 넣음
	public void reset(DrawnCards drawnCards) {
		 if (isEmpty()) {
			deckCards = drawnCards.showAll();
			// 위에 있는 카드는 Drawn되었기 때문에 빼야됨 
			deckCards.remove(deckCards.getCurrent());
		}
		else {
			System.out.println("[Exception::DeckCards::reset(DrawnCards)] Deck에 카드 아직 남아있음, isEmpty()로 검사한 뒤 사용 요망");
		}
	}
	
	// 아직까지 다른 데서 딱히 사용치 않음
	public Card getTop() {
		return deckCards.getCurrent();
	}
	
	// 아직까지 다른 데서 딱히 사용치 않음
	public int size() {
		return deckCards.size();
	}
	
	public boolean isEmpty() {
		return deckCards.isEmpty();
	}
}