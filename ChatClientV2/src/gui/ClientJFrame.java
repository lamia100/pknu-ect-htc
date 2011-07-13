package gui;

import static util.Logger.log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import msg.Definition;
import msg.sub.Join;
import util.User;
import client.Client;
import client.ClientChannel;
import client.ClientManager;
import java.awt.Point;

public class ClientJFrame extends JFrame implements ClientGUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel_1;
	private JLabel lblServerIP;
	private JTextField tfServerIP;
	private JLabel lblNickName;
	private JTextField tfNickName;
	private JButton btnLogin;
	private JTabbedPane tabbedPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientJFrame frame = new ClientJFrame();
					frame.setVisible(true);
					frame.setLocation(0, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public ClientJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanel_1(), BorderLayout.NORTH);
		contentPane.add(getTabbedPane(), BorderLayout.CENTER);
		
	}

	@Override
	public void append(String str) {
		// TODO Auto-generated method stub
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.add(getLblServerIP());
			panel_1.add(getTfServerIP());
			panel_1.add(getLblNickName());
			panel_1.add(getTfNickName());
			panel_1.add(getBtnLogin());
		}
		return panel_1;
	}
	private JLabel getLblServerIP() {
		if (lblServerIP == null) {
			lblServerIP = new JLabel("서버주소");
		}
		return lblServerIP;
	}
	private JTextField getTfServerIP() {
		if (tfServerIP == null) {
			tfServerIP = new JTextField();
			tfServerIP.setText("192.168.189.136");
			tfServerIP.setColumns(10);
		}
		return tfServerIP;
	}
	private JLabel getLblNickName() {
		if (lblNickName == null) {
			lblNickName = new JLabel("닉네임");
		}
		return lblNickName;
	}
	private JTextField getTfNickName() {
		if (tfNickName == null) {
			tfNickName = new JTextField();
			tfNickName.setColumns(10);
			tfNickName.setText(Integer.toString(new Random(System.currentTimeMillis()).nextInt(16777216),16));
		}
		return tfNickName;
	}
	private JButton getBtnLogin() {
		if (btnLogin == null) {
			btnLogin = new JButton("로그인");
			btnLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					getTfNickName().setEditable(false);
					getTfServerIP().setEditable(false);
					Socket socket;
					try {
						socket = new Socket(tfServerIP.getText(), Definition.DEFAULT_PORT);
						User user =new User(socket);
						new Thread(user).start();
						log("Client","서버에 접속성공" + user.getIP() + ":" + socket.getPort());
						append("Client");
						append("서버에 접속성공" + user.getIP() + ":" + socket.getPort());
						ClientManager.getInstance().setName(tfNickName.getText());
						ClientManager.getInstance().setServer(user);
						new Thread(ClientManager.getInstance()).start();
						user.send(new Join(Definition.ALL,tfNickName.getText()));
						
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.exit(1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.exit(1);
					}
				}
			});
		}
		return btnLogin;
	}
	
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.add("asdasdasd",new ManagerPanel());
		}
		return tabbedPane;
	}
	ClientManager manager = ClientManager.getInstance();
	class ManagerPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JScrollPane scrollPane;
		private JPanel panel;
		private JTextArea textArea;
		private JTextField textField;
		
		/**
		 * Create the panel.
		 */
		public ManagerPanel() {
			setLayout(new BorderLayout(0, 2));
			add(getScrollPane(), BorderLayout.CENTER);
			add(getPanel(), BorderLayout.SOUTH);
			
		}
		
		private JScrollPane getScrollPane() {
			if (scrollPane == null) {
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(getTextArea());
			}
			return scrollPane;
		}
		
		private JPanel getPanel() {
			if (panel == null) {
				panel = new JPanel();
				panel.setLayout(new BorderLayout(0, 0));
				panel.add(getTextField(), BorderLayout.CENTER);
			}
			return panel;
		}
		
		private JTextArea getTextArea() {
			if (textArea == null) {
				textArea = new JTextArea();
			}
			return textArea;
		}
		
		private JTextField getTextField() {
			if (textField == null) {
				textField = new JTextField();
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							command(getTextField().getText());
							getTextField().setText("");
						}
					}
				});
				textField.setColumns(10);
			}
			return textField;
		}
		
		private void command(String cmd) {
			cmd = cmd.trim();
			if(ClientManager.getInstance().isClosed()){
				return;
			}
			if (cmd.charAt(0) == '/') {
				int index = cmd.indexOf(' ');
				String op = cmd.substring(1, index);
				if ("join".equals(op) || "j".equals(op)) {
					String channelName = cmd.substring(index + 1);
					if (manager.getChannel(channelName)==null) {
						ClientChannel channel=new ClientChannel(channelName);
						ChannelPanel panel=new ChannelPanel(channelName, manager.getName());
						channel.setPanel(panel);
						getTabbedPane().add(channelName,panel);
						manager.add(channel);
						manager.upload(new Join(channelName,manager.getName()));
						getTabbedPane().setSelectedIndex(getTabbedPane().indexOfTab(channelName));
					}
				}
			} else {
				
			}
		}
	}

}
