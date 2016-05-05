package edu.utdallas.IR2016.Project;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.lucene.index.Term;

/*
  This class generates all tokens from documents and lemma for all the tokens.
*/

public class QueryExpansion {

	
	
	static Stemmer stem = new Stemmer(); 
	static double executionTime = 0.0;
	public static ArrayList<Integer> relevantDocIDList = new ArrayList<Integer>();
	
	//static Pattern pattern = Pattern.compile("<.?title>", Pattern.CASE_INSENSITIVE);
	public static int avgDocLength;
		

	public static void main(String[] args) throws Exception {

	  /*  File folder = new File("/people/cs/s/sanda/cs6322/Cranfield");
		File stopwordsFile = new File("/people/cs/s/sanda/cs6322/resourcesIR/stopwords");
		File queryFile = new File("/people/cs/s/sanda/cs6322/hw3.queries"); */
		TreeMap<String, TermInfo> docStemMap = new TreeMap<String, TermInfo>();
		File folder = new File("C:\\Users\\jk\\Downloads\\Cranfield\\db");
		
		File queryFile = new File("C:\\Users\\jk\\Downloads\\Cranfield\\tp.queries");  

		File[] listOfFiles = folder.listFiles();
		

		//generateTokens("girl paris",listOfFiles);//, docStemMap);
		//avgDocLength = calculateAvgDocLength();
	/*	List<String> queries = QueryProcessor.readAllQueries(queryFile.getAbsolutePath());
         int i = 1;
		for (String query : queries) {
			System.out.println("Query:" + i++);
			processQuery(query, stopWordsList);
		} */
		
	}

	
	public static ArrayList<String> extractStopWords(File stopwordsFile) throws FileNotFoundException, IOException {

		ArrayList<String> stopWordsList = new ArrayList<String>();
		String line = "";
		FileReader fileReader = new FileReader(stopwordsFile);
		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			stopWordsList.add(line);
		}

		return stopWordsList;
	}

	//        Extract tokens from all the documents.
	 
	
	public String generateTokens(String queryText, TreeMap<String,String> documents) throws Exception {
		TreeMap<String, Integer> wordMap = new TreeMap<String, Integer>();
		StringBuilder expandedQuery = new StringBuilder();

		File stopwordsFile = new File("C:\\Users\\Alay\\workspace\\Project_IR\\stopwords");
		ArrayList<String> stopWordsList = extractStopWords(stopwordsFile);
		TreeMap<String, TermInfo> docStemMap = new TreeMap<String, TermInfo>();
		
		int i = 0;
		for (String doc_it : documents.keySet()) {
		
			
				i++;
				
				// Temporary table to hold all the terms & corresponding frequencies in current document
				Map<String, Integer> termFreqTable = new TreeMap<String, Integer>();


				String line = documents.get(doc_it);
				//FileReader fileReader = new FileReader(listOfFiles[j].getAbsoluteFile());
				//@SuppressWarnings("resource")
				//BufferedReader bufferedReader = new BufferedReader(fileReader);
				if (line != null) {
					line = line.toLowerCase();
					line = line.replaceAll("\\<.*?>", "");
					line = line.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");

					 String [] words = line.split(" ");
		                for (int n = 0; n < words.length; n++) {
		                	String word = words[n].trim();
		                	if(word.isEmpty()){
		                		continue;
		                	}
		                	
							// this is the text of the token
							System.out.println(word);
							
							if (stopWordsList.contains(word)) {
								continue;
							}

							stem.add(word.toCharArray(),word.length());
							stem.stem();
							String stemmedWord = stem.toString();
							
                            
							if (!stemmedWord.isEmpty()) {
								if (docStemMap.containsKey(stemmedWord)) {
									TermInfo node = docStemMap.get(stemmedWord);
									node.termFreq += 1;
									if (node.postingFiles.containsKey(i)) {	
										int val = node.postingFiles.get(i);
										node.postingFiles.put(i, val + 1);
									} else {
										node.postingFiles.put(i, 1);
										node.docFreq += 1;
									}
									//System.out.println(stemmedWord + " == "+ node.postingFiles);
								} else {
									TermInfo node = new TermInfo();
									node.termFreq = 1;
									node.postingFiles = new TreeMap<Integer, Integer>();
									if (node.postingFiles.containsKey(i)) {
										int val = node.postingFiles.get(i);
										node.postingFiles.put(i, val + 1);
									} else {
										node.postingFiles.put(i, 1);
										node.docFreq += 1;
									}
									docStemMap.put(stemmedWord, node);
									//System.out.println(stemmedWord + " == "+ node.postingFiles);
								}
							}

						/*	if (termFreqTable.containsKey(stemmedWord)) {
								termFreqTable.put(stemmedWord, termFreqTable.get(stemmedWord) + 1);
							} else {
								termFreqTable.put(stemmedWord, 1);
							} */
						}
					
				relevantDocIDList.add(i);
				
			
					//updateDocumentDetails(i, listOfFiles[j].getName(), title, termFreqTable);
		
				}
				}
		
		int clusterSize = 2;
		String expandedQStr = processQuery(queryText,stopWordsList, clusterSize, docStemMap);
		
		//System.out.println(docStemMap.keySet());
		
		return expandedQStr;
	}
		
	
	public static TreeMap<String, CorrelationMatrix> parseQuery(String queryText, List<String> stopWordsList) {

		
		TreeMap<String, CorrelationMatrix> queryStemMap = new TreeMap<String, CorrelationMatrix>();
		String line = queryText;

		line = line.toLowerCase();
		line = line.replaceAll("\\<.*?>", "");
		line = line.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");

				 String [] words = line.split(" ");
		         for (int n = 0; n < words.length; n++) {
		         	String word = words[n].trim();
		         	if(word.isEmpty()){
		         		continue;
		         	}
		         	
				
				if (stopWordsList.contains(word)) {
					continue;
				}

				stem.add(word.toCharArray(),word.length());
				stem.stem();
				String stemmedWord = stem.toString();
				
			
				if (!stemmedWord.isEmpty()) {
						
					if (queryStemMap.containsKey(stemmedWord)) {
						
						CorrelationMatrix cMat = queryStemMap.get(stemmedWord);
						
						cMat.termFreq++;
						
						queryStemMap.put(stemmedWord, cMat);
					} 
					else {
						
						CorrelationMatrix cMat = new CorrelationMatrix();
						
						cMat.termFreq = 1;
						queryStemMap.put(stemmedWord, cMat);
					}
				}

			}
		
		return queryStemMap;
	}

	
	public static String processQuery(String query, List<String> stopWordsList, int clusterSize,TreeMap<String,TermInfo> docStemMap) throws Exception {
		
		TreeMap<String, CorrelationMatrix> queryStemMap = parseQuery(query, stopWordsList);
		StringBuilder expandedQuery = new StringBuilder();
		System.out.println("Correlation Matrix");
		CorrelationMatrix cMat = new CorrelationMatrix();
		
		List<String> eQuery = new ArrayList<String>();
		
		for (String queryTerm : queryStemMap.keySet()) {
			//System.out.println("query ===========>"+ queryTerm);
			if(!eQuery.contains(queryTerm))
			   eQuery.add(queryTerm);
			//expandedQuery.append(" "+queryTerm);
			TermInfo queryNode = docStemMap.get(queryTerm);
			
			if (queryNode == null) {
				continue;
			}
		
			for (String docTerm : docStemMap.keySet())
			{
				if(queryStemMap.containsKey(docTerm))
				{
					continue;
				}
				TermInfo docNode = docStemMap.get(docTerm);
				
				//System.out.println(docNode.postingFiles);
				int c_UV = 0;
				int c_UU = 0;
				int c_VV = 0;
				double s_UV = 0.0;
				
				if(queryTerm.equalsIgnoreCase(docTerm))
				{
					continue;
				}
				for (Integer i : relevantDocIDList)
				{
					
					TreeMap<Integer, Integer> qp = queryNode.postingFiles;
					TreeMap<Integer, Integer> dp = docNode.postingFiles;
					
					if(qp.containsKey(i) && dp.containsKey(i))
					{
						c_UV += qp.get(i) * dp.get(i); 
						c_UU += qp.get(i) * qp.get(i); 
						c_VV += dp.get(i) * dp.get(i); 
					}
					else
						if(qp.containsKey(i) && !dp.containsKey(i))
						{
							//c_UV += qp.get(i) * dp.get(i); 
							c_UU += qp.get(i) * qp.get(i); 
							//c_VV += dp.get(i) * dp.get(i); 
						}
						else
							if(!qp.containsKey(i) && dp.containsKey(i))
							{
								//c_UV += qp.get(i) * dp.get(i); 
								//c_UU += qp.get(i) * qp.get(i); 
								c_VV += dp.get(i) * dp.get(i); 
							}
						
				}
				 cMat = queryStemMap.get(queryTerm);
				 int sum = c_UV + c_UU + c_VV;
				 
				 //if(sum != 0.0) 
				  s_UV = c_UV /(double) sum;
				 
				cMat.correlationMap.put(docTerm, s_UV);
				
				System.out.println(queryTerm + " * " + docTerm+ " = " + s_UV);
				
				
			}
			
			TreeSet<Entry<String, Double>> sortedSet = new TreeSet<Entry<String,Double>>(new FrequencyComparator());
			sortedSet.addAll(cMat.correlationMap.entrySet());
			Iterator<Entry<String, Double>> iterator = sortedSet.iterator();
			
			
			for (int i = 0; i < clusterSize && iterator.hasNext(); i++) {
				Entry<String, Double> entry = iterator.next();
				
				System.out.println(queryTerm + " * " + entry.getKey()+ " = " + entry.getValue());
				//expandedQuery.append(" "+entry.getKey());
				
				if(!eQuery.contains(entry.getKey()))
					   eQuery.add(entry.getKey());
			}
		}
		
		for(String s : eQuery)
			
			expandedQuery.append(s+ " ");
			
		return expandedQuery.toString();
	}
	
	static class FrequencyComparator implements Comparator<Entry<String,Double>> {
		@Override
		public int compare(Entry<String,Double> o1, Entry<String,Double> o2) {
			if (o1.getValue() < o2.getValue()) {
				return 1;
			}
			return -1;
		}
	}
	
	
	public static class TermInfo {
		int termFreq;
		int docFreq;
		TreeMap<Integer, Integer> postingFiles = new TreeMap<Integer, Integer>();
	}

   
   public static class CorrelationMatrix{
	   
	   TreeMap<String, Double> correlationMap =  new TreeMap<String, Double>();
	   int termFreq;
   }
}
