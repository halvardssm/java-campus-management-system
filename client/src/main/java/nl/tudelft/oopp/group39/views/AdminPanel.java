package nl.tudelft.oopp.group39.views;

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

public class AdminPanel extends Application {

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
        URL xmlUrl = getClass().getResource("/admin/AdminPanel.fxml");
        loader.setLocation(xmlUrl);
        root = loader.load();

        window = primaryStage;
        int width = Integer.parseInt(properties.getProperty("pref.width"));
        int height = Integer.parseInt(properties.getProperty("pref.height"));
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setTitle("Campus Management");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage window;
    private static Parent root;

    /**
     * SceneHandler function to help change scenes.
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
    public static AbstractSceneController sceneControllerHandler(String name) throws IOException {
        System.out.println("Scene changing...");

        FXMLLoader loader = new FXMLLoader(AdminPanel.class.getResource(name));
        root = loader.load();
        int height = 650;
        int width = 900;
        window.setScene(new Scene(root, width, height));

        return loader.getController();
    }
}