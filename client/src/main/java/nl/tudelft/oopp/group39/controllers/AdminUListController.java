package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class AdminUListController {
    @FXML
    private Button backbtn;
    @FXML private TableView<User> usertable;
    @FXML private TableColumn<User, String> idCol;
    @FXML private TableColumn<User, String> nameCol;
    @FXML private TableColumn<User, String> statusCol;

    public class User {

    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

}
