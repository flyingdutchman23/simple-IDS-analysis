package de.tub.insin.ss17.grp1.training.classifier;

import java.util.List;

import de.tub.insin.ss17.grp1.training.MlAlgo;
import de.tub.insin.ss17.grp1.util.ClassIndexs;
import weka.classifiers.Classifier;
import weka.core.Instances;


/**
 * Abstract wrapper class for nearest neighbor classifiers.
 *
 * @author Joris Clement
 *
 */
abstract public class NNClassifier implements MlAlgo {

    final CTUIBk nnClassifier;

    public NNClassifier(List<String> params) {
        this.nnClassifier = new CTUIBk();
        this.setParams(params);
    }

    private void setParams(List<String> params) {
        try {
            this.nnClassifier.setOptions(params.toArray(new String[0]));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Parameters specified for nn classifer were invalid." +
                    " Weka: " + e.getMessage());
        }
    }

    @Override
    public void train(Instances trainingData) {
        this.nnClassifier.setClassIndexs(new ClassIndexs(trainingData));
        ClassifierHelper.catchedBuildClassifier(this.nnClassifier, trainingData);
    }

    @Override
    public Classifier getClassifier() {
        return this.nnClassifier;
    }

    @Override
    public String getFilename() {
        StringBuilder filename = new StringBuilder("nearestNeighbour");

        filename.append("_k=");
        filename.append(this.nnClassifier.getKNN());

        filename.append("_n=");
        filename.append(this.nnClassifier.getNumTraining());

        filename.append("_distweight=");
        filename.append(this.nnClassifier.getDistanceWeighting().getSelectedTag().getReadable());

        return filename.toString();
    }

}
