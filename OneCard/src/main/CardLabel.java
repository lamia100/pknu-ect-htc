package main;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.log4j.*;
import data.*;

public class CardLabel extends JLabel implements Comparable<CardLabel> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CardLabel.class);
	private Card card;

	public CardLabel(Card c1) {
		super();
		card = c1;
		
		ImageIcon icon = new ImageIcon(getClass().getResource(card.getFrontImage()));
		icon.setImage(icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
		setIcon(icon);
	}

	public Card setCard(Card c1) {
		Card temp = card;
		card = c1;
		
		ImageIcon icon = new ImageIcon(getClass().getResource(card.getFrontImage()));
		icon.setImage(icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
		setIcon(icon);
		
		this.repaint();
		
		return temp;
	}

	public Card getCard() {
		return card;
	}

	@Override
	public int compareTo(CardLabel o) {
		return card.compareTo(o.card);
	}

	public String toString() {
		return getCard().toString();
	}
}