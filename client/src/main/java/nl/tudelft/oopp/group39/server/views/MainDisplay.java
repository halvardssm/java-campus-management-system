package nl.tudelft.oopp.group39.server.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class MainDisplay extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage window;
    private static Parent root;
    private static Scene previous;

    /**
     * Switches view to a new scene.
     *
     * @param location the location of the scene fxml
     * @throws IOException if view wasn't found.
     */
    @FXML
    public static void sceneHandler(String location) throws IOException {
        sceneControllerHandler(location);
    }

    /**
     * Changes scenes and returns controller of the new scene.
     *
     * @param location location of the scene we want
     * @return controller of the new scene
     * @throws IOException if there is something wrong
     */
    @FXML
    public static AbstractSceneController sceneControllerHandler(String location)
        throws IOException {
        System.out.println("Scene changing...");

        FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource(location));
        root = loader.load();
        previous = window.getScene();
        window.setScene(new Scene(root, previous.getWidth(), previous.getHeight()));
        return loader.getController();
    }

    /**
     * Switch to the previous scene.
     */
    @FXML
    public static void backToPrevious() {
        window.setScene(previous);
    }

    /**
     * The start of the application.
     *
     * @param primaryStage the primary stage
     * @throws IOException if the file wasn't found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("client/src/main/resources/program.properties"));
            ServerCommunication.url = properties.getProperty("connection.url") + ":"
                + properties.getProperty("connection.port") + "/";
        } catch (Exception e) {
            ServerCommunication.url = "http://localhost:8080/";
            properties.setProperty("pref.width", "900");
            properties.setProperty("pref.height", "600");
        }

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/building/buildingListView.fxml");
        loader.setLocation(xmlUrl);
        root = loader.load();

        window = primaryStage;
        int width = Integer.parseInt(properties.getProperty("pref.width"));
        int height = Integer.parseInt(properties.getProperty("pref.height"));
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setTitle("Campus Management");
        primaryStage.show();
    }
}
