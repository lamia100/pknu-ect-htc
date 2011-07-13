package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import util.Logger;

import msg.TYPE;
import msg.sub.Script;
import msg.sub.Send;
import client.ClientManager;

public class ChannelPanel extends JPanel {
	static ClientManager manager;
	static {
		manager = ClientManager.getInstance();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextField textField;
	private JList userlist;
	private JTextArea textArea;
	private String channel;
	private String name;
	
	/**
	 * Create the panel.
	 */
	public ChannelPanel() {
		initialize();
	}
	
	public ChannelPanel(String channel, String name) {
		this.channel = channel;
		this.name = name;
		initialize();
	}
	
	private void initialize() {
		
		setLayout(new BorderLayout(2, 2));
		add(getPanel(), BorderLayout.SOUTH);
		add(getScrollPane(), BorderLayout.CENTER);
		add(getList(), BorderLayout.EAST);
	}
	
	public void append(String str) {
		getTextArea().append(str);
	}
	
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getTextField());
		}
		return panel;
	}
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextArea());
		}
		return scrollPane;
	}
	long time;
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						String line = getTextField().getText();
						if (line.charAt(0) == '/') {
							if ("/bench".equals(line)) {
								time=System.currentTimeMillis();
								//getTextField().setEditable(false);
								manager.upload(new Script(TYPE.CAST_UNI,channel,manager.getName(),"bench\n"));
							}
						} else {
							manager.upload(new Send(channel, name, line));
							getTextField().setText("");
						}
					}
				}
			});
			textField.setColumns(10);
		}
		return textField;
	}
	
	private JList getList() {
		if (userlist == null) {
			userlist = new JList();
			userlist.setBorder(UIManager.getBorder("ComboBox.border"));
			userlist.setPreferredSize(new Dimension(100, 0));
		}
		return userlist;
	}
	
	private JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
		}
		return textArea;
	}
	
	
}
