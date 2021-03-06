package seng202.team6.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import seng202.team6.utilities.HealthConcernChecker;
import seng202.team6.datahandling.DatabaseManager;
import seng202.team6.models.Activity;
import seng202.team6.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.JUSTIFY;

public class HealthController extends GeneralScreenController {
    /**
     * HBox holds information panels
     */
    @FXML
    private HBox healthBox;

    /**
     * text field acts as a search bar
     */
    @FXML
    private TextField searchBar;

    /**
     * The type for the button that is pushed.
     * 0 : No button pushed
     * 1 : tachycardia button
     * 2 : bradycardia button
     * 3 : cardiovascular button
     * 4 : custom websearch
     */
    private static int type = 0;

    /**
     * The age of the current user
     */
    int age;

    /**
     * The current users activities
     */
    ArrayList<Activity> activities;

    /**
     * String that stores the users search query in text field
     */
    private static String query;

    /**
     * Initializes the health concern screen by displaying the information panel
     * corresponding to a particular health concerns if they are at risk for it.
     *
     * @throws SQLException When there is an error in the database when getting usernames and/or activities.
     */
    public void initialize() throws SQLException {
        getUserDetails();
        int i = 0;
        healthBox.setAlignment(Pos.CENTER);

        //if user has tachycardia, creates information panel with relevant informaton
        if (HealthConcernChecker.checkTachycardia(activities, age)) {

            GridPane healthGrid = addGrid();

            ImageView image = new ImageView("/seng202/team6/resources/pics/hearticon.png");
            healthGrid.setHalignment(image, HPos.CENTER);
            image.setFitHeight(50);
            healthGrid.add(image, i, 0);

            Label title = new Label("TACHYCARDIA");
            Label description = new Label("Tachycardia, also called tachyarrhythmia, is a heart rate that exceeds" +
                    " the normal resting rate. In general, a resting heart rate over 100 beats per minute is accepted as " +
                    "tachycardia in adults. Heart rates above the resting rate may be normal (such as with exercise) or " +
                    "abnormal (such as with electrical problems within the heart). ");
            description.setPrefWidth(280);
            description.setWrapText(true);
            description.setTextAlignment(JUSTIFY);
            title.setStyle("-fx-font: 17 NexaBold; -fx-fill: #494949; ");
            description.setStyle("-fx-font: 13 NexaBold; -fx-fill: #494949;");
            healthGrid.setHalignment(title, HPos.CENTER);
            healthGrid.setHalignment(description, HPos.CENTER);

            healthGrid.add(title, i, 1);
            healthGrid.add(description, i, 2);

            i++;
            healthBox.getChildren().add(healthGrid);
        }

        //if user has bradycardia, creates information panel with relevant informaton
        if (HealthConcernChecker.checkBradycardia(activities, age)) {
            GridPane healthGrid = addGrid();
            ImageView image = new ImageView("/seng202/team6/resources/pics/hearticon.png");
            healthGrid.setHalignment(image, HPos.CENTER);
            image.setFitHeight(50);
            healthGrid.add(image, i, 0);

            Label title = new Label("BRADYCARDIA");
            Label description = new Label("Bradycardia is a condition wherein an individual has a very slow heart " +
                    "rate, typically defined as a resting heart rate of under 60 beats per minute (BPM) in adults. " +
                    "When symptomatic, " +
                    "it may cause fatigue, weakness, dizziness, sweating, and at very low rates, fainting.");
            description.setPrefWidth(280);
            description.setWrapText(true);
            description.setTextAlignment(JUSTIFY);
            title.setStyle("-fx-font: 17 NexaBold; -fx-fill: #494949;");
            description.setStyle("-fx-font: 13 NexaBold; -fx-fill: #494949;");
            healthGrid.setHalignment(title, HPos.CENTER);
            healthGrid.setHalignment(description, HPos.CENTER);
            healthGrid.add(title, i, 1);
            healthGrid.add(description, i, 2);
            i++;
            healthBox.getChildren().add(healthGrid);
        }

        //if user has cardiovascular mortality, creates information panel with relevant informaton
        if (HealthConcernChecker.checkCardiovascularMortality(activities, age)) {
            GridPane healthGrid = addGrid();
            ImageView image = new ImageView("/seng202/team6/resources/pics/hearticon.png");
            healthGrid.setHalignment(image, HPos.CENTER);
            image.setFitHeight(50);
            healthGrid.add(image, i, 0);

            Label title = new Label("CARDIOVASCULAR DISEASE");
            Label description = new Label("Cardiovascular disease (CVD) is a class of diseases that involve the " +
                    "heart or blood vessels. Cardiovascular disease includes coronary artery diseases (CAD) such as " +
                    "angina and myocardial infarction (commonly known as a heart attack). Other CVDs include stroke, " +
                    "heart failure, hypertensive heart disease, rheumatic heart disease, cardiomyopathy, heart arrhythmia, " +
                    "congenital heart disease, valvular heart disease, carditis, aortic aneurysms, peripheral artery disease, " +
                    "thromboembolic disease, and venous thrombosis.");
            description.setPrefWidth(280);
            description.setWrapText(true);
            description.setTextAlignment(JUSTIFY);
            title.setStyle("-fx-font: 17 NexaBold; -fx-fill: #494949;");
            description.setStyle("-fx-font: 13 NexaBold; -fx-fill: #494949;");
            healthGrid.setHalignment(title, HPos.CENTER);
            healthGrid.setHalignment(description, HPos.CENTER);
            healthGrid.add(title, i, 1);
            healthGrid.add(description, i, 2);
            i++;
            healthBox.getChildren().add(healthGrid);
        }
        if (!HealthConcernChecker.checkCardiovascularMortality(activities, age) && !HealthConcernChecker.checkBradycardia(activities, age) && !HealthConcernChecker.checkTachycardia(activities, age)) {
            GridPane healthGrid = addGrid();
            ImageView image = new ImageView("/seng202/team6/resources/pics/hearticon.png");
            healthGrid.setHalignment(image, HPos.CENTER);
            image.setFitHeight(50);
            healthGrid.add(image, i, 0);

            Label title = new Label("NOTHING");
            Label description = new Label("You are healthy!");
            description.setPrefWidth(280);
            description.setWrapText(true);
            description.setTextAlignment(CENTER);
            title.setStyle("-fx-font: 17 NexaBold; -fx-fill: #494949;");
            description.setStyle("-fx-font: 13 NexaBold; -fx-fill: #494949;");
            healthGrid.setHalignment(title, HPos.CENTER);
            healthGrid.setHalignment(description, HPos.CENTER);
            healthGrid.add(title, i, 1);
            healthGrid.add(description, i, 2);
            i++;
            healthBox.getChildren().add(healthGrid);
        }
    }

    /**
     * dynamically creates a information panel gui
     * @return grid
     */
    private GridPane addGrid() {
        //formatting
        GridPane grid = new GridPane();
        healthBox.setMargin(grid, new Insets(50, 20, 50, 20));
        grid.setPrefSize(280, 484);
        grid.setPadding(new Insets(40,20,20,20));
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);

        //sets image icon
        Image image = new Image("/seng202/team6/resources/pics/healthGrid.png");
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        //sets background image
        grid.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));

        return grid;
    }


    /**
     * Gets a the current users age and activities
     *
     * @throws SQLException When there is an error in the database when getting usernames and/or activities.
     */
    private void getUserDetails() throws SQLException {
        DatabaseManager databaseManager = ApplicationManager.getDatabaseManager();
        String userName = ApplicationManager.getCurrentUsername();
        User user = databaseManager.getUserReader().getUser(userName);
        age = user.getAge();
        activities = databaseManager.getActivityManager().getActivitiesWithRecords(ApplicationManager.getCurrentUserID());

    }

    /**
     * Sets the type of the button pushed to 1 (tachycardia button)
     * Redirects user to the web search screen.
     *
     * @param event When the user clicks the tachycardiaButton.
     */
    public void toTachycardiaWebSearchScreen(ActionEvent event) {
        type = 1;
        changeScreen(event, "/seng202/team6/view/WebSearch.fxml", "HEALTH");
    }


    /**
     * Sets the type of the button pushed to 2 (bradycardia button)
     * Redirects user to the web search screen.
     *
     * @param event When the user clicks the bradycardiaButton.
     */
    public void toBradycardiaWebSearchScreen(ActionEvent event) {
        type = 2;
        changeScreen(event, "/seng202/team6/view/WebSearch.fxml", "HEALTH");
    }


    /**
     * Sets the type of the button pushed to 3 (cardiovascular button)
     * Redirects user to the web search screen.
     *
     * @param event When the user clicks the cardiovascularButton.
     */
    public void toCardiovascularWebSearchScreen(ActionEvent event) {
        type = 3;
        changeScreen(event, "/seng202/team6/view/WebSearch.fxml", "HEALTH");
    }


    /**
     * Returns the type of the button pushed
     *
     * @return an int indicating the button that was pushed
     */
    public static int getType() {
        return type;
    }

    /**
     * Sets the type of health concern
     *
     * @param Type
     */
    public static void setType(int Type) {
        type = Type;
    }

    /**
     * changes screen to websearch when user presses enter
     * @param ae
     */
    @FXML
    public void onEnter(ActionEvent ae){
        setType(4);
        setURL(searchBar.getCharacters().toString());
        changeScreen(ae, "/seng202/team6/view/WebSearch.fxml", "HEALTH");
        //searchBar.clear();
    }

    /**
     * sets query to the input from search bar
     * @param searchquery
     */
    public static void setURL(String searchquery) {
        query = searchquery;
    }

    /**
     * returns the search query string
     * @return query
     */
    public static String getURL() {
        return query;
    }
}