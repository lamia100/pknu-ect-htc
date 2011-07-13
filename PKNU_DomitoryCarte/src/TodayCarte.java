import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class TodayCarte {
	public static void main(String[] args) {
		String targetURL = "http://dormitory.pknu.ac.kr/";
				
		System.out.println("������");
		int targetTableIndexList1[] = {35, 37, 39};
		parse(targetURL, targetTableIndexList1);
		
		System.out.println();
		
		System.out.println("�������");
		int targetTableIndexList2[] = {44, 46, 48};
		parse(targetURL, targetTableIndexList2);
	}
	
	public static void parse(String targetURL, int targetTableIndexList[]) {
		try {
			Source target = new Source(new URL(targetURL));
			target.fullSequentialParse(); // ���ۺ��� ������ �±׵鸸 �Ľ�
			
			for (int i = 0; i < targetTableIndexList.length; i++) {
				int targetTableIndex = targetTableIndexList[i];
				
				Element table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndex);
				List<Element> trList = table.getAllElements(HTMLElementName.TR);
				
				for(Element tr:trList){
					List<Element> tdList = tr.getAllElements(HTMLElementName.TD);
					
					for(Element td:tdList){
						
						String value = td.getContent().getTextExtractor().toString().trim();
						
						switch (i) {
						case 0:
							System.out.print("��ħ : ");
							break;
						case 1:
							System.out.print("���� : ");
							break;
						case 2:
							System.out.print("���� : ");
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