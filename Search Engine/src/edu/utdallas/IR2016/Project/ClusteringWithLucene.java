package edu.utdallas.IR2016.Project;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import java.io.File;


public class ClusteringWithLucene {

	
	static float scoreThreshold = 0.8f;
	public static HashMap<String, ArrayList<String>> docClusters = new HashMap<String, ArrayList<String>>();

	public static void cluster() throws IOException {
		IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File("")));
		IndexSearcher searcher = new IndexSearcher(indexReader);
		try {
			int maxDocNum = indexReader.maxDoc();
			for (int docNum = 0; docNum < maxDocNum; docNum++) {

				// Save doc to result list
				Document doc = indexReader.document(docNum);
				ArrayList<String> similarDocs = new ArrayList<String>();
				MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
				// Lower the frequency since content is short
				moreLikeThis.setMinTermFreq(100);
				moreLikeThis.setMinDocFreq(100);
				String content = doc.get("content");
				// Find Similar doc
				Reader reader = new StringReader(content);
				Query query = moreLikeThis.like(reader, "content");

				TopDocs topDocs = searcher.search(query, 100);
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					if (scoreDoc.score > scoreThreshold) {
						similarDocs.add(searcher.doc(scoreDoc.doc).get("filename"));
					}
				}
				docClusters.put(doc.get("filename"), similarDocs);
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void closeIndexReader(IndexReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
