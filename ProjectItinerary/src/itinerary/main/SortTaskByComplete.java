package itinerary.main;

import java.util.Comparator;

//@author A0121409R
/**
 * Sort the contents in a given List<Task> object by Completeness.
 */
public class SortTaskByComplete implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        
        int returnVal = 0;
        
        //Check against Completeness.
        if (o1.isComplete() && !o2.isComplete()) {
            
            //o1 is greater.
            returnVal = 1;
            
        } else if (!o1.isComplete() && o2.isComplete()) {
            
            //o2 is greater.
            returnVal = -1;
            
        }
        
        return returnVal;
    }

}
