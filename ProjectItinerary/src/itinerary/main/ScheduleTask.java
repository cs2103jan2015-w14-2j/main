package itinerary.main;

import java.util.Calendar;

//@author A0121437N
public class ScheduleTask extends Task {
	private Calendar fromDate;
	private Calendar toDate;
	
	//@author generated
	
	public ScheduleTask(int lineNumber, String text, String category,
			boolean isPriority, boolean isComplete, Calendar fromDate,
			Calendar toDate) {
		super(lineNumber, text, category, isPriority, isComplete);
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public Calendar getFromDate() {
		return fromDate;
	}

	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}

	public Calendar getToDate() {
		return toDate;
	}

	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}
}
