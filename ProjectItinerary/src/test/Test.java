package test;

import java.util.*;

import com.google.gson.*;

import itinerary.main.*;

/**
 * For general testing.
 */

public class Test {
    
    public static void main(String args[]) {
        
        /*
        Gson gson = new Gson();
        
        Calendar calendar = Calendar.getInstance();
        String calendarJSONString = gson.toJson(calendar);
        
        Calendar calendar2 = gson.fromJson(calendarJSONString, Calendar.class);
        String calendar2JSONString = gson.toJson(calendar2);
        
        System.out.println(calendar.equals(calendar2));
        System.out.println(calendar.compareTo(calendar2));
        
        System.out.println();
        
        System.out.println(calendar.toString());
        System.out.println(calendar2.toString());
        
        System.out.println();
        
        System.out.println(calendarJSONString);
        System.out.println(calendar2JSONString);
        
        System.out.println();
        
        Task task1 = new Task(1, "T", "ExampleCategory", true, true);
        String task1String = gson.toJson(task1);
        
        Task task2 = gson.fromJson(task1String, Task.class);
        
        System.out.println(task1 == task2);
        System.out.println(task1.equals(task2));
        */
        
        Calendar date1 = Calendar.getInstance();
        date1.set(2014, 6, 5);
        Calendar date2 = Calendar.getInstance();
        date2.set(2014, 6, 14);
        Calendar date3 = Calendar.getInstance();
        date3.set(2014, 3, 2);
        Calendar date4 = Calendar.getInstance();
        date4.set(2014, 4, 2);
        Calendar date5 = Calendar.getInstance();
        date5.set(2015, 1, 12);
        Calendar date6 = Calendar.getInstance();
        date6.set(2015, 3, 3);
        Calendar date7 = Calendar.getInstance();
        date7.set(2015, 3, 12);
        Calendar date8 = Calendar.getInstance();
        date8.set(2015, 3, 13);
        Calendar date9 = Calendar.getInstance();
        date9.set(2015, 3, 14);
        Calendar date10 = Calendar.getInstance();
        date10.set(2015, 3, 15);
        Calendar date11 = Calendar.getInstance();
        date11.set(2015, 3, 16);
        Calendar date12 = Calendar.getInstance();
        date12.set(2015, 3, 17);
        
        List<Task> testSort = new ArrayList<Task>() {

            private static final long serialVersionUID = 1L;
            
            {
                add(new Task(1, "T1", "", true, false));
                add(new ScheduleTask(2, "ST1", "", false, false, date1, date2));
                add(new DeadlineTask(3, "D1", "", true, false, date3));
                add(new Task(4, "T2", "", true, true));
                add(new DeadlineTask(5, "D2", "", false, true, date4));
                add(new ScheduleTask(6, "ST2", "", false, true, date5, date6));
                add(new DeadlineTask(7, "D3", "", true, true, date7));
                add(new ScheduleTask(8, "ST3", "", true, false, date8, date9));
                add(new Task(9, "T3", "", false, true));
                add(new ScheduleTask(10, "ST4", "", false, false, date10, date11));
                add(new DeadlineTask(11, "D4", "", true, true, date12));
                add(new DeadlineTask(12, "D5", "", true, true, date12));
            }
            
        };
        
        testSort = TaskSorter.sort(testSort);
        
        for (Task item : testSort) {
            
            System.out.println(item.getTaskId() + " " + item.getText() + " " + item.isComplete() + " " + item.isPriority());
        }
    }

}
