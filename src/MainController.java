package application;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import jfxtras.styles.jmetro8.JMetro.Style;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;

public class MainController implements Initializable {

	@FXML
	private Button firstClass;
	@FXML
	private Button secondClass;
	@FXML
	private Button thirdClass;
	@FXML
	private Button fourthClass;

	@FXML
	private Button backLab;

	@FXML
	private MenuItem openLabs;
	@FXML
	private MenuItem open2B;
	@FXML
	private MenuItem open3B;
	@FXML
	private MenuItem open3A;
	@FXML
	private MenuItem open4B;
	@FXML
	private MenuItem close;

	@FXML
	private MenuItem queryMenu;
	@FXML
	private MenuItem schemaMenu;

	@FXML
	private Button queryButton;
	@FXML
	private TextField query;
	@FXML
	private TextArea queryOutput;

	@FXML
	private MenuItem emptydb;
	@FXML
	private MenuItem recalc;

	public Map<String, String> dataFiles = new HashMap<String, String>();

	public FXMLLoader loader = new FXMLLoader();
	public JMetro jmetro = new JMetro(Style.DARK);

	public DataBase db = new DataBase();

	public void gotoStuTable(ActionEvent event) throws IOException, SQLException {

		FXMLLoader stuloader = new FXMLLoader();
		stuloader.setLocation(getClass().getResource("Students.fxml"));
		Parent stuPar = stuloader.load();
		Scene stuScene = new Scene(stuPar);

		stuController controller = stuloader.getController();
		String text = ((Button) event.getSource()).getText();
		controller.get_table_name(text);

		stuScene.getStylesheets().add(getClass().getResource("ui/style/Dark Theme.css").toExternalForm());

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setTitle("Students For Class " + text);
		window.setScene(stuScene);
		window.show();

	}

	public void ask_for_file(ActionEvent event) {
		Stage window = MainApp.getPrimaryStage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Data File");
		File selected = fileChooser.showOpenDialog(window);
		if (event.getSource() == open2B) {
			dataFiles.put("2B", selected.getAbsolutePath());
		} else if (event.getSource() == open3B) {
			dataFiles.put("3B", selected.getAbsolutePath());
		} else if (event.getSource() == open3A) {
			dataFiles.put("3A", selected.getAbsolutePath());
		} else if (event.getSource() == open4B) {
			dataFiles.put("4B", selected.getAbsolutePath());

		} else {
			dataFiles.put("Lab", selected.getAbsolutePath());
		}
	}

	public void closeWin() {
		Stage win = MainApp.getPrimaryStage();
		win.close();
	}

	public void openQuery(ActionEvent event) throws IOException {
		Stage window = new Stage();
		window.setTitle("Query DataBase");

		Parent queryPar = FXMLLoader.load(getClass().getResource("QueryScene.fxml"));
		Scene queryScene = new Scene(queryPar);

		queryScene.getStylesheets().add(getClass().getResource("Dark Theme.css").toExternalForm());

		window.setScene(queryScene);
		window.show();

	}

	public void openSchema() throws IOException {
		Stage window = new Stage();
		window.setTitle("Schema");
		Parent queryPar = FXMLLoader.load(getClass().getResource("schema.fxml"));
		Scene queryScene = new Scene(queryPar);

		queryScene.getStylesheets().add(getClass().getResource("ui/style/Dark Theme.css").toExternalForm());
		window.setScene(queryScene);
		window.show();
	}

	public void querydb() throws SQLException {
		queryOutput.clear();
		try {
			db.query(query.getText());
		} catch (SQLException e) {
			System.out.println("No results found...");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		OutputStream out = new OutputStream() {

			@Override
			public void write(int b) throws IOException {

				appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true

		));

	}

	public void appendText(String str) {
		Platform.runLater(() -> queryOutput.appendText(str));
	}

	public void trunc_db() throws SQLException {
		db.truncate_db();
	}

	public void recalcDb() throws FileNotFoundException, SQLException {
		for (Map.Entry<String, String> path : dataFiles.entrySet()) {
			switch (path.getKey()) {
				case ("Lab"):
					db.labFile = path.getValue();
					continue;
				case ("2B"):
					db.studentFile[0] = path.getValue();
					continue;
				case ("3B"):
					db.studentFile[1] = path.getValue();
					continue;
				case ("4B"):
					db.studentFile[2] = path.getValue();
					continue;
				case ("3A"):
					db.studentFile[3] = path.getValue();
					continue;
			}

		}
		db.setup_database();
	}

}
