package test;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import client.Manager;
import javax.swing.border.SoftBevelBorder;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel p_server = null;
	private JTextField tf_serverIP = null;
	private JTextField tf_serverPort = null;
	private JLabel lb_serverIP = null;
	private JLabel lb_serverPort = null;
	private JLabel lb_nickName = null;
	private JTextField tf_nickName = null;
	private JPanel p_user = null;
	private JButton bt_login = null;
	private JLabel lb_channel = null;
	private JTextField tf_channel = null;
	private JButton bt_join = null;
	private JButton bt_exit = null;
	private JButton bt_logout = null;
	private JPanel p_chat = null;
	private JPanel p_msg = null;
	private JTextField tf_msg = null;
	private JButton bt_send = null;
	private JScrollPane sp_msg = null;
	private JTextArea ta_msg = null;
	private GUI gui;
	private Manager connectManager;
	private JScrollPane sp_info = null;
	private JTextArea ta_info = null;
	private JTextField tf_channelSend = null;
	private JLabel lb_channelSend = null;
	private JLabel lb_msg = null;
	
	/**
	 * This is the default constructor
	 */
	public GUI() {
		super();
		initialize();
		gui = this;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(640, 480);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			jContentPane.add(getP_server(), null);
			jContentPane.add(getP_user(), null);
			jContentPane.add(getP_chat(), null);
			jContentPane.add(getP_msg(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes p_server	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_server() {
		if (p_server == null) {
			lb_nickName = new JLabel();
			lb_nickName.setText("Nick Name :");
			lb_serverPort = new JLabel();
			lb_serverPort.setText("ServerPort :");
			lb_serverIP = new JLabel();
			lb_serverIP.setText("Server IP :");
			p_server = new JPanel();
			p_server.setLayout(new FlowLayout());
			p_server.add(lb_serverIP, null);
			p_server.add(getTf_serverIP(), null);
			p_server.add(lb_serverPort, null);
			p_server.add(getTf_serverPort(), null);
			p_server.add(getBt_login(), null);
			p_server.add(getBt_logout(), null);
		}
		return p_server;
	}

	/**
	 * This method initializes tf_serverIP	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_serverIP() {
		if (tf_serverIP == null) {
			tf_serverIP = new JTextField();
			tf_serverIP.setText("127.0.0.1");
			tf_serverIP.setColumns(10);
		}
		return tf_serverIP;
	}

	/**
	 * This method initializes tf_serverPort	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_serverPort() {
		if (tf_serverPort == null) {
			tf_serverPort = new JTextField();
			tf_serverPort.setText("41342");
			tf_serverPort.setColumns(5);
		}
		return tf_serverPort;
	}

	/**
	 * This method initializes tf_nickName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_nickName() {
		if (tf_nickName == null) {
			tf_nickName = new JTextField();
			tf_nickName.setText("inter6");
			tf_nickName.setColumns(10);
		}
		return tf_nickName;
	}

	/**
	 * This method initializes p_user	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_user() {
		if (p_user == null) {
			lb_channel = new JLabel();
			lb_channel.setText("Channel :");
			p_user = new JPanel();
			p_user.setLayout(new FlowLayout());
			p_user.add(lb_nickName, null);
			p_user.add(getTf_nickName(), null);
			p_user.add(lb_channel, null);
			p_user.add(getTf_channel(), null);
			p_user.add(getBt_join(), null);
			p_user.add(getBt_exit(), null);
		}
		return p_user;
	}

	/**
	 * This method initializes bt_login	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_login() {
		if (bt_login == null) {
			bt_login = new JButton();
			bt_login.setText("Login");
			bt_login.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("[GUI] Login 버튼을 눌렀습니다."); // TODO Auto-generated Event stub actionPerformed()
					
					String serverIP = getTf_serverIP().getText();
					int serverPort = Integer.parseInt(getTf_serverPort().getText());
					String nickName = getTf_nickName().getText();
					
					connectManager = new Manager(nickName, gui);
					
					boolean result = connectManager.connectServer(serverIP, serverPort);
					
					if (!result) {
						connectManager = null;
					}
				}
			});
		}
		return bt_login;
	}

	/**
	 * This method initializes tf_channel	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_channel() {
		if (tf_channel == null) {
			tf_channel = new JTextField();
			tf_channel.setText("0");
			tf_channel.setColumns(5);
		}
		return tf_channel;
	}

	/**
	 * This method initializes bt_join	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_join() {
		if (bt_join == null) {
			bt_join = new JButton();
			bt_join.setText("Join");
			bt_join.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("[GUI] Join 버튼을 눌렀습니다."); // TODO Auto-generated Event stub actionPerformed()
					
					if (connectManager != null) {
						String channel = getTf_channel().getText();
						
						boolean result = connectManager.joinChannel(channel);
						
						if (!result) {
							connectManager = null;
						}
					}
					else {
						dspInfo("Login을 하세요.");
					}
				}
			});
		}
		return bt_join;
	}

	/**
	 * This method initializes bt_exit	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_exit() {
		if (bt_exit == null) {
			bt_exit = new JButton();
			bt_exit.setText("Exit");
			bt_exit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("[GUI] Exit 버튼을 눌렀습니다."); // TODO Auto-generated Event stub actionPerformed()
					
					if (connectManager != null) {
						String channel = getTf_channel().getText();
						
						connectManager.exitChannel(channel);
					}
					else {
						dspInfo("Login을 하세요.");
					}
				}
			});
		}
		return bt_exit;
	}

	/**
	 * This method initializes bt_logout	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_logout() {
		if (bt_logout == null) {
			bt_logout = new JButton();
			bt_logout.setText("Logout");
			bt_logout.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("[GUI] Logout 버튼을 눌렀습니다."); // TODO Auto-generated Event stub actionPerformed()
					
					if (connectManager != null) {
						connectManager.disconnectServer();
						connectManager = null;
						
						dspInfo("서버 연결 해제");
					}
					else {
						dspInfo("Login을 하세요.");
					}
				}
			});
		}
		return bt_logout;
	}

	/**
	 * This method initializes p_chat	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_chat() {
		if (p_chat == null) {
			p_chat = new JPanel();
			p_chat.setLayout(new BorderLayout());
			p_chat.add(getSp_msg(), BorderLayout.CENTER);
			p_chat.add(getSp_info(), BorderLayout.NORTH);
		}
		return p_chat;
	}

	/**
	 * This method initializes p_msg	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_msg() {
		if (p_msg == null) {
			lb_msg = new JLabel();
			lb_msg.setText("MSG :");
			lb_channelSend = new JLabel();
			lb_channelSend.setText("Channel :");
			p_msg = new JPanel();
			p_msg.setLayout(new FlowLayout());
			p_msg.add(lb_channelSend, null);
			p_msg.add(getTf_channelSend(), null);
			p_msg.add(lb_msg, null);
			p_msg.add(getTf_msg(), null);
			p_msg.add(getBt_send(), null);
		}
		return p_msg;
	}

	/**
	 * This method initializes tf_msg	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_msg() {
		if (tf_msg == null) {
			tf_msg = new JTextField();
			tf_msg.setColumns(30);
			tf_msg.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					// TODO Auto-generated Event stub keyTyped()
					
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						System.out.println("[GUI] Msg 엔터를 눌렀습니다.");
						
						if (connectManager != null) {
							String channel = getTf_channelSend().getText();
							String msg = getTf_msg().getText();
							
							boolean result = connectManager.sendMsg(channel, msg);
							
							if (!result) {
								connectManager = null;
							}
						}
						else {
							dspInfo("Login을 하세요.");
						}
					}
				}
			});
		}
		return tf_msg;
	}

	/**
	 * This method initializes bt_send	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_send() {
		if (bt_send == null) {
			bt_send = new JButton();
			bt_send.setText("Send");
			bt_send.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("[GUI] Send 버튼을 눌렀습니다."); // TODO Auto-generated Event stub actionPerformed()
					
					if (connectManager != null) {
						String channel = getTf_channelSend().getText();
						String msg = getTf_msg().getText();
						
						boolean result = connectManager.sendMsg(channel, msg);
						
						if (!result) {
							connectManager = null;
						}
					}
					else {
						dspInfo("Login을 하세요.");
					}
				}
			});
		}
		return bt_send;
	}

	/**
	 * This method initializes sp_msg	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_msg() {
		if (sp_msg == null) {
			sp_msg = new JScrollPane();
			sp_msg.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			sp_msg.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sp_msg.setViewportView(getTa_msg());
		}
		return sp_msg;
	}

	/**
	 * This method initializes ta_msg	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTa_msg() {
		if (ta_msg == null) {
			ta_msg = new JTextArea();
			ta_msg.setText("Msg\n");
			ta_msg.setLineWrap(true);
		}
		return ta_msg;
	}

	/**
	 * This method initializes sp_info	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_info() {
		if (sp_info == null) {
			sp_info = new JScrollPane();
			sp_info.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			sp_info.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sp_info.setViewportView(getTa_info());
		}
		return sp_info;
	}

	public void dspMsg(String channel, String nickName, String msg) {
		sp_msg.getVerticalScrollBar().setValue(sp_msg.getVerticalScrollBar().getMaximum());
		ta_msg.append("(CH " + channel + ") [" + nickName + "] " + msg);
	}
	
	public void dspInfo(String info) {
		sp_info.getVerticalScrollBar().setValue(sp_info.getVerticalScrollBar().getMaximum());
		ta_info.append(info + "\n");
	}
	
	/**
	 * This method initializes ta_info	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTa_info() {
		if (ta_info == null) {
			ta_info = new JTextArea();
			ta_info.setColumns(0);
			ta_info.setText("Info\n");
			ta_info.setLineWrap(true);
			ta_info.setRows(5);
		}
		return ta_info;
	}

	/**
	 * This method initializes tf_channelSend	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_channelSend() {
		if (tf_channelSend == null) {
			tf_channelSend = new JTextField();
			tf_channelSend.setColumns(5);
			tf_channelSend.setText("0");
		}
		return tf_channelSend;
	}
}
