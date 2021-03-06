package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.util.ArffLoader;
import weka.core.Instance;
import weka.core.Instances;


public class ArffLoaderTest {

    private final static int IP_SRC_IDX = 3;
    private final static int PORT_SRC_IDX = 4;
    private final static int IP_DEST_IDX = 6;
    private final static int PORT_DEST_IDX = 7;
    private final static int TOS_IDX = 9;
    private final static int TOS_DEST_IDX = 10;

    protected final static String NUMERIC_TO_NOMINAL_COLS = IP_SRC_IDX   + "," +
                                                            IP_DEST_IDX  + "," +
                                                            TOS_IDX      + "," +
                                                            TOS_DEST_IDX + ",";

    @Before
    public void beforeEachTest() {
        TestHelper.assertArff();
    }

    @Test
    public void testBasic() {
        ArffLoader loader = new ArffLoader(TestHelper.ARFF_FOLDER);
        {
            Instances training = loader.loadTraining();
            assertTrue(training.classIndex() == (training.numAttributes() - 1));
            assertTrue(training.numClasses() >= 0);
            assertTrue(training.attribute(IP_SRC_IDX).isNumeric());
            assertTrue(!training.attribute(PORT_SRC_IDX).isNominal());
            assertTrue(training.attribute(IP_DEST_IDX).isNumeric());
            assertTrue(!training.attribute(PORT_DEST_IDX).isNominal());
            assertTrue(training.attribute(TOS_IDX).isNumeric());
            assertTrue(training.attribute(TOS_DEST_IDX).isNumeric());
        }
        {
            Instances test = loader.loadTest();
            assertTrue(test.classIndex() == (test.numAttributes() - 1));
            assertTrue(test.numClasses() >= 0);
            assertTrue(test.attribute(IP_SRC_IDX).isNumeric());
            assertTrue(!test.attribute(PORT_SRC_IDX).isNominal());
            assertTrue(test.attribute(IP_DEST_IDX).isNumeric());
            assertTrue(!test.attribute(PORT_DEST_IDX).isNominal());
            assertTrue(test.attribute(TOS_IDX).isNumeric());
            assertTrue(test.attribute(TOS_DEST_IDX).isNumeric());
        }
    }

    @Test
    public void testCorrectSeperationIntoTrainingAndTest() {
        ArffLoader loader = new ArffLoader(TestHelper.ARFF_FOLDER);
        Instances training = loader.loadTraining();
        Instances test = loader.loadTest();

        for (Instance trainInstance : training) {
            for (Instance testInstance : test) {
                assertFalse(trainInstance.equals(testInstance));
            }
        }
    }

    @Test
    public void testNumericToNominalConversion() {
        ArffLoader loader = new ArffLoader(TestHelper.ARFF_FOLDER, NUMERIC_TO_NOMINAL_COLS);

        Instances trainingData = loader.loadTraining();
        assertTrue(trainingData.attribute(IP_SRC_IDX).isNominal());
        assertTrue(trainingData.attribute(PORT_SRC_IDX).isNumeric());
        assertTrue(trainingData.attribute(IP_DEST_IDX).isNominal());
        assertTrue(trainingData.attribute(PORT_DEST_IDX).isNumeric());
        assertTrue(trainingData.attribute(TOS_IDX).isNominal());
        assertTrue(trainingData.attribute(TOS_DEST_IDX).isNominal());

        Instances testData = loader.loadTest();
        assertTrue(testData.attribute(IP_SRC_IDX).isNominal());
        assertTrue(testData.attribute(PORT_SRC_IDX).isNumeric());
        assertTrue(testData.attribute(IP_DEST_IDX).isNominal());
        assertTrue(testData.attribute(PORT_DEST_IDX).isNumeric());
        assertTrue(testData.attribute(TOS_IDX).isNominal());
        assertTrue(testData.attribute(TOS_DEST_IDX).isNominal());
    }
}
