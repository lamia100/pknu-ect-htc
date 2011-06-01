// 플레이어는 add(Deck에서 가져옴)도 하고 drawn(카드 한장을 냄)도 함
public class PlayerCards {
	private SetADT<Card> playerCards;
	private String playerName;
	private boolean equalUser; // 현재 플레이어가 유저인지 컴퓨터인지 플래그 용도
	
	public PlayerCards(String playerName, boolean user) {
		playerCards = new LinkedArray<Card>();
		this.playerName = playerName;
		equalUser = user;
	}
	
	// 카드 한장을 가져옴
	public void add(Card addCard) {
		playerCards.add(addCard);
	}
	
	// 선택한 카드 한장을 냄
	public void drawn(Card removeCard) {
		if (!isEmpty()) {
			playerCards.remove(removeCard);
		}
		else {
			System.out.println("[Exception::PlayerCards::drawn(Card)] Player(유저)한테 카드 없음, isEmpty()로 검사한 뒤 사용 요망");
		}
	}
	
	// 랜덤으로 카드 한장을 내고, 어떤 카드를 냈는지 알려줌. 컴퓨터에서 사용함
	public Card drawnRandom() {
		if (!isEmpty()) {
			return playerCards.removeRandom();
		}
		else {
			System.out.println("[Exception::PlayerCards::drawnRandom()] Player(컴퓨터)한테 카드 없음, isEmpty()로 검사한 뒤 사용 요망");
			return null;
		}
	}
	
	// 플레이어가 가지고있는 모든 카드를 보여줌
	public SetADT<Card> getAll() {
		return playerCards;
	}
	
	// 아직까지 다른 데서 딱히 사용치 않음
	public int size() {
		return playerCards.size();
	}
	
	public String toString() {
		return playerName;
	}
	
	// 현재 플레이어가 유저인지 컴퓨터인지 알려줌, 컴퓨터라면 drawnRandom을 해야하기 때문
	public boolean equalUser() {
		return equalUser;
	}
	
	public boolean isEmpty() {
		return playerCards.isEmpty();
	}
}