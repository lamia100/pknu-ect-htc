import javax.swing.ImageIcon;

public class Card {
	private int suit;
	private int value;
	private ImageIcon image;
	private boolean check;
	
	public Card(int suit, int value) {
		this.suit = suit;
		this.value = value;
		if (suit == 0 || value == 0) {
			image = new ImageIcon("../OneCard/cardImage/inter6.jpg");
		}
		else {
			image = new ImageIcon("../OneCard/cardImage/" + suit + value + ".jpg");
		}
		check = false;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public int getValue() {
		return value;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public void setCheck(boolean check) {
		this.check = check;
	}
	
	public String toString() {
		String str = "";
		
		switch (value) {
		case 0: str += "All"; break;
		case 1: str += "Ace"; break;
		case 11: str += "Jack"; break;
		case 12: str += "Queen"; break;
		case 13: str += "King"; break;
		case 14: str += "Joker[1]"; break;
		case 15: str += "Joker[2]"; break;
		default: str += value; break;
		}
		
		switch (suit) {
		case 0: str += " of All"; break;
		case 1: str += " of Spade"; break;
		case 2: str += " of Diamond"; break;
		case 3: str += " of Heart"; break;
		case 4: str += " of Club"; break;
		case 5: str += " of Joker"; break;
		}
		
		return str;
	}
}