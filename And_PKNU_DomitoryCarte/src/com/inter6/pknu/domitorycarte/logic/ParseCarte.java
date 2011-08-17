package com.inter6.pknu.domitorycarte.logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import com.inter6.pknu.domitorycarte.data.Const;
import com.inter6.pknu.domitorycarte.data.TodayCarte;
import com.inter6.pknu.domitorycarte.data.WeeklyCarte;

public final class ParseCarte {	
	public static TodayCarte getTodayCarteD() {
		return parseTodayCarte(Const.TODAY_CARTE_URL, Const.TODAY_CARTE_TABLE_INDEX_LIST_D);
	}
	
	public static TodayCarte getTodayCarteY() {
		return parseTodayCarte(Const.TODAY_CARTE_URL, Const.TODAY_CARTE_TABLE_INDEX_LIST_Y);
	}
	
	private static TodayCarte parseTodayCarte(String targetURL, int targetTableIndexList[]) {
		String title = "";
		ArrayList<String> valueList = new ArrayList<String>();
		
		try {
			Source target = new Source(new URL(targetURL));
			target.setLogger(null);
			target.fullSequentialParse();
			
			// 타이틀 시작
			Element title_table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndexList[0]);
			Element title_td = title_table.getAllElements(HTMLElementName.TD).get(0);
			title = title_td.getContent().getTextExtractor().toString().trim();
			// 타이틀 끝
			
			// 메뉴 시작
			for (int i = 1; i < targetTableIndexList.length; i++) {
				int targetTableIndex = targetTableIndexList[i];
				Element table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndex);
				
				List<Element> trList = table.getAllElements(HTMLElementName.TR);

				for (Element tr : trList) {
					List<Element> tdList = tr.getAllElements(HTMLElementName.TD);
					
					for (Element td : tdList) {
						String value = td.getContent().getTextExtractor().toString().trim();
						
						valueList.add(value);
					}
				}
			}
			// 메뉴 끝
			
			target.clearCache();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new TodayCarte(title, valueList);
	}
	
	public static WeeklyCarte getWeeklyCarteD() {
		return parseWeeklyCarte(Const.MONTHLY_CARTE_URL_D, Const.MONTHLY_CARTE_TABLE_INDEX_LIST);
	}
	
	public static WeeklyCarte getWeeklyCarteY() {
		return parseWeeklyCarte(Const.MONTHLY_CARTE_URL_Y, Const.MONTHLY_CARTE_TABLE_INDEX_LIST);
	}
	
	private static WeeklyCarte parseWeeklyCarte(String targetURL, int targetTableIndexList[]) {
		String title = "";
		ArrayList<TodayCarte> valueList = new ArrayList<TodayCarte>();
		
		try {
			Source target = new Source(new URL(targetURL));
			target.setLogger(null);
			target.fullSequentialParse();
			
			// 타이틀 시작
			Element title_table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndexList[0]);
			Element title_td = title_table.getAllElements(HTMLElementName.TD).get(0);
			title = title_td.getContent().getTextExtractor().toString().trim();
			// 타이틀 끝
			
			// 메뉴 시작
			Element menu_table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndexList[1]);
			List<Element> menu_trList = menu_table.getAllElements(HTMLElementName.TR);
			
			int count = 0;
			for (Element menu_tr : menu_trList) {
				count++;
				
				if (count <= 2) {
					continue;
				}
				
				List<Element> menu_tdList = menu_tr.getAllElements(HTMLElementName.TD);
				
				String in_title = "";
				ArrayList<String> in_valueList = new ArrayList<String>();
				
				boolean isTitle = true;
				for (Element menu_td : menu_tdList) {
					String value = menu_td.getContent().getTextExtractor().toString().trim();
					
					if ("".equals(value)) {
						continue;
					}
					
					if (isTitle) {
						in_title = value;
						
						isTitle = false;
						continue;
					}
					
					in_valueList.add(value);
				}
				
				if (!isTitle) {
					valueList.add(new TodayCarte(in_title, in_valueList));
				}
			}
			// 메뉴 끝
			
			target.clearCache();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new WeeklyCarte(title, valueList);
	}
}