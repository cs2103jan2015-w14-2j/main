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

    //@author A0121409R

    public ScheduleTask clone() {

        // Note the String objects might not be deep copied.

        return new ScheduleTask(this.getLineNumber(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getFromDate(),
                                this.getToDate());
    }

    public boolean equals(ScheduleTask toCheck) {

        if (toCheck == null) {

            return false;
        }

        if (toCheck.getLineNumber() != this.getLineNumber()) {

            return false;
        }

        if (!toCheck.getText().equals(this.getText())) {

            return false;
        }

        if (!toCheck.getCategory().equals(this.getCategory())) {

            return false;
        }

        if (toCheck.isPriority() != this.isPriority()) {

            return false;
        }

        if (toCheck.isComplete() != this.isComplete()) {

            return false;
        }

        if (!toCheck.fromDate.equals(this.fromDate)) {

            return false;
        }

        if (!toCheck.toDate.equals(this.toDate)) {

            return false;
        }

        return true;
    }
}
