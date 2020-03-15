package nl.tudelft.oopp.group39.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPController {
        @FXML
        private Button UListView ;
        @FXML
        private Button BView ;
        @FXML
        private Button RoomView;
        @FXML
        private Button EventView;

        @FXML
        private void switchUListView(ActionEvent actionEvent) throws IOException {
                Stage currentstage = (Stage) UListView.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/AdminUserList.fxml"));
                currentstage.setScene(new Scene(root, 700, 600));
        }

        @FXML
        private void switchRoomView(ActionEvent actionEvent) throws IOException {
                Stage currentstage = (Stage) RoomView.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/AdminRoomView.fxml"));
                currentstage.setScene(new Scene(root, 700, 600));
        }

        @FXML
        private void switchEvents(ActionEvent actionEvent) throws IOException {
                System.out.println("Not yet implemented");
        }

        @FXML
        private void switchBuildingView(ActionEvent actionEvent) throws IOException {
                Stage currentstage = (Stage) BView.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/AdminBuildingView.fxml"));
                currentstage.setScene(new Scene(root, 700, 600));
        }

}

