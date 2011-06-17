package test;

import static org.junit.Assert.*;
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
		fail("아직 구현되지 않음");
	}

	@Test
	public void testCompareTo() {
		fail("아직 구현되지 않음");
	}
}