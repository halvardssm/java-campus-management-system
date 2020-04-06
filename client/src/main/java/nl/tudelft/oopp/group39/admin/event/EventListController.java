package nl.tudelft.oopp.group39.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.AdminPanelController;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;

@SuppressWarnings("unchecked")
public class EventListController extends AdminPanelController {
    private ObjectMapper mapper = new ObjectMapper();
    @FXML
    private Button backbtn;
    @FXML
    private MenuBar navBar;
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> idCol;
    @FXML private TableColumn<Event, String> typeCol;
    @FXML private TableColumn<Event, String> startCol;
    @FXML private TableColumn<Event, String> endCol;
    @FXML private TableColumn<Event, String> globalCol;
    @FXML private TableColumn<Event, String> userCol;
    @FXML
    private TableColumn<Event, Event> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Event, Event> updateCol = new TableColumn<>("Update");

    /**
     * Initializes scene.
     */
    public void customInit() {
        try {
            loadAllEvents();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Ensures that all events are put into table view.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    void loadAllEvents() throws JsonProcessingException {

        String events = ServerCommunication.get(ServerCommunication.event);
        System.out.println(events);
        loadEvents(events);
    }

    /**
     * Display events and data in tableView named eventTable.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    void loadEvents(String events) throws JsonProcessingException {
        eventTable.setVisible(true);
        eventTable.getItems().clear();
        eventTable.getColumns().clear();

        ArrayNode body = (ArrayNode) mapper.readTree(events).get("body");
        events = mapper.writeValueAsString(body);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startsAt"));
        globalCol.setCellValueFactory(new PropertyValueFactory<>("global"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endsAt"));
        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> returnCell("Update"));
        Event[] list = mapper.readValue(events, Event[].class);
        ObservableList<Event> data = FXCollections.observableArrayList(list);
        eventTable.setItems(data);
        eventTable.getColumns().addAll(
                idCol, typeCol, startCol, endCol, globalCol, userCol, deleteCol, updateCol);
    }

    /**
     * Gets all (String) User ids.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public ObservableList<String> getUserIds() throws JsonProcessingException {
        String users = ServerCommunication.get(ServerCommunication.user);
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        User[] list = mapper.readValue(users, User[].class);
        List<String> dataList = new ArrayList<>();
        for (User user : list) {
            dataList.add(user.getUsername());
        }
        return FXCollections.observableArrayList(dataList);
    }

    /**
     * Inserts the update and delete buttons into table.
     */
    public TableCell<Event, Event> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Event abcEvent, boolean empty) {
                super.updateItem(abcEvent, empty);

                if (abcEvent == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editEvent(abcEvent);
                                    break;
                                case "Delete":
                                    deleteEvent(abcEvent);
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        };
    }

    /**
     * Sends user to Create Event page.
     *
     * @throws IOException if an error occurs during loading
     */
    public void createEvent() throws IOException {
        FXMLLoader loader = switchFunc("/admin/event/EventCreate.fxml");
        EventCreateController controller = loader.getController();
        controller.customInit();
        controller.changeUserBox();
    }

    /**
     * Deletes selected event.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void deleteEvent(Event event) throws JsonProcessingException {
        String id = Long.toString(event.getId());
        ServerCommunication.removeEvent(id);
        loadAllEvents();
    }

    /**
     * Sends user to the event edit page.
     *
     * @throws IOException if an error occurs during loading
     */
    public void editEvent(Event event) throws IOException {
        FXMLLoader loader = switchFunc("/admin/event/EventEdit.fxml");
        EventEditController controller = loader.getController();
        controller.initData(event);
        controller.changeUserBox();
    }

    /**
     * Goes back to main admin panel.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchBack() throws IOException {
        switchFunc("/admin/AdminPanel.fxml");
    }

    /**
     * Used to switch windows.
     *
     * @throws IOException if an error occurs during loading
     */
    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }
}

