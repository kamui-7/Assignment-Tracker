package application;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class labController implements Initializable {

	private Student selectedStudent;
	private String className;

	@FXML
	private Button saveButton;

	@FXML
	private TableView<Project> labTable;
	@FXML
	private TableColumn<Project, String> labName;
	@FXML
	private TableColumn<Project, Integer> points;
	@FXML
	private TableColumn<Project, Boolean> completed;
	@FXML
	private TableColumn<Project, Date> datefinished;

	private DataBase db = new DataBase();

	public void initData(Student student, String className) throws SQLException {
		selectedStudent = student;
		this.className = className;
		labTable.setItems(getLabs(className, selectedStudent));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		labName.setCellValueFactory(new PropertyValueFactory<Project, String>("projectName"));
		points.setCellValueFactory(new PropertyValueFactory<Project, Integer>("points"));
		completed.setCellFactory(CheckBoxTableCell.forTableColumn(completed));
		completed.setCellValueFactory(new PropertyValueFactory<Project, Boolean>("completed"));
		completed.setCellValueFactory(new Callback<CellDataFeatures<Project, Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Project, Boolean> param) {
				return param.getValue().getCompleted();
			}
		});
		datefinished.setCellValueFactory(new PropertyValueFactory<Project, Date>("dateFinished"));
		labTable.setEditable(true);

	}

	public ObservableList<Project> getLabs(String table, Student student) throws SQLException {
		ObservableList<Project> labInfo = FXCollections.observableArrayList();
		ArrayList<Project> labData = db.get_lab_db(table, student.getName());
		for (Project x : labData) {
			System.out.println(x.getProjectName() + ":    " + x.isCompleted().get());
			labInfo.add(x);
		}
		return labInfo;

	}

	public void save() throws SQLException {
		for (Project project : labTable.getItems()) {
			if (project.getCompleted().getValue() && project.getDateFinished().equalsIgnoreCase("")) {
				Date currDate = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("M-dd-yyyy hh:mm:ss a");
				String strDate = formatter.format(currDate);
				project.setDateFinished(strDate);
				db.update_row(selectedStudent.getName(), "Class_" + className, true, strDate, project.getProjectName());

			}

		}
		labTable.refresh();
	}

}
