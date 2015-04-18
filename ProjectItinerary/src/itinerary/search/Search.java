package itinerary.search;

import itinerary.main.JsonConverter;
import itinerary.main.Task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//@author A0121810Y
public class Search {
	private static final String WORD_FALSE = "false";
	private static final String WORD_TRUE = "true";
	private static final String WORD_AND = " and ";
	private static final String SPECIALCHARACTER_AND = "&";
	private static final String PARSE_DELIMITER = " ";
	private static final String EXCEPT_SPECIALCHARACTER = "[^a-zA-Z0-9]";
	private static final int NUM_HITS = 10;
	private static final int JSON_SECOND = 24;
	private static final int JSON_MINUTE = 20;
	private static final String WILDCARD = "*";
	private static final String QUERY_DELIMITER = PARSE_DELIMITER;
	private static final String DISPLAY_SEARCH = "Searching for: ";
	private static final String ERROR_INDEX = "Error searching through index";
	private static final int SPAN_DISTANCE = 5;
	private static final int LEVENSHTEIN_0 = 0;
	private static final int LEVENSHTEIN_DISTANCE = 2;
	private static final int JSON_HOUROFDAY = 16;
	private static final int JSON_DATE = 12;
	private static final int JSON_MONTH = 8;
	private static final int JSON_YEAR = 4;
	private static final String JSON_DELIMITER = ",|\"|:|\\{|\\}";
	private static final int MIN_LENGTH = 2;
	private static final String LOGGER_IOERROR = ERROR_INDEX;
	private static final String FIELD_ISPRIORITY = "isPriority";
	private static final String FIELD_ISCOMPLETE = "isComplete";
	private static final String FIELD_JSON = "json";
	private static final String FIELD_CATEGORY = "category";
	private static final String FIELD_TEXT = "text";
	private static final String DATE_EPOCH_ERROR = "Error parsing Epoch date";
	private static final String DATE_SIMPLEFORMAT = "dd-M-yyyy hh:mm:ss";
	private static final String DATE_EPOCH = "01-01-1970 00:00:00";
	private static List<String> list;
	private static JsonParser parser;
	private static JsonObject obj;
	private static List<String> typeList;
	ArrayList<String> hitTypeList;
	private static final String ERROR_IO = "Error attempting to search.";
	private static final Logger logger = Logger.getGlobal();
	private static final String FIELD_FROMDATE = "fromDate";
	private static final String FIELD_TODATE = "toDate";
	private static final String FIELD_DEADLINE = "deadline";
	ArrayList<String> hitList;
	StandardAnalyzer analyzer;
	IndexSearcher searcher;
	IndexReader reader;
	IndexWriter writer;
	Document doc;
	Gson gson;

	/**
	 * Search constructor that indexes the current tasklist using a
	 * StandardAnalyzer and the default IndexWriter config.The tasklist must
	 * first be converted to Json. Does not use the default STOP_WORDS_SET
	 * provided by Lucene.
	 * 
	 * @param taskList
	 * @throws SearchException
	 */
	public <T extends Task> Search(List<T> taskList) throws SearchException {
		list = JsonConverter.convertTaskList(taskList);
		parser = new JsonParser();
		typeList = JsonConverter.getTypeList(taskList);
		CharArraySet stopWords = new CharArraySet(5, false);
		analyzer = new StandardAnalyzer(stopWords);
		hitList = new ArrayList<String>();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		createIndex(index, config);
		gson = new Gson();
	}

	/**
	 * Creates a query out of a given SearchTask then searches it. Does this by
	 * building a boolean query out of the not-null fields within SearchTask
	 * 
	 * @param task
	 * @return
	 * @throws SearchException
	 */
	public <T extends Task> List<T> query(SearchTask task)
	        throws SearchException {
		assert task != null;
		BooleanQuery q = new BooleanQuery();

		if (task.isComplete() != null) {
			TermQuery cpQuery = createCompleteQuery(task);
			addMustOccur(q, cpQuery);
		}
		if (task.isPriority() != null) {
			TermQuery cpQuery = createPriorityQuery(task);
			addMustOccur(q, cpQuery);
		}
		if (task.getText() != null) {
			BooleanQuery bQuery = createTextQuery(task);
			addMustOccur(q, bQuery);
		}

		if (task.getCategory() != null) {
			BooleanQuery bQuery = createCategoryQuery(task);
			addMustOccur(q, bQuery);

		}
		if (task.getToDate() != null && task.getFromDate() != null) {
			BooleanQuery bQuery = createDateQuery(task.getToDate(),
			        task.getFromDate());
			addMustOccur(q, bQuery);
		}

		try {
			search(q);
		} catch (IOException e) {
			logger.log(Level.SEVERE, LOGGER_IOERROR, e);
			throw new SearchException(ERROR_IO);
		}
		return JsonConverter.convertJsonList(hitList, hitTypeList);
	}

	/**
	 * creates a query that searches through the category stated in the given
	 * SearchTask
	 * 
	 * @param task
	 * @return
	 */
	private BooleanQuery createCategoryQuery(SearchTask task) {
		BooleanQuery bQuery = createQuery(FIELD_CATEGORY, task.getCategory());
		return bQuery;
	}

	/**
	 * Searches a boolean query, all matches are added to a hitList
	 * 
	 * @param q
	 * @throws SearchException
	 * @throws IOException
	 */
	private void search(BooleanQuery q) throws SearchException, IOException {
		ScoreDoc[] hits = searchQuery(q, searcher);
		addToHitList(hitList, searcher, hits);
	}

	private BooleanQuery createTextQuery(SearchTask task) {
		BooleanQuery bQuery = createQuery(FIELD_TEXT,
		        parseString(task.getText()));
		return bQuery;
	}

	private void addMustOccur(BooleanQuery q, BooleanQuery bQuery) {
		q.add(bQuery, BooleanClause.Occur.MUST);
	}

	private void addMustOccur(BooleanQuery q, TermQuery cpQuery) {
		q.add(cpQuery, BooleanClause.Occur.MUST);
	}

	private TermQuery createPriorityQuery(SearchTask task)
	        throws SearchException {
		TermQuery cpQuery = createCompleteOrPriorityQuery(task.isPriority(),
		        FIELD_ISPRIORITY);
		return cpQuery;
	}

	private TermQuery createCompleteQuery(SearchTask task)
	        throws SearchException {
		TermQuery cpQuery = createCompleteOrPriorityQuery(task.isComplete(),
		        FIELD_ISCOMPLETE);
		return cpQuery;
	}

	/**
	 * Used for basic search, builds a query that only searches through the text
	 * field.
	 * 
	 * @param query
	 * @param field
	 * @return
	 * @throws SearchException
	 */
	public <T extends Task> List<T> query(String query, String field)
	        throws SearchException {
		logger.log(Level.INFO, DISPLAY_SEARCH + query);
		try {

			BooleanQuery q = new BooleanQuery();
			q = createQuery(field, parseString(query));
			search(q);
			reader.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, ERROR_INDEX, e);
			throw new SearchException(ERROR_IO);
		}
		return JsonConverter.convertJsonList(hitList, hitTypeList);
	}

	/**
	 * All search queries are parsed first to remove any special characters as
	 * StandardAnalyzer doesnt index them properly
	 * 
	 * @param query
	 * @return
	 */
	private String parseString(String query) {
		query = query.toLowerCase().replaceAll(SPECIALCHARACTER_AND, WORD_AND);
		return query.toLowerCase().replaceAll(EXCEPT_SPECIALCHARACTER, PARSE_DELIMITER);
	}


	/**
	 * combines a fuzzyQuery and a WildcardQuery into a BooleanQuery This is for
	 * near-match as well as wildcard search to work at the same time
	 * 
	 * @param field
	 * @param Query
	 * @return
	 */
	private BooleanQuery createQuery(String field, String Query) {
		String[] splitQuery = Query.split(QUERY_DELIMITER);
		BooleanQuery bQuery = new BooleanQuery();
		Query wildQ = getWildCardQuery(field, splitQuery);
		Query fuzzyQ = getFuzzyQuery(field, splitQuery);
		bQuery.add(fuzzyQ, BooleanClause.Occur.SHOULD);
		bQuery.add(wildQ, BooleanClause.Occur.SHOULD);
		return bQuery;
	}
//@author A0121810Y-reused
	/**
	 * adds the search results to a hitlist which will then be converted back to
	 * a List<Task>
	 * 
	 * @param hitList
	 * @param searcher
	 * @param hits
	 * @throws IOException
	 */
	private void addToHitList(ArrayList<String> hitList,
	        IndexSearcher searcher, ScoreDoc[] hits) throws IOException {
		hitTypeList = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
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

	/**
	 * Helper method for search(),searches the index for the top 10 closest
	 * matches The number of matches to search for can be changed by changing
	 * the numHits
	 * 
	 * @param q
	 * @param searcher
	 * @return
	 * @throws SearchException
	 */
	private ScoreDoc[] searchQuery(BooleanQuery q, IndexSearcher searcher)
	        throws SearchException {
		int numHits = NUM_HITS;
		TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);
		try {
			searcher.search(q, collector);
		} catch (IOException e) {
			logger.log(Level.SEVERE, ERROR_INDEX, e);
			throw new SearchException(ERROR_IO);
		}
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		return hits;
	}
	/**
	 * The WILDCARD constant is placed after splitQuery[i] so that it searches
	 * for any strings that begin with splitQuery[i], to have it search for any
	 * string that end with splitQuery[i], place the WILDCARD constant before
	 * it. Note that the time to search for wildCardQuerys can increase quite
	 * dramatically the more the indexed items.
	 * 
	 * @param field
	 * @param splitQuery
	 * @return
	 */
	private Query getWildCardQuery(String field, String[] splitQuery) {
		SpanQuery[] queryParts = new SpanQuery[splitQuery.length];
		for (int i = 0; i < splitQuery.length; i++) {
			WildcardQuery wildQuery = new WildcardQuery(new Term(field,
			        splitQuery[i] + WILDCARD));
			queryParts[i] = new SpanMultiTermQueryWrapper<WildcardQuery>(
			        wildQuery);
		}
		SpanNearQuery q = new SpanNearQuery(queryParts, 5, true);
		return q;
	}
	/**
	 * FuzzyQuerys are used in near match search, it done by calculating the
	 * levenshtein distance.It is considered a hit if the text it is searching
	 * has a levenshtein distance of LEVENSHTEIN_DISTANCE from the current
	 * search query.
	 * 
	 * @param field
	 * @param splitQuery
	 * @return
	 */
	private Query getFuzzyQuery(String field, String[] splitQuery) {
		SpanQuery[] queryParts = new SpanQuery[splitQuery.length];
		FuzzyQuery fuzzyQuery;
		for (int i = 0; i < splitQuery.length; i++) {
			if (notMinLength(splitQuery, i)) {
				fuzzyQuery = createFuzzyQuery(field, splitQuery, i,
				        LEVENSHTEIN_DISTANCE);
			} else {
				fuzzyQuery = createFuzzyQuery(field, splitQuery, i,
				        LEVENSHTEIN_0);
			}

			queryParts[i] = new SpanMultiTermQueryWrapper<FuzzyQuery>(
			        fuzzyQuery);
		}
		SpanNearQuery q = new SpanNearQuery(queryParts, SPAN_DISTANCE, true);
		return q;
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
		for (String tasks : list) {
			obj = parser.parse(tasks).getAsJsonObject();
			addDoc(writer, obj);
		}
	}
//@author A0121810Y
	private boolean notMinLength(String[] splitQuery, int i) {
		return splitQuery[i].length() > MIN_LENGTH;
	}

	private FuzzyQuery createFuzzyQuery(String field, String[] splitQuery,
	        int i, int lDistance) {
		FuzzyQuery fuzzyQuery;
		fuzzyQuery = new FuzzyQuery(new Term(field, splitQuery[i]), lDistance);
		return fuzzyQuery;
	}

	public TermQuery createCompleteOrPriorityQuery(boolean setTrue, String field)
	        throws SearchException {
		String isTrue = setTrueOrFalse(setTrue);
		TermQuery termQuery = new TermQuery(new Term(field, isTrue));
		return termQuery;
	}

	private String setTrueOrFalse(boolean setTrue) {
		return setTrue ? WORD_TRUE : WORD_FALSE;
	}

	/**
	 * Since Lucene can only search a single field at a time, this creates a
	 * dateQuery by first searching for tasks with dates in the toDate field
	 * that fall within the specified fromDate and toDate dates inclusive and
	 * then excluding all of those tasks that have fromDate dates from the epoch
	 * to the specified fromDate exclusive.
	 * 
	 * @param toDate
	 * @param fromDate
	 * @return
	 * @throws SearchException
	 */
	public BooleanQuery createDateQuery(Calendar toDate, Calendar fromDate)
	        throws SearchException {
		String sFromDate = DateTools.dateToString(fromDate.getTime(),
		        Resolution.SECOND);
		String sToDate = DateTools.dateToString(toDate.getTime(),
		        Resolution.SECOND);
		BooleanQuery bQuery = new BooleanQuery();
		String epochDate = DateTools.dateToString(getEpoch().getTime(),
		        Resolution.SECOND);
		TermRangeQuery rangeDeadlineQ = TermRangeQuery.newStringRange(
		        FIELD_DEADLINE, sFromDate, sToDate, true, true);
		TermRangeQuery rangeScheduleQ = TermRangeQuery.newStringRange(
		        FIELD_TODATE, sFromDate, sToDate, true, true);
		TermRangeQuery rangeFromScheduleQ = TermRangeQuery.newStringRange(
		        FIELD_FROMDATE, epochDate, sFromDate, false, false);
		bQuery.add(rangeDeadlineQ, BooleanClause.Occur.SHOULD);
		bQuery.add(rangeScheduleQ, BooleanClause.Occur.SHOULD);
		bQuery.add(rangeFromScheduleQ, BooleanClause.Occur.MUST_NOT);
		return bQuery;
	}


	/**
	 * The fields text,category are indexed by TextField in order to tokenize
	 * them. This is to prevent any potential errors arising from using fuzzy
	 * and wildcard queries. Dates are indexed according to Lucene's dateTools,
	 * StringField is used so as to preserve the string literal Priority and
	 * isComplete is also index with StringField in order to preserve the string
	 * literal It also stops working properly if TextField is used.
	 * 
	 * @param writer
	 * @param obj
	 * @throws IOException
	 */
	private void addDoc(IndexWriter writer, JsonObject obj) throws IOException {
		doc = new Document();
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if (key.equals(FIELD_TEXT) || key.equals(FIELD_CATEGORY)) {
				doc.add(new TextField(key, parseString(value.toString()),
				        Field.Store.YES));
			} else if (key.equals(FIELD_TODATE) || key.equals(FIELD_FROMDATE)
			        || key.equals(FIELD_DEADLINE)) {
				Calendar date = convertJsonToDate(value.toString());
				doc.add(new StringField(key, DateTools.dateToString(
				        date.getTime(), Resolution.SECOND), Field.Store.YES));
			} else if (key.equals(FIELD_ISCOMPLETE)
			        || key.equals(FIELD_ISPRIORITY)) {
				doc.add(new StringField(key, value.toString(), Field.Store.YES));
			}
		}
		doc.add(new StringField(FIELD_JSON, obj.toString(), Field.Store.YES));
		writer.addDocument(doc);
	}

	/**
	 * Since DateTools can only be used to convert Date objects,this function
	 * manually converts the JSON Calendar string back into a Calendar object,
	 * which is later converted into a Date object with Calendar.getTime().This
	 * is done because gson is unable to convert it automatically with the
	 * fromJson method.
	 * 
	 * @param obj
	 * @return
	 */
	private Calendar convertJsonToDate(String obj) {
		Calendar date = Calendar.getInstance();
		String[] oString = obj.split(JSON_DELIMITER);
		date.set(Integer.parseInt(oString[JSON_YEAR]),
		        Integer.parseInt(oString[JSON_MONTH]),
		        Integer.parseInt(oString[JSON_DATE]),
		        Integer.parseInt(oString[JSON_HOUROFDAY]),
		        Integer.parseInt(oString[JSON_MINUTE]),
		        Integer.parseInt(oString[JSON_SECOND]));
		return date;
	}

	private void closeWriter(IndexWriter writer) throws IOException {
		writer.close();
	}

	private Calendar getEpoch() throws SearchException {
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
		return fromDate;
	}

}
