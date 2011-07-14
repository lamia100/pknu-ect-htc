package data;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.table.AbstractTableModel;

public class TodayCarte extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private String date;
	private String[][] transCarte;
	
	public TodayCarte(String date, ArrayList<String> carte) {
		this.date = date;
		transCarte = null;
		
		if (carte != null) {
			transCarte = new String[4][2];
			
			transCarte[0][1] = date;
			transCarte[1][0] = "아침";
			transCarte[2][0] = "점심";
			transCarte[3][0] = "저녁";
			
			int i = 1;
			for (String menu : carte) {
				StringTokenizer st = new StringTokenizer(menu);
				
				boolean isFirst = true;
				String refactMenu = "";
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					
					if (isFirst) {
						refactMenu += token;
						isFirst = false;
						continue;
					}
					
					// 메뉴 사이에 + 붙임
					refactMenu += (" + " + token); 
				}
				
				transCarte[i][1] = refactMenu;
				i++;
			}
		}
	}
	
	public String getDate() {
		return date;
	}
	
	public void printCarte() {
		System.out.println(date);
		
		if (transCarte == null) {
			return;
		}
		
		for (int i = 0; i < 3; i++) {
			System.out.println(transCarte[i][0] + " : " + transCarte[i][1]);
		}
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return transCarte[rowIndex][columnIndex];
	}
}