package logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import data.Const;
import data.State;

public class ParseState {
	private final static String targetURL = Const.STATE_URL;
	private final static int targetTableIndexList[] = Const.STATE_TABLE_INDEX_LIST;
	
	private String date;
	private ArrayList<State> stateList;
	
	public ParseState() {
		date = null;
		stateList = null;
	}
	
	public void parse() {
		date = "";
		stateList = new ArrayList<State>();
		
		try {
			Source target = new Source(new URL(targetURL));
			//target.setLogger(null);
			target.fullSequentialParse();
			
			Element table = target.getAllElements(HTMLElementName.TABLE).get(targetTableIndexList[0]);
			List<Element> trList = table.getAllElements(HTMLElementName.TR);
			
			int trCount = 0;
			for (Element tr : trList) {
				trCount++;
				
				if (trCount == 2) {
					continue;
				}
				
				List<Element> tdList = tr.getAllElements(HTMLElementName.TD);
				
				String name = "";
				int total = 0;
				int use = 0;
				
				int tdCount = 0;
				for (Element td : tdList) {
					tdCount++;
					
					// 인코딩 변환
					String value_iso = td.getContent().getTextExtractor().toString();
					String value = new String(value_iso.getBytes("iso-8859-1")).trim();
					
					// date
					if (trCount == 1) {
						date = value;						
						break;
					}
					
					if (tdCount == 1) {
						continue;
					}
					
					switch (tdCount) {
					case 2: // name
						name = value;
						break;
					case 3: // total
						total = Integer.parseInt(value);
						break;
					case 4: // use
						use = Integer.parseInt(value);
						break;	
					}
					
					if (tdCount == 4) {
						break;
					}
				}
				
				if (trCount != 1) {
					stateList.add(new State(name, total, use));
				}
			}
			
			target.clearCache();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDate() {
		return date;
	}

	public ArrayList<State> getStateList() {
		return stateList;
	}
}