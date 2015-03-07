package itinerary.main;

import java.util.Calendar;

//@author A0121437N
public class DeadlineTask extends Task implements Cloneable {
    private Calendar deadline;

    //@author generated
    public DeadlineTask(int lineNumber, String text, String category,
                        boolean isPriority, boolean isComplete,
                        Calendar deadline) {
        super(lineNumber, text, category, isPriority, isComplete);
        this.deadline = deadline;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    //@author A0121409R

    public DeadlineTask clone() {

        // Note the String objects might not be deep copied.

        return new DeadlineTask(this.getLineNumber(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getDeadline());
    }

    public boolean equals(DeadlineTask toCheck) {

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

        if (!toCheck.deadline.equals(this.deadline)) {

            return false;
        }

        return true;
    }
}
