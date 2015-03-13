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
	private static String[] possibleSearchFields = {"isComplete","isPriority","text","to","from","category"};
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
	 
	public <T extends Task> Search(List<T> taskList) throws SearchException {
		list = JsonConverter.convertTaskList(taskList);
		parser = new JsonParser();
		typeList = JsonConverter.getTypeList(taskList);
		analyzer = new StandardAnalyzer();
		hitList = new ArrayList<String>();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		createIndex(index, config);
		
		
	}
	public <T extends Task> List<T> query(SearchTask task) throws SearchException{
		List<String> searchField = task.getSearchField();
		List<String> searchNotField = task.getSearchNotField();
		BooleanQuery q = new BooleanQuery();
		if(searchField != null){
			for(int i =0;i<searchField.size();i++){
				if(searchField.get(i).equals("isComplete")){
					TermQuery cpQuery = createCompleteOrPriorityQuery( task.isComplete(),"isComplete");
					q.add(cpQuery,BooleanClause.Occur.MUST);
				}
				if(searchField.get(i).equals("isPriority")){
					TermQuery cpQuery = createCompleteOrPriorityQuery( task.isPriority(),"isPriority");
					q.add(cpQuery,BooleanClause.Occur.MUST);
				}
				if(searchField.get(i).equals("text")){
					BooleanQuery bQuery = createQuery("text",task.getText());
					q.add(bQuery,BooleanClause.Occur.MUST);
				}
				if(searchField.get(i).equals("category")){
					List<String> categoryList = task.getCategoryList();
					BooleanQuery bQuery = new BooleanQuery();
					for(int j=0;j< categoryList.size();j++){
						BooleanQuery cQuery = createQuery("category",categoryList.get(j));
						bQuery.add(cQuery,BooleanClause.Occur.SHOULD);
						System.out.println(cQuery.toString());
					}
					q.add(bQuery,BooleanClause.Occur.MUST);
					
				}
				if(searchField.get(i).equals("date")){
					BooleanQuery bQuery = createDateQuery(task.getToDate(),task.getFromDate());
					q.add(bQuery,BooleanClause.Occur.MUST);
				}
			}
		}
		searchQuery(q,searcher);
		ScoreDoc[] hits = searchQuery(q, searcher);
		try {
	        addToHitList(hitList, searcher, hits);
	        displayHits(searcher, hits);
        } catch (IOException e) {
	        throw new SearchException(ERROR_IO);
        }
		return JsonConverter.convertJsonList(hitList,hitTypeList);
	}
	public <T extends Task> List<T> query(String query,String field) throws SearchException {
		// The same analyzer should be used for indexing and searching
		
		try {
			BooleanQuery q = new BooleanQuery();
			q = createQuery(field,query);
			ScoreDoc[] hits = searchQuery(q, searcher);
			addToHitList(hitList, searcher, hits);
			displayHits(searcher, hits);
			reader.close();
		} catch (IOException e) {
			throw new SearchException(ERROR_IO);
		}
		return JsonConverter.convertJsonList(hitList,hitTypeList);
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

	private ScoreDoc[] searchQuery(BooleanQuery q, IndexSearcher searcher) throws SearchException {
	    int numHits = 10;
		TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);
		try {
	        searcher.search(q, collector);
        } catch (IOException e) {
	        throw new SearchException(ERROR_IO);
        }
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    return hits;
    }

	private BooleanQuery createQuery(String field, String Query) {
		String[] splitQuery = Query.split(" ");
		BooleanQuery bQuery = new BooleanQuery();
	    Query wildQ = getWildCardQuery(field, splitQuery);
		Query fuzzyQ = getFuzzyQuery(field,splitQuery);
		bQuery.add(fuzzyQ,BooleanClause.Occur.SHOULD);
		bQuery.add(wildQ,BooleanClause.Occur.SHOULD);
		return bQuery;
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

	public TermQuery createCompleteOrPriorityQuery(boolean setTrue,String field) throws SearchException{
		String isTrue = (setTrue ? "true" : "false");
		TermQuery termQuery = new TermQuery(new Term(field,isTrue));
		return termQuery;
	}

	public BooleanQuery createDateQuery(Calendar toDate,Calendar fromDate) throws SearchException{
		Gson gson = new Gson();
		BooleanQuery bQuery = new BooleanQuery();
		TermRangeQuery rangeDeadlineQ = TermRangeQuery.newStringRange("deadline",gson.toJson(fromDate),gson.toJson(toDate),true,true);
		TermRangeQuery rangeScheduleQ = TermRangeQuery.newStringRange("toDate",gson.toJson(fromDate),gson.toJson(toDate),true,true);
		bQuery.add(rangeDeadlineQ,BooleanClause.Occur.SHOULD);
		bQuery.add(rangeScheduleQ,BooleanClause.Occur.SHOULD);
		return bQuery;
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
	private void addDocs(IndexWriter writer) throws IOException {
	    for ( String tasks : list  ){
			obj = parser.parse(tasks).getAsJsonObject();
			addDoc(writer ,obj);
		}
    }
	private void addDoc(IndexWriter writer, JsonObject obj)
	        throws IOException {
		doc = new Document();
		
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if(key.equals("text") || key.equals("category")){
				doc.add(new TextField(key, value.toString(), Field.Store.YES));
			} else {
				doc.add(new StringField(key, value.toString(), Field.Store.YES));
			}
		}
		
		doc.add(new StringField("json",obj.toString(),Field.Store.YES));
		writer.addDocument(doc);
	}
	private void closeWriter(IndexWriter writer) throws IOException {
	    writer.close();
    }

	
}
