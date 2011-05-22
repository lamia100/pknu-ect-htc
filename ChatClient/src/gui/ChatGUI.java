package gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane sp_chat = null;
	private JTextArea ta_chat = null;
	private String channel = null;
	/**
	 * This is the default constructor
	 */
	public ChatGUI(String channel, String nickName) {
		super();
		initialize();
		this.channel = channel;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getSp_chat(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes sp_chat	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_chat() {
		if (sp_chat == null) {
			sp_chat = new JScrollPane();
			sp_chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sp_chat.setViewportView(getTa_chat());
		}
		return sp_chat;
	}

	/**
	 * This method initializes ta_chat	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTa_chat() {
		if (ta_chat == null) {
			ta_chat = new JTextArea();
			ta_chat.setLineWrap(true);
		}
		return ta_chat;
	}
	
	public void dspMsg(String nickName, String msg) {
		sp_chat.getVerticalScrollBar().setValue(sp_chat.getVerticalScrollBar().getMaximum());
		ta_chat.append("(CH " + channel + ") [" + nickName + "] " + msg);
	}
	
	public String getChannel() {
		return channel;
	}

}
