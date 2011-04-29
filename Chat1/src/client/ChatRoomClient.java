package client;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import util.*;
import javax.swing.DefaultListModel;

public class ChatRoomClient extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JList userList = null;
	private JTextField tfSend = null;
	private JScrollPane jScrollPane1 = null;
	public JTextArea taChatting = null;
	public DefaultListModel defaultListModel=null;
	private ChatManager cm=null;
	private RoomData rd=null;
	/**
	 * This is the default constructor
	 */
	public ChatRoomClient(ChatManager cm,RoomData rd) {
		super();
		this.cm=cm;
		this.rd=rd;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(2);
		borderLayout.setVgap(2);
		this.setName(Integer.toString(rd.number));
		this.setLayout(borderLayout);
		this.setSize(502, 294);
		this.add(getJScrollPane(), BorderLayout.EAST);
		this.add(getTfSend(), BorderLayout.SOUTH);
		this.add(getJScrollPane1(), BorderLayout.CENTER);
	}

	/**
       * This method initializes jScrollPane	
       * 	
       * @return javax.swing.JScrollPane	
       */
      private JScrollPane getJScrollPane() {
      	if (jScrollPane == null) {
      		jScrollPane = new JScrollPane();
      		jScrollPane.setPreferredSize(new Dimension(110, 130));
      		jScrollPane.setViewportView(getUserList());
      	}
      	return jScrollPane;
      }

	/**
       * This method initializes userList	
       * 	
       * @return javax.swing.JList	
       */
      private JList getUserList() {
      	if (userList == null) {
      		defaultListModel=new DefaultListModel();
      		userList = new JList();
      		userList.setModel(defaultListModel);
      	}
      	return userList;
      }

	/**
       * This method initializes tfSend	
       * 	
       * @return javax.swing.JTextField	
       */
      private JTextField getTfSend() {
      	if (tfSend == null) {
      		tfSend = new JTextField();
      		tfSend.addActionListener(new java.awt.event.ActionListener() {
      			public void actionPerformed(java.awt.event.ActionEvent e) {
      				cm.sendMsg(new Pack(Pack.RoomMsg,rd.number,new UserData(cm),tfSend.getText()));
      				tfSend.setText("");
      				System.out.println("actionPerformed()"+rd.number); // TODO Auto-generated Event stub actionPerformed()
      			}
      		});
      	}
      	return tfSend;
      }

	/**
       * This method initializes jScrollPane1	
       * 	
       * @return javax.swing.JScrollPane	
       */
      private JScrollPane getJScrollPane1() {
      	if (jScrollPane1 == null) {
      		jScrollPane1 = new JScrollPane();
      		jScrollPane1.setViewportView(getJTextArea());
      	}
      	return jScrollPane1;
      }

	/**
       * This method initializes jTextArea	
       * 	
       * @return javax.swing.JTextArea	
       */
      private JTextArea getJTextArea() {
      	if (taChatting == null) {
      		taChatting = new JTextArea();
      	}
      	return taChatting;
      }
      public String toString()
      {
      	return rd.title;
      }
}  //  @jve:decl-index=0:visual-constraint="164,46"
