package itinerary.main;

import java.util.Comparator;

//@author A0121409R
/**
 * Sorts the contents of a given List<Task> object according to Dates.
 */
public class SortTaskByDates implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {

        int returnVal = 0;

        String classType1 = o1.getClass().getSimpleName();
        String classType2 = o2.getClass().getSimpleName();

        String taskClassName = Task.class.getSimpleName();
        String deadlineTaskClassName = DeadlineTask.class.getSimpleName();
        String scheduleTaskClassName = ScheduleTask.class.getSimpleName();

        // o1 and o2 are of the same class type.

        // Check against their Calendar arguments
        if (classType1.equals(deadlineTaskClassName)
            && classType2.equals(deadlineTaskClassName)) {

            // o1 == DeadlineTask
            // o2 == DeadlineTask

            if (((DeadlineTask) o1).getDeadline()
                                   .before(((DeadlineTask) o2).getDeadline())) {

                // o1 has the earlier Deadline.

                // o1 is greater.
                returnVal = -1;
            } else if (((DeadlineTask) o2).getDeadline()
                                          .before(((DeadlineTask) o1).getDeadline())) {

                // o2 has the earlier Deadline.

                // o2 is greater.
                returnVal = 1;
            } else {

                // o1 and o2 have the exact same Deadlines.

                return 0;

            }
        } else if (classType1.equals(scheduleTaskClassName)
                   && classType2.equals(scheduleTaskClassName)) {

            // o1 == ScheduleTask
            // o2 == ScheduleTask

            if (((ScheduleTask) o1).getToDate()
                                   .before(((ScheduleTask) o2).getToDate())) {

                // o1 has the earlier ToDate.

                // o1 is greater.
                returnVal = -1;
            } else if (((ScheduleTask) o2).getToDate()
                                          .before(((ScheduleTask) o1).getToDate())) {

                // o2 has the earlier ToDate.

                // o2 is greater.
                returnVal = 1;
            } else {

                // o1 and o2 have the exact same ToDates.

                return 0;
            }
        } else {

            // o1 and o2 are regular Tasks.

            return 0;
        }

        return returnVal;
    }

}
