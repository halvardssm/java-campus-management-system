package nl.tudelft.oopp.group39.views;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.controllers.MainSceneController;

public class UsersDisplay extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage window;
    private static Parent root;
    private static int width = 700;
    private static int height = 600;

    /**
     * Doc. TODO Sven
     */
    @FXML
    public static void sceneHandler(String name) throws IOException {
        System.out.println("Scene changing...");
        root = FXMLLoader.load(UsersDisplay.class.getResource(name));
        window.setScene(new Scene(root, width, height));
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

        FXMLLoader loader = new FXMLLoader(UsersDisplay.class.getResource(name));
        root = loader.load();
        window.setScene(new Scene(root, width, height));

        return loader.getController();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/roomReservation.fxml");
        loader.setLocation(xmlUrl);
        root = loader.load();

        window = primaryStage;
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setTitle("Campus Management");
        primaryStage.show();
    }
}
