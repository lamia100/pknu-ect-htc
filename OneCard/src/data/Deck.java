package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.log4j.*;

public class Deck implements Iterable<Card> {
	private static Logger logger = Logger.getLogger(Deck.class);
	
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
		logger.info("덱 초기화가 완료되었습니다.");
	}

	public Card getCard() {
		if (isEmpty()) {
			// JOptionPane.showMessageDialog(null, "덱에 카드가 없습니다. 게임이 종료되었습니다.");
			logger.info("덱에 카드가 없습니다.");
			// System.exit(1);
			return null;
		}
		
		Card result = null;
		
		if (size() == 1) {
			result = deck.remove(0);
		}
		else {
			result = deck.remove(rand.nextInt(deck.size() - 1));
		}
		
		// System.out.println(deck.size());
		logger.debug("덱에서 " + result + "를 가져갔습니다.");
		
		return result;
	}

	public void add(Card card) {
		if (!card.isFake()) {
			deck.add(card);
			
			logger.debug("덱에 " + card + "를 집어넣었습니다.");
		}
	}

	public boolean addAll(Collection<? extends Card> c)
	{
		return deck.addAll(c);
	}
	public boolean isEmpty() {
		return deck.isEmpty();
	}
	
	public int size() {
		return deck.size();
	}

	@Override
	public Iterator<Card> iterator() {
		return deck.iterator();
	}
}