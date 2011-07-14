package main;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.FileDialog;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import logic.StateTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;

import data.Const;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class GUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel p_stateTable = null;
	private JLabel lb_date = null;
	private JScrollPane sp_stateTable = null;
	private JTable tb_stateTable = null;
	private StateTable stateTable = null;
	private int refreshInterval = Const.DEFAULT_REFRESH_INTERVAL_SECOND;
	private DefaultTableCellRenderer centerTableCellRenderer = null;
	private JPanel p_menu = null;
	private JLabel lb_refreshTime = null;
	private JTextField tf_refreshTime = null;
	private JButton bt_save = null;
	private JButton bt_print = null;
	private JPanel p_state = null;
	private JPanel p_refresh = null;
	private JButton bt_refreshTimeInc = null;
	private JButton bt_refreshTimeDec = null;
	private static GUI thisClass = null;
	/**
	 * This method initializes p_stateTable	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_stateTable() {
		if (p_stateTable == null) {
			p_stateTable = new JPanel();
			p_stateTable.setLayout(new BorderLayout());
			p_stateTable.add(getP_state(), BorderLayout.NORTH);
			p_stateTable.add(getSp_stateTable(), BorderLayout.CENTER);
		}
		return p_stateTable;
	}

	/**
	 * This method initializes sp_stateTable	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_stateTable() {
		if (sp_stateTable == null) {
			sp_stateTable = new JScrollPane();
			sp_stateTable.setPreferredSize(new Dimension(453, 231));
			sp_stateTable.setViewportView(getTb_stateTable());
		}
		return sp_stateTable;
	}

	/**
	 * This method initializes tb_stateTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTb_stateTable() {
		if (tb_stateTable == null) {
			centerTableCellRenderer = new DefaultTableCellRenderer();
			centerTableCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
			TableColumn tc_rate = new TableColumn();
			tc_rate.setHeaderValue("이용율");
			tc_rate.setCellRenderer(centerTableCellRenderer);
			tc_rate.setModelIndex(4);
			TableColumn tc_remain = new TableColumn();
			tc_remain.setHeaderValue("잔여 좌석수");
			tc_remain.setCellRenderer(centerTableCellRenderer);
			tc_remain.setModelIndex(3);
			TableColumn tc_use = new TableColumn();
			tc_use.setHeaderValue("사용 좌석수");
			tc_use.setCellRenderer(centerTableCellRenderer);
			tc_use.setModelIndex(2);
			TableColumn tc_total = new TableColumn();
			tc_total.setHeaderValue("전체 좌석수");
			tc_total.setCellRenderer(centerTableCellRenderer);
			tc_total.setModelIndex(1);
			TableColumn tc_name = new TableColumn();
			tc_name.setHeaderValue("열람실명");
			tc_name.setMinWidth(150);
			tc_name.setCellRenderer(centerTableCellRenderer);
			tb_stateTable = new JTable();
			tb_stateTable.setAutoCreateColumnsFromModel(false);
			tb_stateTable.setRowHeight(30);
			tb_stateTable.addColumn(tc_name);
			tb_stateTable.addColumn(tc_total);
			tb_stateTable.addColumn(tc_use);
			tb_stateTable.addColumn(tc_remain);
			tb_stateTable.addColumn(tc_rate);
		}
		return tb_stateTable;
	}

	/**
	 * This method initializes p_menu	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_menu() {
		if (p_menu == null) {
			p_menu = new JPanel();
			p_menu.setLayout(new FlowLayout());
			p_menu.add(getBt_save(), null);
			p_menu.add(getBt_print(), null);
		}
		return p_menu;
	}

	/**
	 * This method initializes tf_refreshTime	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_refreshTime() {
		if (tf_refreshTime == null) {
			tf_refreshTime = new JTextField();
			tf_refreshTime.setColumns(2);
			tf_refreshTime.setEditable(false);
			tf_refreshTime.setText(Integer.toString(refreshInterval));
		}
		return tf_refreshTime;
	}

	/**
	 * This method initializes bt_save	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_save() {
		if (bt_save == null) {
			bt_save = new JButton();
			bt_save.setText("Save");
			bt_save.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					FileDialog fd_save = new FileDialog(thisClass, "파일로 저장", FileDialog.SAVE);
					fd_save.setVisible(true);
					String savePath = fd_save.getDirectory() + fd_save.getFile();
					
					if (savePath == null || savePath.equals("")) {
						JOptionPane.showMessageDialog(thisClass, "파일 경로를 선택하지 않으셨습니다.");
						return;
					}
					
					if (!saveFile(savePath)) {
						JOptionPane.showMessageDialog(thisClass, "I/O 에러로 인해 파일을 저장하지 못했습니다.\n"
								+ "디렉토리 권한이 제한되었을 수 있습니다.");
					}
				}
				
				public boolean saveFile(String savePath) {
					try {
						PrintWriter out = new PrintWriter(new FileWriter(savePath));
						
						out.println("부경대 도서관 좌석 현황");						
						out.println();
						
						out.print(stateTable.getStateTableByString());
						
						out.flush();
						out.close();
						
						return true;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					
					return false;
				}
			});
		}
		return bt_save;
	}

	/**
	 * This method initializes bt_print	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_print() {
		if (bt_print == null) {
			bt_print = new JButton();
			bt_print.setText("Print");
			bt_print.setEnabled(false);
		}
		return bt_print;
	}

	/**
	 * This method initializes p_state	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_state() {
		if (p_state == null) {
			lb_date = new JLabel();
			lb_date.setText("현재 시각");
			lb_date.setFont(new Font("Dialog", Font.BOLD, 14));
			p_state = new JPanel();
			p_state.setLayout(new BorderLayout());
			p_state.add(lb_date, BorderLayout.WEST);
			p_state.add(getP_refresh(), BorderLayout.EAST);
		}
		return p_state;
	}

	/**
	 * This method initializes p_refresh	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_refresh() {
		if (p_refresh == null) {
			lb_refreshTime = new JLabel();
			lb_refreshTime.setText("갱신시간(초) :");
			lb_refreshTime.setFont(new Font("Dialog", Font.BOLD, 14));
			p_refresh = new JPanel();
			p_refresh.setLayout(new FlowLayout());
			p_refresh.add(lb_refreshTime, null);
			p_refresh.add(getTf_refreshTime(), null);
			p_refresh.add(getBt_refreshTimeInc(), null);
			p_refresh.add(getBt_refreshTimeDec(), null);
		}
		return p_refresh;
	}

	/**
	 * This method initializes bt_refreshTimeInc	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_refreshTimeInc() {
		if (bt_refreshTimeInc == null) {
			bt_refreshTimeInc = new JButton();
			bt_refreshTimeInc.setText("▲");
			bt_refreshTimeInc.setFont(new Font("Dialog", Font.BOLD, 10));
			bt_refreshTimeInc.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					refreshInterval++;
					tf_refreshTime.setText(Integer.toString(refreshInterval));
				}
			});
		}
		return bt_refreshTimeInc;
	}

	/**
	 * This method initializes bt_refreshTimeDec	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_refreshTimeDec() {
		if (bt_refreshTimeDec == null) {
			bt_refreshTimeDec = new JButton();
			bt_refreshTimeDec.setText("▼");
			bt_refreshTimeDec.setFont(new Font("Dialog", Font.BOLD, 10));
			bt_refreshTimeDec.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (refreshInterval <= 10) {
						return;
					}
					
					refreshInterval--;
					tf_refreshTime.setText(Integer.toString(refreshInterval));
				}
			});
		}
		return bt_refreshTimeDec;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				thisClass = new GUI();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public GUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(640, 340);
		this.setContentPane(getJContentPane());
		this.setTitle("부경대 도서관 좌석 현황");
		
		stateTable = new StateTable();
		tb_stateTable.setModel(stateTable);
		lb_date.setText(stateTable.getDate());
				
		Thread refreshStateTable = new Thread(this);
		refreshStateTable.start();
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
			jContentPane.add(getP_stateTable(), BorderLayout.NORTH);
			jContentPane.add(getP_menu(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		
		while (true) {
			count++;
			
			try {
				Thread.sleep(1000);
				
				if (count >= refreshInterval) {
					stateTable.refreshStateTable();
					lb_date.setText(stateTable.getDate());
					getTb_stateTable().setModel(stateTable);
					
					count = 0;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
