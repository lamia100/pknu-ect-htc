package data;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	private ArrayList<Card> deck;
	private Random rand;

	public Deck() {
		deck = new ArrayList<Card>();
		rand = new Random();
		
		initialize();
	}

	private void initialize() {
		for (int suit = 1; suit <= 4; suit++) {
			for (int pips = 1; pips <= 13; pips++) {
				add(new Card(suit, pips));
			}
		}
		
		add(new Card(Card.Suit.Joker, Card.Pips.ColorJocker));
		add(new Card(Card.Suit.Joker, Card.Pips.GrayJocker));
		
		// System.out.println(deck.size());
	}

	public Card getCard() {
		if (isEmpty()) {
			return null;
		}
		
		// System.out.println(deck.size());
		
		return deck.remove(rand.nextInt(deck.size() - 1));
	}

	public void add(Card card) {
		if (!card.isFake()) {
			deck.add(card);
		}
	}

	public boolean isEmpty() {
		return deck.isEmpty();
	}
	
	public int size() {
		return deck.size();
	}
}