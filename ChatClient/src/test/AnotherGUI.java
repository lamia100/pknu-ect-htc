package test;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.CardLayout;
import javax.swing.JTabbedPane;

public class AnotherGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel p_server = null;
	private JPanel p_channel = null;
	private JLabel lb_serverIP = null;
	private JTextField tf_serverIP = null;
	private JLabel lb_serverPort = null;
	private JTextField tf_serverPort = null;
	private JButton bt_login = null;
	private JButton bt_logout = null;
	private JLabel lb_nickName = null;
	private JTextField tf_nickName = null;
	private JPanel p_info = null;
	private JPanel p_send = null;
	private JScrollPane sp_info = null;
	private JTextArea ta_info = null;
	private JPanel p_join = null;
	private JLabel lb_channel = null;
	private JTextField tf_channel = null;
	private JButton bt_join = null;
	private JButton bt_exit = null;
	private JLabel lb_send = null;
	private JTextField tf_send = null;
	private JButton bt_send = null;
	private JLabel lb_info = null;
	private JTabbedPane tp_channel = null;
	/**
	 * This is the default constructor
	 */
	public AnotherGUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 640);
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
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getP_server(), BorderLayout.NORTH);
			jContentPane.add(getP_channel(), BorderLayout.CENTER);
			jContentPane.add(getP_info(), BorderLayout.SOUTH);
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
			lb_nickName.setText("NickName :");
			lb_serverPort = new JLabel();
			lb_serverPort.setText("Server Port :");
			lb_serverIP = new JLabel();
			lb_serverIP.setText("Server IP :");
			p_server = new JPanel();
			p_server.setLayout(new FlowLayout());
			p_server.add(lb_serverIP, null);
			p_server.add(getTf_serverIP(), null);
			p_server.add(lb_serverPort, null);
			p_server.add(getTf_serverPort(), null);
			p_server.add(lb_nickName, null);
			p_server.add(getTf_nickName(), null);
			p_server.add(getBt_login(), null);
			p_server.add(getBt_logout(), null);
		}
		return p_server;
	}

	/**
	 * This method initializes p_channel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_channel() {
		if (p_channel == null) {
			p_channel = new JPanel();
			p_channel.setLayout(new BorderLayout());
			p_channel.add(getP_join(), BorderLayout.NORTH);
			p_channel.add(getTp_channel(), BorderLayout.CENTER);
			p_channel.add(getP_send(), BorderLayout.SOUTH);
		}
		return p_channel;
	}

	/**
	 * This method initializes tf_serverIP	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_serverIP() {
		if (tf_serverIP == null) {
			tf_serverIP = new JTextField();
			tf_serverIP.setColumns(10);
			tf_serverIP.setText("210.110.136.195");
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return bt_login;
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return bt_logout;
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
			tf_nickName.setColumns(8);
		}
		return tf_nickName;
	}

	/**
	 * This method initializes p_info	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_info() {
		if (p_info == null) {
			lb_info = new JLabel();
			lb_info.setText("Information");
			p_info = new JPanel();
			p_info.setLayout(new BorderLayout());
			p_info.add(lb_info, BorderLayout.NORTH);
			p_info.add(getSp_info(), BorderLayout.CENTER);
		}
		return p_info;
	}

	/**
	 * This method initializes p_send	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_send() {
		if (p_send == null) {
			lb_send = new JLabel();
			lb_send.setText("Send : ");
			p_send = new JPanel();
			p_send.setLayout(new BorderLayout());
			p_send.add(lb_send, BorderLayout.WEST);
			p_send.add(getTf_send(), BorderLayout.CENTER);
			p_send.add(getBt_send(), BorderLayout.EAST);
		}
		return p_send;
	}

	/**
	 * This method initializes sp_info	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_info() {
		if (sp_info == null) {
			sp_info = new JScrollPane();
			sp_info.setViewportView(getTa_info());
		}
		return sp_info;
	}

	/**
	 * This method initializes ta_info	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTa_info() {
		if (ta_info == null) {
			ta_info = new JTextArea();
			ta_info.setRows(8);
		}
		return ta_info;
	}

	/**
	 * This method initializes p_join	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_join() {
		if (p_join == null) {
			lb_channel = new JLabel();
			lb_channel.setText("Channel :");
			p_join = new JPanel();
			p_join.setLayout(new FlowLayout());
			p_join.add(lb_channel, null);
			p_join.add(getTf_channel(), null);
			p_join.add(getBt_join(), null);
			p_join.add(getBt_exit(), null);
		}
		return p_join;
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return bt_exit;
	}

	/**
	 * This method initializes tf_send	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_send() {
		if (tf_send == null) {
			tf_send = new JTextField();
			tf_send.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return tf_send;
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return bt_send;
	}

	/**
	 * This method initializes tp_channel	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTp_channel() {
		if (tp_channel == null) {
			tp_channel = new JTabbedPane();
		}
		return tp_channel;
	}

}
