package seng202.team6.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team6.controller.ApplicationManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("startScreen2.fxml"));
        primaryStage.setTitle("MATES");
        primaryStage.setScene(new Scene(root, 1366, 768));
        primaryStage.show();
        ApplicationManager.initializeDatabaseManager();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
