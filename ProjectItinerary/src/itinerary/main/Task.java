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

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}

	public void setPriority(boolean isPriority) {
		this.isPriority = isPriority;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
}
