package util;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class Const {
	public final static int SEARCH_TYPE_TOTAL = 0;
	public final static int SEARCH_TYPE_TITLE = 1;
	public final static int SEARCH_TYPE_AUTHOR = 2;
	public final static int SEARCH_TYPE_PRESS = 3;
	public final static int SEARCH_TYPE_KEYWORD = 4;
	
	public final static String getSearchQuery(String searchWord, int searchType) {
		if (searchWord.getBytes().length < 2) {
			return null;
		}

		return "([" + searchWord + ",TOT00,2," + searchType + ",3])";
	}
	
	public final static String getInSearchQuery(String inSearchWord, String prevSearchQuery) {
		if (inSearchWord.getBytes().length < 2) {
			return null;
		}
		
		return "( ([" + inSearchWord + ",TOT00,2,0,3])AND NOT [1,TOT00,3,25,3]) AND (" + prevSearchQuery + ")";
	}
	
	public final static String getSearchURL(String searchQuery) {
		String result = "http://libweb.pknu.ac.kr/dlsearch/dlsearch/TOTWSearchList_Table.asp"
				+ "?syskey=SYSTOT"
				+ "&sysdiv=TOT"
				+ "&searchquery=" + searchQuery;

		return result;
	}
	
	public final static void goToSelectPage(JWebBrowser webBrowser, int page) {
		webBrowser.executeJavascript("javascript:GoToSelectedPage2('" + page + "', \"TOTWSearchList.asp\")");
	}
	
	public final static void goToDetailPage(JWebBrowser webBrowser, int num) {
		webBrowser.executeJavascript("javascript:handleLink(" + num + ",\"0\",\"FullView\",\"AbbrTD\")");
	}
	
	/*
	 * <form action="TOTWSearchFullView.asp" method="post" id=form1 name=form1>
	 * <input type="hidden" name="FullViewDataNo">
	 * <input type="hidden" name="FullViewSysDiv" value="CATCATCATCATCATCATCATCATCATCATCATCATCATCATCAT">
	 * <input type="hidden" name="FullViewControlNo" value="000000298641000000180247000000425196000000425170000000339529000000424785000000423085000000422823000000422400000000421452000000265127000000416471000000415143000000414807000000414638">
	 * <input type="hidden" name="OriginFullViewSysDiv" value="CATCATCATCATCATCATCATCATCATCATCATCATCATCATCAT">
	 * <input type="hidden" name="OriginFullViewControlNo" value="000000298641000000180247000000425196000000425170000000339529000000424785000000423085000000422823000000422400000000421452000000265127000000416471000000415143000000414807000000414638">
	 * <input type="hidden" name="InitialSysKey" value="SYSTOT">
	 * <input type="hidden" name="InitialSysDiv" value="TOT">
	 * <input type="hidden" name="SearchQuery" value=" ([java,TOT00,2,0,3])AND NOT [1,TOT00,3,25,3]">
	 * <input type="hidden" name="ComMethod" value="">
	 * <input type="hidden" name="MainMenuNo" value="MainBody">
	 * <input type="hidden" name="ResultPerPage" value="15">
	 * <input type="hidden" name="RetdMaxResultNo" value="363">
	 * <input type="hidden" name="SearchMainHisToGo" value="-1">
	 * <input type="hidden" name="FullViewPageNo" value="1" id="FullViewPageNo">
	 * <input type="hidden" name="EachBasketContent" value="">
	 * <input type="hidden" name="ReserveBookRequest" value="">
	 * </form>
	 */
}