package player;

import java.util.ArrayList;
import java.util.Iterator;
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
		
		if (state > 1) {
			return c1.isHighPriority(temp);
		} else {
			return c1.isSameRank(temp);
		}
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
		Iterator<Card> ite = hand.iterator();
		
		while (ite.hasNext()) {
			Card c1 = ite.next();
			
			// System.out.println(c1);
			
			if (checkCard(c1)) {
				System.out.print(c1 + ", ");
				
				temp.add(c1);
			}
		}
		
		System.out.println();
		
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
}