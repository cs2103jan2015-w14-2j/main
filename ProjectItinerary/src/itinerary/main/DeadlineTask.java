package itinerary.main;

import java.util.Calendar;

import com.google.gson.Gson;

//@author A0121437N
public class DeadlineTask extends Task implements Cloneable {
    private Calendar deadline;

    //@author generated
    public DeadlineTask(Integer taskId, String text, String category,
                        Boolean isPriority, Boolean isComplete,
                        Calendar deadline) {
        super(taskId, text, category, isPriority, isComplete);
        this.deadline = deadline;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    /*//@author A0121437N
    @Override
    public void updateDetails(Task details) {
        super.updateDetails(details);
        if (details instanceof DeadlineTask) {
            DeadlineTask deadlineDetails = (DeadlineTask) details;
            if (deadlineDetails.getDeadline() != null) {
                this.setDeadline(deadlineDetails.getDeadline());
            }
        }
    }*/

    //@author A0121409R
    public DeadlineTask clone() {
        // Note the String objects might not be deep copied.
        return new DeadlineTask(this.getTaskId(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getDeadline());
    }

    @Override
    public boolean equals(Object deadlineTask) {
        // Overrides default Object equals()
        if (!super.equals(deadlineTask)) {
            return false;
        }

        if (!(deadlineTask instanceof DeadlineTask)) {
            return false;
        }

        Gson gson = new Gson();
        String deadline1 =
                           gson.toJson(((DeadlineTask) deadlineTask).getDeadline());
        String deadline2 = gson.toJson(this.deadline);

        if (!deadline1.equals(deadline2)) {
            return false;
        }

        return true;
    }
}
