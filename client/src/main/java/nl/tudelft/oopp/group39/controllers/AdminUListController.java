package nl.tudelft.oopp.group39.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class AdminUListController
{
    @FXML
    private Button backbtn;
    @FXML private TableView<User> usertable;
    @FXML private TableColumn<User, String> IDCol;
    @FXML private TableColumn<User, String> nameCol;
    @FXML private TableColumn<User, String> statusCol;

    
    final ObservableList<User> data = FXCollections.observableArrayList(
            new User("Jacob", "Smith", "jacob.smith@example.com"),
            new User("Isabella", "Johnson", "isabella.johnson@example.com"),
            new User("Ethan", "Williams", "ethan.williams@example.com"),
            new User("Emma", "Jones", "emma.jones@example.com"),
            new User("Michael", "Brown", "michael.brown@example.com")
    );
    private void start(){
    IDCol.setCellValueFactory(
            new PropertyValueFactory<User,String>("ID")
            );
    nameCol.setCellValueFactory(
        new PropertyValueFactory<User,String>("Name")
        );
    statusCol.setCellValueFactory(
        new PropertyValueFactory<User,String>("Status")
        );
    }

    public static class User {
        private final SimpleStringProperty ID;
        private final SimpleStringProperty Name;
        private final SimpleStringProperty Status;

        private User(String identity, String Naam, String position) {
            this.ID = new SimpleStringProperty(identity);
            this.Name = new SimpleStringProperty(Naam);
            this.Status = new SimpleStringProperty(position);
        }

        public String getID() {
            return ID.get();
        }
        public void setID(String fName) {
            ID.set(fName);
        }

        public String getLastName() {
            return Name.get();
        }
        public void setLastName(String fName) {
            Name.set(fName);
        }

        public String getEmail() {
            return Status.get();
        }
        public void setEmail(String fName) {
            Status.set(fName);
        }

    }

    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

}
