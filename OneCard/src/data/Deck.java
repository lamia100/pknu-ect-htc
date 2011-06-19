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
		
		logger.info("덱 초기화가 완료되었습니다.");
	}

	/**
	 * 덱에 카드 1장을 집어넣음
	 * @param card
	 */
	public void add(Card card) {
		if (!card.isFake()) {
			deck.add(card);
			
			logger.debug("덱에 " + card + "를 집어넣었습니다.");
		}
	}

	/**
	 * 덱에 여러개의 카드를 집어넣음
	 * @param cardList
	 * @return
	 */
	public boolean addAll(Collection<? extends Card> cardList) {
		return deck.addAll(cardList);
	}
	
	/**
	 * 덱에서 랜덤하게 카드 1장을 가져옴
	 * @return 랜덤 카드 1장
	 */
	public Card getCard() {
		if (isEmpty()) {
			logger.info("덱에 카드가 없습니다.");
			
			return null;
		}
		
		Card result = null;
		
		if (size() == 1) {
			result = deck.remove(0);
		}
		else {
			result = deck.remove(rand.nextInt(deck.size() - 1));
		}
		
		logger.debug("덱에서 " + result + "를 가져갔습니다.");
		
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