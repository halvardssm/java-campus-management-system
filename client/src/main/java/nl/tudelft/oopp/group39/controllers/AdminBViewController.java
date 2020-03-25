package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.models.Building;




public class AdminBViewController extends BuildingModifierController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button buildingUpdate;
    @FXML
    private Button buildingAdd;
    @FXML
    private Button buildingDelete;
    @FXML
    private TextField nameFieldNew;
    @FXML
    private TextField updateBuildingField;
    @FXML
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private TextField timeOpenFieldNew;
    @FXML
    private TextField timeClosedFieldNew;
    @FXML
    private TableColumn<Building, String> buildingnameCol = new TableColumn<>("Name");
    @FXML
    private TableColumn<Building, String> buildinglocationCol = new TableColumn<>("Address");
    @FXML
    private TableColumn<Building, String> buildingdescriptionCol = new TableColumn<>("Description");
    @FXML
    private TableColumn<Building, LocalTime> buildingOpenTimeCol = new TableColumn<>("Open Time");
    @FXML
    private TableColumn<Building, LocalTime> buildingClTimeCol = new TableColumn<>("Closing Time");
    @FXML
    private TableView<Building> buildingTable = new TableView<>();



    /**
     * Display buildings and data in tableView buildingTable.
     */
    void loadBuildings() {
        buildingTable.setVisible(true);
        buildingTable.getItems().clear();
        getBuildingsButton();



        buildingnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        buildinglocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        buildingdescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        buildingOpenTimeCol.setCellValueFactory(new PropertyValueFactory<>("open"));
        buildingClTimeCol.setCellValueFactory(new PropertyValueFactory<>("closed"));
    }



    /**
     * TODO - Add building using buttons.
     */
    public void addBuilding() throws IOException {
        newBuildingButton();

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
        timeOpenFieldNew.clear();
        timeClosedFieldNew.clear();
        updateBuildingField.clear();
    }

    /**
     * TODO - Update building using buttons.
     */

    public void updateBuilding() throws IOException {
        updateBuildingButton();

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
        timeOpenFieldNew.clear();
        timeClosedFieldNew.clear();
        updateBuildingField.clear();
    }

    /**
     * Delete building.
     */
    public void deleteBuilding() throws IOException {
        removeBuildingButton();

        updateBuildingField.clear();
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void switchBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
