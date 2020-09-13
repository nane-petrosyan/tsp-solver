package demo.models;

import core.util.AlgorithmSetting;
import core.util.Solver;
import core.configurations.TSPConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        // create a list of destinations
        List<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Yerevan", Arrays.asList(200.0,400.0)));
        destinations.add(new Destination("Rotterdam",Arrays.asList(200.0,400.0)));
        destinations.add(new Destination("Prague",Arrays.asList(200.0,400.0)));
        destinations.add(new Destination("Berlin",Arrays.asList(200.0,400.0)));
        destinations.add(new Destination("Tokyo",Arrays.asList(200.0,400.0)));

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

        Double[][] durationArrayMatrix = new Double[][]{
                {0.0, 447.5, 359.3, 346.8, 793.0},
                {447.8, 0.0, 90.8, 69.5, 933.0},
                {359.0, 90.7, 0.0, 349.1, 906.0},
                {394.0, 69.6, 34.6, 0.0, 891.0},
                {792.0, 934.0, 906.0, 891.0, 0.0}
        };

        List<List<Double>> distanceMatrix = new ArrayList<>();
        List<List<Double>> durationMatrix = new ArrayList<>();

        for (Double[] arrayMatrix : distanceArrayMatrix) {
            distanceMatrix.add(Arrays.asList(arrayMatrix));
        }

        for (Double[] arrayMatrix : durationArrayMatrix) {
            durationMatrix.add(Arrays.asList(arrayMatrix));
        }


        // create a configuration
        TSPConfiguration tspConfiguration = new TSPConfiguration.Builder()
                .setTsp(tsp)
                .setDistanceMatrix(distanceMatrix)
                .setDurationMatrix(durationMatrix)
                .setSolutionClass(Solution.class)
                .build();

        AlgorithmSetting algorithmSetting = new AlgorithmSetting.Builder().setMaxIterations(4000).build();

        Solver solver = new Solver.Builder().setAlgorithmSettings(algorithmSetting).setConfiguration(tspConfiguration).build();
        Solution solution = solver.solve();

        List<Destination> orderedDestinations = solution.destinations;
        for (Destination destination : orderedDestinations) {
            System.out.println("Destination : " + destination.getName());
        }

    }
}
