package itinerary.main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.fortuna.ical4j.model.DateTime;

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

import com.joestelmach.natty.*;

import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import com.google.gson.*;

public class ParserSearch {
	private static List<String> list;
	private static JsonParser parser;
	private static JsonObject obj;
	ArrayList<String> hitTypeList;
	private static final String ERROR_IO = "Error attempting to search.";
	ArrayList<String> hitList;
	StandardAnalyzer analyzer;
	IndexSearcher searcher;
	IndexReader reader;
	IndexWriter writer;
	Document doc;
	String pString;
	List<String> fields;
	String input;
	SearchTask task;
	List<String> categoryList;

	public static void main(String[] args) {
		String cats = "homework work school project";
		String[] catlist = cats.split(" ");
		List<String> list = Arrays.asList(catlist);
		ParserSearch p = new ParserSearch(list);
		p.parseString("search tasks");
	}

	public ParserSearch(List<String> catList) {
		this.categoryList = catList;
		init();
	}

	public SearchTask parseString(String input) {

		this.input = input;
		task = new SearchTask();
		determineDateFields();
		return task;
	}

	private void determineDateFields() {

		List<String> inputList = Arrays.asList(input.split(" "));
		List<String> fields = new ArrayList<String>();
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
		List<DateGroup> groups = parser.parse(input);
		if (groups.size() != 0) {
			List<Date> dates = groups.get(0).getDates();

			if (dates.size() == 1) {
				Calendar endDate = convertToCalendar(dates.get(0));
				if (inputList.contains("week")) {
					int position = inputList.indexOf("week");
					if (inputList.get(position - 1).equals("this")) {
						if (inputList.get(position - 2).equals("before")) {
							List<Date> thisWeek = getDatesForThisWeek(dates
							        .get(0));
							Calendar endWeekDate = convertToCalendar(thisWeek
							        .get(0));
							Calendar startDate = Calendar.getInstance();
							startDate.set(1970, 1, 1);
							task.setFromDate(startDate);
							task.setToDate(endWeekDate);
							fields.add("date");
						} else if (inputList.get(position - 2).equals("after")) {
							List<Date> thisWeek = getDatesForThisWeek(dates
							        .get(0));
							Calendar endWeekDate = convertToCalendar(thisWeek
							        .get(0));
							Calendar endOfTimeDate = Calendar.getInstance();
							endOfTimeDate.set(3000, 1, 1);
							task.setFromDate(endWeekDate);
							task.setToDate(endOfTimeDate);
							fields.add("date");
						} else {
							List<Date> thisWeek = getDatesForThisWeek(dates
							        .get(0));
							Calendar startWeekDate = convertToCalendar(thisWeek
							        .get(0));
							Calendar endWeekDate = convertToCalendar(thisWeek
							        .get(1));
							task.setFromDate(startWeekDate);
							task.setToDate(endWeekDate);
							fields.add("date");
						}
					}
				}
			} else {
				Calendar startDate = convertToCalendar(dates.get(0));
				Calendar endDate = convertToCalendar(dates.get(1));
				task.setFromDate(startDate);
				task.setToDate(endDate);
			}
		}
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

	public Calendar convertToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		return calendar;
	}

	private String getCategoryString() {
		String categories = "";
		for (String category : categoryList) {
			categories += category + " ";
		}
		return categories;
	}

	private void init() {
		HashMap<String, String> pValues = new HashMap<String, String>();
		Gson gson = new Gson();
		SearchTask task = new SearchTask();
		String priority = "priority important importance";
		String complete = "completed done finished";
		String negation = "not low false un";
		String tasks = "todos tasks work";
		String categories = getCategoryString();
		pValues.put("priority", priority);
		pValues.put("complete", complete);
		pValues.put("negation", negation);
		pValues.put("task", tasks);
		pValues.put("category", categories);

		parser = new JsonParser();
		analyzer = new StandardAnalyzer();
		hitList = new ArrayList<String>();

		// create the index
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		try {
			pString = gson.toJson(pValues);
			obj = parser.parse(pString).getAsJsonObject();
			System.out.println(obj.toString());
			createIndex(index, config);
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		obj = parser.parse(pString).getAsJsonObject();
		addDoc(writer, obj);
	}

	private void addDoc(IndexWriter writer, JsonObject obj) throws IOException {
		doc = new Document();

		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			doc.add(new TextField(key, value.toString(), Field.Store.YES));
		}

		writer.addDocument(doc);
	}

	private void closeWriter(IndexWriter writer) throws IOException {
		writer.close();
	}
}
