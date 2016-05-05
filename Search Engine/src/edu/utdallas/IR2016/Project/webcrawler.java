package edu.utdallas.IR2016.Project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class webcrawler {
	
	public static String pcrawl(String url){
		Document doc;
		String io = "";
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").get();
//			Elements p= doc.getElementsByTag("p");
			Elements menu = doc.select("dl[class=menu-item no-def-item]");
			System.out.print("menu : ");
			for (Element link : menu) {
				System.out.print(link.text()+", ");
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return io;
	}
	public static String yelpCrawlForInfo(String url){
		Document doc;
		String out = "";
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get();
			String webaddress = doc.select("meta[property=og:url]").attr("content");
//			System.out.println("webaddress : "+webaddress);
			out += webaddress+" ";
			String name = doc.select("h1[itemprop=name]").text();
//			System.out.println("name : "+name);
			out += name+" ";
			String category = doc.select("span[class=category-str-list]").text();
//			System.out.println("category : "+category);
			out += category+" ";
			Elements menu = doc.select("dl[class^=menu-item]");
//			System.out.print("menu : ");
			for (Element link : menu) {
//				System.out.print(link.select("dt").text()+", ");
				out += link.select("dt").text()+" ";
			}
//			System.out.println();
			
			String priceRange = doc.select("span[itemprop=priceRange]").text();
//			System.out.println("priceRange : "+priceRange);
			out += priceRange+" ";
			String starRating = doc.select("i[class^=star-img]").attr("title");
//			System.out.println("starRating : "+starRating);
			out += starRating+" ";
			String streetAddress = doc.select("span[itemprop=streetAddress]").text();
//			System.out.println("streetAddress : "+streetAddress);
			out += streetAddress+" ";
			String addressLocality = doc.select("span[itemprop=addressLocality]").text();
//			System.out.println("addressLocality : "+addressLocality);
			out += addressLocality+" ";
			String addressRegion = doc.select("span[itemprop=addressRegion]").text();
//			System.out.println("addressRegion : "+addressRegion);
			out += addressRegion+" ";
			String postalCode = doc.select("span[itemprop=postalCode]").text();
//			System.out.println("postalCode : "+postalCode);
			out += postalCode+" ";
//			String telephone = doc.select("span[itemprop=telephone]").text();
//			System.out.println("telephone : "+telephone);
//			out += "telephone : "+telephone+"\n";
			String bizWebsite = doc.select("div[class=biz-website]").select("a").text();
//			System.out.println("bizWebsite : "+bizWebsite);
			out += bizWebsite+" ";
			

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		out = out.replaceAll(",", "");
		System.out.println(out);
		return out;
	}
	public static void newFile(String articleName, String body){
		try{
			if(!body.trim().isEmpty()){

				String path = "yelp/biz/" + articleName;
				File file = new File(path);
				file.getParentFile().mkdirs();
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				writer.println(body);
				writer.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void yelpCrawlForUrls(String url,int flag,int total){
		Document doc;
		String [] s = new String[total];
		int counter = 0;
//		flag = 178;
		try {
			HashMap<String,Integer> hash = initializeHash();
			String[] temp = new String[hash.size()];
			RandomAccessFile Stop = null;
			try {
				Stop = new RandomAccessFile("yelp/urls", "r");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String urlInFile;
			while ((urlInFile = Stop.readLine()) != null) {
				if(urlInFile.startsWith("-")){
					flag++;
					urlInFile = urlInFile.substring(1, urlInFile.length());
				}
				temp[counter++] = urlInFile;
			}
			
			
			System.arraycopy(temp, 0, s, 0, temp.length);
			if(counter == 0){
				System.out.println("adding at "+counter+"->"+url);
				s[counter++] = url;
				newArticle.writeToFile(url);
				hash.put(url, 0);
			}
			
			for(int i = flag; i<s.length;i++){
				System.out.println("currently crawling : "+i+" -> "+s[i]);
				doc = Jsoup.connect(s[i]).userAgent("Mozilla").timeout(0).get();
				s[i] = "-"+s[i];
				Elements urls = doc.select("a");
				for (Element link : urls) {
					String data = "http://www.yelp.com"+link.attr("href");
					if(data.contains("?"))
						data = data.substring(0, data.indexOf("?"));
					if(data.startsWith("http://www.yelp.com/biz/") && !hash.containsKey(data)){
						
						hash.put(data, 0);
						System.out.println("adding at "+counter+"->"+data);
						s[counter++] = data;
						newArticle.writeToFile(data);
//						System.out.println(data);
					}
						
		        }
				Elements surls = doc.select("a[class=page-option available-number]");
				for (Element link : surls) {
					String data = "http://www.yelp.com"+link.attr("href");
					if(data.startsWith("http://www.yelp.com/search") && !hash.containsKey(data)){
						System.out.println("adding at "+counter+"->"+data);
						hash.put(data, 0);
						s[counter++] = data;
						newArticle.writeToFile(data);
//						System.out.println(data);
					}
		        }
				System.out.println("------------------------");
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	static public HashMap<String, Integer> initializeHash() throws Exception {
		HashMap<String, Integer> h = new HashMap<String, Integer>();
		RandomAccessFile Stop = null;
		try {
			Stop = new RandomAccessFile("yelp/urls", "r");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s;
		while ((s = Stop.readLine()) != null) {
			if(s.startsWith("-")){
				s = s.substring(1, s.length());
			}
			h.put(s, 0);
		}
		return h;
	}
	static public void createFiles(int size) throws Exception {
		RandomAccessFile Stop = null;
		
		try {
			Stop = new RandomAccessFile("yelp/Rurls", "r");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s;
		int counter = 1;
		while ((s = Stop.readLine()) != null) {
			if(counter == size){
				if(s.startsWith("http://www.yelp.com/biz/")){
					System.out.println("reading line : "+counter++);
					System.out.println("reading data :"+s);
					size++;
					newArticle.writeNewFile("biz"+counter, yelpCrawlForInfo(s));
					System.out.println("file added");
				}
				else{
					counter++;size++;
				}
			}
			else{
				counter++;
			}
		}
	}
	public static void main(String[] args) throws IOException {
		//	ITERATION 1 below code is to extract urls from the value in variable url
		/*String url = "http://www.yelp.com/search?find_desc=indian+&find_loc=Dallas%2C+TX&ns=1";
		int total = 100;
		yelpCrawlForUrls(url,0,total);*/
		//	ITERATION 1  ends
		// 	below code is to test individual restaurant data
//		String url = "http://www.yelp.com/biz/benihana-dallas";
//		System.out.println(yelpCrawlForInfo(url));
		//	ITERATION 2	below code is for creating new files in a folder yelp/biz/
		try {
			createFiles(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//	ITERATION 2 ends
		 

	}
}
