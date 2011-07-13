package data;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class MonthlyCarte extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private String month;
	private ArrayList<TodayCarte> carte;
	
	public MonthlyCarte(String month, ArrayList<TodayCarte> carte) {
		this.month = month;
		this.carte = carte;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void printCarte() {
		System.out.println(month);
		
		if (carte == null) {
			return;
		}
		
		for (TodayCarte todayCarte : carte) {
			todayCarte.printCarte();
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return carte.size() * 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub		
		return carte.get(rowIndex / 4).getValueAt(rowIndex % 4, columnIndex);
	}
}