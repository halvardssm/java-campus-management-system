package nl.tudelft.oopp.group39.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminUListController
{
    @FXML
    private Button backbtn;

    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }


}
