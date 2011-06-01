import java.util.Iterator;
import java.util.Random;

public class Logic {
	private DeckCards deck;
	private DrawnCards drawn;
	
	private RingLinkedArray<PlayerCards> player; 
	private PlayerCards user;
	private PlayerCards com1;
	private PlayerCards com2;
	
	// ���� ������ Rule ������ ����
	private int drawnSuit = 0;
	private int drawnValue = 0;
	private int attackCount = 1;
	private int turnCount = 1; 
	private boolean turnReverse = false;
	
	// �÷��̾���� �����ϰ� ������ �����Ѵ�.
	public Logic() {
		deck = new DeckCards();
		drawn = new DrawnCards();
		
		player = new RingLinkedArray<PlayerCards>();
		user = new PlayerCards("inter6.com", true);
		com1 = new PlayerCards("Computer1", false);
		com2 = new PlayerCards("Computer2", false);
		
		// ���� �ֱٿ� �߰��� ��尡 current�� �ǹǷ�, �������� add��
		player.add(com2);
		player.add(com1);
		player.add(user);   
		
		// == [Rule 2] �÷��̾���� ���� 5���� �и� ������ �����ϸ�, �������� 1�常 ���� ���� ���´�. ==
		PlayerCards selectPlayer;
		for (int i=0; i<3; i++) {
			selectPlayer = player.getNext(1);
			for (int j=0; j<5; j++) {
				selectPlayer.add(deck.pop());
			}
		}
		
		Card popCard = deck.pop();
		drawn.push(popCard);
		
		drawnSuit = popCard.getSuit();
		drawnValue = popCard.getValue();
		
		attackCount();
		// == [Rule 2] �� ==
		
		// == [Rule 7] ù �Ͽ����� Ư������ ������ ���� �ʴ´�. ==
		ignoreSpecial();
		// == [Rule 7] �� ==
		
		check();
		
		System.out.println("[Logic] �ʱ�ȭ �Ϸ��");
	}
	
	// ���� �÷��̿� ���� ���񽺸� �����ϰ�, GUI���� ������ ���¸� �˷���
	public String[] userTurn(int userSuit, int userValue, int changeSuit) {
		String[] userState = new String[3];
		/* information ��Ŷ
		 * userState[0] : Who ? User
		 * userState[1] : User Change Suit ?
		 * userState[2] : User Win or OneCard ?
		 */
		
		// drawn�� ī�尡 ���ٸ�, add
		if (userSuit == 0) {
			userState = add(user);
		}
		
		// drawn�� ī�尡 �ִٸ�, �ش� ī�带 ã�Ƽ� drawn
		else {
			Iterator<Card> it = user.getAll().iterator();
			boolean found = false;
			while (it.hasNext() && !found) {
				Card checkCard = it.next();
				if (checkCard.getSuit() == userSuit && checkCard.getValue() == userValue) {
					userState = drawn(user, checkCard, changeSuit);
					found = true;
				}
			}
		} 

		return userState;
	}
	
	// ��ǻ�� �÷��̿� ���� ���񽺸� �����ϰ�, GUI���� ��ǻ���� ���¸� �˷���
	public String[] comTurn() {
		String[] comState = new String[3];
		/* information ��Ŷ
		 * comState[0] : Who ? Com1 or Com2
		 * comState[1] : Com Change Suit ?
		 * comState[2] : Com Win or OneCard ?
		 */
		
		if (!player.getCurrent().isEmpty()) {
			SetADT<Card> checkCards = new LinkedArray<Card>();
			Iterator<Card> it = player.getCurrent().getAll().iterator();
			while (it.hasNext()) {
				Card tempCard = it.next();
				if (tempCard.getCheck()) {
					checkCards.add(tempCard);
				}
			}
			
			// drawn�� ī�尡 ���ٸ�, add
			if (checkCards.isEmpty()) {
				comState = add(player.getCurrent());
			}
			
			// drawn�� ī�尡 �ִٸ�, random���� ī�带 drawn
			else {
				Card temp = checkCards.removeRandom();
				
				// == [Rule 6-3] 7(����): ������ ���� �ٲ� �� �ִ�. ==
				int changeSuit = -1;
				if (temp.getValue() == 7) {
					Random rand = new Random();
					changeSuit = rand.nextInt(4) + 1;
				}
				// == [Rule 6-3] �� == 
				
				comState = drawn(player.getCurrent(), temp, changeSuit);
			}
		}
		
		return comState;
	}
	
	/* �÷��̾ ������� ������ ��,
	 * Deck���� attakCount��ŭ ī�带 ����� �� �÷��̾�� �߰��ϰ�, �÷��̾��� ������ �ǳ��ִ� ���� ����
	 */
	private String[] add(PlayerCards who) {
		String[] whoState = { who.toString(), null, ""};
		/* information ��Ŷ
		 * whoState[0] : Who ?
		 * whoState[1] : Who Change Suit ? No
		 * whoState[2] : Who Win or OneCard ? No
		 */
		
		Card popCard;
		for (int i=0; i<attackCount; i++) {
			// ���� Deck�� ����ִٸ�, drawn�� ī����� Deck�� ����
			if (deck.isEmpty()) {
				deck.reset(drawn);
				Card drawnCard = drawn.getTop();
				drawn = new DrawnCards();
				drawn.push(drawnCard);
				
				System.out.println("Logic::add(PlayerCards) : Deck�� ���µ�");
			}
			
			popCard = deck.pop();
			who.add(popCard);
			System.out.println("[Logic::add(PlayerCards)] " + who + "�� Deck���� " + popCard + "�� ������. (" + (i+1) + "��°)");	
		}
		
		// 1, 2, ��Ŀ ī��� ���ݴ��� ��, ���� �÷��̾�� ������ ���� ����
		ignoreSpecial();
		
		// ���� �÷��̾�� attackCount, turnCount�� ������ ���� ����
		attackCount = 1;
		turnCount = 1;
		
		if (turnReverse) {
			player.getPrev(1);
		}
		else {
			player.getNext(1);
		}
		check();
		
		System.out.println("[Logic::drawn(Card)] ������� ������ �� " + attackCount + "��ŭ Deck���� �����;� ��");
		
		return whoState;
	}

	/* �÷��̾ �� �������� ��,
	 * �÷��̾ �� ī�带 drawn�� �߰��ϰ�, �÷��̾��� ������ �ǳ��ִ� ���� ����
	 */
	private String[] drawn(PlayerCards who, Card drawnCard, int changeSuit) {
		String[] whoState = { who.toString(), null, ""};
		/* information ��Ŷ
		 * whoState[0] : Who ?
		 * whoState[1] : Who Change Suit ?
		 * whoState[2] : Who Win or OneCard ?
		 */
		
		who.drawn(drawnCard);
		drawn.push(drawnCard);
		
		System.out.println("[Logic::drawn(PlayerCards, Card, int)] " + who + "��  " + drawnCard + "�� ��");
		
		if (changeSuit > 0 && changeSuit < 5) {
			drawnSuit = changeSuit;
			
			switch (changeSuit) {
			case 1: whoState[1] = "Spade"; break;
			case 2: whoState[1] = "Diamond"; break;
			case 3: whoState[1] = "Heart"; break;
			case 4: whoState[1] = "Club"; break;
			}
			
			System.out.println("[Logic::drawn(PlayerCards, Card, int)] �� �� �ִ� ī���� ������  " + drawnSuit + "�� �����");
		}
		else {
			drawnSuit = drawnCard.getSuit();
		}
		drawnValue = drawnCard.getValue();
		
		attackCount();
		turnCount();		
		check();
		
		if (who.isEmpty()) {			
			whoState[2] = "Win";
		}
		else if (who.size() == 1) {
			whoState[2] = "OneCard";
		}
		
		return whoState;
	}
	
	// drawn�� ī���� attackCount�� �������ִ� ���� ����
	private void attackCount() {
		if (drawnSuit == 5) {
			attackCount = 5;
		}
		else {
			attackCount = 1;
			
			// == [Rule 6-1] A(���̽�): ���� �÷��̾ 3���� ��������. ==
			if (drawnValue == 1) {
				attackCount = 3;
			}
			// == [Rule 6-1] �� ==
			
			// == [Rule 6-2] 2(��): ���� �÷��̾ 2���� ��������. ==
			else if (drawnValue == 2) {
				attackCount = 2;
			}
			// == [Rule 6-2] �� ==
			
			// == [Rule 6-7] Joker(��Ŀ): ���� �÷��̾ 5���� ��������. ==
			else if (drawnSuit == 5) {
				attackCount = 5;
			}
			// == [Rule 6-7] �� ==
		}
		System.out.println("[Logic::drawn(Card)] ������� ������ �� " + attackCount + "��ŭ Deck���� �����;� ��");
	}
	
	// drawn�� ī���� turnCount�� �������ְ�, ���� �÷��̾ ���������ִ� ���� ����
	private void turnCount() {
		turnCount = 1;
		
		// == [Rule 6-4] J(��): ���� �÷��̾ �ǳʶٰ� �� ���� �÷��̾�� ������ ����. ==
		if (drawnValue == 11) {
			turnCount = 2;
		}
		// == [Rule 6-4] �� ==
		
		// == [Rule 6-5] Q(��): ���� ������ �ٲ۴�. ==
		else if (drawnValue == 12) {
			turnReverse = !turnReverse;
		}
		// == [Rule 6-5] �� ==
		
		// == [Rule 6-6] K(ŷ) : ���� �÷��̾ �ѹ� �� �Ѵ�. ==
		else if (drawnValue == 13) {
			turnCount = 3;
		}
		// == [Rule 6-6] �� ==
		
		if (turnReverse) {
			player.getPrev(turnCount);
		}
		else {
			player.getNext(turnCount);
		}
	}
	
	// Ư�� ���ǿ��� 1, 2, ��Ŀ ī���� ������ Ǯ���ִ� ���� ����
	private void ignoreSpecial() {
		if (drawnValue == 1 || drawnValue == 2) {
			drawnValue = 0;
		}
		if (drawnSuit == 5) {
			drawnSuit = 0;
			drawnValue = 0;
		}
	}
	
	// �� �÷��̾���� ���� �� �� �ִ� ī�带 ���� üũ���ִ� ���� ���� 
	private void check() {
		PlayerCards selectPlayer;
		
		for (int i=0; i<3; i++) {
			selectPlayer = player.getNext(1);
			
			Iterator<Card> it = selectPlayer.getAll().iterator();
			
			Card testCard;
			int testSuit;
			int testValue;
			
			while(it.hasNext()) {
				testCard = it.next();
				testSuit = testCard.getSuit();
				testValue = testCard.getValue();
				
				if (drawnSuit == 0) {
					testCard.setCheck(true);
				}
				
				// == [Rule 6-7] Joker(��Ŀ): ���̳� ���ڿ� ������� �� �� �ִ�. ==
				else if (testSuit == 5) {
					testCard.setCheck(true);
				}
				// == [Rule 6-7] �� ==
				
				else {
					if (drawnValue == 0) {
						if (drawnSuit == testSuit) {
							testCard.setCheck(true);
						}
						else {
							testCard.setCheck(false);
						}
					}
					else {
						// == [Rule 6-1] A(���̽�): A�� ��Ŀ�� ����� �� �ִ�. ==
						if (drawnValue == 1) {
							if (testValue == 1 || testSuit == 5) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
						// == [Rule 6-1] �� ==
						
						// == [Rule 6-2] 2(��): 2�� A, ��Ŀ�� ����� �� �ִ�. ==
						else if (drawnValue == 2) {
							if (testValue == 2 || testValue == 1 || testSuit == 5) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
						// == [Rule 6-2] �� ==
						
						// == [Rule 6-7] Joker(��Ŀ): ��Ŀ�� �����̵� A�� ����� �� �ִ�. ==
						else if (drawnSuit == 5) {
							if (testSuit == 5 || (testSuit == 1 && testValue == 1)) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
						// == [Rule 6-7] �� ==
						
						else {
							if (drawnSuit == testSuit || drawnValue == testValue) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
					}
				}
			}
			
			System.out.println("[Logic::check()] " + selectPlayer + "�� �������ִ� ī�� ���� : " + selectPlayer.getAll().size());
		}
		System.out.println("[Logic::check()] �� �÷��̾���� �� �� �ִ� ī����� üũ��");
		System.out.println("[Logic::check()] �� �� �ִ� ī���� ���� : Suit(" + drawnSuit + "), Value(" + drawnValue + ")");
		System.out.println("[Logic::check()] ���� �÷��̾�� " + player.getCurrent());
	}
	
	// ���� drawn�� ī�带 �����ִ� ����, GUI���� �ʿ�� ��
	public Card getDrawnCard() {
		return drawn.getTop();
	}
	
	// ���� drawn ������ �����ִ� ����, GUI���� �ʿ�� ��
	public Card getDrawnCondition() {
		return new Card(drawnSuit, drawnValue);
	}
	
	// ������ ������ �ִ� ī����� �����ִ� ����, GUI���� �ʿ�� ��.
	public SetADT<Card> getUserCards() {
		return user.getAll();
	}
	
	// ���� �����ҷ��� �÷��̾ �������� Ȯ�����ִ� ����, GUI���� �ʿ�� ��
	public boolean equalUser() {
		return player.getCurrent().equalUser();
	}
}