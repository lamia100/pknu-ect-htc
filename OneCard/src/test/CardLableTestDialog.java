package test;

import javax.swing.JDialog;

import main.CardLabel;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class CardLableTestDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JButton jButtonYES = null;
	private JButton jButtonNO = null;
	public int result=0;
	private JPanel jPanel2 = null;
	private JLabel jLabel = null;
	private JLabel jLabel2 = null;
	public CardLableTestDialog(CardLabel cardLabel ) {
		// TODO Auto-generated constructor stub
		jLabel=cardLabel;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(218, 298));
        this.setModal(true);
        this.setContentPane(getJPanel());
		
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			//jLabel = new JLabel();
			//jLabel.setText("JLabel");
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(jLabel, BorderLayout.CENTER);
			jPanel.add(getJPanel1(), BorderLayout.SOUTH);
			jPanel.add(getJPanel2(), BorderLayout.CENTER);
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());
			jPanel1.add(getJButtonYES(), null);
			jPanel1.add(getJButtonNO(), null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jButtonYES	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonYES() {
		if (jButtonYES == null) {
			jButtonYES = new JButton();
			jButtonYES.setText("예");
			jButtonYES.setPreferredSize(new Dimension(71, 25));
			jButtonYES.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					result=1;
					dispose();
				}
			});
		}
		return jButtonYES;
	}
	/**
	 * This method initializes jButtonNO	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonNO() {
		if (jButtonNO == null) {
			jButtonNO = new JButton();
			jButtonNO.setText("아니오");
			jButtonNO.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					result=2;
					dispose();
				}
			});
		}
		return jButtonNO;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
//			jLabel = new JLabel();
//			jLabel.setText("JLabel");
			jLabel2 = new JLabel();
			jLabel2.setText(" ");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());
			jPanel2.add(jLabel, null);
			jPanel2.add(jLabel2, null);
			jLabel2.setText(((CardLabel)jLabel).toString() +"가 맞습니까?");
		}
		return jPanel2;
	}
	
}  //  @jve:decl-index=0:visual-constraint="38,122"
