package edu.utdallas.IR2016.Project;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.parser.ParseException;

import edu.uci.ics.jung.algorithms.scoring.HITS;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class SearchIndex {


	public SearchBean searchIndex(String querystr) throws IOException,
	FileNotFoundException, ParseException,
	org.apache.lucene.queryparser.classic.ParseException {

		File pathToIndex = new File("C:\\Users\\Alay\\workspace\\Project_IR\\Index");
		//File pathToCrawled = new File("H:\\crawlData");
		// Analyze which part of the document is to be indexed
		// KeywordAnalyzer analyzer = new KeywordAnalyzer();
		// SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_40);
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		Directory directory = FSDirectory.open(pathToIndex);
		//Directory directory = new RAMDirectory();
	//	IndexWriterConfig indexConfig = new IndexWriterConfig(
			//	Version.LUCENE_40, analyzer);

		// Query Preparation
		String queryString = querystr;
		List<String> list= getTokenStream(queryString, analyzer);
		String[] strStr= list.toArray(new String[list.size()]);
		BooleanQuery booleanquery = new BooleanQuery();
		DisjunctionMaxQuery query = new DisjunctionMaxQuery(0.1f);
		booleanquery.setBoost(2.0f);
		for(String s: strStr)
		{
			//query.add(new Term("Text", s));
			booleanquery.add(new BooleanClause(new TermQuery(new Term("Text",s)),BooleanClause.Occur.MUST));
		}
		query.add(booleanquery);
		for(String s: strStr)
		{
			query.add(new TermQuery(new Term("Text",s)));
		}
		// Search
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(20, true);
		indexSearcher.search(query, collector);
		System.out.println("Total Hits: "+collector.getTotalHits());
		ScoreDoc[] results = collector.topDocs().scoreDocs;
		DirectedSparseGraph g= getGraph(results, indexSearcher );
		HITS<String, String> ranker= new HITS<String, String>(g);
		ranker.setMaxIterations(20);
		ranker.evaluate();
		HashMap<String, Double> unsorted= new HashMap<String, Double>();
		for (Object v : g.getVertices()) {
			//System.out.println(v+ranker.)
			//System.out.println(v+"\t"+ranker.getVertexScore(v.toString()));
			unsorted.put(v.toString(), ranker.getVertexScore(v.toString()).authority);
		}
		//System.out.println("------------------------------------------------");
		HashMap<String, Double> sorted = sortByValue(unsorted);
		Iterator iter= sorted.entrySet().iterator();
		Map.Entry<String, Double>  pair;
		ArrayList<String> hitResults= new ArrayList<String>();
		/*while(iter.hasNext())
		{
			pair= (Map.Entry)iter.next();
			//System.out.println(pair.getKey()+"  "+pair.getValue());
			for (int i = 0; i < results.length; ++i) 
			{
				int docId = results[i].doc;
				Document d = indexSearcher.doc(docId);
				if(d.get("URL").equals(pair.getKey()))
				{
					hitResults.add(d.get("URL")+":::"+d.get("Title"));
					break;
				}
			}
		}*/
		int k=0;
		while(iter.hasNext() && k<10)	
		{
			pair= (Map.Entry)iter.next();
			hitResults.add(pair.getKey());
			k++;
		}
		
		ScoreDoc[] clusterResults = new ScoreDoc[10];
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		System.out.println("The length is "+results.length);
		
		/*System.out.println(clusterData.size());
		System.out.println(list1.size());
		System.out.println(list2.size());
		System.out.println(list3.size());*/
		// Display Results
		/*System.out.println("Results Length: " + results.length);
		for (int i = 0; i < results.length; ++i) {
			int docId = results[i].doc;
			Document d = indexSearcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("URL"));
			//System.out.println("DocID: "+d.get("DocId"));
			System.out.println("Title: "+d.get("Title"));
		}*/
		
		ArrayList<String> answer= new ArrayList<String>();
		String str="";
		//System.out.println("Results Length: " + results.length);
		for (int i = 0; i < results.length; ++i) {
			str="";
			int docId = results[i].doc;
			Document d = indexSearcher.doc(docId);
			//System.out.println((i + 1) + ". " + d.get("URL"));
			//System.out.println("DocID: "+d.get("DocId"));
			//System.out.println("Title: "+d.get("Title"));
			str=str+d.get("URL")+":::"+d.get("Title");
			answer.add(str);
		}
		
		TreeMap<String, String> tree= getData(indexSearcher, results);
		indexReader.close();

		SearchBean p= new SearchBean(answer, tree,hitResults);
		System.out.println(tree);
		return p;
	}
	public static DirectedSparseGraph<String, String> getGraph(ScoreDoc[] results, IndexSearcher indexSearcher) throws IOException
	{
		DirectedSparseGraph<String, String> g= new DirectedSparseGraph();
		int count=0;
		for (int i = 0; i < results.length; ++i) 
		{
			int docId = results[i].doc;
			Document d = indexSearcher.doc(docId);	
			String[] outgoinglinks= d.get("OutGoingLinks").split(" ");
			g.addVertex(d.get("URL"));
			for(String s: outgoinglinks)
			{
				if(g.addVertex(s))
				{
					g.addEdge(Integer.toString(count), d.get("URL"), s);
					count++;
				}
			}
		}
		return g;
	}
	
	public static HashMap<String, Double> sortByValue(HashMap<String, Double> unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
	}

	public TreeMap<String, String> getData(IndexSearcher indexSearcher, ScoreDoc[] results) throws IOException
	{
		TreeMap<String, String> answer= new TreeMap<String, String>();
		for (int i = 0; i < results.length; ++i) {
			int docId = results[i].doc;
			Document d = indexSearcher.doc(docId);
			//System.out.println((i + 1) + ". " + d.get("URL"));
			//System.out.println("DocID: "+d.get("DocId"));
			//System.out.println("Title: "+d.get("Title"));
			//s=s+d.get("URL")+":::"+d.get("Title");
			answer.put(d.get("DocId"), d.get("Text"));
		}
		return answer;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
		
		SearchIndex si = new SearchIndex();
		SearchBean obj = (SearchBean)si.searchIndex("Indian Flavours");
		System.out.println(obj.titleString);
		
	}
	
		List<String> getTokenStream(String queryText, StandardAnalyzer analyzer) throws IOException {
        StringReader stringReader = new StringReader(queryText);
        TokenStream tokenStream = analyzer.tokenStream(queryText, stringReader);
        CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
     //   OffsetAttribute offsetAtt = tokenStream.addAttribute(OffsetAttribute.class);
        List<String> queryTerms = new ArrayList<String>();
        try {
 
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                queryTerms.add(termAtt.toString());
            }
            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            tokenStream.close();
            tokenStream.close();
        }
        return queryTerms;
    }

}
