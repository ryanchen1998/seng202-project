package seng202.team6.utilities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * GeneralUtilities handles all the general validation such as date, time, int, double and duplicates
 */
public class GeneralUtilities {

    /**
     * Checks if given string can be parsed into a LocalDate.
     * @param inDate string to be parsed into a LocalDate.
     * @return false if given string cannot be parsed into a LocalDate, true if it can.
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate);
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given String can be parse into a Local Date.
     * @param inDate string to be parsed into a LocalDate.
     * @return false if given string cannot be parsed into a LocalDate, true if it can.
     */
    public static boolean isValidDateWithFormat(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate);
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if given string can be parsed into a LocalTime.
     * @param inTime string to be parsed into a LocalTime.
     * @return false if given string cannot be parsed into a LocalTime, true if it can.
     */
    public static boolean isValidTime(String inTime) {
        DateTimeFormatter strictTimeFormatter = DateTimeFormatter.ofPattern("H:mm:ss")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalTime.parse(inTime, strictTimeFormatter);
        } catch (DateTimeParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if given string can be parsed into an int.
     * @param inInt string to be parsed into an int.
     * @return false if given string cannot be parsed into an int, true if it can.
     */
    public static boolean isValidInt(String inInt) {
        try {
            Integer.parseInt(inInt);
        } catch (NumberFormatException pe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if given string can be parsed into a double.
     * @param inDouble string to be parsed into a double.
     * @return false if given string cannot be parsed into a double, true if it can.
     */
    public static boolean isValidDouble(String inDouble) {
        try {
            Double.parseDouble(inDouble);
        } catch (NumberFormatException pe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if there are duplicates within a list. Momentarily not used because sample data fails with this.
     * @param listContainingDuplicates An array list of strings
     * @return false if two records with the same date and time are found, true otherwise.
     */
    public static boolean hasNoDuplicates(ArrayList<String> listContainingDuplicates) {
        final Set<String> set1 = new HashSet<>();
        for (String dateTime : listContainingDuplicates) {
            if (!set1.add(dateTime)) {
                return false;
            }
        }
        return true;
    }
}
