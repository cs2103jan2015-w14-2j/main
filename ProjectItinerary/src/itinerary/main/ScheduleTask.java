package itinerary.main;

import java.util.Calendar;

import com.google.gson.Gson;

//@author A0121437N
public class ScheduleTask extends Task implements Cloneable {
    private Calendar fromDate;
    private Calendar toDate;

    public ScheduleTask(Integer taskId, String text, String category,
                        Boolean isPriority, Boolean isComplete,
                        Calendar fromDate, Calendar toDate) {
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

    //@author A0121409R
    public ScheduleTask clone() {
        // Note the String objects might not be deep copied.
        return new ScheduleTask(this.getTaskId(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getFromDate(),
                                this.getToDate());
    }

    @Override
    public boolean equals(Object scheduleTask) {
        // Overrides Object equals() method
        if (!super.equals(scheduleTask)) {
            return false;
        }

        if (!(scheduleTask instanceof ScheduleTask)) {
            return false;
        }

        Gson gson = new Gson();

        String fromDate1 =
                           gson.toJson(((ScheduleTask) scheduleTask).getFromDate());
        String fromDate2 = gson.toJson(this.fromDate);

        if (!fromDate1.equals(fromDate2)) {
            return false;
        }

        String toDate1 =
                         gson.toJson(((ScheduleTask) scheduleTask).getFromDate());
        String toDate2 = gson.toJson(this.toDate);

        if (!toDate1.equals(toDate2)) {
            return false;
        }

        return true;
    }
}
