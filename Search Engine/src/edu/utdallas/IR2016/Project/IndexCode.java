package edu.utdallas.IR2016.Project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class IndexCode {

	public static DirectedSparseGraph g= new DirectedSparseGraph();
	public static int p=0;
	public static void main(String[] args) throws IOException,
			FileNotFoundException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {

		File pathToIndex = new File("C:\\Users\\Alay\\workspace\\Project_IR\\Index");
		File pathToCrawled = new File("H:\\crawlData");

		// Analyze which part of the document is to be indexed
		// KeywordAnalyzer analyzer = new KeywordAnalyzer();
		// SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_40);
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		Directory directory = FSDirectory.open(pathToIndex);
		//Directory directory = new RAMDirectory();
		IndexWriterConfig indexConfig = new IndexWriterConfig(
				Version.LUCENE_40, analyzer);

		IndexWriter indexWriter = new IndexWriter(directory, indexConfig);

		if (pathToCrawled.isDirectory()) {
			File[] files = pathToCrawled.listFiles();

			for (File file : files) {
				if (file.canRead()) {
					addToIndex(file, indexWriter);
				}

			}
		}

		indexWriter.close();

		/*// Query Preparation
		String queryString = "permanent";
		QueryParser queryParser = new QueryParser(Version.LUCENE_40, "data",
				analyzer);
		queryParser.setDefaultOperator(Operator.AND);

		Query query = queryParser.parse(queryString);

		// Search
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
		indexSearcher.search(query, collector);
		System.out.println("Total Hits: "+collector.getTotalHits());
		ScoreDoc[] results = collector.topDocs().scoreDocs;

		// Display Results
		System.out.println("Results Length: " + results.length);
		for (int i = 0; i < results.length; ++i) {
			int docId = results[i].doc;
			Document d = indexSearcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("url"));
			System.out.println("DocID: "+d.get("docID"));
			System.out.println("Title: "+d.get("title"));
		}

		indexReader.close();*/

	}

	private static void addToIndex(File file, IndexWriter indexWriter)
			throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		//System.out.println(file.getName());
		try{
		Object obj = jsonParser.parse(new FileReader(file));
		JSONObject jsonObject = (JSONObject) obj;

		String url = jsonObject.get("URL").toString();
		String data = jsonObject.get("Text").toString();
		String docID = jsonObject.get("DocId").toString();
		String title = "";
		String outgoinglinks= jsonObject.get("OutGoingLinks").toString();
		String[] out= outgoinglinks.split(" ");
		g.addVertex(url);
		for(String s: out)
		{
			if(g.addVertex(s))
			{
				g.addEdge(Integer.toString(p), url, s);
				p++;
			}
		}
		if(jsonObject.get("Title")!=null)
			title = jsonObject.get("Title").toString();

		// System.out.println(data);
		// System.out.println(url);

		Document document = new Document();
		document.add(new StringField("URL", url, Field.Store.YES));
		document.add(new TextField("Text", data, Field.Store.YES));
		document.add(new StringField("DocId", docID, Field.Store.YES));
		document.add(new TextField("Title", title, Field.Store.YES));
		document.add(new StringField("OutGoingLinks", outgoinglinks, Field.Store.YES));


		indexWriter.addDocument(document);
		} catch(Exception e)
		{
			System.out.println(file.getName());
			return;
		}

	}

}
