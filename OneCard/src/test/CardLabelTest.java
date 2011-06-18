package test;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import junit.framework.Assert;

import main.CardLabel;

import org.junit.*;

import data.Card;

public class CardLabelTest {

	@Test
	public void testCardLabel() {
		CardLableTestDialog dialog=new CardLableTestDialog(new CardLabel(new Card(Card.Suit.Heart, Card.Pips.Ace)));
		dialog.setVisible(true);
		Assert.assertEquals(dialog.result, 1);
	}

}
