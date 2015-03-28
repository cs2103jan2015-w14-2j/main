package itinerary.main;

import java.util.Comparator;

//@author A0121409R

/**
 * Sorts the contents of a given List<Task> object according to description.
 */
public class SortTaskByDesc implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        return o1.getText().compareTo(o2.getText());
    }

}
