package itinerary.main;

//@author A0121437N
public class Task implements Cloneable {
    private int taskId;
    private String text;
    private String category;
    private boolean isPriority;
    private boolean isComplete;

    //@author generated
    public Task(int taskId, String text, String category,
                boolean isPriority, boolean isComplete) {
        super();
        this.setTaskId(taskId);
        this.setText(text);
        this.setCategory(category);
        this.setPriority(isPriority);
        this.setComplete(isComplete);
    }

    public int getTaskId() {
        return taskId;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setText(String text) {        
        this.text = text;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
    
    //@author A0121437N
    public void updateDetails (Task details) {
    	if (details.getText() != null) {
    		this.setText(details.getText());
    	}
    	
    	if (details.getCategory() != null) {
    		this.setCategory(details.getCategory());
    	}
    	
    	if (details.isComplete() != this.isComplete()) {
    		this.setComplete(details.isComplete());
    	}
    	
    	if (details.isPriority() != this.isPriority()) {
    		this.setPriority(details.isPriority());
    	}
    }

    //@author A0121409R
    public Task clone() {
        // Note the String objects might not be deep copied.
        return new Task(this.getTaskId(), this.getText(),
                        this.getCategory(), this.isPriority(),
                        this.isComplete());
    }

    public boolean equals(Task task) {
        if (task == null) {
            return false;
        }

        if (task.getTaskId() != this.taskId) {
            return false;
        }

        if (!task.getText().equals(this.text)) {
            return false;
        }

        if (!task.getCategory().equals(this.category)) {
            return false;
        }

        if (task.isPriority() != this.isPriority) {
            return false;
        }

        if (task.isComplete() != this.isComplete) {
            return false;
        }

        return true;
    }
}
