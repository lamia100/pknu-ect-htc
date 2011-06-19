package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.*;
import main.Manager;
import data.Card;

public class Computer implements Player {
	private static Logger logger = Logger.getLogger(Computer.class);
	
	private Manager manager = null;
	private ArrayList<Card> hand;
	private String name;

	public Computer(Manager manager, String name) {
		this.manager = manager;
		hand = new ArrayList<Card>();
		this.name = name;
	}

	private boolean checkCard(Card card) {
		Card temp = manager.getOpenCard();
		int state = manager.getState();
		boolean result = false;
		
		if (state > 1) {
			result = card.isHighPriority(temp);
		} else {
			result = card.isSameRank(temp);
		}
		
		if (result) {
			logger.debug(card + "�� �� �� �ֽ��ϴ�.");
		}
		else {
			logger.debug(card + "�� �� �� �����ϴ�.");
		}
		
		return result;
	}

	public void setTurn(boolean isTurn) {
		if (isTurn) {
			dropCard();
		}
	}

	public void addCard(Card card) {
		hand.add(card);
	}

	/**
	 * ��ǻ�Ͱ� �����ִ� ī�� ��, �����ϰ� �� �� �ִ� ī�� 1���� ��
	 */
	private void dropCard() {
		ArrayList<Card> temp = new ArrayList<Card>();
		
		for (Card target : hand) {
			if (checkCard(target)) {
				temp.add(target);
			}
		}
		
		if (temp.size() > 1) {
			manager.dropCard(hand.remove(hand.indexOf(temp.get(new Random().nextInt(temp.size() - 1)))));
		} else if (temp.size() == 1) {
			manager.dropCard(hand.remove(hand.indexOf(temp.get(0))));
		} else {
			manager.dropCard(null);
		}
	}

	public boolean isEmpty() {
		return false;
	}

	public String toString() {
		return name;
	}

	public int suitChange() {
		return new Random().nextInt(3) + 1;
	}

	@Override
	public int getHandSize() {
		return hand.size();
	}
	
	@Override
	public List<Card> getHand() {
		return hand;
	}
}