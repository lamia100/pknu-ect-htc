package main;

import javax.swing.JOptionPane;

import org.apache.log4j.*;
import utility.*;
import player.*;
import data.*;

public class Manager {
	private static Logger logger = Logger.getLogger(Manager.class);
	
	private Node<Player> currentPlayer;
	private Deck deck;
	private CircleLinkedList<Player> playerList;
	private int attackcount = 0;
	private boolean direction = true;
	private CardLabel openCard;
	private OneCardGUI user;

	// true next, false prev
	public Manager(OneCardGUI user, int usercount) {
		this.user = user;
		this.openCard = user.getOpenCardLabel();
		
		initialize(user, usercount);
	}

	public void initialize(Player user, int usercount) {
		deck = new Deck();
		playerList = new CircleLinkedList<Player>();
		playerList.add(user);
		currentPlayer = playerList.getHead();
		
		for (int i = 1; i < usercount; i++) {
			Computer temp = new Computer(this, "컴퓨터" + i);
			// temp.start();
			playerList.add(temp);
		}
		
		// 플레이어당 7장씩 패 돌림
		for (int k = 0; k < usercount; k++) {
			for (int i = 0; i < 7; i++) {
				currentPlayer.getElement().addCard(deck.getCard());
				currentPlayer = currentPlayer.getNext();
			}
		}
		
		logger.info("게임 초기화가 완료되었습니다.");
		logger.info("===== 유저의 턴입니다. =====");
	}

	public void addCardLabel(CardLabel cl1) {
		user.repaintUserCard();
		
		if (cl1 == null) {
			addCard(null);
		} else {
			addCard(cl1.getCard());
		}
	}

	public void addCard(Card c1) {
		if (playerList.size() == 1) {
			winner(currentPlayer.getElement());
		}
		
		currentPlayer.getElement().setTurn(false);
		
		// System.out.println(c1 + " : " + currentPlayer.getElement());
		
		if (c1 != null) {
			logger.info(c1 + "를 냈습니다.");
			
			deck.add(openCard.getCard());
			openCard.setCard(c1);
			
			if (currentPlayer.getElement().isEmpty()) {
				winner(currentPlayer.getElement());
			}
			
			checkCardAbility(c1);
		} else {
			// System.out.println("카드를 먹습니다. 처묵처묵");
			logger.info("카드를 내지 못했습니다.");
			
			feedCard();
		}
	}
	
	public int getState() {
		return attackcount;
	}

	public Card getOpenCard() {
		return openCard.getCard();
	}

	private void feedCard() {
		if (attackcount == 0) {
			attackcount = 1;
		}
		
		logger.info("가져가야 할 카드는 총 " + attackcount + "장입니다.");
		
		for (int i = 0; i < attackcount; i++) {
			Card fromDeck = deck.getCard();
			
			// 덱이 비었을 때 플레이어들이 가지고있는 카드 개수를 비교하여 승리자 선출 - 시작
			if (fromDeck == null) {
				int minHandSize = Integer.MAX_VALUE;
				Player minPlayer = null;
				
				for (Player target : playerList) {
					if (target.getHandSize() < minHandSize) {
						minHandSize = target.getHandSize();
						minPlayer = target;
					}
				}
				
				winner(minPlayer);
				
				return;
			}
			// 덱이 비었을 때 플레이어들이 가지고있는 카드 개수를 비교하여 승리자 선출 - 끝
			
			currentPlayer.getElement().addCard(fromDeck);
		}
		
		attackcount = 0;
		
		if (currentPlayer.getElement().equals(user)) {
			user.repaintUserCard();
		}
		
		nextTurn();
	}

	public void winner(Player player) {
		// System.out.println("Winner is" + player);
		JOptionPane.showMessageDialog(user, player + "의 승리입니다.");
		logger.info(player + "의 승리입니다.");
		System.exit(0);
	}

	public void checkCardAbility(Card c1) {
		// System.out.println(c1.getAbility());
		
		if (c1.getAbility() < 1) {
			nextTurn();
		} else if (c1.getAbility() < Card.Ability.Attack) {
			this.attackcount += c1.getAbility();
			
			// System.out.println("공격점수" + attackcount);
			logger.info(c1 + "의 공격점수는 " + attackcount + "장 입니다.");
			
			nextTurn();
		} else if (c1.getAbility() == Card.Ability.Jump) {
			jump();
		} else if (c1.getAbility() == Card.Ability.Revers) {
			revers();
		} else if (c1.getAbility() == Card.Ability.OneMore) {
			oneMore();
		} else if (c1.getAbility() == Card.Ability.SuitChange) {
			suitChange();
		}
	}

	private void jump() {
		// System.out.println("점프");
		logger.info("점프하였습니다.");
		
		if (direction) {
			currentPlayer = currentPlayer.getNext();
		} else {
			currentPlayer = currentPlayer.getPrev();
		}
		
		nextTurn();
	}

	private void oneMore() {
		// System.out.println("한번더");
		logger.info("한번 더 플레이합니다.");
		
		currentPlayer.getElement().setTurn(true);
	}

	private void revers() {
		// System.out.println("반대로");
		logger.info("반대 방향으로 턴이 진행됩니다.");
		
		direction = !direction;
		
		nextTurn();
	}

	private void suitChange() {
		deck.add(openCard.getCard());
		int k = currentPlayer.getElement().suitChange();
		
		// System.out.println("SuitChange : " + k);
		
		Card temp = new Card(k, 7);
		temp.setFake();
		openCard.setCard(temp);
		
		// System.out.println("SuitChange : " + openCard.getCard());
		logger.info("낼 수 있는 조건이 " + openCard.getCard() + "로 변경되었습니다.");
		
		nextTurn();
	}

	private void nextTurn() {
		if (direction) {
			currentPlayer = currentPlayer.getNext();
		} else {
			currentPlayer = currentPlayer.getPrev();
		}
		
		// System.out.println("턴 : " + currentPlayer.getElement());
		logger.info("===== 턴이 종료되었습니다. =====");
		logger.info("===== " + currentPlayer.getElement() + "의 턴입니다. =====");
		
		currentPlayer.getElement().setTurn(true);
	}
}
