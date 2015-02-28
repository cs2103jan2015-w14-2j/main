package itinerary.main;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class Search {
	private static List<String> list;
	private static JsonParser parser;
	private static JsonObject obj;
	public Search(List<String> taskList){
		list = taskList;
		parser = new JsonParser();
	}
	//this function takes in the query and the field that it is supposed to check. Fields are those found in Task and its subclasses.
	public List<String> query(String query,String field) throws IOException, ParseException {
		// The same analyzer should be used for indexing and searching
		StandardAnalyzer analyzer = new StandardAnalyzer();
		ArrayList<String> hitList = new ArrayList<String>();
		// 1. create the index
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		
		for ( String tasks : list  ){
			obj = parser.parse(tasks).getAsJsonObject();
			addDoc(w ,obj);
		}
		
		w.close();
		//create the queryparser, field is the key, query is the value to be searched.
		Query q = new QueryParser(field, analyzer).parse(query);
		int numHits = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		//creates the collector, num hits is the max number of hits that it returns
		TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		//add to the hitList
		addToHitList(hitList, searcher, hits);
		displayHits(searcher, hits);
		reader.close();
		return hitList;
	}
	private void addToHitList(ArrayList<String> hitList,
            IndexSearcher searcher, ScoreDoc[] hits) throws IOException {
	    for(int i = 0;i<hits.length;i++){
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			hitList.add(d.get("json"));
		}
    }
	private void displayHits(IndexSearcher searcher, ScoreDoc[] hits)
            throws IOException {
	    System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("text"));
		}
    }

	private static void addDoc(IndexWriter w, JsonObject obj )
	        throws IOException {
		Document doc = new Document();
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			   String key = entry.getKey();
			   JsonElement value = entry.getValue();
			   doc.add(new TextField(key, value.toString(), Field.Store.YES));

		}
		doc.add(new TextField("json",obj.toString(),Field.Store.YES));
		w.addDocument(doc);
	}
}
