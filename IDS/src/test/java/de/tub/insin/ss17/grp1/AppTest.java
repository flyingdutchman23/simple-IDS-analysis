package de.tub.insin.ss17.grp1;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Unit test for IDS.
 */
public class AppTest
{

    protected final static String ARFF_FOLDER = "./src/main/resources/testArff/";

    protected final static String BASIC_NN_PARAMS = "k=5,dist=20.0,distweight=none";

    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @After
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }

    // TODO add checks
    @Test
    public void testTrainCommand() {
        String[] argv = {"train",
                         "-c", "lnns",
                         "-f", ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        App.main(argv);
    }

    // TODO add checks
    @Ignore
    @Test
    public void testTestCommand() {
        String[] argv = {"train,test",
                         "-c", "lnns",
                         "-f", ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        App.main(argv);
    }

    // TODO add test for invalid args
    @Ignore
    @Test
    public void testWrongInput() {
    }

}
