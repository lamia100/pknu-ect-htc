package player;

import java.util.List;
import data.*;

public interface Player {
	public void addCard(Card c1);
	public void setTurn(boolean isTurn);
	public boolean isEmpty();
	public int suitChange();
	public int getHandSize();
	public List<Card> getHand();
}