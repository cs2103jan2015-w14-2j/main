package itinerary.main;

import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;

public class SearchTask extends Task implements Cloneable {
	private Calendar fromDate;
    private Calendar toDate;
    private Calendar deadline;
    private List<String> searchField;
    private List<String> categoryList;
    private List<String> searchNotField;
    //@author generated
    public SearchTask() {
        super(1,"","", false, false);
    }
    public SearchTask(int taskId, String text, String category,
                        boolean isPriority, boolean isComplete,
                        Calendar fromDate, Calendar toDate,List<String> fields) {
        super(taskId, text, category, isPriority, isComplete);
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.searchField = fields;
    }
    public List<String> getSearchField() {
    	return searchField;
    }
    public void setSearchField(List<String> fields) {
    	this.searchField = fields;
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
        ;//SearchTask does not require updating
    }

    //@author A0121409R
    public SearchTask clone() {
        // Note the String objects might not be deep copied.
        return new SearchTask(this.getTaskId(), this.getText(),
                                this.getCategory(), this.isPriority(),
                                this.isComplete(), this.getFromDate(),
                                this.getToDate(),this.getSearchField());
    }

    @Override
    public boolean equals(Object searchTask) {
        // Overrides Object equals() method
        if (!super.equals(searchTask)) {
            return false;
        }

        if (!(searchTask instanceof SearchTask)) {
            return false;
        }

        Gson gson = new Gson();

        String fromDate1 =
                           gson.toJson(((SearchTask) searchTask).getFromDate());
        String fromDate2 = gson.toJson(this.fromDate);

        if (!fromDate1.equals(fromDate2)) {
            return false;
        }

        String toDate1 =
                         gson.toJson(((SearchTask) searchTask).getFromDate());
        String toDate2 = gson.toJson(this.toDate);

        if (!toDate1.equals(toDate2)) {
            return false;
        }

        return true;
    }
	public List<String> getCategoryList() {
	    return categoryList;
    }
	public void setCategoryList(List<String> categoryList) {
	    this.categoryList = categoryList;
    }
	public List<String> getSearchNotField() {
	    return searchNotField;
    }
	public void setSearchNotField(List<String> searchNotField) {
	    this.searchNotField = searchNotField;
    }
	public Calendar getDeadline() {
	    return deadline;
    }
	public void setDeadline(Calendar deadline) {
	    this.deadline = deadline;
    }
}
