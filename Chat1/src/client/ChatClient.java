package client;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import util.*;

import java.awt.GridBagLayout;
import java.awt.CardLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;

public class ChatClient extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel panelLobbyChat = null;
	private JTextField tfSend = null;
	private JPanel panelLogin = null;
	private JLabel lblID = null;
	private JButton btnLogin = null;
	private JScrollPane jScrollPane1 = null;
	private ChatManager cm = null; //  @jve:decl-index=0:
	private JTextField tfLogin = null;
	private JList jList = null;
	private JScrollPane jScrollPane = null;
	private JPanel panelRoomList = null;
	private JPanel panelRoomCtrl = null;
	private JButton btnCreateRoom = null;
	private JScrollPane jScrollPane2 = null;
	private JTable tableRoomList = null;
	private JPanel panelLobby = null;
	private JTabbedPane panelMain = null;
	public JTextArea taChatting = null;
	public String name = ""; //  @jve:decl-index=0:
	public DefaultListModel defaultListModel = null;
	private JButton btnRefresh = null;
	private HashMap<Integer, ChatRoomClient> roomTab = null; //  @jve:decl-index=0:
	private DefaultTableModel ListModel;

	/**
	 * This is the default constructor
	 */
	public ChatClient() {
		super();
		initialize();
	}

	public void addRoom(RoomData rd)
	{
		ChatRoomClient temp = new ChatRoomClient(cm, rd);
		panelMain.addTab(rd.title, temp);
		System.out.println(rd.number + "숫자");
		roomTab.put(rd.number, temp);
		System.out.println(roomTab.get(rd.number));
		//System.out.println("대화방 생성 번호="+rd.number);
	}

	public void appendChat(String msg, int room)
	{
		ChatRoomClient crc;
		if (room == 0) {
			getTaChatting().append(msg);
			taChatting.setCaretPosition(taChatting.getText().length());
		} else if (room > 0) {
			//system.out.println(roomTab.get(room));
			if ((crc = roomTab.get(room)) != null)
				;
			{
				JTextArea temp = crc.taChatting;
				temp.append(msg);
				temp.setCaretPosition(temp.getText().length());
			}
		}
	}

	private void logIn()
	{
		getPanelLogin().setVisible(false);
		getPanelMain().setVisible(true);
		if (tfLogin.getText().length() > 8) {
			name = tfLogin.getText().substring(0, 8);
		} else {
			name = tfLogin.getText();
		}
		cm = new ChatManager(getThis());
		new Thread(cm).start();
		//System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
		//addRoom(new RoomData(1,"123"));
	}

	public void setRoomList(Vector<RoomData> vrd)
	{
		ListModel.setRowCount(0);
		System.out.println(vrd.size());
		for (int i = 0; i < vrd.size(); i++) {
			ListModel.addRow(vrd.get(i).rowDate());
		}

	}

	public void addListUser(int room, UserData user)
	{
		DefaultListModel temp = null;
		if (room == 0) {
			//System.out.println("로비 사용자 목록");
			temp = defaultListModel;
		} else if (room > 0) {
			if (roomTab.get(room) != null) {
				temp = roomTab.get(room).defaultListModel;
				//System.out.println("대화방 목록 받음"+room);
			} else {
				return;
			}
		}
		System.out.println(user.toString());
		temp.addElement(user.toString());
	}

	public void removeListUser(int room, UserData user)
	{
		DefaultListModel temp = null;
		if (room == 0) {
			temp = this.defaultListModel;
		} else if (room > 0) {
			if (roomTab.get(room) != null) {
				temp = roomTab.get(room).defaultListModel;
			}
		}
		temp.removeElement(user.toString());
	}

	@SuppressWarnings("rawtypes")
	public void setList(Pack p)
	{
		int room = p.roomdata.number;
		//UserData user=p.user;
		DefaultListModel temp = null;
		if (room == 0) {
			temp = this.defaultListModel;
		} else if (room > 0) {
			if (roomTab.get(room) != null) {
				temp = roomTab.get(room).defaultListModel;
			} else {
				System.out.println(roomTab.get(roomTab));
				return;
			}
		}
		//temp.removeElement(user.toString());
		Vector tempVector = (Vector) p.data;

		for (int i = 0; i < tempVector.size(); i++) {
			temp.addElement(tempVector.get(i).toString());
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(744, 522);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		roomTab = new HashMap<Integer, ChatRoomClient>();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				if (cm != null)
					cm.sendLogOut();
				System.exit(0);
				//System.out.println("windowClosing()"); // TODO Auto-generated Event stub windowClosing()
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private ChatClient getThis()
	{
		return this;
	}

	private JPanel getJContentPane()
	{
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new CardLayout());
			jContentPane.add(getPanelLogin(), getPanelLogin().getName());
			jContentPane.add(getPanelMain(), getPanelMain().getName());
		}
		return jContentPane;
	}

	/**
	 * This method initializes panelLobbyChat	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelLobbyChat()
	{
		if (panelLobbyChat == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(2);
			borderLayout.setVgap(2);
			panelLobbyChat = new JPanel();
			panelLobbyChat.setName("panelLobby");
			panelLobbyChat.setVisible(true);
			panelLobbyChat.setPreferredSize(new Dimension(760, 200));
			panelLobbyChat.setLayout(borderLayout);
			panelLobbyChat.add(getJScrollPane(), BorderLayout.EAST);
			panelLobbyChat.add(getTfSend(), BorderLayout.SOUTH);
			panelLobbyChat.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return panelLobbyChat;
	}

	/**
	 * This method initializes tfSend	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfSend()
	{
		if (tfSend == null) {
			tfSend = new JTextField();
			tfSend.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					cm.sendMsg(new Pack(Pack.RoomMsg, 0, new UserData(cm), tfSend.getText()));
					tfSend.setText("");
					//System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return tfSend;
	}

	/**
	 * This method initializes taChatting	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaChatting()
	{
		if (taChatting == null) {
			taChatting = new JTextArea();
			taChatting.setEditable(false);
			taChatting.setLineWrap(true);
			taChatting.setWrapStyleWord(false);
		}
		return taChatting;
	}

	/**
	 * This method initializes panelLogin	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelLogin()
	{
		if (panelLogin == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.insets = new Insets(126, 1, 0, 60);
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 0, 149, 0);
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(5, 170, 4, 4);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.SOUTHEAST;
			gridBagConstraints.gridx = 0;
			lblID = new JLabel();
			lblID.setText("닉 네 임");
			lblID.setHorizontalTextPosition(SwingConstants.CENTER);
			lblID.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			lblID.setHorizontalAlignment(SwingConstants.CENTER);
			panelLogin = new JPanel();
			panelLogin.setLayout(new GridBagLayout());
			panelLogin.setName("panelLogin");
			panelLogin.add(lblID, gridBagConstraints);
			panelLogin.add(getTfLogin(), gridBagConstraints11);
			panelLogin.add(getBtnLogin(), gridBagConstraints2);
		}
		return panelLogin;
	}

	/**
	 * This method initializes btnLogin	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnLogin()
	{
		if (btnLogin == null) {
			btnLogin = new JButton();
			btnLogin.setText("입장");
			btnLogin.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					logIn();
				}
			});
		}
		return btnLogin;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1()
	{
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane1.setViewportView(getTaChatting());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes tfLogin	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfLogin()
	{
		if (tfLogin == null) {
			tfLogin = new JTextField();
			tfLogin.setColumns(0);
			tfLogin.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					logIn();
				}
			});
			tfLogin.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e)
				{
					if (tfLogin.getText().length() > 8)
						e.consume();
					//System.out.println("keyTyped()"); // TODO Auto-generated Event stub keyTyped()
				}
			});
		}
		return tfLogin;
	}

	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJList()
	{
		if (jList == null) {
			jList = new JList();
			defaultListModel = new DefaultListModel();
			jList.setModel(defaultListModel);
		}
		return jList;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane()
	{
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(110, 0));
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes panelRoomList	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelRoomList()
	{
		if (panelRoomList == null) {
			panelRoomList = new JPanel();
			panelRoomList.setLayout(new BorderLayout());
			panelRoomList.setPreferredSize(new Dimension(0, 0));
			panelRoomList.add(getPanelRoomCtrl(), BorderLayout.SOUTH);
			panelRoomList.add(getJScrollPane2(), BorderLayout.CENTER);
		}
		return panelRoomList;
	}

	/**
	 * This method initializes panelRoomCtrl	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelRoomCtrl()
	{
		if (panelRoomCtrl == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			flowLayout.setVgap(2);
			panelRoomCtrl = new JPanel();
			panelRoomCtrl.setLayout(flowLayout);
			panelRoomCtrl.add(getBtnRefresh(), null);
			panelRoomCtrl.add(getBtnCreateRoom(), null);
		}
		return panelRoomCtrl;
	}

	/**
	 * This method initializes btnCreateRoom	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCreateRoom()
	{
		if (btnCreateRoom == null) {
			btnCreateRoom = new JButton();
			btnCreateRoom.setText("방 만들기");
			btnCreateRoom.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					cm.sendRoomCreate("방제목");
					//System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return btnCreateRoom;
	}

	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane2()
	{
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getTableRoomList());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes tableRoomList	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTableRoomList()
	{
		if (tableRoomList == null) {
			ListModel = new DefaultTableModel() {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int i, int c)
				{
					return false;
				}
			};
			ListModel.setColumnCount(2);

			TableColumn colTitle = new TableColumn();
			colTitle.setPreferredWidth(700);
			colTitle.setHeaderValue("방 제목");
			colTitle.setModelIndex(1);
			TableColumn colNumber = new TableColumn();
			colNumber.setHeaderValue("방 번호");
			tableRoomList = new JTable();
			tableRoomList.setAutoCreateColumnsFromModel(false);
			tableRoomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableRoomList.setRowSelectionAllowed(true);
			tableRoomList.setCellSelectionEnabled(true);
			tableRoomList.setEnabled(true);
			tableRoomList.setShowGrid(true);
			tableRoomList.setColumnSelectionAllowed(false);
			tableRoomList.setName("");
			tableRoomList.setModel(ListModel);
			tableRoomList.addColumn(colNumber);
			tableRoomList.addColumn(colTitle);
			tableRoomList.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					int row;
					if (e.getClickCount() == 2) {
						row = tableRoomList.rowAtPoint(e.getPoint());
						int roomnum = Integer.parseInt((String) ListModel.getValueAt(row, 0));
						if (roomTab.get(roomnum) == null)
							cm.sendRoomIn(new RoomData(roomnum, (String) ListModel.getValueAt(row,
									1)));
						//System.out.println(Integer.parseInt((String) ListModel.getValueAt(row, 0)));
					}

					//row에서 roomData를 읽고 roomIn
					//System.out.println("mouseClicked()"); // TODO Auto-generated Event stub mouseClicked()
				}
			});
		}
		return tableRoomList;
	}

	/**
	 * This method initializes panelLobby	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelLobby()
	{
		if (panelLobby == null) {
			panelLobby = new JPanel();
			panelLobby.setLayout(new BorderLayout());
			panelLobby.setPreferredSize(new Dimension(760, 450));
			panelLobby.setName("panelLobby2");
			panelLobby.add(getPanelLobbyChat(), BorderLayout.SOUTH);
			panelLobby.add(getPanelRoomList(), BorderLayout.CENTER);
		}
		return panelLobby;
	}

	/**
	 * This method initializes panelMain	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getPanelMain()
	{
		if (panelMain == null) {
			panelMain = new JTabbedPane();
			panelMain.setName("jTabbedPane");
			panelMain.addTab("로비", null, getPanelLobby(), null);
		}
		return panelMain;
	}

	/**
	 * This method initializes btnRefresh	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRefresh()
	{
		if (btnRefresh == null) {
			btnRefresh = new JButton();
			btnRefresh.setText("새로고침");
			btnRefresh.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					cm.sendGetRoomList();
					//System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return btnRefresh;
	}

	public static void main(String args[])
	{
		new ChatClient().setVisible(true);
	}
} //  @jve:decl-index=0:visual-constraint="156,22"
