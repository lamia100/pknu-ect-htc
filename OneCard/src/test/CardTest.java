package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

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

		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < 10; i++) {
			int suit = rand.nextInt(4) + 1;
			Card c3 = new Card(suit, rand.nextInt(13) + 1);
			Card c4 = new Card(suit, rand.nextInt(13) + 1);
			assertTrue(c3.isSameRank(c4));
		}
		for (int i = 0; i < 10; i++) {
			int pips = rand.nextInt(4) + 1;
			Card c3 = new Card(rand.nextInt(4) + 1, pips);
			Card c4 = new Card(rand.nextInt(4) + 1, pips);
			assertTrue(c3.isSameRank(c4));
		}
	}

	@Test
	public void testDiffrenteRank() {

		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < 20; i++) {
			int suit1 = 0;
			int suit2 = 0;
			do {
				suit1 = rand.nextInt(4) + 1;
				suit2 = rand.nextInt(4) + 1;
			} while (suit1 == suit2);

			int pip1 = 0;
			int pip2 = 0;
			do {
				pip1 = rand.nextInt(13) + 1;
				pip2 = rand.nextInt(13) + 1;
			} while (pip1 == pip2);

			Card c3 = new Card(suit1, pip1);
			Card c4 = new Card(suit2, pip2);
			assertFalse(c3.isSameRank(c4));
		}
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