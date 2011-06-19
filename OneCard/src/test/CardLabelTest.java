package test;

import static org.junit.Assert.*;

import java.util.Random;

import main.CardLabel;

import org.junit.*;

import data.Card;

public class CardLabelTest {

	@Test
	public void testCardLabel() {
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			Card card =new Card(rand.nextInt(4) + 1, rand.nextInt(13) + 1);
			CardLableTestDialog dialog = new CardLableTestDialog(new CardLabel(card));
			System.out.println(card.getPips()+" "+ card.getSuit());
			dialog.setVisible(true);
			assertEquals(dialog.result, 1);
		}
	}

}