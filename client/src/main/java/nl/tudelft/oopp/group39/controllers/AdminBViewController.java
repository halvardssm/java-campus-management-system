package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
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
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;




public class AdminBViewController extends MainSceneController implements Initializable {

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
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private TextField timeOpenFieldNew;
    @FXML
    private TextField timeClosedFieldNew;
    @FXML
    private TextField updateBuildingField;
    @FXML
    private TableColumn<Building, String> buildingnameCol = new TableColumn<>("Name");
    @FXML
    private TableColumn<Building, String> buildingidCol = new TableColumn<>("ID");
    @FXML
    private TableColumn<Building, String> buildinglocationCol = new TableColumn<>("Address");
    @FXML
    private TableColumn<Building, String> buildingdescriptionCol = new TableColumn<>("Description");
    @FXML
    private TableColumn<Building, LocalTime> buildingOpenTimeCol = new TableColumn<>("Open Time");
    @FXML
    private TableColumn<Building, LocalTime> buildingCTimeCol = new TableColumn<>("Closing Time");
    @FXML
    private TableView<Building> buildingTable = new TableView<>();


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBuildings();
    }

    /**
     * Display buildings and data in tableView buildingTable. -- Likely doesn't work yet.
     */
    void loadBuildings() {
        buildingTable.setVisible(true);
        buildingTable.getItems().clear();
        createAlert(ServerCommunication.get(ServerCommunication.building));


        buildingnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        buildingidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        buildinglocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        buildingdescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        buildingOpenTimeCol.setCellValueFactory(new PropertyValueFactory<>("open"));
        buildingCTimeCol.setCellValueFactory(new PropertyValueFactory<>("closed"));
    }


    /**
     * Adds a new building with auto-generated ID.
     */
    public void addBuilding() throws IOException {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        String open = getTime(timeOpenFieldNew.getText(), true);
        String closed = getTime(timeClosedFieldNew.getText(), false);
        createAlert(ServerCommunication.addBuilding(name, location, desc, open, closed));

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
        timeOpenFieldNew.clear();
        timeClosedFieldNew.clear();
        updateBuildingField.clear();
    }

    /**
     * Updates building based on ID specified.
     */

    public void updateBuilding() throws IOException {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        String open = getTime(timeOpenFieldNew.getText(), true);
        String closed = getTime(timeClosedFieldNew.getText(), false);
        String id = updateBuildingField.getText();
        id = id.contentEquals("") ? "1" : id;
        createAlert(ServerCommunication.updateBuilding(name, location, desc, open, closed, id));

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
        String id = updateBuildingField.getText();
        id = id.contentEquals("") ? "1" : id;
        ServerCommunication.removeBuilding(id);
        createAlert(ServerCommunication.get(ServerCommunication.building));

        updateBuildingField.clear();
    }

    /**
     * Doc. TODO
     */
    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
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

}
