package itinerary.main;

import java.util.Calendar;

//@author A0121437N
public class Task {
	private int lineNumber;
	private String text;
	private String category;
	private Calendar fromDate;
	private Calendar toDate;
	private boolean isPriority;
	private boolean isComplete;

	//@author generated
	public Task(int lineNumber, String text, String category,
			Calendar fromDate, Calendar toDate, boolean isPriority,
			boolean isComplete) {
		super();
		this.lineNumber = lineNumber;
		this.text = text;
		this.category = category;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.isPriority = isPriority;
		this.isComplete = isComplete;
	}

	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getText() {
		return text;
	}

	public String getCategory() {
		return category;
	}

	public Calendar getFromDate() {
		return fromDate;
	}

	public Calendar getToDate() {
		return toDate;
	}

	public boolean isPriority() {
		return isPriority;
	}

	public boolean isComplete() {
		return isComplete;
	}
}
