package itinerary.main;

// @author A0121437N
public class Task implements Cloneable {
    private int lineNumber;
    private String text;
    private String category;
    private boolean isPriority;
    private boolean isComplete;

    // @author generated
    public Task(int lineNumber, String text, String category,
                boolean isPriority, boolean isComplete) {
        super();
        this.setLineNumber(lineNumber);
        this.setText(text);
        this.setCategory(category);
        this.setPriority(isPriority);
        this.setComplete(isComplete);
    }

    public int getLineNumber() {
        return lineNumber;
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

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setText(String text) {
        
        if (text == null) {
            
            text = "";
        }
        
        this.text = text;
    }

    public void setCategory(String category) {
        
        if (category == null) {
            
            category = "";
        }
        
        this.category = category;
    }

    public void setPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    // @author A0121409R

    public Task clone() {

        // Note the String objects might not be deep copied.

        return new Task(this.getLineNumber(), this.getText(),
                        this.getCategory(), this.isPriority(),
                        this.isComplete());
    }

    public boolean equals(Task toCheck) {

        if (toCheck == null) {

            return false;
        }

        if (toCheck.getLineNumber() != this.lineNumber) {

            return false;
        }

        if (!toCheck.getText().equals(this.text)) {

            return false;
        }

        if (!toCheck.getCategory().equals(this.category)) {

            return false;
        }

        if (toCheck.isPriority() != this.isPriority) {

            return false;
        }

        if (toCheck.isComplete() != this.isComplete) {

            return false;
        }

        return true;
    }
}
