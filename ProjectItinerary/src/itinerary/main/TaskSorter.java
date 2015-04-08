package itinerary.main;

import java.util.Collections;
import java.util.List;

//@author A0121409R

/**
 * Distinguishes which of the two Tasks is "greater" than the other. The order
 * is:
 * <p>
 * <li>1) Tasks with Priority.
 * <li>2) Tasks with no Priority.
 * <li>3) Tasks marked as Completed.</li>
 * </p>
 * <br>
 * Within each section, it is arranged by:
 * <p>
 * <li>1) DeadlineTask.
 * <li>2) ScheduleTask.
 * <li>3) Task.</li>
 * </p>
 * <br>
 * When they are of the same task type, then it is arranged according to:
 * <p>
 * <li>1) Earlier Date.
 * <li>2) Task Description.</li>
 * </p>
 * <br>
 */
public class TaskSorter {
    
    public static List<Task> sort(List<Task> toSort) {
        
        Collections.sort(toSort, new SortTaskByDesc());
        Collections.sort(toSort, new SortTaskByTaskType());
        Collections.sort(toSort, new SortTaskByDates());
        Collections.sort(toSort, new SortTaskByPriority());
        Collections.sort(toSort, new SortTaskByComplete());
        
        return toSort;
    }
}
