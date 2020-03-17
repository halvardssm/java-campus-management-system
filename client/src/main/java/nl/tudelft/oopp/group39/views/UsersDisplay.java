package nl.tudelft.oopp.group39.views;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UsersDisplay extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage window;
    private static Parent root;

    /**
     * Doc. TODO Sven
     */
    @FXML
    public static void sceneHandler(String name) throws IOException {
        System.out.println("Scene changing...");
        root = FXMLLoader.load(UsersDisplay.class.getResource(name));
        window.setScene(new Scene(root));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/mainScene.fxml");
        loader.setLocation(xmlUrl);
        root = loader.load();

        window = primaryStage;
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
