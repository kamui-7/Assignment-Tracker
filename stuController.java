package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import jfxtras.styles.jmetro8.JMetro.Style;

public class stuController implements Initializable {
	
	
	@FXML private Button backStu;

	
	private DataBase db = new DataBase(); 
	private JMetro jmetro = new JMetro(Style.DARK);
	private String className;
	
	
	@FXML public TableView<Student> stuTableView;
	@FXML public TableColumn<Student, String> stuName;
	@FXML public Button goLab;
	
	
	public Scene setStart() throws IOException {

		Parent start = FXMLLoader.load(getClass().getResource("Welcome Screen.fxml"));
    	Scene startscene = new Scene(start);
    	
    	
        jmetro.applyTheme(startscene);
        startscene.getStylesheets().add(getClass().getResource("Dark Theme.css").toExternalForm());
        return startscene;
	}
	
	public void go_back_stu(ActionEvent event) throws IOException {

		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		Scene welcome = setStart();
		window.setTitle("Lab/Project Progress Tracker");
		window.setScene(welcome);
		window.show();
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		stuName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));

	}
	

	public void get_table_name(String table) throws SQLException{
		this.className = table;

		stuTableView.setItems(getStudents("Class_" + className));


	}
	
	public ObservableList<Student> getStudents(String table) throws SQLException{
		ObservableList<Student> students = FXCollections.observableArrayList();
		ArrayList<Student> stuData = db.get_stu_db(table);
		for ( Student x : stuData) {
			students.add(x);
		}
		return students;
		
		
		
	}
	
	public void go_lab(ActionEvent event) throws IOException, SQLException {
		Stage labStage = new Stage();
		
		FXMLLoader labloader = new FXMLLoader();
		labloader.setLocation(getClass().getResource("LabView.fxml"));
		Parent labPar = labloader.load();
		Scene labScene = new Scene(labPar); 	

		labController controller = labloader.getController();
		controller.initData((Student) stuTableView.getSelectionModel().getSelectedItem(), className);
		labScene.getStylesheets().add(getClass().getResource("Dark Theme.css").toExternalForm());
		labStage.setTitle(stuTableView.getSelectionModel().getSelectedItem().getName());
		labStage.setScene(labScene);
		labStage.show();
	}
	
	
	
	
}
