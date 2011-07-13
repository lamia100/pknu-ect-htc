import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import net.htmlparser.jericho.*;

public class TodayCarte {
	public static void main(String[] args) {
		String targetURL = "http://dormitory.pknu.ac.kr/";
				
		System.out.println("세종관");
		int targetTableIndexList1[] = {35, 37, 39};
		parse(targetURL, targetTableIndexList1);
		
		System.out.println();
		
		System.out.println("광개토관");
		int targetTableIndexList2[] = {44, 46, 48};
		parse(targetURL, targetTableIndexList2);
	}
	
	public static void parse(String targetURL, int targetTableIndexList[]) {
		try {
			Source target = new Source(new URL(targetURL));
			target.fullSequentialParse(); // 시작부터 끝까지 태그들만 파싱
			
			for (int i = 0; i < targetTableIndexList.length; i++) {
				int targetTableIndex = targetTableIndexList[i];
				
				Element table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndex);
				List<Element> trList = table.getAllElements(HTMLElementName.TR);
				
				Iterator<Element> trIter = trList.iterator();

				while (trIter.hasNext()) {
					Element tr = trIter.next();
					List<Element> tdList = tr.getAllElements(HTMLElementName.TD);
					
					Iterator<Element> tdIter = tdList.iterator();
					
					while (tdIter.hasNext()) {
						Element td = tdIter.next();
						String value = td.getContent().getTextExtractor().toString().trim();
						
						switch (i) {
						case 0:
							System.out.print("아침 : ");
							break;
						case 1:
							System.out.print("점심 : ");
							break;
						case 2:
							System.out.print("저녁 : ");
							break;
						}
						
						System.out.println(value);
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}