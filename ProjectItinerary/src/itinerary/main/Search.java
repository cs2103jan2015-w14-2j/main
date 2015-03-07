package itinerary.main;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

//@author A0121810Y
public class Search {
	private static List<String> list;
	private static JsonParser parser;
	private static JsonObject obj;
	private static List<String> typeList;
	ArrayList<String> hitTypeList;

	public <T extends Task> Search(List<T> taskList){
		list = JsonConverter.convertTaskList(taskList);
		parser = new JsonParser();
		typeList = JsonConverter.getTypeList(taskList);
	}
	//this function takes in the query and the field that it is supposed to check. Fields are those found in Task and its subclasses.
	public <T extends Task> List<T> query(String query,String field) throws IOException, ParseException {
		// The same analyzer should be used for indexing and searching
		StandardAnalyzer analyzer = new StandardAnalyzer();
		ArrayList<String> hitList = new ArrayList<String>();
		// 1. create the index
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		BooleanQuery q = new BooleanQuery();
		addDocs(w);
		closeWriter(w);
		String[] splitQuery = splitQuery(query);
		createQuery(field, q, splitQuery);
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc[] hits = searchQuery(q, searcher);
		addToHitList(hitList, searcher, hits);
		displayHits(searcher, hits);
		reader.close();
		return JsonConverter.convertJsonList(hitList,hitTypeList);
	}
	private ScoreDoc[] searchQuery(BooleanQuery q, IndexSearcher searcher)
            throws IOException {
	    int numHits = 10;
		TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    return hits;
    }
	private void closeWriter(IndexWriter w) throws IOException {
	    w.close();
    }
	private void addDocs(IndexWriter w) throws IOException {
	    for ( String tasks : list  ){
			obj = parser.parse(tasks).getAsJsonObject();
			addDoc(w ,obj);
		}
    }
	private String[] splitQuery(String query) {
	    String[] splitQuery = query.split(" ");
		for(int i=0;i<splitQuery.length;i++){
			splitQuery[i] = splitQuery[i]+"*";
		}
	    return splitQuery;
    }
	private void createQuery(String field, BooleanQuery q, String[] splitQuery) {
	    Query wildQ = getWildCardQuery(field, splitQuery);
		Query fuzzyQ = getFuzzyQuery(field,splitQuery);
		q.add(fuzzyQ,BooleanClause.Occur.SHOULD);
		q.add(wildQ,BooleanClause.Occur.SHOULD);
    }
	private Query getWildCardQuery(String field, String[] splitQuery) {
	    SpanQuery[] queryParts = new SpanQuery[splitQuery.length];
	    for (int i = 0; i < splitQuery.length; i++) {
	        WildcardQuery wildQuery = new WildcardQuery(new Term(field, splitQuery[i]));
	        queryParts[i] = new SpanMultiTermQueryWrapper<WildcardQuery>(wildQuery);
	    }
	    SpanNearQuery q = new SpanNearQuery(queryParts,5,true);
	    return q;
    }
	private Query getFuzzyQuery(String field, String[] splitQuery) {
	    SpanQuery[] queryParts = new SpanQuery[splitQuery.length];
	    for (int i = 0; i < splitQuery.length; i++) {
	        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(field, splitQuery[i]));
	        queryParts[i] = new SpanMultiTermQueryWrapper<FuzzyQuery>(fuzzyQuery);
	    }
	    SpanNearQuery q = new SpanNearQuery(queryParts,5,true);
	    return q;
    }
	private void addToHitList(ArrayList<String> hitList,
            IndexSearcher searcher, ScoreDoc[] hits) throws IOException {
		hitTypeList = new ArrayList<String>();
	    for(int i = 0;i<hits.length;i++){
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			hitList.add(d.get("json"));
			hitTypeList.add(typeList.get(docId));
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
