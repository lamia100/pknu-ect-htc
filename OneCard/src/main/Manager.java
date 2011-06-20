package main;

import javax.swing.JOptionPane;
import org.apache.log4j.*;
import utility.*;
import player.*;
import data.*;

public class Manager {
	private static Logger logger = Logger.getLogger(Manager.class);

	private Player currentPlayer;
	private Deck deck;
	private CircleLinkedList<Player> playerList;
	private int attackCount = 0;
	private boolean direction = true;
	private CardLabel openCard;
	private OneCardGUI user;

	public Manager(OneCardGUI userGUI, int userCount) {
		this.user = userGUI;
		this.openCard = userGUI.getOpenCardLabel();

		initialize(userGUI, userCount);
	}

	public void initialize(Player user, int userCount) {
		deck = new Deck();
		playerList = new CircleLinkedList<Player>();
		
		// userCount만큼 유저와 컴퓨터들을 생성
		playerList.add(user);
		currentPlayer = playerList.getElement();

		for (int i = 1; i < userCount; i++) {
			Computer temp = new Computer(this, "컴퓨터" + i);
			playerList.add(temp);
		}

		// 플레이어당 7장씩 패 돌림
		for (int k = 0; k < userCount; k++) {
			for (int i = 0; i < 7; i++) {
				currentPlayer.addCard(deck.getCard());
				currentPlayer = playerList.getNext();
			}
		}

		logger.info("게임 초기화가 완료되었습니다.");
		logger.info("===== 유저의 턴입니다. =====");
	}

	/*
	 * 유저 GUI가 낸 카드를 게임 룰에 따라 처리
	 * @param dropCardLabel 유저 GUI로부터 받은 카드가 담긴 레이블
	public void dropCardLabel(CardLabel dropCardLabel) {
		user.repaintUserCard();

		if (dropCardLabel == null) {
			dropCard(null);
		} else {
			dropCard(dropCardLabel.getCard());
		}
	}
	 */
	/**
	 * 플레이어가 낸 카드를 게임 룰에 따라 처리
	 * @param dropCard
	 */
	public void dropCard(Card dropCard) {
		if (playerList.size() == 1) {
			winner(currentPlayer);
		}

		currentPlayer.setTurn(false);

		if (dropCard != null) {
			logger.info(dropCard + "를 냈습니다.");

			deck.add(openCard.getCard());
			openCard.setCard(dropCard);

			if (currentPlayer.isEmpty()) {
				winner(currentPlayer);
			}

			checkCardAbility(dropCard);
		} else {
			logger.info("카드를 내지 못했습니다.");

			feedCard();
		}
	}

	public int getState() {
		return attackCount;
	}

	public Card getOpenCard() {
		return openCard.getCard();
	}

	/**
	 * 플레이어에게 카드를 먹입니다. 
	 */
	private void feedCard() {
		if (attackCount == 0) {
			attackCount = 1;
		}

		logger.info("가져가야 할 카드는 총 " + attackCount + "장입니다.");

		// 플레이어가 20장 이상을 가지게 된다면, 해당 플레이어는 파산
		if (currentPlayer.getHandSize() + attackCount > 20) {
			logger.info(currentPlayer + "가 파산했습니다.");
			
			playerList.remove();
			deck.addAll(currentPlayer.getHand());
			currentPlayer = playerList.getElement();
			
			attackCount = 0;
			nextTurn();
			
			return;
		}

		// 공격 횟수만큼 플레이어가 덱에서 카드를 가져감
		for (int i = 0; i < attackCount; i++) {
			currentPlayer.addCard(deck.getCard());
		}

		attackCount = 0;

		if (currentPlayer.equals(user)) {
			user.repaintUserCard();
		}

		nextTurn();
	}

	private void winner(Player player) {
		JOptionPane.showMessageDialog(user, player + "의 승리입니다.");
		logger.info(player + "의 승리입니다.");
		System.exit(0);
	}

	/**
	 * 특수 카드에 대한 처리 
	 * @param checkCard
	 */
	public void checkCardAbility(Card checkCard) {
		if (checkCard.getAbility() < 1) {
			nextTurn();
		} else if (checkCard.getAbility() < Card.Ability.Attack) {
			this.attackCount += checkCard.getAbility();

			logger.info(checkCard + "의 공격점수는 " + attackCount + "장 입니다.");

			nextTurn();
		} else if (checkCard.getAbility() == Card.Ability.Jump) {
			jump();
		} else if (checkCard.getAbility() == Card.Ability.Revers) {
			reverseDirection();
		} else if (checkCard.getAbility() == Card.Ability.OneMore) {
			oneMore();
		} else if (checkCard.getAbility() == Card.Ability.SuitChange) {
			suitChange();
		}
	}

	private void jump() {
		logger.info("점프하였습니다.");

		if (direction) {
			currentPlayer = playerList.getNext();
		} else {
			currentPlayer = playerList.getPrev();
		}

		nextTurn();
	}

	private void oneMore() {
		logger.info("한번 더 플레이합니다.");

		currentPlayer.setTurn(true);
	}

	private void reverseDirection() {
		logger.info("반대 방향으로 턴이 진행됩니다.");

		direction = !direction;

		nextTurn();
	}

	/**
	 * 낼 수 있는 카드의 모양을 변경
	 */
	private void suitChange() {
		deck.add(openCard.getCard());
		int k = currentPlayer.suitChange();

		Card temp = new Card(k, 7);
		temp.setFake();
		openCard.setCard(temp);

		logger.info("낼 수 있는 조건이 " + openCard.getCard() + "로 변경되었습니다.");

		nextTurn();
	}

	private void nextTurn() {
		if (direction) {
			currentPlayer = playerList.getNext();
		} else {
			currentPlayer = playerList.getPrev();
		}

		logger.info("===== 턴이 종료되었습니다. =====");
		logger.info("===== " + currentPlayer + "의 턴입니다. =====");

		currentPlayer.setTurn(true);
	}
}