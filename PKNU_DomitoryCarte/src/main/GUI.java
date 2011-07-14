package main;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import logic.ParseCarte;
import data.MonthlyCarte;
import data.TodayCarte;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel p_main = null;
	private JPanel p_select = null;
	private JPanel p_carte = null;
	private JRadioButton rb_carteD = null;
	private JRadioButton rb_carteY = null;
	private JPanel p_todayCarte = null;
	private JPanel p_monthlyCarte = null;
	private JTable tb_todayCarte = null;
	private JScrollPane sp_monthlyCarte = null;
	private JTable tb_monthlyCarte = null;
	private JLabel lb_date = null;
	private JLabel lb_monthly = null;
	private ButtonGroup bg_select = new ButtonGroup();  //  @jve:decl-index=0:
	private DefaultTableCellRenderer CenterTableCellRenderer = null;
	private JPanel p_menu = null;
	private JButton bt_save = null;
	private JButton bt_print = null;
	private static GUI thisClass = null;
	private TodayCarte todayCarte = null;
	private MonthlyCarte monthlyCarte = null;
	/**
	 * This method initializes p_main	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_main() {
		if (p_main == null) {
			p_main = new JPanel();
			p_main.setLayout(new BorderLayout());
			p_main.add(getP_select(), BorderLayout.NORTH);
			p_main.add(getP_carte(), BorderLayout.CENTER);
			p_main.add(getP_menu(), BorderLayout.SOUTH);
		}
		return p_main;
	}

	/**
	 * This method initializes p_select	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_select() {
		if (p_select == null) {
			p_select = new JPanel();
			p_select.setLayout(new FlowLayout());
			p_select.add(getRb_carteD(), null);
			p_select.add(getRb_carteY(), null);
		}
		return p_select;
	}

	/**
	 * This method initializes p_carte	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_carte() {
		if (p_carte == null) {
			p_carte = new JPanel();
			p_carte.setLayout(new BorderLayout());
			p_carte.add(getP_todayCarte(), BorderLayout.NORTH);
			p_carte.add(getP_monthlyCarte(), BorderLayout.CENTER);
		}
		return p_carte;
	}

	/**
	 * This method initializes rb_carteD	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_carteD() {
		if (rb_carteD == null) {
			rb_carteD = new JRadioButton();
			rb_carteD.setText("대연(세종관)");
			rb_carteD.setSelected(true);
			rb_carteD.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// 오늘 식단 시작
					todayCarte = ParseCarte.getTodayCarteD();
					lb_date.setText("오늘 (" + todayCarte.getDate() + ") 식단");
					
					tb_todayCarte.setModel(todayCarte);
					// 오늘 식단 끝
					
					// 주간 식단 시작
					monthlyCarte = ParseCarte.getMonthlyCarteD();
					lb_monthly.setText("이번주 (" + monthlyCarte.getMonth() + ") 식단");
					
					tb_monthlyCarte.setModel(monthlyCarte);
					// 주간 식단 끝
				}
			});
			
			bg_select.add(rb_carteD);
		}
		return rb_carteD;
	}

	/**
	 * This method initializes rb_carteY	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_carteY() {
		if (rb_carteY == null) {
			rb_carteY = new JRadioButton();
			rb_carteY.setText("용당(광개토관)");
			rb_carteY.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// 오늘 식단 시작
					todayCarte = ParseCarte.getTodayCarteY();
					lb_date.setText("오늘 (" + todayCarte.getDate() + ") 식단");
					
					tb_todayCarte.setModel(todayCarte);
					// 오늘 식단 끝
					
					// 주간 식단 시작
					monthlyCarte = ParseCarte.getMonthlyCarteY();
					lb_monthly.setText("이번주 (" + monthlyCarte.getMonth() + ") 식단");
					
					tb_monthlyCarte.setModel(monthlyCarte);
					// 주간 식단 끝
				}
			});
			
			bg_select.add(rb_carteY);
		}
		return rb_carteY;
	}

	/**
	 * This method initializes p_todayCarte	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_todayCarte() {
		if (p_todayCarte == null) {
			lb_date = new JLabel();
			lb_date.setText("오늘 식단");
			lb_date.setFont(new Font("Dialog", Font.BOLD, 14));
			p_todayCarte = new JPanel();
			p_todayCarte.setLayout(new BorderLayout());
			p_todayCarte.add(lb_date, BorderLayout.NORTH);
			p_todayCarte.add(getTb_todayCarte(), BorderLayout.CENTER);
		}
		return p_todayCarte;
	}

	/**
	 * This method initializes p_monthlyCarte	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_monthlyCarte() {
		if (p_monthlyCarte == null) {
			lb_monthly = new JLabel();
			lb_monthly.setText("이번주 식단");
			lb_monthly.setFont(new Font("Dialog", Font.BOLD, 14));
			p_monthlyCarte = new JPanel();
			p_monthlyCarte.setLayout(new BorderLayout());
			p_monthlyCarte.add(lb_monthly, BorderLayout.NORTH);
			p_monthlyCarte.add(getSp_monthlyCarte(), BorderLayout.CENTER);
		}
		return p_monthlyCarte;
	}

	/**
	 * This method initializes tb_todayCarte	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTb_todayCarte() {
		if (tb_todayCarte == null) {
			TableColumn tc_today2 = new TableColumn();
			tc_today2.setModelIndex(1);
			tc_today2.setCellRenderer(CenterTableCellRenderer);
			TableColumn tc_today1 = new TableColumn();
			tc_today1.setMaxWidth(50);
			tc_today1.setCellRenderer(CenterTableCellRenderer);
			tb_todayCarte = new JTable();
			tb_todayCarte.setAutoCreateColumnsFromModel(false);
			tb_todayCarte.setRowHeight(30);
			tb_todayCarte.addColumn(tc_today1);
			tb_todayCarte.addColumn(tc_today2);
		}
		return tb_todayCarte;
	}

	/**
	 * This method initializes sp_monthlyCarte	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_monthlyCarte() {
		if (sp_monthlyCarte == null) {
			sp_monthlyCarte = new JScrollPane();
			sp_monthlyCarte.setViewportView(getTb_monthlyCarte());
		}
		return sp_monthlyCarte;
	}

	/**
	 * This method initializes tb_monthlyCarte	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTb_monthlyCarte() {
		if (tb_monthlyCarte == null) {
			TableColumn tc_monthly2 = new TableColumn();
			tc_monthly2.setModelIndex(1);
			tc_monthly2.setCellRenderer(CenterTableCellRenderer);
			TableColumn tc_monthly1 = new TableColumn();
			tc_monthly1.setMaxWidth(50);
			tc_monthly1.setCellRenderer(CenterTableCellRenderer);
			tb_monthlyCarte = new JTable();
			tb_monthlyCarte.setAutoCreateColumnsFromModel(false);
			tb_monthlyCarte.setRowHeight(30);
			tb_monthlyCarte.addColumn(tc_monthly1);
			tb_monthlyCarte.addColumn(tc_monthly2);
			tb_monthlyCarte.setTableHeader(null);
		}
		return tb_monthlyCarte;
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
						
						if (bg_select.getSelection() == getRb_carteD().getModel()) {
							out.println("대연(세종관) 기숙사 식단표");
						}
						else {
							out.println("용당(광개토관) 기숙사 식단표");
						}
						
						out.println();
						
						out.println("오늘 (" + todayCarte.getDate() + ") 식단");
						out.println(todayCarte.getCarteByString());
						
						out.println();
						
						out.println("이번주 (" + monthlyCarte.getMonth() + ") 식단");
						out.print(monthlyCarte.getCarteByString());
						
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
			bt_print.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return bt_print;
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
		
		getRb_carteD().doClick();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		CenterTableCellRenderer = new DefaultTableCellRenderer();
		CenterTableCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		
		this.setSize(640, 820);
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
			jContentPane.add(getP_main(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}
