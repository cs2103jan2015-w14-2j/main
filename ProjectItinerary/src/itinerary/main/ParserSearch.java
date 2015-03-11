package itinerary.main;

import java.util.*;

public class ParserSearch {
	public HashMap<String,String> fields;
	SearchTask task;
	String input;
	String[] stopWords = {"tasks","from","to","priority","completed","category"};
	List<String> stopList= Arrays.asList(stopWords);

	public ParserSearch(String input){
		SearchTask task = new SearchTask();
		this.input = input;
	}
	public SearchTask parseString(){
		String key =null;
		String value = "";
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
		parseFields();
		return task;
	}
	private void parseFields(){
		for( String k : fields.keySet()){
			switch(k){
				case "tasks":
					parseTask(fields.get(k));
				case "to":
					parseDate(fields.get(k),k);
				case "from":
					parseDate(fields.get(k),k);
				case "completed":
					parseCompleted(fields.get(k));
				case "priority":
					parsePriority(fields.get(k));
				case "category":
					parseCategory(fields.get(k));
			}
		}
	}
	private void parseCategory(String string) {
	    // TODO Auto-generated method stub
	    
    }
	private void parsePriority(String string) {
	    // TODO Auto-generated method stub
	    
    }
	private void parseCompleted(String string) {
	    // TODO Auto-generated method stub
	    
    }
	private void parseDate(String string, String k) {
	    // TODO Auto-generated method stub
	    
    }
	private void parseTask(String string) {
	    // TODO Auto-generated method stub
	    
    }
}
