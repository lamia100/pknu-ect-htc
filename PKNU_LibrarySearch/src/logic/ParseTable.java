package logic;

import java.util.List;
import java.util.StringTokenizer;

import data.Book;
import data.BookTable;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class ParseTable {
	public static BookTable parseTable(String htmlSource) {
		Source target = new Source(htmlSource);
		target.fullSequentialParse();

		// 검색건, 페이지 번호 파싱 - 시작
		int resultNum = 0;
		int currentPage = 0;
		int lastPage = 0;

		{
			Element table = target.getAllElements(HTMLElementName.TABLE).get(11);
			Element tr = table.getAllElements(HTMLElementName.TR).get(0);
			
			List<Element> tdList = tr.getAllElements(HTMLElementName.TD);
			
			// 검색 결과 없음
			if (tdList.size() < 2) {
				return new BookTable(0, 0, 0);
			}
			
			Element td = tdList.get(1);

			String page = td.getTextExtractor().toString().trim();

			StringTokenizer st = new StringTokenizer(page, "건[/]");

			int count = 0;
			while (st.hasMoreTokens()) {
				String data = st.nextToken();
				
				switch (count) {
				case 0:
					resultNum = Integer.parseInt(data);
					break;
				case 3:
					currentPage = Integer.parseInt(data);
					break;
				case 4:
					lastPage = Integer.parseInt(data);
					break;
				}

				count++;
			}
		}
		// 검색건, 페이지 번호 파싱 - 끝

		BookTable resultTable = new BookTable(resultNum, currentPage, lastPage);

		// 검색 목록 파싱 - 시작
		{
			Element table = target.getAllElements(HTMLElementName.TABLE).get(13);
			List<Element> trList = table.getAllElements(HTMLElementName.TR);

			int count_tr = 0;
			for (Element tr : trList) {
				count_tr++;
				
				if (count_tr == 1) {
					continue;
				}
				
				List<Element> tdList = tr.getAllElements(HTMLElementName.TD);
				
				String title = "";
				String author = "";
				String press = "";
				
				int count_td = 0;
				for (Element td : tdList) {
					count_td++;
					
					if (count_td <= 2) {
						continue;
					}
					
					String data = td.getTextExtractor().toString().trim();
					
					switch (count_td) {
					case 3:
						title = data;
						break;
					case 4:
						author = data;
						break;
					case 5:
						press = data;
						break;
					}
					
					if (count_td == 5) {
						Book result = new Book(title, author, press);
												
						resultTable.addBook(result);
						
						break;
					}
				}
			}
		}
		// 검색 목록 파싱 - 끝
		
		System.out.println(resultTable.toString() + "\n");
		
		return resultTable;
	}
}
