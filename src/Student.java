package application;

import javafx.beans.property.SimpleStringProperty;

public class Student {

	private SimpleStringProperty name;

	public Student(String name) {
		super();
		this.name = new SimpleStringProperty(name);
	}

	public String getName() {
		return name.get();
	}

}
