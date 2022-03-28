public class Perceptron {

    private int inDimensions;
    private double[] weights;
    private double learningRate;
    private double threshold;
    private int output;
    private int expectedVal;

    private final String firstIrisType = "Iris-setosa"; //"Iris-virginica";
    private final String secondIrisType = "Iris-versicolor";
    private final boolean displayInfoOn;

    public Perceptron(int inDimensions, double learningRate, boolean displayInfoOn) {

        this.inDimensions = inDimensions;
        this.learningRate = learningRate;
        this.displayInfoOn = displayInfoOn;

        weights = new double[inDimensions];
        //RANDOMIZE THE PERCEPTRON
        for(int i = 0; i < inDimensions; i++)
            weights[i] = Math.random();
        threshold = Math.random();

    }

    public String guessTheIris(Iris iris) {

        double[] inData = iris.getParams();
        double scalarVectorValues = calculateScalarVectorValues(inData, weights);
        String irisType;

        if(displayInfoOn)
            System.out.println("Sum = " + scalarVectorValues +
                               ",\nThreshold = " + threshold);

        if(checkIfOverTheThreshold(scalarVectorValues, threshold))
            irisType = firstIrisType;
        else
            irisType = secondIrisType;

        return irisType;
    }

    //LEARN ON THE IRIS
    public void learnOnNewData(Iris iris) {

        double[] inData = iris.getParams();
        String irisType = iris.getType();

        double vectorScalarValue = calculateScalarVectorValues(inData, weights);

        //Iris-setosa -> 1 / Iris-versicolor -> 0
        this.expectedVal = 0;

        if(irisType.equals(firstIrisType))
            this.expectedVal = 1;

        if(checkIfOverTheThreshold(vectorScalarValue, threshold))
            this.output = 1;
        else
            this.output = 0;

        if(displayInfoOn) {

            System.out.println("Sum " + vectorScalarValue);
            System.out.println("Threshold " + threshold);

            if (expectedVal - output != 0) {

                System.out.println("Expected " + expectedVal);
                System.out.println("Answered " + output);
                System.out.println("Answer was " + irisType);
                System.out.println("Now Learning");
                learn(inData);

            }
            else {

                System.out.println("Expected " + expectedVal);
                System.out.println("Answered " + output);
                System.out.println("Answer was " + irisType);
                System.out.println("No Need To Learn");

            }

        }
        else
            if (expectedVal - output != 0)
                learn(inData);

    }

    public void learn(double[] inData) {

        double newThreshold = -1;
        double mulFactor = (expectedVal - output) * this.learningRate;

        for(int i = 0; i < inDimensions; i++)
            inData[i] *= mulFactor;
        newThreshold *= mulFactor;

        for(int i = 0; i < inDimensions; i++)
            weights[i] += inData[i];
        this.threshold += newThreshold;

    }

    public double calculateScalarVectorValues(double[] vec1, double[] vec2) {

        int vecSize = vec1.length;
        double sum = 0;

        for(int i = 0; i < vecSize; i++)
            sum += (vec1[i] * vec2[i]);

        return sum;

    }

    public boolean checkIfOverTheThreshold(double value, double threshold) {

        return value > threshold;

    }

    public void writeWeights() {
        System.out.println("Weights: ");
        for(double d : weights)
            System.out.println("    " + d);
    }

    public void writeThreshold() {
        System.out.println("Threshold:\n    " + threshold + "\n");
    }

}
