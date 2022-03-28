import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        final boolean displayInfoOn = true;

        double learningRate = Double.parseDouble(args[0]);
        String trainFileName = args[1];
        String testFileName = args[2];

        int howManyLearningCycles = 3;
        int dimensions = 1;
        boolean dimensionsKnown = false;

        double testIrisCount = 0f;
        double correctGuessCount = 0f;
        double accuracy;

        try{

            BufferedReader trainFileReader = new BufferedReader(new FileReader(trainFileName+".csv"));
            BufferedReader testFileReader = new BufferedReader(new FileReader(testFileName+".csv"));

            List<Iris> trainingIrisList = new ArrayList<>();
            List<Iris> testIrisList = new ArrayList<>();

            Map<String, Double> correctAnswersPerType = new TreeMap<>();
            double totalAnswersPerType;

            String line;
            //Wczytywanie TrainingSetu
            while((line = trainFileReader.readLine()) != null) {

                if (!dimensionsKnown) {

                    dimensions = findDim(line, ',');
                    dimensionsKnown = true;

                }

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);
                trainingIrisList.add(iris);

            }
            //Wczytywanie TestSetu
            while((line = testFileReader.readLine()) != null) {

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);
                testIrisList.add(iris);

                String mapKey = iris.getType();
                if (!correctAnswersPerType.containsKey(mapKey))
                    correctAnswersPerType.put(mapKey, 0.0);

                testIrisCount++;

            }

            totalAnswersPerType = testIrisCount / 2;

            trainFileReader.close();
            testFileReader.close();

            //Tworzenie perceptronu
            Perceptron perceptron = new Perceptron(dimensions, learningRate, displayInfoOn);

            System.out.println("INITIAL VALUES: ");
            perceptron.writeWeights();
            perceptron.writeThreshold();

            Collections.shuffle(trainingIrisList);
            Collections.shuffle(testIrisList);

            System.out.println("NOW LEARNING\n");

            //Uczenie perceptronu
            for(int i = 1; i <= howManyLearningCycles; i++) {

                for (Iris trainingIris : trainingIrisList) {
                    perceptron.learnOnNewData(trainingIris);

                    if(displayInfoOn) {
                        System.out.println("Learning cycle number: " + i);
                        perceptron.writeWeights();
                        perceptron.writeThreshold();
                    }

                }

            }

            System.out.println("-------------------");
            System.out.println("-------------------");
            System.out.println("-------------------");
            System.out.println();

            //Testowanie
            for(Iris testIris : testIrisList) {

                String guess = perceptron.guessTheIris(testIris);

                System.out.println("Prediction for this Iris: " + guess);
                System.out.println("The Correct Answer Was: " + testIris.getType());

                if(guess.equals(testIris.getType())) {

                    System.out.println("Prediction Was Correct.\n");
                    correctGuessCount++;

                    //Add to map
                    String mapKey = testIris.getType();
                    for (Map.Entry<String, Double> e : correctAnswersPerType.entrySet())
                        if (e.getKey().equals(mapKey))
                            e.setValue(e.getValue() + 1.0);



                }
                else
                    System.out.println("Prediction Was Incorrect.\n");

            }

            //Wyniki
            accuracy = correctGuessCount / testIrisCount * 100;
            System.out.println("Overall Accuracy = " + accuracy + "% over " + testIrisCount + " guesses.");
            for(Map.Entry<String, Double> e : correctAnswersPerType.entrySet())
            {
                double typeAccuracy = e.getValue() / totalAnswersPerType;
                typeAccuracy *= 100;
                System.out.println("Accuracy for " + e.getKey() + " = " + typeAccuracy + "% over " +
                                   totalAnswersPerType + " guesses.");

            }

            System.out.println();
            System.out.println("-------------------");
            System.out.println("-------------------");
            System.out.println("-------------------");
            System.out.println();

            //User Input
            while(true) {

                System.out.println("INPUT A NEW IRIS:");
                Scanner userInputScanner = new Scanner(System.in);
                line = userInputScanner.nextLine();

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);

                String guess = perceptron.guessTheIris(iris);

                System.out.println("Prediction for this Iris: " + guess);
                System.out.println("The Correct Answer Was: " + iris.getType());

                if(guess.equals(iris.getType()))
                    System.out.println("Prediction Was Correct.");
                else
                    System.out.println("Prediction Was Incorrect.");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int findDim(String s, char c) {

        int x = 0;

        for(int i=0; i<s.length(); i++)
            if(s.charAt(i) == c)
                x++;

        return x;

    }

}
