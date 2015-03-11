package itinerary.main;

import java.util.*;

public class ParserSearch {
	public HashMap<String,String> fields;
	public ParserSearch(String input){
		String key =null;
		String value = "";
		String[] stopWords = {"tasks","from","to","priority","completed","category"};
		List<String> stopList= Arrays.asList(stopWords);
		ArrayList<String> splitInput = new ArrayList<String>(Arrays.asList(input.split(" ")));
		splitInput.remove(0);
		fields = new HashMap<String,String>();
		for(int i=0;i<splitInput.size();i++){
			if(stopList.contains(splitInput.get(i).toLowerCase())){
				if(key != null){
					
					fields.put(key,value.trim());
					value = "";
				}
				key = splitInput.get(i);
				if(key.equals("priority")|| key.equals("completed")){
					if(i+1<splitInput.size()){
						value += splitInput.get(i+1)+" ";
					}
					if(i-1>=0){
						value += splitInput.get(i-1)+ " ";
					}
				}
			} else {
				value += splitInput.get(i)+ " ";
			}
		}
		fields.put(key,value);
		System.out.println(fields.toString());
	}
}
