package data;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class BookTable extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private int resultNum;
	private int currentPage;
	private int lastPage;
	private ArrayList<Book> bookList;
	
	public BookTable(int resultNum, int currentPage, int lastPage) {
		this.resultNum = resultNum;
		this.currentPage = currentPage;
		this.lastPage = lastPage;
		
		bookList = new ArrayList<Book>();
	}
	
	public void addBook(Book result) {
		bookList.add(result);
	}
	
	public int getResultNum() {
		return resultNum;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public ArrayList<Book> getBookList() {
		return bookList;
	}

	public String getTitle() {
		return "검색건 : " + resultNum + "  현재/마지막 페이지 번호 : " + currentPage + "/" + lastPage;
	}
	
	public String toString() {
		String result = getTitle();
		
		for (Book book : bookList) {
			result += "\n\n" + book.toString();
		}
		
		return result;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return bookList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		String result = "";
		
		switch (columnIndex) {
		case 0:
			result = bookList.get(rowIndex).getTitle();
			break;
		case 1:
			result = bookList.get(rowIndex).getAuthor();
			break;
		case 2:
			result = bookList.get(rowIndex).getPress();
			break;
		}
		
		return result;
	}
}