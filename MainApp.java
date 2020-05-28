package application;

import java.io.IOException;
import jfxtras.styles.jmetro8.JMetro;
import jfxtras.styles.jmetro8.JMetro.Style;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage window;
    private AnchorPane rootLayout;
    private GridPane TableLayout;
    private FXMLLoader loader = new FXMLLoader();
    private JMetro jmetro = new JMetro(Style.DARK);
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.window = primaryStage;
        this.window.setTitle("Lab/Project Progress Tracker");
        showStartMenu();
    }
    public void showStartMenu() throws IOException {
    	loader.setLocation(getClass().getResource("Welcome Screen.fxml"));
    	rootLayout = loader.load();
    	Scene welcomeScreen = new Scene(rootLayout);
    	window.setScene(welcomeScreen);
        jmetro.applyTheme(welcomeScreen);
        welcomeScreen.getStylesheets().add(getClass().getResource("Dark Theme.css").toExternalForm());
        window.show();
    
    }
    static public Stage getPrimaryStage() {
        return MainApp.window;
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}