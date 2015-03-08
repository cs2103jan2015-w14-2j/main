package itinerary.main;

import java.util.Calendar;

//@author A0121437N
public class DeadlineTask extends Task implements Cloneable {
    private Calendar deadline;

    //@author generated
    public DeadlineTask(int taskId, String text, String category,
                        boolean isPriority, boolean isComplete,
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
    
    //@author A0121437N
    @Override
	public void updateDetails(Task details) {
		super.updateDetails(details);
		if (details instanceof DeadlineTask) {
			DeadlineTask deadlineDetails = (DeadlineTask) details;
			if (deadlineDetails.getDeadline() != null) {
				this.setDeadline(deadlineDetails.getDeadline());
			}
		}
	}
    

    //@author A0121409R
    public DeadlineTask clone() {
        // Note the String objects might not be deep copied.
        return new DeadlineTask(this.getTaskId(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getDeadline());
    }

    @Override
	public boolean equals(Object deadlineTask) {
	    //Overrides default Object equals()
        if (!super.equals(deadlineTask)) {
        	return false;
        }
        
        if (!(deadlineTask instanceof DeadlineTask)) {
            return false;
        }

        if (!((DeadlineTask) deadlineTask).deadline.equals(this.deadline)) {
            return false;
        }

        return true;
    }
}
