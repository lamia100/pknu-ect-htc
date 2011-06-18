package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import data.Card;

public class CardTest {

	@Test
	public void testCard() {
		Card c1 = new Card(Card.Suit.Spade, Card.Pips.Ace);
		Card c2 = new Card(Card.Suit.Club, 7);
		Card c3 = new Card(Card.Suit.Diamond, Card.Pips.Jack);

		assertEquals(c1.getAbility(), 5);
		assertEquals(c2.getAbility(), Card.Ability.SuitChange);
		assertEquals(c3.getAbility(), Card.Ability.Jump);
	}

	@Test
	public void testIsSameRank() {
		Card c1 = new Card(Card.Suit.Spade, Card.Pips.Ace);
		Card c2 = new Card(Card.Suit.Club, Card.Pips.Ace);

		assertTrue(c1.isSameRank(c2));

		Card c3 = new Card(Card.Suit.Diamond, Card.Pips.Jack);
		Card c4 = new Card(Card.Suit.Heart, Card.Pips.King);

		assertFalse(c3.isSameRank(c4));
	}

	@Test
	public void testIsHighPriority() {
		Card c1 = new Card(Card.Suit.Spade, Card.Pips.Ace);
		Card c2 = new Card(Card.Suit.Club, Card.Pips.Ace);

		assertTrue(c1.isHighPriority(c2));

		Card c3 = new Card(Card.Suit.Diamond, Card.Pips.Jack);
		Card c4 = new Card(Card.Suit.Heart, Card.Pips.King);

		assertFalse(c3.isHighPriority(c4));
	}

	@Test
	public void testCompareTo() {
		Card[] c1 = new Card[3];
		c1[0] = new Card(Card.Suit.Spade, Card.Pips.King);
		c1[1] = new Card(Card.Suit.Spade, Card.Pips.Ace);
		c1[2] = new Card(Card.Suit.Spade, 5);

		Card[] c2 = Arrays.copyOf(c1, c1.length);
		Arrays.sort(c2);

		assertEquals(c1[0], c2[2]);
		assertEquals(c1[1], c2[0]);
		assertEquals(c1[2], c2[1]);
	}
}