import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import net.htmlparser.jericho.*;

public class MonthlyCarte {
	public static void main(String[] args) {
		System.out.println("세종관");
		String targetURL1 = "http://dormitory.pknu.ac.kr/?page=life/life_02&category=1";
		int targetTableIndexList1[] = {15, 16};
		parse(targetURL1, targetTableIndexList1);
		
		System.out.println();
		
		System.out.println("광개토관");
		String targetURL2 = "http://dormitory.pknu.ac.kr/?page=life/life_02&category=3";
		int targetTableIndexList2[] = {15, 16};
		parse(targetURL2, targetTableIndexList2);
	}
	
	public static void parse(String targetURL, int targetTableIndexList[]) {
		try {
			Source target = new Source(new URL(targetURL));
			target.fullSequentialParse(); // 시작부터 끝까지 태그들만 파싱
			
			// 타이틀 시작
			Element title_table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndexList[0]);
			Element title_td = title_table.getAllElements(HTMLElementName.TD).get(0);
			String title = title_td.getContent().getTextExtractor().toString().trim();
			
			System.out.println(title);
			// 타이틀 끝
			
			// 메뉴 시작
			Element menu_table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndexList[1]);
			List<Element> menu_trList = menu_table.getAllElements(HTMLElementName.TR);
			
			Iterator<Element> menu_trIter = menu_trList.iterator();

			int count = 0;
			while (menu_trIter.hasNext()) {
				count++;
				
				if (count <= 2) {
					menu_trIter.next();
					continue;
				}
				
				Element menu_tr = menu_trIter.next();
				List<Element> menu_tdList = menu_tr.getAllElements(HTMLElementName.TD);
				
				Iterator<Element> menu_tdIter = menu_tdList.iterator();
				
				int i = 0;
				while (menu_tdIter.hasNext()) {
					Element td = menu_tdIter.next();
					String value = td.getContent().getTextExtractor().toString().trim();
					
					if ("".equals(value)) {
						continue;
					}
					
					switch (i) {
					case 1:
						System.out.print("아침 : ");
						break;
					case 2:
						System.out.print("점심 : ");
						break;
					case 3:
						System.out.print("저녁 : ");
						break;
					}
					
					System.out.println(value);
					
					i++;
				}
			}
			// 메뉴 끝
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}