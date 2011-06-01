import java.util.Iterator;
import java.util.Random;

public class Logic {
	private DeckCards deck;
	private DrawnCards drawn;
	
	private RingLinkedArray<PlayerCards> player; 
	private PlayerCards user;
	private PlayerCards com1;
	private PlayerCards com2;
	
	// 현재 게임의 Rule 정보를 저장
	private int drawnSuit = 0;
	private int drawnValue = 0;
	private int attackCount = 1;
	private int turnCount = 1; 
	private boolean turnReverse = false;
	
	// 플레이어들을 생성하고 게임을 셋팅한다.
	public Logic() {
		deck = new DeckCards();
		drawn = new DrawnCards();
		
		player = new RingLinkedArray<PlayerCards>();
		user = new PlayerCards("inter6.com", true);
		com1 = new PlayerCards("Computer1", false);
		com2 = new PlayerCards("Computer2", false);
		
		// 가장 최근에 추가한 노드가 current가 되므로, 역순으로 add함
		player.add(com2);
		player.add(com1);
		player.add(user);   
		
		// == [Rule 2] 플레이어들은 각각 5장의 패를 가지고 시작하며, 나머지는 1장만 빼고 덱에 놓는다. ==
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
		// == [Rule 2] 끝 ==
		
		// == [Rule 7] 첫 턴에서는 특정패의 영향을 받지 않는다. ==
		ignoreSpecial();
		// == [Rule 7] 끝 ==
		
		check();
		
		System.out.println("[Logic] 초기화 완료됨");
	}
	
	// 유저 플레이에 대한 서비스를 제공하고, GUI에게 유저의 상태를 알려줌
	public String[] userTurn(int userSuit, int userValue, int changeSuit) {
		String[] userState = new String[3];
		/* information 패킷
		 * userState[0] : Who ? User
		 * userState[1] : User Change Suit ?
		 * userState[2] : User Win or OneCard ?
		 */
		
		// drawn할 카드가 없다면, add
		if (userSuit == 0) {
			userState = add(user);
		}
		
		// drawn할 카드가 있다면, 해당 카드를 찾아서 drawn
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
	
	// 컴퓨터 플레이에 대한 서비스를 제공하고, GUI에게 컴퓨터의 상태를 알려줌
	public String[] comTurn() {
		String[] comState = new String[3];
		/* information 패킷
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
			
			// drawn할 카드가 없다면, add
			if (checkCards.isEmpty()) {
				comState = add(player.getCurrent());
			}
			
			// drawn할 카드가 있다면, random으로 카드를 drawn
			else {
				Card temp = checkCards.removeRandom();
				
				// == [Rule 6-3] 7(세븐): 문양을 새로 바꿀 수 있다. ==
				int changeSuit = -1;
				if (temp.getValue() == 7) {
					Random rand = new Random();
					changeSuit = rand.nextInt(4) + 1;
				}
				// == [Rule 6-3] 끝 == 
				
				comState = drawn(player.getCurrent(), temp, changeSuit);
			}
		}
		
		return comState;
	}
	
	/* 플레이어가 방어하지 못했을 때,
	 * Deck에서 attakCount만큼 카드를 갖고온 뒤 플레이어에게 추가하고, 플레이어의 정보를 건네주는 내부 서비스
	 */
	private String[] add(PlayerCards who) {
		String[] whoState = { who.toString(), null, ""};
		/* information 패킷
		 * whoState[0] : Who ?
		 * whoState[1] : Who Change Suit ? No
		 * whoState[2] : Who Win or OneCard ? No
		 */
		
		Card popCard;
		for (int i=0; i<attackCount; i++) {
			// 만약 Deck이 비어있다면, drawn된 카드들을 Deck에 넣음
			if (deck.isEmpty()) {
				deck.reset(drawn);
				Card drawnCard = drawn.getTop();
				drawn = new DrawnCards();
				drawn.push(drawnCard);
				
				System.out.println("Logic::add(PlayerCards) : Deck이 리셋됨");
			}
			
			popCard = deck.pop();
			who.add(popCard);
			System.out.println("[Logic::add(PlayerCards)] " + who + "가 Deck에서 " + popCard + "를 가져옴. (" + (i+1) + "번째)");	
		}
		
		// 1, 2, 조커 카드로 공격당한 후, 다음 플레이어는 제한을 받지 않음
		ignoreSpecial();
		
		// 다음 플레이어는 attackCount, turnCount에 제한을 받지 않음
		attackCount = 1;
		turnCount = 1;
		
		if (turnReverse) {
			player.getPrev(1);
		}
		else {
			player.getNext(1);
		}
		check();
		
		System.out.println("[Logic::drawn(Card)] 방어하지 못했을 때 " + attackCount + "만큼 Deck에서 가져와야 됨");
		
		return whoState;
	}

	/* 플레이어가 방어에 성공했을 때,
	 * 플레이어가 낸 카드를 drawn에 추가하고, 플레이어의 정보를 건네주는 내부 서비스
	 */
	private String[] drawn(PlayerCards who, Card drawnCard, int changeSuit) {
		String[] whoState = { who.toString(), null, ""};
		/* information 패킷
		 * whoState[0] : Who ?
		 * whoState[1] : Who Change Suit ?
		 * whoState[2] : Who Win or OneCard ?
		 */
		
		who.drawn(drawnCard);
		drawn.push(drawnCard);
		
		System.out.println("[Logic::drawn(PlayerCards, Card, int)] " + who + "가  " + drawnCard + "를 냄");
		
		if (changeSuit > 0 && changeSuit < 5) {
			drawnSuit = changeSuit;
			
			switch (changeSuit) {
			case 1: whoState[1] = "Spade"; break;
			case 2: whoState[1] = "Diamond"; break;
			case 3: whoState[1] = "Heart"; break;
			case 4: whoState[1] = "Club"; break;
			}
			
			System.out.println("[Logic::drawn(PlayerCards, Card, int)] 낼 수 있는 카드의 종류가  " + drawnSuit + "로 변경됨");
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
	
	// drawn한 카드의 attackCount를 저장해주는 내부 서비스
	private void attackCount() {
		if (drawnSuit == 5) {
			attackCount = 5;
		}
		else {
			attackCount = 1;
			
			// == [Rule 6-1] A(에이스): 다음 플레이어가 3장을 가져간다. ==
			if (drawnValue == 1) {
				attackCount = 3;
			}
			// == [Rule 6-1] 끝 ==
			
			// == [Rule 6-2] 2(투): 다음 플레이어가 2장을 가져간다. ==
			else if (drawnValue == 2) {
				attackCount = 2;
			}
			// == [Rule 6-2] 끝 ==
			
			// == [Rule 6-7] Joker(조커): 다음 플레이어가 5장을 가져간다. ==
			else if (drawnSuit == 5) {
				attackCount = 5;
			}
			// == [Rule 6-7] 끝 ==
		}
		System.out.println("[Logic::drawn(Card)] 방어하지 못했을 때 " + attackCount + "만큼 Deck에서 가져와야 됨");
	}
	
	// drawn한 카드의 turnCount를 저장해주고, 다음 플레이어를 포인팅해주는 내부 서비스
	private void turnCount() {
		turnCount = 1;
		
		// == [Rule 6-4] J(잭): 다음 플레이어를 건너뛰고 그 다음 플레이어에게 순서가 간다. ==
		if (drawnValue == 11) {
			turnCount = 2;
		}
		// == [Rule 6-4] 끝 ==
		
		// == [Rule 6-5] Q(퀸): 진행 순서를 바꾼다. ==
		else if (drawnValue == 12) {
			turnReverse = !turnReverse;
		}
		// == [Rule 6-5] 끝 ==
		
		// == [Rule 6-6] K(킹) : 현재 플레이어가 한번 더 한다. ==
		else if (drawnValue == 13) {
			turnCount = 3;
		}
		// == [Rule 6-6] 끝 ==
		
		if (turnReverse) {
			player.getPrev(turnCount);
		}
		else {
			player.getNext(turnCount);
		}
	}
	
	// 특정 조건에서 1, 2, 조커 카드의 제한을 풀어주는 내부 서비스
	private void ignoreSpecial() {
		if (drawnValue == 1 || drawnValue == 2) {
			drawnValue = 0;
		}
		if (drawnSuit == 5) {
			drawnSuit = 0;
			drawnValue = 0;
		}
	}
	
	// 각 플레이어들이 현재 낼 수 있는 카드를 각각 체크해주는 내부 서비스 
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
				
				// == [Rule 6-7] Joker(조커): 무늬나 숫자에 상관없이 낼 수 있다. ==
				else if (testSuit == 5) {
					testCard.setCheck(true);
				}
				// == [Rule 6-7] 끝 ==
				
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
						// == [Rule 6-1] A(에이스): A나 조커로 방어할 수 있다. ==
						if (drawnValue == 1) {
							if (testValue == 1 || testSuit == 5) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
						// == [Rule 6-1] 끝 ==
						
						// == [Rule 6-2] 2(투): 2나 A, 조커로 방어할 수 있다. ==
						else if (drawnValue == 2) {
							if (testValue == 2 || testValue == 1 || testSuit == 5) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
						// == [Rule 6-2] 끝 ==
						
						// == [Rule 6-7] Joker(조커): 조커나 스페이드 A로 방어할 수 있다. ==
						else if (drawnSuit == 5) {
							if (testSuit == 5 || (testSuit == 1 && testValue == 1)) {
								testCard.setCheck(true);
							}
							else {
								testCard.setCheck(false);
							}
						}
						// == [Rule 6-7] 끝 ==
						
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
			
			System.out.println("[Logic::check()] " + selectPlayer + "가 가지고있는 카드 갯수 : " + selectPlayer.getAll().size());
		}
		System.out.println("[Logic::check()] 각 플레이어들이 낼 수 있는 카드들이 체크됨");
		System.out.println("[Logic::check()] 낼 수 있는 카드의 조건 : Suit(" + drawnSuit + "), Value(" + drawnValue + ")");
		System.out.println("[Logic::check()] 다음 플레이어는 " + player.getCurrent());
	}
	
	// 현재 drawn된 카드를 보여주는 서비스, GUI에서 필요로 함
	public Card getDrawnCard() {
		return drawn.getTop();
	}
	
	// 현재 drawn 조건을 보여주는 서비스, GUI에서 필요로 함
	public Card getDrawnCondition() {
		return new Card(drawnSuit, drawnValue);
	}
	
	// 유저가 가지고 있는 카드들을 보여주는 서비스, GUI에서 필요로 함.
	public SetADT<Card> getUserCards() {
		return user.getAll();
	}
	
	// 현재 진행할려는 플레이어가 유저인지 확인해주는 서비스, GUI에서 필요로 함
	public boolean equalUser() {
		return player.getCurrent().equalUser();
	}
}