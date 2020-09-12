package core.util;

import core.annotations.Destinations;
import core.configurations.TSPConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Solver {
    private TSPConfiguration configuration;
    private AlgorithmSetting algorithmSetting;
    private Class<?> solutionType;

    private Solver() {
    }

    public Solver(Builder builder) {
        this.configuration = builder.configuration;
        this.algorithmSetting = builder.algorithmSetting;
        this.solutionType = configuration.getSolutionClass();
    }

    public static class Builder {
        private TSPConfiguration configuration;
        private AlgorithmSetting algorithmSetting = new AlgorithmSetting.Builder().build();

        public Builder setConfiguration(TSPConfiguration configuration) {
            this.configuration = configuration;

            return this;
        }

        public Builder setAlgorithmSettings(AlgorithmSetting algorithmSettings) {
            this.algorithmSetting = algorithmSettings;

            return this;
        }

        public Solver build() {
            if (configuration == null) {
                throw new IllegalStateException("Please provide a configuration for TSP");
            }

            return new Solver(this);
        }
    }

    public TSPConfiguration getConfiguration() {
        return configuration;
    }

    public AlgorithmSetting getAlgorithmSetting() {
        return algorithmSetting;
    }

    // TODO : needs proper implementation
    public <solutionType> solutionType solve() throws IllegalAccessException, InstantiationException {
        algorithmSetting.setCalculationCriterion(configuration.getDistanceMatrix());

        Generator generator = new Generator(algorithmSetting);
        List<Chromosome> lastPopulation = generator.evolve();
        Chromosome bestSolution = generator.bestInPopulation(lastPopulation);

        Field tspDestinationsField = getDestinationsField(configuration.getTsp());
        Collection<?> tspDestinationList = (Collection<?>) tspDestinationsField.get(configuration.getTsp());

        List<Object> destinationList = new ArrayList<>(tspDestinationList);
        List<Object> orderedList = new ArrayList<>();

        for (int order = 0; order < bestSolution.getGenotype().size(); order++) {
            orderedList.add(destinationList.get(bestSolution.getGenotype().get(order)));
        }


        solutionType solution = (solutionType) solutionType.newInstance();
        Field collectionField = getSolutionDestinations(solution);
        collectionField.set(solution, orderedList);

        return solution;
    }

    private Field getDestinationsField(Object tsp) throws IllegalAccessException {

        Field destinations = null;
        int destinationAnnotationQuantity = 0;

        if (tsp == null) {
            throw new IllegalStateException("Please configure TSP class properly.");
        }

        for (Field field : tsp.getClass().getFields()) {
            if (field.isAnnotationPresent(Destinations.class)) {

                if (!Collection.class.isAssignableFrom(field.getType())) {
                    throw new IllegalStateException("Destinations of TSP must represent a collection.");
                }

                // TODO : catch here
                destinations = field;

                if (destinationAnnotationQuantity > 0) {
                    throw new IllegalStateException("There must be only one collection of destinations in TSP.");
                }

                destinationAnnotationQuantity++;
            }
        }

        return destinations;
    }

    private Field getSolutionDestinations(Object solution) {
        int destinationAnnotationQuantity = 0;
        Field collectionField = null;
        for (Field field : solution.getClass().getFields()) {
            if (field.isAnnotationPresent(Destinations.class)) {

                if (!List.class.isAssignableFrom(field.getType())) {
                    throw new IllegalStateException("Destinations of solution must represent an ordered collection.");
                }

                if (destinationAnnotationQuantity > 0) {
                    throw new IllegalStateException("There must be only one collection of destinations in solution.");
                }

                collectionField = field;

                destinationAnnotationQuantity++;
            }
        }

        return collectionField;
    }
}

