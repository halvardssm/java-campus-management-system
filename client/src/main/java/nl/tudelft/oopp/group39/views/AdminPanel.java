package nl.tudelft.oopp.group39.views;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import nl.tudelft.oopp.group39.controllers.MainSceneController;

public class AdminPanel extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/Admin/AdminPanel.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 650);
        window = primaryStage;
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage window;
    private static Parent root;
    private static int width = 900;
    private static int height = 650;

    /**
     * Doc. TODO Sven
     */
    @FXML
    public static void sceneHandler(String name) throws IOException {
        sceneControllerHandler(name);
    }

    /**
     * Changes scenes and returns controller of the new scene.
     *
     * @param name name of the scene we want
     * @return controller of the new scene
     * @throws IOException if there is something wrong
     */
    @FXML
    public static MainSceneController sceneControllerHandler(String name) throws IOException {
        System.out.println("Scene changing...");

        FXMLLoader loader = new FXMLLoader(AdminPanel.class.getResource(name));
        root = loader.load();
        window.setScene(new Scene(root, width, height));

        return loader.getController();
    }
}