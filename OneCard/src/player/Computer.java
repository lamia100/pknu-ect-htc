package player;

import java.util.ArrayList;
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

	private boolean checkCard(Card c1) {
		Card temp = manager.getOpenCard();
		int state = manager.getState();
		boolean result = false;
		
		if (state > 1) {
			result = c1.isHighPriority(temp);
		} else {
			result = c1.isSameRank(temp);
		}
		
		if (result) {
			logger.debug(c1 + "를 낼 수 있습니다.");
		}
		else {
			logger.debug(c1 + "는 낼 수 없습니다.");
		}
		
		return result;
	}

	public void setTurn(boolean isTurn) {
		if (isTurn) {
			dropCard();
		}
	}

	public void addCard(Card c1) {
		hand.add(c1);
	}

	private void dropCard() {
		/*
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		
		ArrayList<Card> temp = new ArrayList<Card>();
		
		for (Card target : hand) {
			if (checkCard(target)) {
				temp.add(target);
			}
		}
		
		if (temp.size() > 1) {
			manager.addCard(hand.remove(hand.indexOf(temp.get(new Random().nextInt(temp.size() - 1)))));
		} else if (temp.size() == 1) {
			manager.addCard(hand.remove(hand.indexOf(temp.get(0))));
		} else {
			manager.addCard(null);
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
}