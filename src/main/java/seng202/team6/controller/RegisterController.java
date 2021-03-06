package seng202.team6.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import seng202.team6.datahandling.DatabaseManager;
import seng202.team6.utilities.UserDataValidation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * <h1>Register Controller</h1>
 * <p>Initialises and applies functionality to the Register screen allowing users to make profiles in the database</p>
 */
public class RegisterController extends GeneralScreenController {

    /**
     * The maximum number of Users
     */
    int MAX_USER_NUMBER = 5;

    /**
     * The default number of steps for the user step goal
     */
    int DEFAULT_STEP_GOAL = 70000;

    /**
     * The default distance gor the user distance goal (KM's)
     */
    int DEFAULT_DISTANCE_GOAL = 30;

    /**
     * User details textual fields.
     */
    @FXML
    private TextField usernameEntry, firstNameEntry, lastNameEntry, heightEntry, weightEntry;

    /**
     * Date Picker for user to choose their birth date.
     */
    @FXML
    private DatePicker birthDateEntry;

    /**
     * Drop down field for user to pick their gender.
     */
    @FXML
    private ComboBox<String> genderComboBox;

    /**
     * User Registering textual details.
     */
    private String username, first, last, gender;

    /**
     * User registering numeric details.
     */
    private double height, weight;
    /**
     * The date of birth of the user.
     */
    private LocalDate birthDate;

    /**
     * The Application Database Manager.
     */
    private DatabaseManager databaseManager = ApplicationManager.getDatabaseManager();

    /**
     * Initialises the gender options for the drop down box.
     */
    @FXML
    void initialize() {
        ObservableList<String> availableChoices = FXCollections.observableArrayList("Male", "Female");
        genderComboBox.setItems(availableChoices);
    }

    /**
     * Redirects the user to the start screen when the back button clicked.
     * @param event When the back button is clicked.
     */
    @FXML
    public void toStartScreen(Event event) {
        if (ApplicationManager.exitEditingCheck()) {
            changeScreen(event, "/seng202/team6/view/StartScreen.fxml", "START");
        }
    }

    /**
     * Creates a new user with validated data from the input register form.
     * @param event When the user clicks the register button.
     * @throws ClassNotFoundException Error when getting class in database.
     * @throws SQLException Error with database.
     */
    @FXML
    public void createNewUser(ActionEvent event) {
        ArrayList<String> usernames = databaseManager.getUserReader().getUsernames();
        // Checks for Register Limit
        if (usernames.size() == MAX_USER_NUMBER) {
            ApplicationManager.displayPopUp("Maximum User Limit", "The maximum number of users allowed has been reached.\nYou cannot create more users.", "error");
        } else {

            // Checks for Duplicates
            setEnteredData();
            boolean duplicate = false;
            for (String user : usernames) {
                if (user.equalsIgnoreCase(username)) {
                    duplicate = true;
                }
            }

            // Checks if duplicates present then if so and notifies if so.
            if (duplicate == true) {
                ApplicationManager.displayPopUp("Username Already Exists", "Please choose a different username.", "error");
            } else if (validEnteredData()) {
                try {
                    databaseManager.getUserWriter().addUser(username, birthDate.toString(), first, last, gender, height, weight, DEFAULT_STEP_GOAL, DEFAULT_DISTANCE_GOAL);
                    ApplicationManager.displayPopUp("User Creation", "Well done you just created the user " + username + ".", "confirmation");
                    changeScreen(event, "/seng202/team6/view/StartScreen.fxml", "START");
                } catch (SQLException e) {
                    e.printStackTrace();
                    ApplicationManager.displayPopUp(e.getClass().getSimpleName(), e.getMessage(), "error");
                }

            }
        }
    }

    /**
     * Sets the class variables to the data from the form. Checks if the numeric data can be converted,
     * if not displays an error pop up.
     */
    private void setEnteredData() {
        username = usernameEntry.getText();
        first = firstNameEntry.getText();
        last = lastNameEntry.getText();
        gender = genderComboBox.getValue();
        birthDate = birthDateEntry.getValue();
        try {
            height = Double.parseDouble(heightEntry.getText());
            weight = Double.parseDouble(weightEntry.getText());
        } catch (NumberFormatException e) {
            ApplicationManager.displayPopUp("Invalid Data", "Please enter numerical data using numbers.", "error");
        }
    }

    /**
     * Validates all the entered user details from top to bottom, displaying error pop ups accordingly.
     * @return Whether all the entered user details are valid.
     */
    private boolean validEnteredData() {
        return UserDataValidation.validateUserName(username) &&
                UserDataValidation.validateName(first, "First Name") &&
                UserDataValidation.validateName(last, "Last Name") &&
                UserDataValidation.validateBirthDate(birthDate) &&
                UserDataValidation.validateGender(gender) &&
                UserDataValidation.validateDoubleValue(height, "Height", 280, 55, "cm") &&
                UserDataValidation.validateDoubleValue(weight, "Weight", 600,2, "kg");
    }
}
