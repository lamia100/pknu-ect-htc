package com.inter6.pknu.domitorycarte.data;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TodayCarte {
	private static final long serialVersionUID = 1L;
	
	private String date;
	private String[] transCarte;
	
	public TodayCarte(String date, ArrayList<String> carte) {
		this.date = date;
		transCarte = null;
		
		if (carte != null) {
			transCarte = new String[3];
			
			int i = 0;
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
					refactMenu += ("\n" + token); 
				}
				
				transCarte[i] = refactMenu;
				i++;
			}
		}
	}
	
	public String getDate() {
		return date;
	}
	
	public String getCarte(int when) {
		return transCarte[when];
	}
	
	
	/*
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
	
	public String getCarteByString() {
		String carteByString = "";
		
		if (transCarte == null) {
			return null;
		}
		
		for (int i = 1; i < transCarte.length; i++) {
			if (i != 1) {
				carteByString += "\n";
			}
			
			carteByString += (transCarte[i][0] + " : " + transCarte[i][1]);
		}
		
		return carteByString;
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

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return 4;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return transCarte[rowIndex][columnIndex];
	}
	*/
}