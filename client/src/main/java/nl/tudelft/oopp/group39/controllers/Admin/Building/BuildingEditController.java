package nl.tudelft.oopp.group39.controllers.Admin.Building;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.models.Building;

public class BuildingEditController extends BuildingListController implements Initializable {

    private Building building;
    @FXML
    private Button backbtn;
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


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(Building building) {
        this.building = building;
        nameFieldNew.setPromptText(building.getName());
        locationFieldNew.setPromptText(building.getLocation());
        descriptionFieldNew.setPromptText(building.getDescription());
        timeOpenFieldNew.setPromptText(building.getOpen());
        timeClosedFieldNew.setPromptText(building.getClosed());
    }

    /**
     * Doc. TODO
     */
    public String getTime(String time, boolean open) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
        if (open) {
            return time.contentEquals("") ? formatter.format(LocalTime.MAX) : time;
        }
        return time.contentEquals("") ? formatter.format(LocalTime.MIN) : time;
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Building/BuildingList.fxml", currentstage);
    }

    public void updateBuilding() throws IOException {
        String name = nameFieldNew.getText();
        name = name.contentEquals("") ? building.getName() : name;
        System.out.println(name);
        String location = locationFieldNew.getText();
        location = location.contentEquals("") ? building.getLocation() : location;
        String desc = descriptionFieldNew.getText();
        desc = desc.contentEquals("") ? building.getDescription() : desc;
        String open = timeOpenFieldNew.getText();
        open = open.contentEquals("") ? building.getOpen() : getTime(open, true);
        String closed =timeClosedFieldNew.getText();
        closed = closed.contentEquals("") ? building.getClosed() : getTime(closed, false);
        String id = Integer.toString(building.getId());
        ServerCommunication.updateBuilding(name, location, desc, open, closed, id);
        getBack();
        createAlert("Updated: " + building.getName());

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
        timeOpenFieldNew.clear();
        timeClosedFieldNew.clear();
    }

}
