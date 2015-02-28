package itinerary.main;

import java.util.Calendar;

//@author A0121437N
public class DeadlineTask extends Task {
	private Calendar deadline;

	//@author generated
	public DeadlineTask(int lineNumber, String text, String category,
			boolean isPriority, boolean isComplete, Calendar deadline) {
		super(lineNumber, text, category, isPriority, isComplete);
		this.deadline = deadline;
	}

	public Calendar getDeadline() {
		return deadline;
	}

	public void setDeadline(Calendar deadline) {
		this.deadline = deadline;
	}
}
