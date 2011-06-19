package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
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
		
		logger.info("�� �ʱ�ȭ�� �Ϸ�Ǿ����ϴ�.");
	}

	/**
	 * ���� ī�� 1���� �������
	 * @param card
	 */
	public void add(Card card) {
		if (!card.isFake()) {
			deck.add(card);
			
			logger.debug("���� " + card + "�� ����־����ϴ�.");
		}
	}

	/**
	 * ���� �������� ī�带 �������
	 * @param cardList
	 * @return
	 */
	public boolean addAll(Collection<? extends Card> cardList) {
		return deck.addAll(cardList);
	}
	
	/**
	 * ������ �����ϰ� ī�� 1���� ������
	 * @return ���� ī�� 1��
	 */
	public Card getCard() {
		if (isEmpty()) {
			logger.info("���� ī�尡 �����ϴ�.");
			
			return null;
		}
		
		Card result = null;
		
		if (size() == 1) {
			result = deck.remove(0);
		}
		else {
			result = deck.remove(rand.nextInt(deck.size() - 1));
		}
		
		logger.debug("������ " + result + "�� ���������ϴ�.");
		
		return result;
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