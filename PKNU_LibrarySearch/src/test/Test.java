package test;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import util.Const;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String SearchWord = "자바"; // 영어는 2글자 이상, 한글은 1글자 이상 (2byte 이상)
				
		if (SearchWord.length() == 0) {
			return;
		}

		int SearchItemNo = 0; // 전체 = 0, 제목 = 1, 저자 = 2, 출판사 = 3, 주제어 = 4
		
		/*
		// 카테고리
		SearchItemNo = document.SmallSearchForm.SearchItem.value.substring(6, 5);

		// 전체 검색일 경우 0
		if (document.SmallSearchForm.SearchItem.value == "TOTAL") {
			SearchItemNo = "0";
		}
		*/

		// 검색 쿼리
		String SearchQuery = "([" + SearchWord + ",TOT00,2," + SearchItemNo + ",3])";
		// SearchQuery = SearchQuery + "AND NOT [1,TOT00,3,25,3]";
		
		/*
		String ViewQuery = "[키워드/ "
				+ document.SmallSearchForm.SearchItem.options[document.SmallSearchForm.SearchItem.selectedIndex].text
				+ ":" + SearchWord + "]";
		*/
		
		// 결과 내 검색
		boolean isInResult = true;
		String SearchInWord = "XML";
		
		if (isInResult) {
			SearchQuery = "( ([" + SearchInWord + ",TOT00,2,0,3])AND NOT [1,TOT00,3,25,3]) AND (" + SearchQuery + ")";
			// ViewQuery = "[키워드/ 전체:자바]" + "AND" + ViewQuery;
		}

		// 결과 요청 URL
		String href = Const.getSearchURL(SearchQuery);
		
		// 실행
		try {			
			String[] cmd = {"C:\\Program Files (x86)\\Internet Explorer\\iexplore.exe", href};
			Runtime.getRuntime().exec(cmd);
					
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public final static String getSearchURL(String viewQuery, String searchQuery) {
		String result = "http://libweb.pknu.ac.kr/dlsearch/dlsearch/TOTWSearchList_Table.asp"
				+ "?syskey=SYSTOT&sysdiv=TOT"
				+ "&searchmethod=KWRD"
				+ "&mainmenuno=MainBody"
				+ "&ViewQuery=" + viewQuery
				+ "&searchquery=" + searchQuery;

		return result;
	}
	*/
}