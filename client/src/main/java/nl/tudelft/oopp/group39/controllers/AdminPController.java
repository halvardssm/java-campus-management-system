package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class AdminPController {
    @FXML
    private Button userlistView;
    @FXML
    private Button buildingView;
    @FXML
    private Button roomView;
    @FXML
    private Button eventView;

    @FXML
    private void switchUListView(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) userlistView.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminUserList.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    @FXML
    private void switchRoomView(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) roomView.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminRoomView.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    @FXML
    private void switchEvents(ActionEvent actionEvent) throws IOException {
        System.out.println("Not yet implemented");
    }

    @FXML
    private void switchBuildingView(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) buildingView.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminBuildingView.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

}

