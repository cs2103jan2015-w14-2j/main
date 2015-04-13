package itinerary.search;

import itinerary.main.Task;

import java.util.Calendar;

import com.google.gson.Gson;

//@author A0121810Y
/**
 * A searchTask contains all fields that a deadlineTask,ScheduleTask and a Task
 * have. Any non-null field in SearchTask is searched with the value of the
 * field as the parameters when a Search.query() is run on it.
 *
 */
public class SearchTask extends Task implements Cloneable {
	private Calendar fromDate;
	private Calendar toDate;
	private Calendar deadline;

	public SearchTask() {
		super(null, null, null, null, null);
	}

	public SearchTask(Integer taskId, String text, String category,
	        Boolean isPriority, Boolean isComplete, Calendar fromDate,
	        Calendar toDate) {
		super(taskId, text, category, isPriority, isComplete);
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

	// @author A0121409R
	public SearchTask clone() {
		// Note the String objects might not be deep copied.
		return new SearchTask(this.getTaskId(), this.getText(),
		        this.getCategory(), this.isPriority(), this.isComplete(),
		        this.getFromDate(), this.getToDate());
	}

	@Override
	public boolean equals(Object searchTask) {
		// Overrides Object equals() method
		if (!super.equals(searchTask)) {
			return false;
		}

		if (!(searchTask instanceof SearchTask)) {
			return false;
		}

		Gson gson = new Gson();

		String fromDate1 = gson.toJson(((SearchTask) searchTask).getFromDate());
		String fromDate2 = gson.toJson(this.fromDate);

		if (!fromDate1.equals(fromDate2)) {
			return false;
		}

		String toDate1 = gson.toJson(((SearchTask) searchTask).getFromDate());
		String toDate2 = gson.toJson(this.toDate);

		if (!toDate1.equals(toDate2)) {
			return false;
		}

		return true;
	}

	public Calendar getDeadline() {
		return deadline;
	}

	public void setDeadline(Calendar deadline) {
		this.deadline = deadline;
	}
}
