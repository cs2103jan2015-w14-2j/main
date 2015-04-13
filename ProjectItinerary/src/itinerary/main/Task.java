package itinerary.main;

//@author A0121437N
public class Task implements Cloneable {
    private Integer taskId;
    private String text;
    private String category;
    private Boolean isPriority;
    private Boolean isComplete;

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
    
    public static Task updateDetails (Task template, Task details) {
    	if (details instanceof DeadlineTask) {
    		template = new DeadlineTask(template.taskId, template.text, template.category,
    				template.isPriority, template.isComplete,
    				((DeadlineTask)details).getDeadline());
    	} else if (details instanceof ScheduleTask) {
    		template = new ScheduleTask(template.taskId, template.text, template.category,
    				template.isPriority, template.isComplete,
    				((ScheduleTask)details).getFromDate(),
    				((ScheduleTask)details).getToDate());
    	}    	
    	if (details.text != null) {
    		template.setText(details.getText());
    	}    	
    	if (details.category != null) {
    		template.setCategory(details.getCategory());
    	}    	
    	if (details.isComplete != null && details.isComplete != template.isComplete) {
    		template.setComplete(details.isComplete());
    	}    	
    	if (details.isPriority != null && details.isPriority == true) {
    		if(template.isPriority){
    			template.setPriority(false);
    		}
    		else{
    			template.setPriority(true);
    		}
    	}
    	return template;
    }

    //@author A0121409R
    @Override
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
    public boolean equals(Object object) {
        //Overrides default Object equals().
        if (object == null) {
            return false;
        }
        if (!(object instanceof Task)) {
            return false;
        }
        Task task = (Task) object;
		if (task.getTaskId() != this.taskId) {
            return false;
        }
        if (!task.getText().equals(this.text)) {
            return false;
        }
        if (task.getCategory() != null && !task.getCategory().equals(this.category)) {
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
