package application;

import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Project {

	private SimpleStringProperty projectName;
	private int points;
	private BooleanProperty completed;
	private String dateFinished;

	public Project(String project, int points, Boolean completed, String dateFinished) {
		this.projectName = new SimpleStringProperty(project);
		this.points = points;
		this.completed = new SimpleBooleanProperty(completed);
		this.dateFinished = dateFinished;
	}

	public String getProjectName() {
		return projectName.get();
	}

	public int getPoints() {
		return points;
	}

	public ObservableValue<Boolean> getCompleted() {
		return completed;
	}

	public String getDateFinished() {
		return dateFinished;
	}

	public void setCompleted(Boolean completed) {
		this.completed.set(completed);
	}

	public void setDateFinished(String dateFinished) {
		this.dateFinished = dateFinished;
	}

	public BooleanProperty isCompleted() {
		return completed;
	}

}
