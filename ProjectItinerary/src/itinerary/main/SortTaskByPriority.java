package itinerary.main;

import java.util.Comparator;

//@author A0121409R

/**
 * Sorts the contents of a given List<Task> object by Priority.
 */
public class SortTaskByPriority implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        
        int returnVal = 0;
        
        //Check against Priority.
        if (o1.isPriority() == true && o2.isPriority() == false) {
            
            //o1 is greater.
            returnVal = -1;
            
        } else if (o2.isPriority() == true && o1.isPriority() == false) {
            
            //o2 is greater.
            returnVal = 1;
            
        } else {
            
            returnVal = 0;
        }
        
        return returnVal;
    }

}
