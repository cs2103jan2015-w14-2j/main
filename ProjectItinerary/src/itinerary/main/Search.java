package itinerary.main;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.TermQuery;
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
import org.apache.lucene.search.TermRangeQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
/**
 * 
 * README:to use this, first create a search object,specifying if youre searching by date in the constructor
 * then call search.query
 * search by date takes in 2 calendar objects and the field to search in.it returns any date that falls in between the two
 * given calendar dates inclusive.
 * 
 *
 */
//@author A0121810Y
public class Search {
	private static List<String> list;
	private static JsonParser parser;
	private static JsonObject obj;
	private static List<String> typeList;
	ArrayList<String> hitTypeList;
	private static final String ERROR_IO = "Error attempting to search.";
	ArrayList<String> hitList;
	StandardAnalyzer analyzer;
	IndexSearcher searcher;
	IndexReader reader;
	IndexWriter writer;
	Document doc;
	boolean isDate;
	public <T extends Task> Search(List<T> taskList,boolean booleanOrDateSearch) throws SearchException{
		isDate = booleanOrDateSearch;
		list = JsonConverter.convertTaskList(taskList);
		parser = new JsonParser();
		typeList = JsonConverter.getTypeList(taskList);
		analyzer = new StandardAnalyzer();
		hitList = new ArrayList<String>();
		
		//create the index
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		createIndex(index, config);
				
	}
	
	private void createIndex(Directory index, IndexWriterConfig config)
            throws SearchException {
	    try {
	        writer = new IndexWriter(index, config);
	        addDocs(writer);
			closeWriter(writer);
			reader = DirectoryReader.open(index);
			searcher = new IndexSearcher(reader);
		} catch (IOException e) {
			throw new SearchException(ERROR_IO);
		}
    }
	//this function takes in the query and the field that it is supposed to check. Fields are those found in Task and its subclasses.
	public <T extends Task> List<T> query(String query,String field) throws SearchException {
		// The same analyzer should be used for indexing and searching
		
		try {
			BooleanQuery q = new BooleanQuery();
			String[] splitQuery = splitQuery(query);
			createQuery(field, q, splitQuery);
			ScoreDoc[] hits = searchQuery(q, searcher);
			addToHitList(hitList, searcher, hits);
			displayHits(searcher, hits);
			reader.close();
		} catch (IOException e) {
			throw new SearchException(ERROR_IO);
		}
		return JsonConverter.convertJsonList(hitList,hitTypeList);
	}
	public <T extends Task> List<T> query(Calendar toDate,Calendar fromDate,String field) throws SearchException{
		Gson gson = new Gson();
		BooleanQuery q = new BooleanQuery();
		TermRangeQuery rangeQ = TermRangeQuery.newStringRange(field,gson.toJson(fromDate),gson.toJson(toDate),true,true);
		q.add(rangeQ,BooleanClause.Occur.MUST);
		
		try {
			ScoreDoc[] hits = searchQuery(q, searcher);
			addToHitList(hitList, searcher, hits);
	        displayHits(searcher, hits);
			reader.close();
		} catch (IOException e) {
			throw new SearchException(ERROR_IO);
		}
		return JsonConverter.convertJsonList(hitList,hitTypeList);
	}
	public <T extends Task> List<T> query(boolean setTrue,String field) throws SearchException{
		String isTrue = (setTrue ? "true" : "false");
		BooleanQuery q = new BooleanQuery();
		TermQuery termQuery = new TermQuery(new Term(field,isTrue));
		q.add(termQuery,BooleanClause.Occur.MUST);
		try {
			ScoreDoc[] hits = searchQuery(q, searcher);
			addToHitList(hitList, searcher, hits);
	        displayHits(searcher, hits);
			reader.close();
		} catch (IOException e) {
			throw new SearchException(ERROR_IO);
		}
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
	private void closeWriter(IndexWriter writer) throws IOException {
	    writer.close();
    }
	private void addDocs(IndexWriter writer) throws IOException {
	    for ( String tasks : list  ){
			obj = parser.parse(tasks).getAsJsonObject();
			addDoc(writer ,obj);
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

	private void addDoc(IndexWriter writer, JsonObject obj)
	        throws IOException {
		doc = new Document();
		if(isDate){
			for (Entry<String, JsonElement> entry : obj.entrySet()) {
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				doc.add(new StringField(key, value.toString(), Field.Store.YES));
			}
		} else {
			for (Entry<String, JsonElement> entry : obj.entrySet()) {
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				doc.add(new TextField(key, value.toString(), Field.Store.YES));
			}
		}
		
		doc.add(new StringField("json",obj.toString(),Field.Store.YES));
		writer.addDocument(doc);
	}
	
}
