package edu.utdallas.IR2016.Project;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearchAPI {
	
	public static ArrayList<String> googleSearch (String search) {
		ArrayList<String> list = new ArrayList<String>();
		
		try{
		String google = "http://www.google.com/search?q=";
		String charset = "UTF-8";
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0";
		Elements links = Jsoup.connect(google + search).userAgent(userAgent).get().select("div.g>h3>a");
		
		System.out.println(links.size());
		for (Element link : links) {
			
		    String title = link.text();
		    String url = link.absUrl("href");
		    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

		   if (!url.startsWith("http")) {
		        continue;
		    }	
		    String data=url+" "+title;
		    list.add(data);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;	
	}
	
	public static void main(String[] args) {
	}

}
