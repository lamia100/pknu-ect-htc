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

		String SearchWord = "�ڹ�"; // ����� 2���� �̻�, �ѱ��� 1���� �̻� (2byte �̻�)
				
		if (SearchWord.length() == 0) {
			return;
		}

		int SearchItemNo = 0; // ��ü = 0, ���� = 1, ���� = 2, ���ǻ� = 3, ������ = 4
		
		/*
		// ī�װ�
		SearchItemNo = document.SmallSearchForm.SearchItem.value.substring(6, 5);

		// ��ü �˻��� ��� 0
		if (document.SmallSearchForm.SearchItem.value == "TOTAL") {
			SearchItemNo = "0";
		}
		*/

		// �˻� ����
		String SearchQuery = "([" + SearchWord + ",TOT00,2," + SearchItemNo + ",3])";
		// SearchQuery = SearchQuery + "AND NOT [1,TOT00,3,25,3]";
		
		/*
		String ViewQuery = "[Ű����/ "
				+ document.SmallSearchForm.SearchItem.options[document.SmallSearchForm.SearchItem.selectedIndex].text
				+ ":" + SearchWord + "]";
		*/
		
		// ��� �� �˻�
		boolean isInResult = true;
		String SearchInWord = "XML";
		
		if (isInResult) {
			SearchQuery = "( ([" + SearchInWord + ",TOT00,2,0,3])AND NOT [1,TOT00,3,25,3]) AND (" + SearchQuery + ")";
			// ViewQuery = "[Ű����/ ��ü:�ڹ�]" + "AND" + ViewQuery;
		}

		// ��� ��û URL
		String href = Const.getSearchURL(SearchQuery);
		
		// ����
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