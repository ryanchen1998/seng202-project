package seng202.team6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng202.team6.models.MatesAI;

/**
 * Unit test for simple App.
 */
public class BuddyAITest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BuddyAITest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BuddyAITest.class );
    }


    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        String userName1 = "TestUser";
        MatesAI bud1 = new MatesAI();
        String userGreeting = bud1.getGreeting(userName1);
        assertEquals(userGreeting, "Hello TestUser");
    }
}
