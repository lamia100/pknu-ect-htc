package data;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {
	private static final long serialVersionUID = 1L;

	public static class Pips {
		public final static int Ace = 1;
		/*
		public final static int Two = 2;
		public final static int Three = 3;
		public final static int Four = 4;
		public final static int Five = 5;
		public final static int Six = 6;
		public final static int Seven= 7;
		public final static int Eight = 8;
		public final static int Nine = 9;
		public final static int Ten = 10;
		*/
		
		public final static int Jack = 11;
		public final static int Queen = 12;
		public final static int King = 13;
		public final static int ColorJocker = 14;
		public final static int GrayJocker = 15;
	}

	public static class Suit {
		public final static int Club = 1;
		public final static int Diamond = 2;
		public final static int Heart = 3;
		public final static int Spade = 4;
		public final static int Joker = 5;
	}

	public static class Ability {
		public final static int Attack = 100;
		public final static int Jump = 101;
		public final static int Revers = 102;
		public final static int OneMore = 103;
		public final static int SuitChange = 104;
	}

	// 카드 정보
	private int suit;
	private int pips;
	private int ability;
	private int priority;
	private boolean isFake = false;
	private String front;
	public static final String back = "/image/back.png";

	public Card(int suit, int pips) {
		this.suit = suit;
		this.pips = pips;
		ability = 0;
		priority = 0;
		front = "/image/" + suit + "_" + pips + ".png";
		
		switch (pips) {
		case Card.Pips.Ace:
			if (suit == Card.Suit.Spade) {
				priority = 3;
				ability = 5;
			} else {
				priority = 2;
				ability = 3;
			}
			break;
		case 2:
			priority = 1;
			ability = 2;
			break;
		case 7:
			ability = Card.Ability.SuitChange;
			break;
		case Card.Pips.Jack:
			ability = Card.Ability.Jump;
			break;
		case Card.Pips.Queen:
			ability = Card.Ability.Revers;
			break;
		case Card.Pips.King:
			ability = Card.Ability.OneMore;
			break;
		case Card.Pips.ColorJocker:
			ability = 10;
			priority = 3;
			break;
		case Card.Pips.GrayJocker:
			ability = 7;
			priority = 3;
			break;
		}
	}

	public Card(int suit, int pips, int ability) {
		this.suit = suit;
		this.pips = pips;
		this.ability = ability;
		front = "/image/" + suit + "_" + pips + ".png";
		
		switch (pips) {
		case Card.Pips.Ace:
			if (suit == Card.Suit.Spade) {
				priority = 3;
			} else {
				priority = 2;
			}
			break;
		case 2:
			priority = 1;
			break;
		case Card.Pips.ColorJocker:
		case Card.Pips.GrayJocker:
			priority = 3;
			break;
		default:
			priority = 0;
		}
	}

	/**
	 * 이 카드가 다른 카드와 종류나 숫자가 같은지 체크
	 * @param c1 다른 카드
	 * @return 같으면 true, 다르면 false
	 */
	public boolean isSameRank(Card c1) {
		if (suit == c1.suit || pips == c1.pips || c1.suit == Card.Suit.Joker || suit == Card.Suit.Joker) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 공격시 이 카드가 다른 카드를 방어할 수 있는지 체크
	 * @param c1 다른 카드
	 * @return 방어 가능하면 true, 불가능하면 false
	 */
	public boolean isHighPriority(Card c1) {
		if (this.pips == c1.pips) {
			// 같은 숫자(A,2..)일 때 방어 가능 
						
			return true;
		} else if (this.priority >= c1.priority) {
			// 우선순위가 같거나 높고, 모양이 같을 때 방어 가능
			
			return (this.suit == c1.suit) || (this.suit == Card.Suit.Joker);
		}
		
		return false;
	}

	public int getAbility() {
		return ability;
	}

	public int getSuit() {
		return suit;
	}

	public int getPips() {
		return pips;
	}

	public String getFrontImage() {
		return front;
	}

	public static String getBackImage() {
		return back;
	}

	@Override
	public int compareTo(Card o) {
		return (suit * 1000 + pips) - (o.suit * 1000 + o.pips);
	}

	public String toString() {
		String temp = "";
		
		switch (suit) {
		case 1:
			temp += "♣";
			break;
		case 2:
			temp += "◆";
			break;
		case 3:
			temp += "♥";
			break;
		case 4:
			temp += "♠";
			break;
		case 5:
			temp += "Joker";
			break;
		}
		
		switch (pips) {
		case Card.Pips.Ace:
			temp += "A";
			break;
		case Card.Pips.Jack:
			temp += "J";
			break;
		case Card.Pips.Queen:
			temp += "Q";
			break;
		case Card.Pips.King:
			temp += "K";
			break;
		case Card.Pips.ColorJocker:
			temp += " Color";
			break;
		case Card.Pips.GrayJocker:
			temp += " Gray";
			break;
		default:
			temp += pips;
			break;
		}
		
		return temp;
	}

	public void setFake() {
		this.isFake = true;
	}

	public boolean isFake() {
		return isFake;
	}
}