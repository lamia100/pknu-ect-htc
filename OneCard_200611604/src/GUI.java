import java.util.Iterator;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
	private JPanel p_main;
	private JPanel p_top;
	private JPanel p_center;
	private JPanel p_center_in;
	private JPanel p_center_deck;
	private JPanel p_center_drawn;
	private JPanel p_bottom;
	private JPanel p_bottom_in;
	private JScrollPane sp_bottom_in;
	private JPanel p_bottom_card;
	
	private JLabel lb_state;
	private JButton bt_restart;
	private JButton bt_deckImage;
	private JLabel lb_deckCard;
	private JLabel lb_drawnImage;
	private JLabel lb_drawnCard;
	private JLabel lb_com1State;
	private JLabel lb_com2State;
	private JLabel lb_userState;
	
	private Logic logic;
	private boolean gameOver;
	
	public GUI() {
		super("OneCard - 200611604");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(640, 400);
		Dimension dim = getToolkit().getScreenSize();
		setLocation(dim.width/2 - super.getWidth()/2, dim.height/2 - super.getHeight()/2);

		// GUI 컴포넌트 정의 - 시작
		p_main = new JPanel(new BorderLayout());
		{
			p_top = new JPanel(new BorderLayout());
			{
				lb_state = new JLabel("OneCard :: 200611604 - 서보룡", SwingConstants.CENTER);
				bt_restart = new JButton("Restart");
				bt_restart.addActionListener(this);
			}
			p_top.add(BorderLayout.CENTER, lb_state);
			p_top.add(BorderLayout.EAST, bt_restart);
			
			p_center = new JPanel();
			{
				p_center_in = new JPanel(new BorderLayout());
			}
			p_center.add(p_center_in);
			
			lb_com1State = new JLabel("Com1", SwingConstants.CENTER);
			lb_com2State = new JLabel("Com2", SwingConstants.CENTER);
			
			p_bottom = new JPanel(new BorderLayout());
			{
				lb_userState = new JLabel("내가 가지고있는 카드들", SwingConstants.CENTER);
				
				p_bottom_in = new JPanel();
				sp_bottom_in = new JScrollPane(p_bottom_in);
				sp_bottom_in.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			}
			p_bottom.add(BorderLayout.NORTH, lb_userState);
			p_bottom.add(BorderLayout.CENTER, sp_bottom_in);
		}
		p_main.add(BorderLayout.NORTH, p_top);
		p_main.add(BorderLayout.CENTER, p_center);
		p_main.add(BorderLayout.WEST, lb_com1State);
		p_main.add(BorderLayout.EAST, lb_com2State);
		p_main.add(BorderLayout.SOUTH, p_bottom);

		getContentPane().add(p_main);
		// GUI 컴포넌트 정의 - 끝
		
		logic = new Logic();
		guiRefresh();
		lb_state.setText("[info] 새로운 게임이 시작되었습니다.");
	}
	
	// GUI 컴포넌트들을 Refresh 해줌
	private void guiRefresh() {
		p_center_in.removeAll();
		{
			p_center_deck = new JPanel(new BorderLayout());
			{
				bt_deckImage = new JButton(new ImageIcon("../OneCard/cardImage/back.jpg"));
				bt_deckImage.addActionListener(this);
				lb_deckCard = new JLabel("Deck", SwingConstants.CENTER);
			}
			p_center_deck.add(BorderLayout.CENTER, bt_deckImage);
			p_center_deck.add(BorderLayout.SOUTH, lb_deckCard);
				
			p_center_drawn = new JPanel(new BorderLayout());
			{
				lb_drawnImage = new JLabel(logic.getDrawnCard().getImage());
				lb_drawnCard = new JLabel("Drawn", SwingConstants.CENTER);
			}
			p_center_drawn.add(BorderLayout.CENTER, lb_drawnImage);
			p_center_drawn.add(BorderLayout.SOUTH, lb_drawnCard);
		}
		p_center_in.add(BorderLayout.WEST, p_center_deck);
		p_center_in.add(BorderLayout.EAST, p_center_drawn);
		p_center_in.validate();
		
		int disableCount = 0;
		p_bottom_in.removeAll();
		{	
			SetADT<JButton> bt_userCards = new LinkedArray<JButton>();
			p_bottom_card = new JPanel();
			{
				Iterator<Card> it = logic.getUserCards().iterator();
				JButton bt_temp;
				
				while (it.hasNext()) {
					Card tempCard = it.next();
					bt_temp = new JButton(tempCard.getImage());
					bt_temp.setToolTipText(tempCard.toString());
					bt_temp.addActionListener(this);
					if ( !tempCard.getCheck() ) {
						bt_temp.setEnabled(false);
						disableCount++;
					}
					bt_userCards.add(bt_temp);
				}
			}
			Iterator<JButton> it = bt_userCards.iterator();
			while (it.hasNext()) {
				p_bottom_card.add(it.next());
			}
		}
		p_bottom_in.add(p_bottom_card);
		p_bottom_in.validate();
		sp_bottom_in.validate();
		sp_bottom_in.setVisible(false);
		sp_bottom_in.setVisible(true);
		
		lb_state.setText("Drawn 조건 : " + logic.getDrawnCondition());
		lb_userState.setText("내가 낼 수 있는 / 가지고 있는 카드들 : " + (logic.getUserCards().size() - disableCount) + " / " + logic.getUserCards().size());
		
		System.out.println("[GUI::guiRefresh()] GUI Refresh 됨");
	}
	
	// 버튼을 눌렀을 때 Action
	public void actionPerformed(ActionEvent e) {
		// 재시작 버튼
		if (e.getSource() == bt_restart) {
			gameOver = false;
			
			logic = new Logic();
			guiRefresh();
		}
		
		// Deck 버튼 -> User Add -> Com AI
		else if (e.getSource() == bt_deckImage) {
			if (!gameOver) {
				logic.userTurn(0, 0, -1);
				guiRefresh();
				
				while (!logic.equalUser() && !gameOver) {
					String[] comState = logic.comTurn();
					if (comState[2].equals("Win")) {
						JOptionPane.showMessageDialog(this, comState[0] + "이 이겼습니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
						lb_state.setText("[win] " + comState[0] + "이 이겼습니다.");
						gameOver = true;
					}
					else {
						if (comState[1] != null) {
							JOptionPane.showMessageDialog(this, comState[0] + "가 " + comState[1] + "로 Change를 선언합니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
						}
						
						if (comState[2].equals("OneCard")) {
							JOptionPane.showMessageDialog(this, comState[0] + "가 OneCard를 외쳤습니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				guiRefresh();
			}
		}
			
		// Card 버튼 -> Drawn -> Com AI
		else {
			if (!gameOver) {
				String drawnStr = ((JButton)e.getSource()).getToolTipText();
				int drawnSuit = analyzeSuit(drawnStr);
				int drawnValue = analyzeValue(drawnStr);
				int changeSuit = -1;
				
				// == [Rule 6-3] 7(세븐): 문양을 새로 바꿀 수 있다. ==
				String[] selectSuit = {"Spade", "Diamond", "Heart", "Club"}; 
				if (drawnValue == 7) {
					changeSuit = JOptionPane.showOptionDialog(null, "Select a suit", "OneCard - 200611604",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							null, selectSuit, selectSuit[selectSuit.length - 1]) + 1;
				}
				// == [Rule 6-3] 끝 ==
				
				String[] userState = logic.userTurn(drawnSuit, drawnValue, changeSuit);
				guiRefresh();
				
				if (userState[2].equals("Win")) {
					JOptionPane.showMessageDialog(this, "당신이 이겼습니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
					gameOver = true;
				}
				else {
					if (userState[2].equals("OneCard")) {
						JOptionPane.showMessageDialog(this, "유저가 OneCard를 외칩니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
					}
					
					while (!logic.equalUser() && !gameOver) {
						String[] comState = logic.comTurn();
						if (comState[2].equals("Win")) {
							JOptionPane.showMessageDialog(this, comState[0] + "이 이겼습니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
							gameOver = true;
						}
						else {
							if (comState[1] != null) {
								JOptionPane.showMessageDialog(this, comState[0] + "가 " + comState[1] + "로 Change를 선언합니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
							}
							
							if (comState[2].equals("OneCard")) {
								JOptionPane.showMessageDialog(this, comState[0] + "가 OneCard를 외쳤습니다.", "OneCard - 200611604", JOptionPane.INFORMATION_MESSAGE);
							}	
						}
					}
				}
				guiRefresh();
			}
		} 
	}
	
	/* JButton에서 각 카드에 대한 정보를 String으로 저장을 했기 때문에
	 * 해당 String에 대한 suit 정보를 뽑아주는 내부 서비스
	 */
	private int analyzeSuit(String analyzeStr) {
		String suitStr = analyzeStr.substring(analyzeStr.indexOf("of")+3, analyzeStr.length());
		int suit = 0;
		
		if (suitStr.equals("Spade")) {
			suit = 1;
		}
		else if (suitStr.equals("Diamond")) {
			suit = 2;
		}
		else if (suitStr.equals("Heart")) {
			suit = 3;
		}
		else if (suitStr.equals("Club")) {
			suit = 4;
		}
		else if (suitStr.equals("Joker")) {
			suit = 5;
		}
		
		return suit;
	}
	
	/* JButton에서 각 카드에 대한 정보를 String으로 저장을 했기 때문에
	 * 해당 String에 대한 value 정보를 뽑아주는 내부 서비스
	 */
	private int analyzeValue(String analyzeStr) {
		String valueStr = analyzeStr.substring(0, analyzeStr.indexOf("of")-1);
		int value = 0;
		
		if (valueStr.equals("Ace")) {
			value = 1;
		}
		else if (valueStr.equals("Jack")) {
			value = 11;
		}
		else if (valueStr.equals("Queen")) {
			value = 12;
		}
		else if (valueStr.equals("King")) {
			value = 13;
		}
		else if (valueStr.equals("Joker[1]")) {
			value = 14;
		}
		else if (valueStr.equals("Joker[2]")) {
			value = 15;
		}
		else {
			value = Integer.parseInt(valueStr); 
		}
		
		return value;
	}
}