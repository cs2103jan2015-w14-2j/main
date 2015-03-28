package itinerary.main;

import java.util.Comparator;

//@author A0121409R
/**
 * Sorts the contents of a given List<Task> object by Task Type. <br>
 * Order: <li>1) DeadlineTask <li>2) ScheduleTask <li>3) Task
 */
public class SortTaskByTaskType implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {

        String classType1 = o1.getClass().getSimpleName();
        String classType2 = o2.getClass().getSimpleName();

        String taskClassName = Task.class.getSimpleName();
        String deadlineTaskClassName = DeadlineTask.class.getSimpleName();
        String scheduleTaskClassName = ScheduleTask.class.getSimpleName();

        int returnVal = 0;

        if (!classType1.equals(classType2)) {

            // o1 and o2 are not of the same class type.
            if (classType1.equals(deadlineTaskClassName)
                && !classType2.equals(deadlineTaskClassName)) {

                // o1 == DeadlineTask
                // o2 == ScheduleTask || Task

                // o1 is greater.
                returnVal = -1;

            } else if (classType2.equals(deadlineTaskClassName)
                       && !classType1.equals(deadlineTaskClassName)) {

                // o1 == ScheduleTask || Task
                // o2 == DeadlineTask

                // o2 is greater.
                returnVal = 1;

            } else if (classType1.equals(scheduleTaskClassName)
                       && classType2.equals(taskClassName)) {

                // o1 == ScheduleTask
                // o2 == Task

                // o1 is greater.
                returnVal = -1;

            } else {

                // o1 == Task
                // o2 == ScheduleTask

                // o2 is greater.
                returnVal = 1;
            }
        } else {

            returnVal = 0;
        }

        return returnVal;
    }
}
