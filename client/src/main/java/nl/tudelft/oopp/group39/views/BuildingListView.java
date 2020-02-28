package nl.tudelft.oopp.group39.views;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BuildingListView extends Application {

    public static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        window = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/buildingListView.fxml"));

        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.setMinHeight(primaryStage.getScene().getHeight());
        primaryStage.setMinWidth(primaryStage.getScene().getWidth());
        primaryStage.show();
    }
}