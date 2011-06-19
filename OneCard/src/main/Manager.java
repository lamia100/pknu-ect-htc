package main;

import java.util.List;

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
		currentPlayer = playerList.getElement();

		for (int i = 1; i < usercount; i++) {
			Computer temp = new Computer(this, "��ǻ��" + i);
			// temp.start();
			playerList.add(temp);
		}

		// �÷��̾�� 7�徿 �� ����
		for (int k = 0; k < usercount; k++) {
			for (int i = 0; i < 7; i++) {
				currentPlayer.addCard(deck.getCard());
				currentPlayer = playerList.getNext();
			}
		}

		logger.info("���� �ʱ�ȭ�� �Ϸ�Ǿ����ϴ�.");
		logger.info("===== ������ ���Դϴ�. =====");
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
			winner(currentPlayer);
		}

		currentPlayer.setTurn(false);

		// System.out.println(c1 + " : " + currentPlayer.getElement());

		if (c1 != null) {
			logger.info(c1 + "�� �½��ϴ�.");

			deck.add(openCard.getCard());
			openCard.setCard(c1);

			if (currentPlayer.isEmpty()) {
				winner(currentPlayer);
			}

			checkCardAbility(c1);
		} else {
			// System.out.println("ī�带 �Խ��ϴ�. ó��ó��");
			logger.info("ī�带 ���� ���߽��ϴ�.");

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

		logger.info("�������� �� ī��� �� " + attackcount + "���Դϴ�.");

		if (currentPlayer.getHandSize() + attackcount > 20) {
			logger.info(currentPlayer + "�� �Ļ��߽��ϴ�.");
			playerList.remove();
			deck.addAll(currentPlayer.gethand());
			currentPlayer = playerList.getElement();
			attackcount = 0;
			nextTurn();
			return;
		}

		for (int i = 0; i < attackcount; i++) {
			currentPlayer.addCard(deck.getCard());
		}

		attackcount = 0;

		if (currentPlayer.equals(user)) {
			user.repaintUserCard();
		}

		nextTurn();
	}

	public void winner(Player player) {
		// System.out.println("Winner is" + player);
		JOptionPane.showMessageDialog(user, player + "�� �¸��Դϴ�.");
		logger.info(player + "�� �¸��Դϴ�.");
		System.exit(0);
	}

	public void checkCardAbility(Card c1) {
		// System.out.println(c1.getAbility());

		if (c1.getAbility() < 1) {
			nextTurn();
		} else if (c1.getAbility() < Card.Ability.Attack) {
			this.attackcount += c1.getAbility();

			// System.out.println("��������" + attackcount);
			logger.info(c1 + "�� ���������� " + attackcount + "�� �Դϴ�.");

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
		// System.out.println("����");
		logger.info("�����Ͽ����ϴ�.");

		if (direction) {
			currentPlayer = playerList.getNext();
		} else {
			currentPlayer = playerList.getPrev();
		}

		nextTurn();
	}

	private void oneMore() {
		// System.out.println("�ѹ���");
		logger.info("�ѹ� �� �÷����մϴ�.");

		currentPlayer.setTurn(true);
	}

	private void revers() {
		// System.out.println("�ݴ��");
		logger.info("�ݴ� �������� ���� ����˴ϴ�.");

		direction = !direction;

		nextTurn();
	}

	private void suitChange() {
		deck.add(openCard.getCard());
		int k = currentPlayer.suitChange();

		// System.out.println("SuitChange : " + k);

		Card temp = new Card(k, 7);
		temp.setFake();
		openCard.setCard(temp);

		// System.out.println("SuitChange : " + openCard.getCard());
		logger.info("�� �� �ִ� ������ " + openCard.getCard() + "�� ����Ǿ����ϴ�.");

		nextTurn();
	}

	private void nextTurn() {
		if (direction) {
			currentPlayer = playerList.getNext();
		} else {
			currentPlayer = playerList.getPrev();
		}

		// System.out.println("�� : " + currentPlayer.getElement());
		logger.info("===== ���� ����Ǿ����ϴ�. =====");
		logger.info("===== " + currentPlayer + "�� ���Դϴ�. =====");

		currentPlayer.setTurn(true);
	}
}
