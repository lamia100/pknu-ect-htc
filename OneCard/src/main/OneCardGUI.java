package main;

import javax.swing.*;

import org.apache.log4j.*;

import data.Card;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import player.Player;
import java.awt.Dimension;
import java.awt.Rectangle;

public class OneCardGUI extends JFrame implements MouseListener,Player{

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(OneCardGUI.class);  //  @jve:decl-index=0:
	private JPanel jContentPane = null;
	private ArrayList<CardLabel> hand=null;  //  @jve:decl-index=0:
	private JPanel MainPanel = null;
	private JButton deckButton = null;
	private CardLabel openCardLabel = null;
	private JLabel userCardLabel = null;
	private Manager manager=null;
	private boolean isTurn=true;


	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				logger.info("게임을 종료합니다.");
				System.exit(0);
			}
		});
		this.pack();
		hand=new ArrayList<CardLabel>();
		manager=new Manager(this,3);
		enableCard();
	}
	public OneCardGUI() {
		super();
		initialize();
		//Card c=new Card(0,14);
		//card.setIcon(c.getFrontImage());
	}
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	private JPanel getMainPanel() {
		if (MainPanel == null) {
			userCardLabel = new JLabel();
			userCardLabel.setBounds(new Rectangle(31, 257, 175, 23));
			userCardLabel.setText("사용자가 가지고 있는 카드");
			MainPanel = new JPanel();
			MainPanel.setLayout(null);
			MainPanel.setPreferredSize(new Dimension(800, 560));
			MainPanel.add(getDeckButton(), null);
			MainPanel.add(getOpenCardLabel(), null);
			MainPanel.add(userCardLabel, null);
		}
		return MainPanel;
	}
	public CardLabel getOpenCardLabel()
	{
		if(openCardLabel==null){
			openCardLabel = new CardLabel(new Card(1,2));
			openCardLabel.setText("JLabel");
			openCardLabel.setSize(new Dimension(150, 200));
			openCardLabel.setLocation(new Point(420, 40));
		}
		return openCardLabel;
	}
	private JButton getDeckButton() {
		if (deckButton == null) {
			deckButton = new JButton();
			deckButton.setBounds(new Rectangle(225, 35, 160, 210));
			ImageIcon icon=new ImageIcon(getClass().getResource("/image/back_up.png"));
			icon.setImage(icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
			deckButton.setIcon(icon);
			icon=new ImageIcon(getClass().getResource("/image/back_dn.png"));
			icon.setImage(icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
			deckButton.setSelectedIcon(icon);
			icon=new ImageIcon(getClass().getResource("/image/back.png"));
			icon.setImage(icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
			deckButton.setRolloverIcon(icon);
			deckButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					manager.addCard(null);
				}
			});
		}
		return deckButton;
	}
	void repaintUserCard() {
		//System.out.println(hand.size());
		Collections.sort(hand);
		Rectangle r=new Rectangle(10+30*hand.size(),330,150,200);
		for(int i=hand.size()-1;i>=0;i--)
		{
			CardLabel temp=hand.get(i);
			if(MainPanel.getComponentZOrder(temp)!=-1)
				MainPanel.remove(temp);
			temp.setBounds(r);
			r.translate(-30, 0);
			MainPanel.add(temp,null);
			if(temp.getMouseListeners().length==0)
				temp.addMouseListener(this);
		}
		MainPanel.repaint();
		//enableCard();
	}
	class moveCard extends Thread
	{
		CardLabel cl;
		Point p;
		int count =10;
		public moveCard(CardLabel cl,Point p)
		{
			this.cl=cl;
			this.p=new Point((p.x-cl.getLocation().x)/count ,(p.y-cl.getLocation().y)/count);
		}
		
		public void run()
		{
			while(p.equals(cl.getLocation()))
			{
				Point p1=cl.getLocation();
				p1.translate(p.x, p.y);
				cl.setLocation(p1);
			}
			for(int i=0;i<count-1;i++)
			{
				Point p1=cl.getLocation();
				p1.translate(p.x, p.y);
				cl.setLocation(p1);
				try {
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			MainPanel.remove(cl);
			manager.addCardLabel(hand.remove(hand.indexOf(cl)));
			//repaintUserCard();
			//openCardLabel.setCard(cl.getCard());
			//repaint();
		}
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {

		Point p =((JLabel)e.getSource()).getLocation();
		p.y=300;
		((JLabel)e.getSource()).setLocation(p);

	}
	public void mouseExited(MouseEvent e) {
		Point p =((JLabel)e.getSource()).getLocation();
		p.y=330;
		((JLabel)e.getSource()).setLocation(p);

	}
	@Override
	public void mousePressed(MouseEvent e) {

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println(isTurn);
		if(isTurn&&((CardLabel)e.getSource()).isEnabled()){
			logger.info("유저가 GUI를 통해 명령을 내립니다.");
			
			new moveCard((CardLabel)e.getSource(),openCardLabel.getLocation()).start();
			//isTurn=false;
		}
	}
	private void enableCard()
	{
		Card temp_card;
		if(manager!=null){
			temp_card=manager.getOpenCard();
			int state=manager.getState();
			
			/*
			Iterator<CardLabel> ite=hand.iterator();
			while(ite.hasNext())
			{
				CardLabel temp=ite.next();
				if(state>1){
					temp.setEnabled(temp.getCard().isHighPriority(temp_card));
				}else{
					temp.setEnabled(temp.getCard().isSameRank(temp_card));
				}
			}
			*/
			
			for (CardLabel target : hand) {
				boolean result = false;
				
				if (state > 1) {
					result = target.getCard().isHighPriority(temp_card);
				}
				else {
					result = target.getCard().isSameRank(temp_card);
				}
				
				target.setEnabled(result);
				
				if (result) {
					logger.debug(target + "를 낼 수 있습니다.");
				}
				else {
					logger.debug(target + "는 낼 수 없습니다.");
				}
			}
		}
	}
	@Override
	public void addCard(Card c1) {
		hand.add(new CardLabel(c1));
		repaintUserCard();
	}

	@Override
	public boolean isEmpty() {
		return hand.isEmpty();
	}
	@Override
	public void setTurn(boolean isTurn) {
		this.isTurn=isTurn;
		if(isTurn){
			enableCard();
		}
	}
	public String toString()
	{
		return "유저";
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				OneCardGUI thisClass = new OneCardGUI();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
	@Override
	public int suitChange() {
		return JOptionPane.showOptionDialog(null, "Select a Card Suit",
				"Card Game WAR",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null,
				new String[]{"클로버","다이아몬드","하트","스페이드"}, "스페이드")+1;
	}
} 
