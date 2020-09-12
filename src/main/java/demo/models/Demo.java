package demo.models;

import core.configurations.AlgorithmSetting;
import core.configurations.Solver;
import core.configurations.TSPConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        // create a list of destinations
        List<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Yerevan"));
        destinations.add(new Destination("Rotterdam"));
        destinations.add(new Destination("Prague"));
        destinations.add(new Destination("Berlin"));
        destinations.add(new Destination("Tokyo"));

        // create a TSP
        SampleTsp tsp = new SampleTsp(destinations);

        // create a distance matrix
        Double[][] distanceArrayMatrix = new Double[][]{
                {0.0, 4478.5, 3593.3, 3464.8, 7930.0},
                {4477.8, 0.0, 906.8, 691.5, 9339.0},
                {3592.0, 906.7, 0.0, 349.1, 9064.0},
                {3941.0, 691.6, 349.6, 0.0, 8915.0},
                {7922.0, 9343.0, 9064.0, 8915.0, 0.0}
        };

        List<List<Double>> distanceMatrix = new ArrayList<>();

        for (Double[] arrayMatrix : distanceArrayMatrix) {
            distanceMatrix.add(Arrays.asList(arrayMatrix));
        }

        // create a configuration
        TSPConfiguration tspConfiguration = new TSPConfiguration.Builder()
                .setTsp(tsp)
                .setDistanceMatrix(distanceMatrix)
                .setSolutionClass(Solution.class)
                .build();

        AlgorithmSetting algorithmSetting = new AlgorithmSetting.Builder().setMaxIterations(2000).build();

        Solver solver = new Solver(tspConfiguration, algorithmSetting);
        Solution solution = solver.solve();

        List<Destination> orderedDestinations = solution.destinations;
        for (Destination destination : orderedDestinations) {
            System.out.println("Destination : " + destination.getName());
        }

    }
}
