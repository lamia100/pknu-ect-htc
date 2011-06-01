// Drawn�� ������ push�� ��
/* �̰� �ʿ��� ������ Deck�� ������� ������ ��ӵ� ���
 * �÷��̾ ���� �������ִ� ī��� üũ -> Deck ����� �ϴ� �ͺ���
 * ���ݱ��� �÷��̾���� �� ī����� ��Ƶ� -> Deck ����� �ϴ°� �ξ� ���ϰ� �������̰� ���� 
 */
public class DrawnCards {
	private SetADT<Card> drawnCards;
	
	public DrawnCards() {
		drawnCards = new LinkedArray<Card>();
	}
	
	// ī�� ������ Drawn�� �߰���
	public void push(Card pushCard) {
		drawnCards.add(pushCard);
	}
	
	// �ֱ� Drawn�� ī�� ������ ������ 
	public Card getTop() {
		return drawnCards.getCurrent();
	}
	
	// Drawn�� ��� ī����� �ǳ���, DeckCards Ŭ�������� Deck ������� �� �����
	public SetADT<Card> showAll() {
		return drawnCards;
	}
}