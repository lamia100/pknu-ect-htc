// �÷��̾�� add(Deck���� ������)�� �ϰ� drawn(ī�� ������ ��)�� ��
public class PlayerCards {
	private SetADT<Card> playerCards;
	private String playerName;
	private boolean equalUser; // ���� �÷��̾ �������� ��ǻ������ �÷��� �뵵
	
	public PlayerCards(String playerName, boolean user) {
		playerCards = new LinkedArray<Card>();
		this.playerName = playerName;
		equalUser = user;
	}
	
	// ī�� ������ ������
	public void add(Card addCard) {
		playerCards.add(addCard);
	}
	
	// ������ ī�� ������ ��
	public void drawn(Card removeCard) {
		if (!isEmpty()) {
			playerCards.remove(removeCard);
		}
		else {
			System.out.println("[Exception::PlayerCards::drawn(Card)] Player(����)���� ī�� ����, isEmpty()�� �˻��� �� ��� ���");
		}
	}
	
	// �������� ī�� ������ ����, � ī�带 �´��� �˷���. ��ǻ�Ϳ��� �����
	public Card drawnRandom() {
		if (!isEmpty()) {
			return playerCards.removeRandom();
		}
		else {
			System.out.println("[Exception::PlayerCards::drawnRandom()] Player(��ǻ��)���� ī�� ����, isEmpty()�� �˻��� �� ��� ���");
			return null;
		}
	}
	
	// �÷��̾ �������ִ� ��� ī�带 ������
	public SetADT<Card> getAll() {
		return playerCards;
	}
	
	// �������� �ٸ� ���� ���� ���ġ ����
	public int size() {
		return playerCards.size();
	}
	
	public String toString() {
		return playerName;
	}
	
	// ���� �÷��̾ �������� ��ǻ������ �˷���, ��ǻ�Ͷ�� drawnRandom�� �ؾ��ϱ� ����
	public boolean equalUser() {
		return equalUser;
	}
	
	public boolean isEmpty() {
		return playerCards.isEmpty();
	}
}