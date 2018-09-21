package seng202.team6.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team6.datahandling.DatabaseManager;
import seng202.team6.models.Activity;
import seng202.team6.models.ActivityDataPoint;
import seng202.team6.models.User;

import javafx.scene.image.ImageView;

import javax.activity.ActivityCompletedException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RawDataController2 extends WorkoutsNavigator{

    @FXML
    private TableView rawDataTable;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label notesLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ListView activitySelect;

    private static ListView activitySelectEdit;

    @FXML
    private ImageView filterButton;

    private DatabaseManager dbManager = ApplicationManager.getDatabaseManager();

    public void initialize() throws SQLException {
        User currUser = dbManager.getUser(ApplicationManager.getCurrentUserName());
        ObservableList<Integer> activityList = FXCollections.observableArrayList(dbManager.getActivityIDs(currUser.getUserID()));
        activitySelect.setItems(activityList);
        setupTable();
    }

    private TableColumn make_column(String name, String attributeName, int size) {
        TableColumn col = new TableColumn(name);
        col.setCellValueFactory(new PropertyValueFactory<>(attributeName));
        col.setPrefWidth((rawDataTable.getPrefWidth()/5)-1);
        col.setResizable(false);
        return col;
    }

    private void setupTable() {
        TableColumn timeCol = make_column("Time", "time", 120);
        TableColumn heartRateCol = make_column("Heart Rate", "heartRate", 120);
        TableColumn latitudeCol = make_column("Latitude", "latitude", 120);
        TableColumn longitudeCol = make_column("Longitude", "longitude", 120);
        TableColumn elevationCol = make_column("Elevation", "elevation", 120);

        rawDataTable.getColumns().addAll(timeCol, heartRateCol, latitudeCol, longitudeCol, elevationCol);
    }

    public void addRecordToTable(ActivityDataPoint record) throws SQLException {
        rawDataTable.getItems().add(record);
    }

    public void showActivity() throws SQLException {
        ArrayList<ActivityDataPoint> records = dbManager.getActivityRecords((int) activitySelect.getSelectionModel().getSelectedItem());
        for ( int i = 0; i<rawDataTable.getItems().size(); i++) {
            rawDataTable.getItems().clear();
        }

        for (ActivityDataPoint record : records) {
            addRecordToTable(record);
        }

        descriptionLabel.setText(dbManager.getActivity((int) activitySelect.getSelectionModel().getSelectedItem()).getDescription());
        dateLabel.setText(dbManager.getActivity((int) activitySelect.getSelectionModel().getSelectedItem()).getStartDate().toString());
        typeLabel.setText(dbManager.getActivity((int) activitySelect.getSelectionModel().getSelectedItem()).getType());
        notesLabel.setText(dbManager.getActivity((int) activitySelect.getSelectionModel().getSelectedItem()).getNotes());

        activitySelectEdit = activitySelect;

    }

    public static ListView getActivitySelector() {
        return activitySelectEdit;
    }

    @FXML
    public void filterActivities(Event event) {
        ApplicationManager.displayPopUp("Activity Filtering", "To be - filtering window", "notification");
    }

    @FXML
    public void lightenFilter(Event event) {

    }

    @FXML
    public void darkenFilter(Event event) {

    }

    /**
     * Navigates the user to the Workouts Splash Screen.
     * @param event When the user clicks on a button directing to this screen.
     */
    @FXML
    public void toActivityEditScreen(Event event) {
        changeScreen(event, "/seng202/team6/view/RawDataVeiwerEditing.fxml");
    }
}
