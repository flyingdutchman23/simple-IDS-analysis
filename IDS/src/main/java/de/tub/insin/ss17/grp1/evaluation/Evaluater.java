package de.tub.insin.ss17.grp1.evaluation;

import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.shared.RuntimeWekaException;
import de.tub.insin.ss17.grp1.shared.SharedConstants;
import de.tub.insin.ss17.grp1.shared.SharedUtil;
import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.ResultPersistence;


/**
 * Class for the evaluation of a trained classifier model. It measures the
 * performance of the selected classifier. The evaluation result are stored in
 * files next to the arff data.
 *
 * This class uses the test data of the arff folder for the evaluation.
 *
 * @author Joris Clement
 *
 */
public class Evaluater {

    private final static Logger log = LoggerFactory.getLogger(Evaluater.class);

    private final Classifier classifier;

    private final Evaluation evaluation;

    private ClassIndexs classIndexs;

    /**
     * Initializes the evaluation with a trained classifier and the training
     * data. The training data is just there to check the format.
     *
     * @param classifier
     *            the trained classifier.
     * @param trainingData
     *            the training data
     */
    public Evaluater(Classifier classifier, Instances trainingData) {
        this.classifier = classifier;
        try {
            this.evaluation = new Evaluation(trainingData);
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed to start evaluation." +
                                           "Probably wrong classifier or training data." +
                                           e.getMessage());
        }
        this.classIndexs = new ClassIndexs(trainingData);
    }

    /**
     * Removes the instances, which are labeled Background, because they are not
     * tested.
     *
     * @param testData
     *            the test data containing the background data
     */
    public void removeBackground(Instances testData) {
        Iterator<Instance> it = testData.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            int classValue = SharedUtil.checkedConvert(instance.classValue());
            if (classValue == this.classIndexs.BACKGROUND) {
                it.remove();
            }
        }
    }

    /**
     * Performs the evaluation on given test data. The results of the evaluation
     * are stored in files.
     *
     * @param testData
     *            the test data
     * @param resultPersistence
     *            the class, which manages the storing of the results
     */
    public void evaluate(Instances testData, ResultPersistence resultPersistence) {
        log.debug("start: evaluate");
        int sizeWithBackground = testData.size();
        this.removeBackground(testData);

        long startTime = System.nanoTime();
        try {
            evaluation.evaluateModel(classifier, testData);
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed to do evaluation."
                                         + e.getMessage());
        }
        long duration = System.nanoTime() - startTime;

        Metrics metrics = new Metrics(evaluation.confusionMatrix(), this.classIndexs);

        resultPersistence
                .saveSummary(this.generateTextResult(metrics, sizeWithBackground, duration));
        Visualizer visualizer = new Visualizer(resultPersistence);
        visualizer.plotAll(metrics);
        log.debug("finished: evaluate");
    }

    private String generateTextResult(Metrics metrics,
                                      int sizeWithBackground,
                                      long duration) {
        StringBuilder result = new StringBuilder();

        result.append("Information about evaluated classifier: " + System.lineSeparator());
        result.append(this.classifier.toString());
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());

        result.append("Test Set size with background: " + sizeWithBackground);
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());
        result.append("Results: " + System.lineSeparator());
        result.append(System.lineSeparator());
        result.append(evaluation.toSummaryString());
        result.append(System.lineSeparator());
        result.append("TP Count: " + metrics.tps());
        result.append(System.lineSeparator());
        result.append("FP Count: " + metrics.fps());
        result.append(System.lineSeparator());
        result.append("TN Count: " + metrics.tns());
        result.append(System.lineSeparator());
        result.append("FN Count: " + metrics.fns());
        result.append(System.lineSeparator());
        result.append("TP Ratio, Recall: " + metrics.truePositiveRate());
        result.append(System.lineSeparator());
        result.append("FP Ratio: " + metrics.falsePositiveRate());
        result.append(System.lineSeparator());
        result.append("TN Ratio, Specificity: " + metrics.trueNegativeRate());
        result.append(System.lineSeparator());
        result.append("FN Ratio: " + metrics.falseNegativeRate());
        result.append(System.lineSeparator());

        result.append(this.printConfusionMatrix(this.evaluation.confusionMatrix()));
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());

        final double seconds = ((double) duration / 1000000000);
        result.append("Time duration of the weka test set evaluation of this classifier: ");
        result.append(System.lineSeparator());
        result.append(new DecimalFormat("#.##########").format(seconds) + " Seconds");

        return result.toString();
    }

    private String printConfusionMatrix(double[][] confusionMatrix) {
        StringBuilder confDesc = new StringBuilder();
        confDesc.append(System.lineSeparator());
        confDesc.append("Confusion Matrix: ");
        confDesc.append(System.lineSeparator());
        confDesc.append("Row, Column for " + SharedConstants.BACKGROUND + "(BACKGROUND) is: "
                + this.classIndexs.BACKGROUND);
        confDesc.append(System.lineSeparator());
        confDesc.append("Row, Column for " + SharedConstants.NORMAL + "(NORMAL) is: "
                + this.classIndexs.NORMAL);
        confDesc.append(System.lineSeparator());
        confDesc.append("Row, Column for " + SharedConstants.BOTNET + "(BOTNET) is: "
                + this.classIndexs.BOTNET);
        confDesc.append(System.lineSeparator());
        for (int i = 0; i < confusionMatrix.length; i++) {
            confDesc.append(Arrays.toString(confusionMatrix[i]));
            confDesc.append(System.lineSeparator());
        }
        return confDesc.toString();
    }
}
