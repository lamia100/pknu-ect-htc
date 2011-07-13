package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import logic.ParseCarte;
import data.MonthlyCarte;
import data.TodayCarte;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JToolBar tb_main = null;
	private JMenuItem mi_save = null;
	private JMenuItem mi_print = null;
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
	private TableColumn tableColumn = null;  //  @jve:decl-index=0:visual-constraint="978,416"

	/**
	 * This method initializes tb_main	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getTb_main() {
		if (tb_main == null) {
			tb_main = new JToolBar();
			tb_main.add(getMi_save());
			tb_main.add(getMi_print());
		}
		return tb_main;
	}

	/**
	 * This method initializes mi_save	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMi_save() {
		if (mi_save == null) {
			mi_save = new JMenuItem();
			mi_save.setText("Save");
		}
		return mi_save;
	}

	/**
	 * This method initializes mi_print	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMi_print() {
		if (mi_print == null) {
			mi_print = new JMenuItem();
			mi_print.setText("Print");
		}
		return mi_print;
	}

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
			rb_carteD.setText("�뿬(������)");
			rb_carteD.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					
					// ���� �Ĵ� ����
					TodayCarte todayCarte = ParseCarte.getTodayCarteD();
					lb_date.setText("���� (" + todayCarte.getDate() + ") �Ĵ�");
					
					tb_todayCarte.setModel(todayCarte);
					// ���� �Ĵ� ��
					
					// �ְ� �Ĵ� ����
					MonthlyCarte monthlyCarte = ParseCarte.getMonthlyCarteD();
					lb_monthly.setText("�̹��� (" + monthlyCarte.getMonth() + ") �Ĵ�");
					
					tb_monthlyCarte.setModel(monthlyCarte);
					// �ְ� �Ĵ� ��
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
			rb_carteY.setText("���(�������)");
			rb_carteY.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					
					// ���� �Ĵ� ����
					TodayCarte todayCarte = ParseCarte.getTodayCarteY();
					lb_date.setText("���� (" + todayCarte.getDate() + ") �Ĵ�");
					
					tb_todayCarte.setModel(todayCarte);
					// ���� �Ĵ� ��
					
					// �ְ� �Ĵ� ����
					MonthlyCarte monthlyCarte = ParseCarte.getMonthlyCarteY();
					lb_monthly.setText("�̹��� (" + monthlyCarte.getMonth() + ") �Ĵ�");
					
					tb_monthlyCarte.setModel(monthlyCarte);
					// �ְ� �Ĵ� ��
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
			lb_date.setText("���� �Ĵ�");
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
			lb_monthly.setText("�̹��� �Ĵ�");
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
			tc_today2.setPreferredWidth(500);
			TableColumn tc_today1 = new TableColumn();
			tc_today1.setPreferredWidth(10);
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
			TableColumn tableColumn2 = new TableColumn();
			tableColumn2.setModelIndex(1);
			tableColumn2.setPreferredWidth(500);
			TableColumn tableColumn1 = new TableColumn();
			tableColumn1.setPreferredWidth(10);
			tb_monthlyCarte = new JTable();
			tb_monthlyCarte.setAutoCreateColumnsFromModel(false);
			tb_monthlyCarte.setRowHeight(30);
			tb_monthlyCarte.addColumn(tableColumn1);
			tb_monthlyCarte.addColumn(tableColumn2);
		}
		return tb_monthlyCarte;
	}

	/**
	 * This method initializes tableColumn	
	 * 	
	 * @return javax.swing.table.TableColumn	
	 */
	@SuppressWarnings("unused")
	private TableColumn getTableColumn() {
		if (tableColumn == null) {
			tableColumn = new TableColumn();
		}
		return tableColumn;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI thisClass = new GUI();
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
			jContentPane.add(getTb_main(), BorderLayout.NORTH);
			jContentPane.add(getP_main(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}
