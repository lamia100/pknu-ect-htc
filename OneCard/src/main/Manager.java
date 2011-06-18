package main;

import utility.*;
import player.*;
import data.*;

public class Manager {
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
		
		System.out.println(c1 + " : " + currentPlayer.getElement());
		
		if (c1 != null) {
			deck.add(openCard.getCard());
			openCard.setCard(c1);
			
			System.out.println("OpenCard : " + openCard.getCard());
			
			if (currentPlayer.getElement().isEmpty()) {
				winner(currentPlayer.getElement());
			}
			
			checkCardAbility(c1);
		} else {
			System.out.println("카드를 먹습니다. 처묵처묵");
			
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
		
		for (int i = 0; i < attackcount; i++) {
			currentPlayer.getElement().addCard(deck.getCard());
		}
		
		attackcount = 0;
		
		if (currentPlayer.getElement().equals(user)) {
			user.repaintUserCard();
		}
		
		nextTurn();
	}

	public void winner(Player player) {
		System.out.println("Winner is" + player);
		System.exit(0);
	}

	public void checkCardAbility(Card c1) {
		System.out.println(c1.getAbility());
		
		if (c1.getAbility() < 1) {
			nextTurn();
		} else if (c1.getAbility() < Card.Ability.Attack) {
			this.attackcount += c1.getAbility();
			
			System.out.println("공격점수" + attackcount);
			
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
		System.out.println("점프");
		
		if (direction) {
			currentPlayer = currentPlayer.getNext();
		} else {
			currentPlayer = currentPlayer.getPrev();
		}
		
		nextTurn();
	}

	private void oneMore() {
		System.out.println("한번더");
		
		currentPlayer.getElement().setTurn(true);
	}

	private void revers() {
		System.out.println("반대로");
		
		direction = !direction;
		
		nextTurn();
	}

	private void suitChange() {
		deck.add(openCard.getCard());
		int k = currentPlayer.getElement().suitChange();
		
		System.out.println("SuitChange : " + k);
		
		Card temp = new Card(k, 7);
		temp.setFake();
		openCard.setCard(temp);
		
		System.out.println("SuitChange : " + openCard.getCard());
		
		nextTurn();
	}

	private void nextTurn() {
		if (direction) {
			currentPlayer = currentPlayer.getNext();
		} else {
			currentPlayer = currentPlayer.getPrev();
		}
		
		System.out.println("턴 : " + currentPlayer.getElement());
		
		currentPlayer.getElement().setTurn(true);
	}
}
