package seng202.team6.datahandling;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils.Null;

import seng202.team6.controller.ApplicationManager;
import seng202.team6.models.Activity;
import seng202.team6.models.ActivityDataPoint;
import seng202.team6.models.User;

public class DatabaseManager implements DataLoader {
    private Connection con;
    private boolean hasData = false;

    public DatabaseManager() throws ClassNotFoundException, SQLException {
       getConnection();
    }

    public ResultSet displayUsers() throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM user");
        return res;
    }

    /**
     * Given a username, finds the details of that user in the data base and 
     * returns a User object with these details.
     * @param aUsername The username used to lookup the user in the database.
     * @return A user object with the details of the user with the given username.
     * @throws SQLException Occurs when there is an error in the query process.
     */
    public User getUser(String aUsername) throws SQLException {
        // Checks the connection to the database.
        if(con == null) {
            getConnection();
        }

        // Trys to query the database for a user.
        Statement statement = con.createStatement();
        String sqlString = "SELECT * FROM user WHERE username LIKE '" + aUsername + "'";
        ResultSet userData = statement.executeQuery("SELECT * FROM user WHERE username LIKE '" + aUsername + "'");

        // Gets data from the database.
        int id = userData.getInt("userid");
        String firstName = userData.getString("firstname");
        String lastName = userData.getString("lastname");
        String dobString = userData.getString("dateofbirth");
        String gender = userData.getString("gender");
        Double height = userData.getDouble("height");
        Double weight = userData.getDouble("weight");
        Double stridelength = userData.getDouble("stridelength");
        LocalDate dob = LocalDate.parse(dobString);

        // Creates a User model using database data.
        User user = new User(firstName, lastName, dob, gender, height, weight, stridelength, aUsername, id);
        return user; 
    }

    public ArrayList<ActivityDataPoint> getDataPoints(Activity activity) throws SQLException {
        if(con == null) {
            getConnection();
        }
        int activityid = activity.getActivityid();
        Statement state = con.createStatement();
        ArrayList<ActivityDataPoint> dataPoints = new ArrayList<>();
        ResultSet res = state.executeQuery("SELECT * FROM record WHERE activityid = " + activityid);
        while(res.next()){
            String datetime = res.getString("datetime");
            String[] parts = datetime.split("T");
            String recordTime = parts[1];
            LocalTime localStartTime = LocalTime.parse(recordTime);
            int heartRate = res.getInt("heartrate");
            Double latitude = res.getDouble("latitude");
            Double longitude = res.getDouble("longitude");
            Double elevation = res.getDouble("elevation");
            ActivityDataPoint dataPoint = new ActivityDataPoint(localStartTime, heartRate, latitude, longitude, elevation);
            dataPoints.add(dataPoint);
        }
        return dataPoints;
    }

    public ResultSet displayActivities() throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM activity");
        return res;
    }

    public ResultSet displayRecords() throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM record");
        return res;
    }

    public ArrayList<String> getUsernames() throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }
        
        ArrayList<String> users = new ArrayList<String>();
        System.out.println("Here");
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM user");
        while(res.next()){
            users.add(res.getString("username"));
        }
        return users;
    }

    private void getConnection() {
        
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Data.db");
            initialiseDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ApplicationManager.displayPopUp("Database Error", "There is a problem with the database. It may not exist!", "error");
        } catch (SQLException e) {
            e.printStackTrace();
            ApplicationManager.displayPopUp("Database Error", "Unfortunately, there is a problem the database connection.", "error");
        }
    }

    private void initialiseDatabase() throws SQLException {
        if(!hasData) {
            hasData = true;
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            if(!res.next()) {
                System.out.println("Building the User table...");
                //Build the tables
                //Create users table
                Statement userTableStatement = con.createStatement();
                String userTablesql = "CREATE TABLE IF NOT EXISTS user(userid INTEGER PRIMARY KEY,"
                        + "username text UNIQUE NOT NULL,"
                        + "dateofbirth text,"
                        + "firstname text,"
                        + "lastname text,"
                        + "gender text,"
                        + "height REAL,"
                        + "weight REAL,"
                        + "stridelength REAL);";
                userTableStatement.execute(userTablesql);
                //Create activities table
                Statement activityTableStatement = con.createStatement();
                String activityTablesql = "CREATE TABLE IF NOT EXISTS activity(activityid INTEGER PRIMARY KEY," + "userid INTEGER,"
                        + "description text,"
                        + "start text,"
                        + "end text,"
                        + "distance REAL"
                        + "workout text,"
                        + "FOREIGN KEY(userid) REFERENCES user(userid));";
                activityTableStatement.execute(activityTablesql);
                //Create workouts table

                //Create records table
                Statement recordTableStatement = con.createStatement();
                String recordTablesql = "CREATE TABLE IF NOT EXISTS record(activityid INTEGER,"
                        + "datetime text,"
                        + "heartrate INTEGER,"
                        + "latitude REAL,"
                        + "longitude REAL,"
                        + "elevation REAL,"
                        + "FOREIGN KEY(activityid) REFERENCES activity(activityid));";
                recordTableStatement.execute(recordTablesql);
                //inserting some sample data
                String sqlprep1 = "INSERT INTO user(username, dateofbirth, firstname, lastname, gender, height, weight, stridelength) VALUES(?,?,?,?,?,?,?,?)";
                PreparedStatement prep = con.prepareStatement(sqlprep1);
                prep.setString(1, "Billythekidzz");
                prep.setString(2, "1998-08-23");
                prep.setString(3, "Gavin");
                prep.setString(4, "Ong");
                prep.setString(5, "male");
                prep.setDouble(6, 170.0);
                prep.setDouble(7, 65.0);
                prep.setDouble(8, 2.0);
                prep.execute();
                System.out.println("User tables built");
            }
        }
    }

    public Connection getCon(){
        return con;
    }

    public void addUser(String username, String dob, String firstname, String lastname, String gender, double height, double weight, double stridelength) throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }
        String sqlprep1 = "INSERT INTO user(username,dateofbirth,firstname,lastname,gender,height,weight,stridelength) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement prep = con.prepareStatement(sqlprep1);
        prep.setString(1, username);
        prep.setString(2, dob);
        prep.setString(3, firstname);
        prep.setString(4, lastname);
        prep.setString(5, gender);
        prep.setDouble(6, height);
        prep.setDouble(7, weight);
        prep.setDouble(8, stridelength);
        prep.execute();
    }

    public void addActivity(int userid, String description, String start, String end, String workout, double distance) throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }
        String sqlprep1 = "INSERT INTO activity(userid,description,start,end,distance,workout) VALUES(?,?,?,?,?,?)";
        PreparedStatement prep = con.prepareStatement(sqlprep1);
        prep.setInt(1, userid);
        prep.setString(2, description);
        prep.setString(3, start);
        prep.setString(4, end);
        prep.setDouble(5, distance);
        prep.setString(6, workout);
        prep.execute();
    }

    /**
     *Takes a userid and returns a list of activities associated with the user
     * @param userid The user id used to look up the user in the database.
     * @return An array list of activities associated with the user id.
     * @throws SQLException
     */
    public ArrayList<Activity> getActivities(int userid) throws SQLException {
        if(con == null) {
            getConnection();
        }
        ArrayList<Activity> activities = new ArrayList<>();
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM activity WHERE userid = " + userid);
        while(res.next()){
            String activityDescription = res.getString("description");
            String activityStart = res.getString("start");
            String[] startParts = activityStart.split("T");
            String activityStartDate = startParts[0];
            String activityStartTime = startParts[1];
            String activityEnd = res.getString("end");
            System.out.println(activityEnd);
            String[] endParts = activityEnd.split("T");
            String activityEndDate = endParts[0];
            String activityEndTime = endParts[1];
            //String activityWorkout = res.getString("workout");
            String activityWorkout = "testworkout";
            int activityid = res.getInt("activityid");
            LocalDate localStartDate = LocalDate.parse(activityStartDate);
            LocalDate localEndDate = LocalDate.parse(activityEndDate);
            LocalTime localStartTime = LocalTime.parse(activityStartTime);
            LocalTime localEndTime = LocalTime.parse(activityEndTime);
            Activity activity = new Activity(activityid, activityWorkout, activityDescription, localStartDate, localEndDate, localStartTime, localEndTime);
            ArrayList<ActivityDataPoint> dataPoints = this.getDataPoints(activity);
            for (ActivityDataPoint dataPoint : dataPoints) {
                activity.addActivityData(dataPoint);
            }
            activity.updateMaxHeartRate();
            activity.updateMinHeartRate();
            activities.add(activity);
        }
        return activities;
    }
}
