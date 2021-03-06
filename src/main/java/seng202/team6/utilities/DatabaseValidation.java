package seng202.team6.utilities;

import seng202.team6.controller.ApplicationManager;
import seng202.team6.datahandling.DatabaseManager;
import seng202.team6.models.Activity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

/**
 * DatabaseValidation handles all the database validation before being inputted in the database.
 */
public class DatabaseValidation {

    /**
     * A function that validates the data date, time, heart rate, latitude, longitude and elevation.
     * @param data An ArrayList of data to be validated.
     * @param databaseManager An instance of Database Manager
     * @return Returns true if data is valid and false if not.
     * @throws SQLException
     */
    public static boolean validate(ArrayList<String[]> data, DatabaseManager databaseManager) throws SQLException {
        if(!validateNotEmpty(data)) {
            return false;
        }else {
            if (!(validateFirstLine(data) && validateLineLength(data))) {
                return false;
            } else {
                for (String[] line : data) {
                    if (!line[0].equalsIgnoreCase("#start")) {
                        if (!(validateDate(line[0])
                                && validateTime(line[1])
                                && validateHeartRate(line[2])
                                && validateLatitude(line[3])
                                && validateLongitude(line[4])
                                && validateElevation(line[5]))) {
                            return false;
                        } else {
                            if (!validateNonDuplicateData(data, databaseManager)) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        }
    }

    /**
     * A function that validates the line length of the CSV file.
     * @param data An ArrayList of String data that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateLineLength(ArrayList<String[]> data){
        if(data.get(0).length < 6){
            System.out.println("Invalid line length detected!");
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "There is an invalid number of data fields detected in the CSV file.", "error");
            }
            return false;
        }
        for (String[] line : data){
            if(!line[0].equalsIgnoreCase("#start")) {
                int i = 0;
                while (i < 6){
                    if (line[i].length() == 0) {
                        System.out.println("Missing field detected!");
                        if(ApplicationManager.getCurrentUserID() != 0) {
                            ApplicationManager.displayPopUp("Invalid Data", "There is a missing field detected in the CSV file.", "error");
                        }
                        return false;
                    }
                    i++;
                }
            }
        }
        return true;
    }

    /**
     * A function that validates the first line of the CSV data.
     * @param data An ArrayList String data that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateFirstLine(ArrayList<String[]> data){
        String[] firstLine = data.get(0);
        if(firstLine[0].equalsIgnoreCase("#start")){
            return true;
        }
        else {
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Invalid first line of the CSV file detected.", "error");
            }
            System.out.println("Invalid first line detected!");
            return false;
        }
    }

    /**
     * A function that validates if the activity data is empty.
     * @param data An ArrayList String data that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateNotEmpty(ArrayList<String[]> data){
        if(!(data.size() < 2 && data.get(0).length == 1 && data.get(0)[0].length() == 0)){
            return true;
        }
        else {
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "The CSV file is empty.", "error");
            }
            System.out.println("Empty csv file detected!");
            return false;
        }
    }

    /**
     * A function that validates the longitude data from the activity data.
     * @param inLongitude A String that represents the longitude that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateLongitude(String inLongitude){
        if(!GeneralUtilities.isValidDouble(inLongitude)){
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure longitude is a numerical value and is in range of -180 to 180 degrees.", "error");
            }
            System.out.println("Invalid longitude detected!");
            return false;
        }
        else{
            double longitude = Double.parseDouble(inLongitude);
            if(!(longitude >= -180 && longitude <= 180)){
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Please ensure longitude is in range of -180 to 180 degrees.", "error");
                }
                System.out.println("Invalid longitude detected! Longitude out of range.");
                return false;
            }
            else{
                return true;
            }
        }
    }

    /**
     * A function that validates the latitude data from the activity data.
     * @param inLatitude A String that represents the latitude that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateLatitude(String inLatitude){
        if(!GeneralUtilities.isValidDouble(inLatitude)){
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure latitude is a numerical value and is in range of -90 to 90 degrees.", "error");
            }
            System.out.println("Invalid latitude detected!");
            return false;
        }
        else{
            double latitude = Double.parseDouble(inLatitude);
            if(!(latitude >= -90 && latitude <= 90)){
                System.out.println("Invalid latitude detected! Latitude out of range.");
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Please ensure latitude is in range of -90 to 90 degrees.", "error");
                }
                return false;
            }
            else{
                return true;
            }
        }
    }

    /**
     * A function that validates the heart rate data from the activity data.
     * @param inHeartRate A String that represents the heart rate that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateHeartRate(String inHeartRate){
        if(!GeneralUtilities.isValidInt(inHeartRate)){
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure heart rate is a numerical value and is in range of 0 to 400 bpm.", "error");
            }
            System.out.println("Invalid heart rate detected!");
            return false;
        }
        else{
            int heartRate = Integer.parseInt(inHeartRate);
            if(!(heartRate >= 0 && heartRate <= 400)){
                System.out.println("Invalid heart rate detected! Heart rate out of range.");
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Please ensure heart rate is in range of 0 to 400 bpm.", "error");
                }
                return false;
            }
            else{
                return true;
            }
        }
    }

    /**
     * A function that validates the elevation data from the activity data.
     * @param inElevation A String that represents the elevation that is being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateElevation(String inElevation){
        if(!GeneralUtilities.isValidDouble(inElevation)){
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure elevation is a numerical value and is in range of -420 to 9000 m.", "error");
            }
            System.out.println("Invalid elevation detected!");
            return false;
        }
        else{
            double elevation = Double.parseDouble(inElevation);
            if(!(elevation >= -420 && elevation <= 9000)){
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Please ensure elevation is in range of -420 to 9000 m.", "error");
                }
                System.out.println("Invalid elevation detected! Elevation out of range.");
                return false;
            }
            else{
                return true;
            }
        }
    }

    /**
     * A function that validates the date data from the activity data.
     * @param date A String that represents the date being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateDate(String date){
        if(!GeneralUtilities.isValidDate(date)){
            System.out.println("Invalid date detected!");
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure date is of the form DD/MM/YY.", "error");
            }
            return false;
        }
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            LocalDate newDate = LocalDate.parse(date, formatter);
            if(newDate.isAfter(LocalDate.now())){
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Please ensure that date is not referencing a future date.", "error");
                }
                System.out.println("Future date detected!");
                return false;
            }
            else{
                return true;
            }
        }
    }

    /**
     * A function that validates the date with a certain format.
     * @param date A String parameter that represents the date being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateDateWithFormat(String date){
        if(!GeneralUtilities.isValidDateWithFormat(date)){
            System.out.println("Invalid date detected!");
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure date is of the form \"yyyy-MM-dd\".", "error");
            }
            return false;
        }
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
            LocalDate newDate = LocalDate.parse(date, formatter);
            if(newDate.isAfter(LocalDate.now())){
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Future date detected! Please ensure that date is not referencing a future date.", "error");
                }
                System.out.println("Future date detected!");
                return false;
            }
            else{
                return true;
            }
        }
    }

    /**
     * A function that validates time from the activity data.
     * @param time A String parameter that represents the time being validated.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateTime(String time){
        if(!GeneralUtilities.isValidTime(time)){
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure that time is of the form HH:mm:ss!", "error");
            }
            System.out.println("Invalid time detected!");
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * A function that validates the start and end date to ensure start date is before the end
     * date.
     * @param startDate A String parameter that represents the start date of the activity.
     * @param endDate A String parameter that represents the end date of the activity.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateStartEndDate(LocalDate startDate, LocalDate endDate){
        if(!(startDate.isBefore(endDate) || startDate.isEqual(endDate))){
            if(ApplicationManager.getCurrentUserID() != 0) {
                ApplicationManager.displayPopUp("Invalid Data", "Please ensure start date is before end date.", "error");
            }
            System.out.println("Invalid date detected!");
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * A function that ensures the start time is before the end time.
     * @param startTime A String that represents the start time.
     * @param endTime A String that represents the end time.
     * @return Returns true if data is valid and false if not.
     */
    public static boolean validateStartEndTime(String startTime, String endTime, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter strictTimeFormatter = DateTimeFormatter.ofPattern("H:mm:ss")
                .withResolverStyle(ResolverStyle.STRICT);
        LocalTime start = LocalTime.parse(startTime, strictTimeFormatter);
        LocalTime end = LocalTime.parse(endTime, strictTimeFormatter);
        if(startDate.isEqual(endDate)) {
            if (!(start.isBefore(end))) {
                if (ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Please ensure start time is before end time.", "error");
                }
                System.out.println("Invalid times. Make sure start time is before end time.");
                return false;
            } else {
                return true;
            }
        }
        else{
            return true;
        }
    }

    /**
     * Validates if the given record is within the start and end date times of another activity.
     * @param data A record in a csv file
     * @return True if the record is not a duplicate, false otherwise.
     * @throws SQLException
     */
    public static boolean validateNonDuplicateData(ArrayList<String[]> data, DatabaseManager databaseManager) throws SQLException {
        ArrayList<Activity> activities = databaseManager.getActivityManager().getActivities(ApplicationManager.getCurrentUserID());
        for (String[] line : data){
            if(!line[0].equalsIgnoreCase("#start")){
                for (Activity activity : activities){
                    LocalTime recordTime = LocalTime.parse(line[1]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate recordDate = LocalDate.parse(line[0], formatter);
                    if(recordTime.isAfter(activity.getStartTime()) && recordTime.isBefore(activity.getEndTime())
                            && (recordDate.isEqual(activity.getStartDate()) || recordDate.isEqual(activity.getEndDate()))){
                        if(ApplicationManager.getCurrentUserID() != 0) {
                            ApplicationManager.displayPopUp("Invalid Data", "Duplicate activity data detected.", "error");
                        }
                        System.out.println("Duplicate data detected!");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * A function that validates if the given activity data is a duplicate or not.
     * @param endTime A String that represents the end time being validated.
     * @param startDate A String that represents the start date being validated.
     * @param endDate A String that represents the end date being validated.
     * @param databaseManager An instance of the database manager.
     * @return Returns true if data is valid and false if not.
     * @throws SQLException
     */
    public static boolean validateNonDuplicateActivity(LocalTime endTime, LocalDate startDate, LocalDate endDate, DatabaseManager databaseManager) throws SQLException {
        ArrayList<Activity> activities = databaseManager.getActivityManager().getActivities(ApplicationManager.getCurrentUserID());
        for (Activity activity : activities){
            if((endTime.isAfter(activity.getStartTime()) && endTime.isBefore(activity.getEndTime()))
                    && (startDate.isEqual(activity.getStartDate()) || endDate.isEqual(activity.getEndDate()))){
                if(ApplicationManager.getCurrentUserID() != 0) {
                    ApplicationManager.displayPopUp("Invalid Data", "Duplicate activity data detected.", "error");
                }
                System.out.println("Duplicate data detected!");
                return false;
            }
        }
        return true;
    }
}
