package itinerary.main;

//@author A0121437N
public class Task implements Cloneable {
    private Integer taskId;
    private String text;
    private String category;
    private Boolean isPriority;
    private Boolean isComplete;

    //@author generated
    public Task(Integer taskId, String text, String category,
                Boolean isPriority, Boolean isComplete) {
        super();
        this.setTaskId(taskId);
        this.setText(text);
        this.setCategory(category);
        this.setPriority(isPriority);
        this.setComplete(isComplete);
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public Boolean isPriority() {
        return isPriority;
    }

    public Boolean isComplete() {
        return isComplete;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setText(String text) {        
        this.text = text;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(Boolean isPriority) {
        this.isPriority = isPriority;
    }

    public void setComplete(Boolean isComplete) {
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
    	
    	if (details.isPriority() == true) {
    		if(!this.isPriority()){
    			this.setPriority(false);
    		}
    		else{
    			this.setPriority(true);
    		}
    	}
    }

    //@author A0121409R
    public Task clone() {
        // Note the String objects might not be deep copied.
        return new Task(this.getTaskId(), this.getText(),
                        this.getCategory(), this.isPriority(),
                        this.isComplete());
    }
    
    public Boolean hasNoText() {

        if (this.text == null || this.text.isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean hasNoCategory() {

        if (this.category == null || this.category.isEmpty()) {
            return true;
        }
        return false;
    }
    

    @Override
    public Boolean equals(Object task) {
        //Overrides default Object equals().
        if (task == null) {
            return false;
        }
        
        if (!(task instanceof Task)) {
            return false;
        }

        if (((Task) task).getTaskId() != this.taskId) {
            return false;
        }

        if (!((Task) task).getText().equals(this.text)) {
            return false;
        }

        if (!((Task) task).getCategory().equals(this.category)) {
            return false;
        }

        if (((Task) task).isPriority() != this.isPriority) {
            return false;
        }

        if (((Task) task).isComplete() != this.isComplete) {
            return false;
        }

        return true;
    }
}
