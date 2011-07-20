package main;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import logic.ParseTable;
import util.Const;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowOpeningEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowWillOpenEvent;
import data.BookTable;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.awt.Font;
import java.util.Stack;
import java.awt.GridBagLayout;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel p_searchWord = null;
	private JPanel p_select = null;
	private JRadioButton rb_total = null;
	private JRadioButton rb_title = null;
	private JRadioButton rb_author = null;
	private JRadioButton rb_press = null;
	private JRadioButton rb_keyword = null;
	private JLabel lb_type = null;
	private JPanel p_input = null;
	private JLabel lb_searchWord = null;
	private JTextField tf_searchWord = null;
	private JCheckBox cb_inSearch = null;
	private JPanel p_browser = null;
	private ButtonGroup bg_type = new ButtonGroup();  //  @jve:decl-index=0:
	private JButton bt_search = null;
	private JWebBrowser webBrowser = null;
	private int searchType = 0;
	private static GUI thisClass = null;
	private BookTable bookTable = null;
	private String searchQuery = "";  //  @jve:decl-index=0:
	private Stack<String> searchQueryStack = new Stack<String>();  //  @jve:decl-index=0:
	private JPanel p_result = null;
	private JScrollPane sp_result = null;
	private JTable tb_result = null;
	private JPanel p_page = null;
	private JPanel p_state = null;
	private JLabel lb_state = null;
	private int tableMouseClickedCount = 0;
	private JButton bt_prev = null;
	private JButton bt_next = null;
	private JPanel p_result_in = null;
	private JWebBrowser detailBrowser = null;
	
	/**
	 * This method initializes p_searchWord	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_searchWord() {
		if (p_searchWord == null) {
			p_searchWord = new JPanel();
			p_searchWord.setLayout(new BorderLayout());
			p_searchWord.add(getP_select(), BorderLayout.NORTH);
			p_searchWord.add(getP_input(), BorderLayout.CENTER);
		}
		return p_searchWord;
	}

	/**
	 * This method initializes p_select	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_select() {
		if (p_select == null) {
			lb_type = new JLabel();
			lb_type.setText("검색 분류 :");
			p_select = new JPanel();
			p_select.setLayout(new FlowLayout());
			p_select.add(lb_type, null);
			p_select.add(getRb_total(), null);
			p_select.add(getRb_title(), null);
			p_select.add(getRb_author(), null);
			p_select.add(getRb_press(), null);
			p_select.add(getRb_keyword(), null);
		}
		return p_select;
	}

	/**
	 * This method initializes rb_total	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_total() {
		if (rb_total == null) {
			rb_total = new JRadioButton();
			rb_total.setText("전체");
			rb_total.setSelected(true);
			rb_total.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					searchType = Const.SEARCH_TYPE_TOTAL;
				}
			});
			bg_type.add(rb_total);
		}
		return rb_total;
	}

	/**
	 * This method initializes rb_title	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_title() {
		if (rb_title == null) {
			rb_title = new JRadioButton();
			rb_title.setText("제목");
			rb_title.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					searchType = Const.SEARCH_TYPE_TITLE;
				}
			});
			bg_type.add(rb_title);
		}
		return rb_title;
	}

	/**
	 * This method initializes rb_author	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_author() {
		if (rb_author == null) {
			rb_author = new JRadioButton();
			rb_author.setText("저자");
			rb_author.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					searchType = Const.SEARCH_TYPE_AUTHOR;
				}
			});
			bg_type.add(rb_author);
		}
		return rb_author;
	}

	/**
	 * This method initializes rb_press	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_press() {
		if (rb_press == null) {
			rb_press = new JRadioButton();
			rb_press.setText("출판사");
			rb_press.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					searchType = Const.SEARCH_TYPE_PRESS;
				}
			});
			bg_type.add(rb_press);
		}
		return rb_press;
	}

	/**
	 * This method initializes rb_keyword	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRb_keyword() {
		if (rb_keyword == null) {
			rb_keyword = new JRadioButton();
			rb_keyword.setText("키워드");
			rb_keyword.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					searchType = Const.SEARCH_TYPE_KEYWORD;
				}
			});
			bg_type.add(rb_keyword);
		}
		return rb_keyword;
	}

	/**
	 * This method initializes p_input	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_input() {
		if (p_input == null) {
			lb_searchWord = new JLabel();
			lb_searchWord.setText("검색어 :");
			p_input = new JPanel();
			p_input.setLayout(new FlowLayout());
			p_input.add(lb_searchWord, null);
			p_input.add(getTf_searchWord(), null);
			p_input.add(getCb_inSearch(), null);
			p_input.add(getBt_search(), null);
		}
		return p_input;
	}

	/**
	 * This method initializes tf_searchWord	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf_searchWord() {
		if (tf_searchWord == null) {
			tf_searchWord = new JTextField();
			tf_searchWord.setColumns(20);
			tf_searchWord.setText("java");
		}
		return tf_searchWord;
	}

	/**
	 * This method initializes cb_inSearch	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCb_inSearch() {
		if (cb_inSearch == null) {
			cb_inSearch = new JCheckBox();
			cb_inSearch.setText("결과 내 검색");
		}
		return cb_inSearch;
	}

	/**
	 * This method initializes p_browser	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_browser() {
		if (p_browser == null) {
			p_browser = new JPanel();
			p_browser.setLayout(new BorderLayout());
			
			webBrowser = new JWebBrowser();
			webBrowser.addWebBrowserListener(new WebBrowserListener() {
				public void locationChanged(WebBrowserNavigationEvent arg0) {
					bookTable = ParseTable.parseTable(webBrowser.getHTMLContent());
					
					getTb_result().setModel(bookTable);
					lb_state.setText("[INFO] " + bookTable.getTitle());
				}
				
				public void windowWillOpen(WebBrowserWindowWillOpenEvent arg0) {}
				public void windowOpening(WebBrowserWindowOpeningEvent arg0) {}
				public void windowClosing(WebBrowserEvent arg0) {}
				public void titleChanged(WebBrowserEvent arg0) {}
				public void statusChanged(WebBrowserEvent arg0) {}
				public void locationChanging(WebBrowserNavigationEvent arg0) {}
				public void locationChangeCanceled(WebBrowserNavigationEvent arg0) {}
				public void loadingProgressChanged(WebBrowserEvent arg0) {}
				public void commandReceived(WebBrowserCommandEvent arg0) {}
			});
			
			detailBrowser = new JWebBrowser();
			detailBrowser.addWebBrowserListener(new WebBrowserListener() {
				public void locationChanged(WebBrowserNavigationEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println(detailBrowser.getHTMLContent());
				}
				
				public void windowWillOpen(WebBrowserWindowWillOpenEvent arg0) {}
				public void windowOpening(WebBrowserWindowOpeningEvent arg0) {}
				public void windowClosing(WebBrowserEvent arg0) {}
				public void titleChanged(WebBrowserEvent arg0) {}
				public void statusChanged(WebBrowserEvent arg0) {}
				public void locationChanging(WebBrowserNavigationEvent arg0) {}
				public void locationChangeCanceled(WebBrowserNavigationEvent arg0) {}
				public void loadingProgressChanged(WebBrowserEvent arg0) {}
				public void commandReceived(WebBrowserCommandEvent arg0) {}
			});
			
			p_browser.add(webBrowser, BorderLayout.CENTER);
			p_browser.add(detailBrowser, BorderLayout.SOUTH);
		}
		return p_browser;
	}

	/**
	 * This method initializes bt_search	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_search() {
		if (bt_search == null) {
			bt_search = new JButton();
			bt_search.setText("검색");
			bt_search.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					String searchWord = getTf_searchWord().getText();
					
					if (searchWord.getBytes().length < 2) {
						JOptionPane.showMessageDialog(thisClass, "검색어는 한글 1글자 이상, 영어 2글자 이상이여야 합니다.");
						return;
					}
					
					if (cb_inSearch.isSelected()) {
						searchQuery = Const.getInSearchQuery(searchWord, searchQueryStack.peek());
					}
					else {
						searchQuery = Const.getSearchQuery(searchWord, searchType);
					}
					
					String searchURL = Const.getSearchURL(searchQuery);
					
					webBrowser.navigate(searchURL);
					detailBrowser.navigate(searchURL);
					
					searchQueryStack.push(searchQuery);
				}
			});
		}
		return bt_search;
	}

	/**
	 * This method initializes p_result	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_result() {
		if (p_result == null) {
			p_result = new JPanel();
			p_result.setLayout(new BorderLayout());
			p_result.add(getP_state(), BorderLayout.NORTH);
			p_result.add(getP_result_in(), BorderLayout.CENTER);
			p_result.add(getP_page(), BorderLayout.SOUTH);
		}
		return p_result;
	}

	/**
	 * This method initializes sp_result	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSp_result() {
		if (sp_result == null) {
			sp_result = new JScrollPane();
			sp_result.setViewportView(getTb_result());
		}
		return sp_result;
	}

	/**
	 * This method initializes tb_result	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTb_result() {
		if (tb_result == null) {
			TableColumn tc_press = new TableColumn();
			tc_press.setModelIndex(2);
			tc_press.setHeaderValue("출판사");
			TableColumn tc_author = new TableColumn();
			tc_author.setModelIndex(1);
			tc_author.setHeaderValue("저자");
			TableColumn tc_title = new TableColumn();
			tc_title.setHeaderValue("제목");
			tb_result = new JTable();
			tb_result.setAutoCreateColumnsFromModel(false);
			tb_result.setFont(new Font("Dialog", Font.PLAIN, 14));
			tb_result.setRowHeight(30);
			tb_result.addColumn(tc_title);
			tb_result.addColumn(tc_author);
			tb_result.addColumn(tc_press);			
			tb_result.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					if (tableMouseClickedCount == 0) {
						tableMouseClickedCount++;
						return;
					}
					
					Const.goToDetailPage(detailBrowser, getTb_result().getSelectedRow());
					
					tableMouseClickedCount = 0;
				}
			});
		}
		return tb_result;
	}

	/**
	 * This method initializes p_page	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_page() {
		if (p_page == null) {
			p_page = new JPanel();
			p_page.setLayout(new FlowLayout());
			p_page.setVisible(true);
			p_page.add(getBt_prev(), null);
			p_page.add(getBt_next(), null);
		}
		return p_page;
	}

	/**
	 * This method initializes p_state	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_state() {
		if (p_state == null) {
			lb_state = new JLabel();
			lb_state.setText("[INFO] developed by inter6.com");
			p_state = new JPanel();
			p_state.setLayout(new FlowLayout());
			p_state.add(lb_state, null);
		}
		return p_state;
	}

	/**
	 * This method initializes bt_prev	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_prev() {
		if (bt_prev == null) {
			bt_prev = new JButton();
			bt_prev.setText("◀");
			bt_prev.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int currentPage = bookTable.getCurrentPage();
					
					if (currentPage == 1) {
						lb_state.setText("현재 페이지가 첫 페이지입니다.");
						return;
					}
					
					--currentPage;
					
					Const.goToSelectPage(webBrowser, currentPage);
					Const.goToSelectPage(detailBrowser, currentPage);
				}
			});
		}
		return bt_prev;
	}

	/**
	 * This method initializes bt_next	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBt_next() {
		if (bt_next == null) {
			bt_next = new JButton();
			bt_next.setText("▶");
			bt_next.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int currentPage = bookTable.getCurrentPage();
					
					if (currentPage == bookTable.getLastPage()) {
						lb_state.setText("현재 페이지가 마지막 페이지입니다.");
						return;
					}
					
					++currentPage;
					
					Const.goToSelectPage(webBrowser, currentPage);
					Const.goToSelectPage(detailBrowser, currentPage);
				}
			});
		}
		return bt_next;
	}

	/**
	 * This method initializes p_result_in	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getP_result_in() {
		if (p_result_in == null) {
			p_result_in = new JPanel();
			p_result_in.setLayout(new BorderLayout());
			p_result_in.add(getSp_result(), BorderLayout.CENTER);
		}
		return p_result_in;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NativeInterface.open();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				thisClass = new GUI();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
		NativeInterface.runEventPump();
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
		this.setSize(1080, 720);
		this.setContentPane(getJContentPane());
		this.setTitle("부경대 도서관 검색기");
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
			jContentPane.add(getP_searchWord(), BorderLayout.NORTH);
			jContentPane.add(getP_result(), BorderLayout.CENTER);
			jContentPane.add(getP_browser(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

}
