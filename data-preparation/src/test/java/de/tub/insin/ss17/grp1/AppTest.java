package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class AppTest {

    private final static String destFolder = "../IDS/src/main/resources/test/";

    private final static String ctuFolder = "src/main/resources/TestCTU13/";

    private final static String scenarios = "6,11,12";

    @Before
    public void beforeEachTest() {
        FileUtils.deleteQuietly(new File(destFolder));
    }

    @After
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }

    // TODO split data correctly
    @Ignore
    @Test
    public void testSplittedScenarios() {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder};
        App.main(argv);
    }

    @Test
    public void testSeperateTestScenario() {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder,
                         "-t"};
        App.main(argv);
    }

    //TODO fix test
    @Ignore
    @Test(expected = Exception.class)
    public void testNoArgs() throws Exception {
        String[] argv = {};
        App.main(argv);
    }
}
