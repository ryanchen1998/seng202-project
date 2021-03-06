package seng202.team6.models;

import junit.framework.TestCase;

import java.time.LocalDate;

public class UserTest extends TestCase {

    LocalDate dob = LocalDate.of(2011, 5, 8);
    LocalDate dob2 = LocalDate.of(2001, 1, 9);
    LocalDate dob3 = LocalDate.of(2001, 12, 9);
    LocalDate now = LocalDate.now();
    private User user = new User("Bob", "Wortsy", dob, "Male", 166.5, 55.0,  "bob101", 56778, 50, 100);

    public void testSetName() {
        user.setName("Mike", "Wobs");
        assertEquals("Mike", user.getFirstName());
        assertEquals("Wobs", user.getLastName());
    }

    public void testGetFirstName() {
        assertEquals("Bob", user.getFirstName());
    }

    public void testGetLastName() {
        assertEquals("Wortsy", user.getLastName());
    }

    public void testSetAgeAndDOB() {
        user.setAgeAndDOB(dob2);
        assertEquals(17, user.getAge());
        assertEquals(dob2, user.getDOB());

        user.setAgeAndDOB(dob3);
        assertEquals(16, user.getAge());
        assertEquals(dob3, user.getDOB());
    }

    public void testGetAge() {
        assertEquals(7, user.getAge());
    }

    public void testGetDOB() {
        assertEquals(dob, user.getDOB());
    }

    public void testSetGender() {
        user.setGender("Female");
        assertEquals("Female", user.getGender());

        user.setGender("Male");
        assertEquals("Male", user.getGender());
    }

    public void testGetGender() {
        assertEquals("Male", user.getGender());
    }

    public void testSetHeight() {
        user.setHeight(170.0);
        assertEquals(170.0, user.getHeight(), 0.1);
    }

    public void testGetHeight() {
        assertEquals(166.5, user.getHeight(), 0.1);
    }

    public void testSetWeight() {
        user.setWeight(60.0);
        assertEquals(60.0, user.getWeight(), 0.1);
    }

    public void testGetWeight() {
        assertEquals(55.0, user.getWeight(), 0.1);
    }

    public void testSetStrideLength() {
        user.setWalkingStrideLength(3.0);
        assertEquals(3.0, user.getWalkingStrideLength(), 0.1);
    }

    public void testGetStrideLength() {
        assertEquals(2.2, user.getWalkingStrideLength(), 0.1);
    }

    public void testSetUsername() {
        user.setUsername("Doublelift");
        assertEquals("Doublelift", user.getUsername());
    }

    public void testGetUsername() {
        assertEquals("bob101", user.getUsername());
    }

    public void testGetFullName() {
        assertEquals("Bob Wortsy", user.getFullName());
    }

    public void testSetUserID() {
        user.setUserID(89001);
        assertEquals(89001, user.getUserID());
    }

    public void testGetUserID() {
        assertEquals(56778, user.getUserID());
    }

    public void testCalculateBMI() {
        User testUser;

        LocalDate date = LocalDate.of(1998, 5, 18);
        testUser = new User("Joe", "Bloggs",date,"Male", 170.0, 64, "JBloggs", 12345, 50, 100);
        assertEquals(testUser.calculateBMI(), 22.1, 0.05);   // Average BMI

        testUser.setWeight(2);   //Minimum weight
        testUser.setHeight(55);  //Minimum height
        assertEquals(testUser.calculateBMI(), 6.6, 0.05);

        testUser.setWeight(600);  //Maximum weight
        testUser.setHeight(280);   //Maximum Height
        assertEquals(testUser.calculateBMI(), 76.5, 0.05);

        // Smallest possible BMI (within the app)
        testUser.setWeight(2);
        testUser.setHeight(280);
        assertEquals(testUser.calculateBMI(), 0.3, 0.05);

        // Largest possible BMI (within the app)
        testUser.setWeight(600);
        testUser.setHeight(55);
        assertEquals(testUser.calculateBMI(), 1983.5, 0.05);

    }

    public void testAnalyseBMI() {
        User testUser;

        LocalDate date = LocalDate.of(1998, 5, 18);
        testUser = new User("Joe", "Bloggs",date,"Male", 170.0, 64, "JBloggs", 12345, 50, 100);

        assertEquals("Healthy Weight", testUser.analyseBMI()); // Middle healthy Weight user - BMI of 22.1

        testUser.setWeight(53.5);                                 // Minimum Healthy weight user - BMI of 18.5
        assertEquals( "Healthy Weight", testUser.analyseBMI());

        testUser.setWeight(71.8);                                 // Maximum Healthy weight user - BMI of 24.8
        assertEquals( "Healthy Weight", testUser.analyseBMI());



        testUser.setWeight(30);                                 // Very Under weight user - BMI of 10.4
        assertEquals( "UnderWeight",testUser.analyseBMI() );

        testUser.setWeight(40);                              // Middle Under weight user - BMI of 13.8
        assertEquals("UnderWeight", testUser.analyseBMI());

        testUser.setWeight(53.2);                              // Maximum Under weight user - BMI of 18.4
        assertEquals( "UnderWeight", testUser.analyseBMI());



        testUser.setWeight(72);                                 // Minimum Over Weight user - BMI of 24.9
        assertEquals("OverWeight", testUser.analyseBMI());

        testUser.setWeight(79.5);                                 // Middle Over Weight user - BMI of 27.5
        assertEquals( "OverWeight", testUser.analyseBMI());

        testUser.setWeight(86);                                 // Maximum Over Weight user - BMI of 29.8
        assertEquals( "OverWeight", testUser.analyseBMI());




        testUser.setWeight(86.5);                                 // Minimum Obese user - BMI of 29.9
        assertEquals("Obese",testUser.analyseBMI());

        testUser.setWeight(94);                                 // Middle Obese user - BMI of 32.2
        assertEquals("Obese", testUser.analyseBMI());

        testUser.setWeight(94);                                 // Very Obese user - BMI of 34.9
        assertEquals("Obese", testUser.analyseBMI());
    }
}