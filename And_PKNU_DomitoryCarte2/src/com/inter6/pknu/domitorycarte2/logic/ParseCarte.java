package com.inter6.pknu.domitorycarte2.logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.inter6.pknu.domitorycarte2.data.Const;
import com.inter6.pknu.domitorycarte2.data.TodayCarte;
import com.inter6.pknu.domitorycarte2.data.WeeklyCarte;

public final class ParseCarte {
	public static String getHtmlSource(String targetURL) throws URISyntaxException, ClientProtocolException, IOException {
		// 파라미터 지정
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Const.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, Const.SO_TIMEOUT);
		
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI(targetURL));
        
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        
        return EntityUtils.toString(httpEntity);
	}
	
	public static TodayCarte getTodayCarteD() {
		return parseTodayCarte(Const.TODAY_CARTE_URL, Const.TODAY_CARTE_TABLE_INDEX_LIST_D);
	}

	public static TodayCarte getTodayCarteY() {
		return parseTodayCarte(Const.TODAY_CARTE_URL, Const.TODAY_CARTE_TABLE_INDEX_LIST_Y);
	}
	
	public static WeeklyCarte getWeeklyCarteD() {
		return parseWeeklyCarte(Const.WEEKLY_CARTE_URL_D, Const.WEEKLY_CARTE_TABLE_INDEX_LIST);
	}

	public static WeeklyCarte getWeeklyCarteY() {
		return parseWeeklyCarte(Const.WEEKLY_CARTE_URL_Y, Const.WEEKLY_CARTE_TABLE_INDEX_LIST);
	}
	
	private static TodayCarte parseTodayCarte(String targetURL, int targetTableIndexList[]) {
		String title = "";
		ArrayList<String> valueList = new ArrayList<String>();

		try {
			Source target = new Source(getHtmlSource(targetURL));
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
			
			Log.d("[ParseCarte.parseTodayCarte]", "SUC Parse");
		}
		catch (MalformedURLException e) {
			Log.d("[ParseCarte.parseTodayCarte]", "MalformedURLException");
			return null;
		}
		catch (URISyntaxException e) {
			Log.d("[ParseCarte.parseTodayCarte]", "URISyntaxException");
			return null;
		}
		catch (ClientProtocolException e) {
			Log.d("[ParseCarte.parseTodayCarte]", "ClientProtocolException");
			return null;
		}
		catch (IOException e) {
			Log.d("[ParseCarte.parseTodayCarte]", "IOException");
			return null;
		}

		return new TodayCarte(title, valueList);
	}

	private static WeeklyCarte parseWeeklyCarte(String targetURL, int targetTableIndexList[]) {
		String title = "";
		ArrayList<TodayCarte> valueList = new ArrayList<TodayCarte>();

		try {
			Source target = new Source(getHtmlSource(targetURL));
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
			
			Log.d("[ParseCarte.parseWeeklyCarte]", "SUC Parse");
		}
		catch (MalformedURLException e) {
			Log.d("[ParseCarte.parseWeeklyCarte]", "MalformedURLException");
			return null;
		}
		catch (URISyntaxException e) {
			Log.d("[ParseCarte.parseWeeklyCarte]", "URISyntaxException");
			return null;
		}
		catch (ClientProtocolException e) {
			Log.d("[ParseCarte.parseWeeklyCarte]", "ClientProtocolException");
			return null;
		}
		catch (IOException e) {
			Log.d("[ParseCarte.parseWeeklyCarte]", "IOException");
			return null;
		}

		return new WeeklyCarte(title, valueList);
	}
}