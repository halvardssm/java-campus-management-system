package nl.tudelft.oopp.group39.views;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class UsersDisplay extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage window;
    private static Parent root;

    @FXML
    public static void sceneHandler(String name) throws IOException {
        System.out.println("Scene changing...");
        root = FXMLLoader.load(UsersDisplay.class.getResource(name));
        window.setScene(new Scene(root, 700, 600));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/buildingListView.fxml");
        loader.setLocation(xmlUrl);
        root = loader.load();

        window = primaryStage;
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
    }
}
