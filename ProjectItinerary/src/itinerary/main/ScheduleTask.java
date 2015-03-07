package itinerary.main;

import java.util.Calendar;

//@author A0121437N
public class ScheduleTask extends Task implements Cloneable {
    private Calendar fromDate;
    private Calendar toDate;

    //@author generated

    public ScheduleTask(int lineNumber, String text, String category,
                        boolean isPriority, boolean isComplete,
                        Calendar fromDate, Calendar toDate) {
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

    //@author A0121437N
    @Override
	public void updateDetails(Task details) {
		super.updateDetails(details);
		if (details instanceof ScheduleTask) {
			ScheduleTask scheduleDetails = (ScheduleTask) details;
			
			if (scheduleDetails.getFromDate() != null) {
				this.setFromDate(scheduleDetails.getFromDate());
			}
			
			if (scheduleDetails.getToDate() != null){
				this.setToDate(scheduleDetails.getToDate());
			}
		}
	}

	//@author A0121409R
    public ScheduleTask clone() {
        // Note the String objects might not be deep copied.
        return new ScheduleTask(this.getLineNumber(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getFromDate(),
                                this.getToDate());
    }

    public boolean equals(ScheduleTask scheduleTask) {
        if (!super.equals(scheduleTask)) {
        	return false;
        }

        if (!scheduleTask.fromDate.equals(this.fromDate)) {
            return false;
        }

        if (!scheduleTask.toDate.equals(this.toDate)) {
            return false;
        }

        return true;
    }
}
