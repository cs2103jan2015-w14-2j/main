package itinerary.test;

import java.util.*;
import com.google.gson.*;

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
        
        System.out.println(calendar.toString());
        System.out.println(calendar2.toString());
        
        System.out.println();
        
        System.out.println(calendarJSONString);
        System.out.println(calendar2JSONString);
        
    }

}
