package test;

import java.util.*;

import com.google.gson.*;

import itinerary.main.*;

/**
 * For general testing.
 */

public class Test {
    
    public static void main(String args[]) {
        
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
        
        
    }

}
