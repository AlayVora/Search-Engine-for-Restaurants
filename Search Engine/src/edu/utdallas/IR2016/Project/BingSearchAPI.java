package edu.utdallas.IR2016.Project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

public class BingSearchAPI {
	public static ArrayList<String> bSearch (String find) {
		
		ArrayList<String> bList = new ArrayList<String>();
		try{
        final String accKey = "iUvZEHhruQpXTykdqoY76duA4/OwPYks2tOUZn7+G4I";
        final String urlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%%27%s%%27&$format=JSON";
        final String query = URLEncoder.encode(find, Charset.defaultCharset().name());
        final String bingUrl = String.format(urlPattern, query);
        final String accountKeyEnc = Base64.encodeBase64String((accKey + ":" + accKey).getBytes());
        final URL url = new URL(bingUrl);
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
        
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            final JSONObject json = new JSONObject(response.toString());
            final JSONObject d = json.getJSONObject("d");
            final JSONArray results = d.getJSONArray("results");
            final int resultsLength = results.length();
            for (int i = 0; i < resultsLength; i++) {
                final JSONObject aResult = results.getJSONObject(i);
                bList.add(aResult.get("Url")+" "+aResult.get("Title"));
            }
        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
        return bList;
    }
}
