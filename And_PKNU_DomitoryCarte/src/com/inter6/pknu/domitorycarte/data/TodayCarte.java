package com.inter6.pknu.domitorycarte.data;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TodayCarte {
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
					
					// 메뉴 사이에 줄내림
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
}