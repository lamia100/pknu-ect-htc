package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import data.Card;
import data.Deck;

public class DeckTest {

	@Test
	public void testDeck() {
		Deck d = new Deck();
		
		assertEquals(d.size(), 54);
		
		int suit = 1;
		int pips = 1;
		for (Card target : d) {
			// suit üũ			
			switch (suit) {
			case 1:
				assertEquals(target.getSuit(), Card.Suit.Club);
				break;
			case 2:
				assertEquals(target.getSuit(), Card.Suit.Diamond);
				break;
			case 3:
				assertEquals(target.getSuit(), Card.Suit.Heart);
				break;
			case 4:
				assertEquals(target.getSuit(), Card.Suit.Spade);
				break;
			case 5:
				assertEquals(target.getSuit(), Card.Suit.Joker);
				break;
			}
			
			// pips üũ
			if (suit == 5 && pips == 1) {
				assertEquals(target.getPips(), Card.Pips.ColorJocker);
			}
			else if (suit == 5 && pips == 2) {
				assertEquals(target.getPips(), Card.Pips.GrayJocker);
			}
			else {
				assertEquals(target.getPips(), pips);
			}
			
			pips++;
			if (pips > 13) {
				suit++;
				pips = 1;
			}
		}
	}

	@Test
	public void testGetCard() {
		Deck d = new Deck();
		
		while (d.size() > 0) {
			d.getCard();
		}
		
		assertEquals(d.size(), 0);
	}

	@Test
	public void testAdd() {
		ArrayList<Card> test = new ArrayList<Card>();
		
		for (int suit = 1; suit <= 4; suit++) {
			for (int pips = 1; pips <= 13; pips++) {
				test.add(new Card(suit, pips));
			}
		}
		
		test.add(new Card(Card.Suit.Joker, Card.Pips.ColorJocker));
		test.add(new Card(Card.Suit.Joker, Card.Pips.GrayJocker));
		
		
		Deck deck = new Deck();
		
		while (deck.size() > 0) {
			deck.getCard();
		}
		
		for (Card target : test) {
			deck.add(target);
			assertEquals(target, deck.getCard());
		}
	}
}