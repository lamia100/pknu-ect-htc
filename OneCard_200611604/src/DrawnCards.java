// Drawn은 무조건 push만 함
/* 이게 필요한 이유는 Deck이 비었지만 게임이 계속될 경우
 * 플레이어가 현재 가지고있는 카드들 체크 -> Deck 재생성 하는 것보다
 * 지금까지 플레이어들이 낸 카드들을 모아둠 -> Deck 재생성 하는게 훨씬 편하고 직관적이고 빠름 
 */
public class DrawnCards {
	private SetADT<Card> drawnCards;
	
	public DrawnCards() {
		drawnCards = new LinkedArray<Card>();
	}
	
	// 카드 한장을 Drawn에 추가함
	public void push(Card pushCard) {
		drawnCards.add(pushCard);
	}
	
	// 최근 Drawn된 카드 한장을 보여줌 
	public Card getTop() {
		return drawnCards.getCurrent();
	}
	
	// Drawn된 모든 카드들을 건네줌, DeckCards 클래스에서 Deck 재생성할 때 사용함
	public SetADT<Card> showAll() {
		return drawnCards;
	}
}