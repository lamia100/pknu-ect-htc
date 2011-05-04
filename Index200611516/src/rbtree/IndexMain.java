package rbtree;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class IndexMain extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanelControl = null;
	private JButton jButtonFileOpen = null;
	private JList jList = null;
	private JTextArea jTextArea = null;
	private JScrollPane jScrollPane = null;
	private JPanel jPanel1 = null;
	private JTextField jTextField = null;
	private Vector<IndexWord> list = null;
	private JPanel jPanel2 = null;
	
	/**
	 * This is the default constructor
	 */
	public IndexMain() {
		super();
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(630, 454);
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
			BorderLayout borderLayout2 = new BorderLayout();
			borderLayout2.setHgap(2);
			borderLayout2.setVgap(2);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout2);
			jContentPane.add(getJPanelControl(), BorderLayout.SOUTH);
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(2);
			borderLayout.setVgap(5);
			jPanel = new JPanel();
			jPanel.setLayout(borderLayout);
			jPanel.add(getJTextArea(), BorderLayout.CENTER);
			jPanel.add(getJPanel1(), BorderLayout.WEST);
			jPanel.add(getJPanel2(), BorderLayout.NORTH);
		}
		return jPanel;
	}
	
	/**
	 * This method initializes jPanelControl
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelControl() {
		if (jPanelControl == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			jPanelControl = new JPanel();
			jPanelControl.setLayout(flowLayout);
			jPanelControl.add(getJButtonFileOpen(), null);
		}
		return jPanelControl;
	}
	
	/**
	 * This method initializes jButtonFileOpen
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonFileOpen() {
		if (jButtonFileOpen == null) {
			jButtonFileOpen = new JButton();
			jButtonFileOpen.setText("파일 열기");
			jButtonFileOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					JFileChooser fileopen = new JFileChooser(new File("."));
					int rect = fileopen.showDialog(jContentPane, "Open File");
					if (rect == JFileChooser.APPROVE_OPTION) {
						File file = fileopen.getSelectedFile();
						Indexer indexer = new Indexer();
						indexer.setTextFile(file);
						indexer.start();
						indexer.removeUnnecessaryNodes();
						list = new Vector<IndexWord>(indexer.getList());
						getJList().setListData(list);
						
					}
					
				}
			});
		}
		return jButtonFileOpen;
	}
	
	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
			jList.setFixedCellHeight(18);
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						IndexWord word = (IndexWord) getJList().getSelectedValue();
						System.out.println(getJList().getSelectedIndex());
						getJTextArea().setText(word.toString() + "\n");
						getJTextArea().append("count : "+ word.getCount()+"\nlines : ");
						ArrayList<Integer> lines = word.getLine();
						for (int i = 0; i < lines.size() - 1; i++) {  
							getJTextArea().append(lines.get(i) + ", ");
						}
						getJTextArea().append(lines.get(lines.size() - 1).toString());
					}
				}
			});
		}
		return jList;
	}
	
	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setEditable(false);
			jTextArea.setLineWrap(true);
		}
		return jTextArea;
	}
	
	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(200, 131));
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPane.setEnabled(false);
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}
	
	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setVgap(3);
			borderLayout1.setHgap(3);
			jPanel1 = new JPanel();
			jPanel1.setLayout(borderLayout1);
			jPanel1.add(getJScrollPane(), BorderLayout.CENTER);
			jPanel1.add(getJTextField(), BorderLayout.NORTH);
		}
		return jPanel1;
	}
	
	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setColumns(20);
			jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					System.out.println("keyTyped()"); // TODO Auto-generated Event stub keyTyped()
					if (list == null)
						return;
					int c = getJTextField().getCaretPosition();
					String s = getJTextField().getText();
					s = s.substring(0, c) + e.getKeyChar() + s.substring(c);
					int index = firstIndexOf(s);
					
					getJList().setSelectedIndex(index);
					getJScrollPane().getVerticalScrollBar().setValue(getJList().getFixedCellHeight() * index);
					
				}
			});
		}
		return jTextField;
	}
	
	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
		}
		return jPanel2;
	}
	
	private int firstIndexOf(String str) {
		int s = 0;
		int e = list.size() - 1;
		int m = 0;
		int cmp = 0;
		while (s <= e) {
			m = (s + e) / 2;
			cmp = list.get(m).getWord().compareToIgnoreCase(str);
			if (cmp >= 0) {
				e = m - 1;
			} else if (cmp < 0) {
				s = m + 1;
			}
		}
		cmp = list.get(m).getWord().compareToIgnoreCase(str);
		if (cmp >= 0)
			return m;
		else
			return m + 1;
	}
	public static void main(String [] args) {
		new IndexMain().setVisible(true);
	}
} //  @jve:decl-index=0:visual-constraint="10,10"
