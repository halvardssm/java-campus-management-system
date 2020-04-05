package nl.tudelft.oopp.group39.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class MainAdminController extends AbstractSceneController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Used to switch between different FXML files/scenes.
     */

    @FXML
    public FXMLLoader mainSwitch(String resource, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 900, 650));
        return loader;
    }
}
