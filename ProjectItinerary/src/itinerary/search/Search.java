package itinerary.search;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
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

import itinerary.main.Constants;
import itinerary.main.JsonConverter;
import itinerary.main.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
/**
 * 
 * 
 *
 */
//@author A0121810Y
public class Search {
	private static final String LOGGER_IOERROR = "Error searching through index";
	private static final String FIELD_ISPRIORITY = "isPriority";
	private static final String FIELD_ISCOMPLETE = "isComplete";
	private static final String FIELD_JSON = "json";
	private static final String FIELD_CATEGORY = "category";
	private static final String FIELD_TEXT = "text";
	private static final String FIELD_FROMDATE = "fromDate";
	private static final String FIELD_TODATE = "toDate";
	private static final String FIELD_DEADLINE = "deadline";
	private static final String DATE_EPOCH_ERROR = "Error parsing Epoch date";
	private static final String DATE_SIMPLEFORMAT = "dd-M-yyyy hh:mm:ss";
	private static final String DATE_EPOCH = "01-01-1970 00:00:00";
	private static String[] possibleSearchFields = {FIELD_ISCOMPLETE,FIELD_ISPRIORITY,FIELD_TEXT,"to","from",FIELD_CATEGORY};
	private static List<String> list;
	private static JsonParser parser;
	private static JsonObject obj;
	private static List<String> typeList;
	ArrayList<String> hitTypeList;
	private static final String ERROR_IO = "Error attempting to search.";
	private static final Logger logger = Logger.getGlobal();
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
		CharArraySet stopWords = new CharArraySet(5,false);
		analyzer = new StandardAnalyzer(stopWords);
		//analyzer = new StandardAnalyzer();
		hitList = new ArrayList<String>();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		createIndex(index, config);
		
		
	}
	
	public <T extends Task> List<T> query(SearchTask task) throws SearchException{
		assert task!= null;
		List<String> searchField = task.getSearchField();
		List<String> searchNotField = task.getSearchNotField();
		BooleanQuery q = new BooleanQuery();
		if(searchField != null){
			for(int i =0;i<searchField.size();i++){
				if(searchingForComplete(searchField, i)){
					TermQuery cpQuery = createCompleteQuery(task);
					addMustOccur(q, cpQuery);
				}
				if(searchingForPriority(searchField, i)){
					TermQuery cpQuery = createPriorityQuery(task);
					addMustOccur(q, cpQuery);
				}
				if(searchingForText(searchField, i)){
					BooleanQuery bQuery = createTextQuery(task);
					addMustOccur(q, bQuery);
				}
				if(searchingForCategory(searchField, i)){
					List<String> categoryList = task.getCategoryList();
					BooleanQuery bQuery = new BooleanQuery();
					for(int j=0;j< categoryList.size();j++){
						BooleanQuery cQuery = createCategoryQuery(categoryList,
                                j);
						addShouldOccur(bQuery, cQuery);
					}
					addMustOccur(q, bQuery);
					
				}
				if(searchField.get(i).equals(FIELD_DEADLINE)){
					BooleanQuery bQuery = createDeadlineQuery(task);
					addMustOccur(q, bQuery);
				}
			}
		}
		
		try {
			search(q);
        } catch (IOException e) {
        	logger.log(Level.SEVERE, LOGGER_IOERROR, e);
	        throw new SearchException(ERROR_IO);
        }
		return JsonConverter.convertJsonList(hitList,hitTypeList);
	}

	private void search(BooleanQuery q) throws SearchException, IOException {
	    ScoreDoc[] hits = searchQuery(q, searcher);
	    addToHitList(hitList, searcher, hits);
	    displayHits(searcher, hits);
    }

	private BooleanQuery createDeadlineQuery(SearchTask task)
            throws SearchException {
	    BooleanQuery bQuery = createDeadlineQuery(task.getDeadline());
	    return bQuery;
    }

	private boolean searchingForCategory(List<String> searchField, int i) {
	    return searchField.get(i).equals(FIELD_CATEGORY);
    }

	private boolean searchingForComplete(List<String> searchField, int i) {
	    return searchField.get(i).equals(FIELD_ISCOMPLETE);
    }

	private boolean searchingForPriority(List<String> searchField, int i) {
	    return searchField.get(i).equals(FIELD_ISPRIORITY);
    }

	private boolean searchingForText(List<String> searchField, int i) {
	    return searchField.get(i).equals(FIELD_TEXT);
    }

	private BooleanQuery createTextQuery(SearchTask task) {
	    BooleanQuery bQuery = createQuery(FIELD_TEXT,task.getText());
	    return bQuery;
    }

	private BooleanQuery createCategoryQuery(List<String> categoryList, int j) {
	    BooleanQuery cQuery = createQuery(FIELD_CATEGORY,categoryList.get(j));
	    return cQuery;
    }

	private void addShouldOccur(BooleanQuery bQuery, BooleanQuery cQuery) {
	    bQuery.add(cQuery,BooleanClause.Occur.SHOULD);
    }

	private void addMustOccur(BooleanQuery q, BooleanQuery bQuery) {
	    q.add(bQuery,BooleanClause.Occur.MUST);
    }

	private void addMustOccur(BooleanQuery q, TermQuery cpQuery) {
	    q.add(cpQuery,BooleanClause.Occur.MUST);
    }

	private TermQuery createPriorityQuery(SearchTask task)
            throws SearchException {
	    TermQuery cpQuery = createCompleteOrPriorityQuery( task.isPriority(),FIELD_ISPRIORITY);
	    return cpQuery;
    }

	private TermQuery createCompleteQuery(SearchTask task)
            throws SearchException {
	    TermQuery cpQuery = createCompleteOrPriorityQuery( task.isComplete(),FIELD_ISCOMPLETE);
	    return cpQuery;
    }
	public <T extends Task> List<T> query(String query,String field) throws SearchException {
		// The same analyzer should be used for indexing and searching
		logger.log(Level.INFO, "Searching for: " + query);
		try {
			
			BooleanQuery q = new BooleanQuery();
			q = createQuery(field,query.toLowerCase());
			search(q);
			reader.close();
		} catch (IOException e) {
        	logger.log(Level.SEVERE, "Error searching through index ()", e);
			throw new SearchException(ERROR_IO);
		}
		return JsonConverter.convertJsonList(hitList,hitTypeList);
	}
	private void addToHitList(ArrayList<String> hitList,
            IndexSearcher searcher, ScoreDoc[] hits) throws IOException {
		hitTypeList = new ArrayList<String>();
	    for(int i = 0;i<hits.length;i++){
			addHitList(hitList, searcher, hits, i);
		}
    }

	private void addHitList(ArrayList<String> hitList, IndexSearcher searcher,
            ScoreDoc[] hits, int i) throws IOException {
	    int docId = hits[i].doc;
	    Document d = searcher.doc(docId);
	    hitList.add(d.get(FIELD_JSON));
	    hitTypeList.add(typeList.get(docId));
    }
	private void displayHits(IndexSearcher searcher, ScoreDoc[] hits)
            throws IOException {
	    System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get(FIELD_TEXT));
		}
    }

	private ScoreDoc[] searchQuery(BooleanQuery q, IndexSearcher searcher) throws SearchException {
	    int numHits = 10;
		TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);
		try {
	        searcher.search(q, collector);
        } catch (IOException e) {
        	logger.log(Level.SEVERE, "Error searching through index ()", e);
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
		System.out.println(bQuery.toString());
		return bQuery;
    }
	private Query getWildCardQuery(String field, String[] splitQuery) {
	    SpanQuery[] queryParts = new SpanQuery[splitQuery.length];
	    for (int i = 0; i < splitQuery.length; i++) {
	        WildcardQuery wildQuery = new WildcardQuery(new Term(field, splitQuery[i]+"*"));
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
		String isTrue = setTrueOrFalse(setTrue);
		TermQuery termQuery = new TermQuery(new Term(field,isTrue));
		return termQuery;
	}

	private String setTrueOrFalse(boolean setTrue) {
	    return setTrue ? "true" : "false";
    }
	public BooleanQuery createDeadlineQuery(Calendar deadline) throws SearchException{
		Gson gson = new Gson();
		Calendar fromDate = getEpoch();
		BooleanQuery bQuery = new BooleanQuery();
		TermRangeQuery rangeDeadlineQ = TermRangeQuery.newStringRange(FIELD_DEADLINE,gson.toJson(fromDate),gson.toJson(deadline),true,true);
		bQuery.add(rangeDeadlineQ,BooleanClause.Occur.MUST);
		return bQuery;
	}	
	public BooleanQuery createDateQuery(Calendar toDate,Calendar fromDate) throws SearchException{
		Gson gson = new Gson();
		BooleanQuery bQuery = new BooleanQuery();
		Calendar epochDate = getEpoch();
		TermRangeQuery rangeDeadlineQ = TermRangeQuery.newStringRange(FIELD_DEADLINE,gson.toJson(fromDate),gson.toJson(toDate),true,true);
		TermRangeQuery rangeScheduleQ = TermRangeQuery.newStringRange(FIELD_TODATE,gson.toJson(fromDate),gson.toJson(toDate),true,true);
		TermRangeQuery rangeFromScheduleQ = TermRangeQuery.newStringRange(FIELD_FROMDATE, gson.toJson(epochDate), gson.toJson(fromDate), false, true);
		bQuery.add(rangeDeadlineQ,BooleanClause.Occur.SHOULD);
		bQuery.add(rangeScheduleQ,BooleanClause.Occur.SHOULD);
		bQuery.add(rangeFromScheduleQ,BooleanClause.Occur.MUST_NOT);
		return bQuery;
	}	
	public BooleanQuery createWeekQuery(Calendar day) throws SearchException{
		Gson gson = new Gson();
		Date dayDate = day.getTime();
		List<Date> weekRange = getDatesForThisWeek(dayDate);
		Calendar fromDate = dateToCalendar(weekRange.get(0));
		Calendar toDate = dateToCalendar(weekRange.get(1));
		BooleanQuery bQuery = new BooleanQuery();
		TermRangeQuery rangeDeadlineQ = TermRangeQuery.newStringRange(FIELD_DEADLINE,gson.toJson(fromDate),gson.toJson(toDate),true,true);
		bQuery.add(rangeDeadlineQ,BooleanClause.Occur.MUST);
		return bQuery;
	}	
	private Calendar dateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
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
			if(key.equals(FIELD_TEXT) || key.equals(FIELD_CATEGORY)){
				doc.add(new TextField(key, value.toString(), Field.Store.YES));
			} else {
				doc.add(new StringField(key, value.toString(), Field.Store.YES));
			}
		}
		
		doc.add(new StringField(FIELD_JSON,obj.toString(),Field.Store.YES));
		writer.addDocument(doc);
	}
	private void closeWriter(IndexWriter writer) throws IOException {
	    writer.close();
    }
	private Calendar getEpoch() throws SearchException{
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_SIMPLEFORMAT);
		String dateInString = DATE_EPOCH;
		Date date;
        Calendar fromDate = Calendar.getInstance();
        try {
	        date = sdf.parse(dateInString);
			fromDate.setTime(date);
        } catch (ParseException e) {
        	logger.log(Level.SEVERE, DATE_EPOCH_ERROR, e);
        	throw new SearchException(ERROR_IO);
        }
        assert(fromDate.getTime().toString().equals(date.toString()));
		return fromDate;
	}
	private List<Date> getDatesForThisWeek(Date date) {
		List<Date> dates = new ArrayList<Date>();

		LocalDate today = date.toInstant().atZone(ZoneId.systemDefault())
		        .toLocalDate();
		LocalDate monday = today.with(previousOrSame(MONDAY));
		LocalDate sunday = today.with(nextOrSame(SUNDAY));
		dates.add(Date.from(monday.atStartOfDay(ZoneId.systemDefault())
		        .toInstant()));
		dates.add(Date.from(sunday.atStartOfDay(ZoneId.systemDefault())
		        .toInstant()));
		System.out.println("Today: " + today);
		System.out.println("Monday of the Week: " + monday);
		System.out.println("Sunday of the Week: " + sunday);
		return dates;

	}
}
