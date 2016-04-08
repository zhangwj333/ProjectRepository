package junstech.model;

import java.util.ArrayList;

public class Question {

	String title;
	
	ArrayList<Option> options = new ArrayList<Option>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<Option> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<Option> options) {
		this.options = options;
	}
	
	public void addOption(Option option){
		this.options.add(option);
	}
	
}
