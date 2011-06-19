package player;

import data.*;

public interface Player {
	public void addCard(Card c1);
	// public boolean checkCard(Card c1);
	public void setTurn(boolean isTurn);
	public boolean isEmpty();
	public int suitChange();
	public int getHandSize();
}