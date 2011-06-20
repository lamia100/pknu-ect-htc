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
		
		// userCount��ŭ ������ ��ǻ�͵��� ����
		playerList.add(user);
		currentPlayer = playerList.getElement();

		for (int i = 1; i < userCount; i++) {
			Computer temp = new Computer(this, "��ǻ��" + i);
			playerList.add(temp);
		}

		// �÷��̾�� 7�徿 �� ����
		for (int k = 0; k < userCount; k++) {
			for (int i = 0; i < 7; i++) {
				currentPlayer.addCard(deck.getCard());
				currentPlayer = playerList.getNext();
			}
		}

		logger.info("���� �ʱ�ȭ�� �Ϸ�Ǿ����ϴ�.");
		logger.info("===== ������ ���Դϴ�. =====");
	}

	/*
	 * ���� GUI�� �� ī�带 ���� �꿡 ���� ó��
	 * @param dropCardLabel ���� GUI�κ��� ���� ī�尡 ��� ���̺�
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
	 * �÷��̾ �� ī�带 ���� �꿡 ���� ó��
	 * @param dropCard
	 */
	public void dropCard(Card dropCard) {
		if (playerList.size() == 1) {
			winner(currentPlayer);
		}

		currentPlayer.setTurn(false);

		if (dropCard != null) {
			logger.info(dropCard + "�� �½��ϴ�.");

			deck.add(openCard.getCard());
			openCard.setCard(dropCard);

			if (currentPlayer.isEmpty()) {
				winner(currentPlayer);
			}

			checkCardAbility(dropCard);
		} else {
			logger.info("ī�带 ���� ���߽��ϴ�.");

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
	 * �÷��̾�� ī�带 ���Դϴ�. 
	 */
	private void feedCard() {
		if (attackCount == 0) {
			attackCount = 1;
		}

		logger.info("�������� �� ī��� �� " + attackCount + "���Դϴ�.");

		// �÷��̾ 20�� �̻��� ������ �ȴٸ�, �ش� �÷��̾�� �Ļ�
		if (currentPlayer.getHandSize() + attackCount > 20) {
			logger.info(currentPlayer + "�� �Ļ��߽��ϴ�.");
			
			playerList.remove();
			deck.addAll(currentPlayer.getHand());
			currentPlayer = playerList.getElement();
			
			attackCount = 0;
			nextTurn();
			
			return;
		}

		// ���� Ƚ����ŭ �÷��̾ ������ ī�带 ������
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
		JOptionPane.showMessageDialog(user, player + "�� �¸��Դϴ�.");
		logger.info(player + "�� �¸��Դϴ�.");
		System.exit(0);
	}

	/**
	 * Ư�� ī�忡 ���� ó�� 
	 * @param checkCard
	 */
	public void checkCardAbility(Card checkCard) {
		if (checkCard.getAbility() < 1) {
			nextTurn();
		} else if (checkCard.getAbility() < Card.Ability.Attack) {
			this.attackCount += checkCard.getAbility();

			logger.info(checkCard + "�� ���������� " + attackCount + "�� �Դϴ�.");

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
		logger.info("�����Ͽ����ϴ�.");

		if (direction) {
			currentPlayer = playerList.getNext();
		} else {
			currentPlayer = playerList.getPrev();
		}

		nextTurn();
	}

	private void oneMore() {
		logger.info("�ѹ� �� �÷����մϴ�.");

		currentPlayer.setTurn(true);
	}

	private void reverseDirection() {
		logger.info("�ݴ� �������� ���� ����˴ϴ�.");

		direction = !direction;

		nextTurn();
	}

	/**
	 * �� �� �ִ� ī���� ����� ����
	 */
	private void suitChange() {
		deck.add(openCard.getCard());
		int k = currentPlayer.suitChange();

		Card temp = new Card(k, 7);
		temp.setFake();
		openCard.setCard(temp);

		logger.info("�� �� �ִ� ������ " + openCard.getCard() + "�� ����Ǿ����ϴ�.");

		nextTurn();
	}

	private void nextTurn() {
		if (direction) {
			currentPlayer = playerList.getNext();
		} else {
			currentPlayer = playerList.getPrev();
		}

		logger.info("===== ���� ����Ǿ����ϴ�. =====");
		logger.info("===== " + currentPlayer + "�� ���Դϴ�. =====");

		currentPlayer.setTurn(true);
	}
}