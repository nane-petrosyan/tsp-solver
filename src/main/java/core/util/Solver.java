package core.util;

import core.annotations.ArrivalTime;
import core.annotations.Destinations;
import core.annotations.TimeWindow;
import core.configurations.TSPConfiguration;

import java.lang.reflect.Field;
import java.util.*;

// TODO : create proper fitness normalizer
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

    // TODO : needs proper implementation, remove unchecked casts, wrap exceptions etc.
    public <solutionType> solutionType solve() throws IllegalAccessException, InstantiationException {
        algorithmSetting.setCalculationCriterion(configuration.getDistanceMatrix());


        Field tspDestinationsField = getDestinationsField(configuration.getTsp());
        Collection<?> tspDestinationList = (Collection<?>) tspDestinationsField.get(configuration.getTsp());
        configureTimeWindows(tspDestinationList);


        Generator generator = new Generator(algorithmSetting);
        if (configuration.getDurationMatrix() != null) {
            generator.addPenalizer(new TimeWindowPenalizer(configuration.getDurationMatrix()));
        }

        List<Chromosome> lastPopulation = generator.evolve();
        Chromosome bestSolution = generator.bestInPopulation(lastPopulation);

        List<Object> destinationList = new ArrayList<>(tspDestinationList);
        List<Object> orderedList = new ArrayList<>();

        for (int order = 0; order < bestSolution.getGenotype().size(); order++) {
            orderedList.add(destinationList.get(bestSolution.getGenotype().get(order)));
        }


        solutionType solution = (solutionType) solutionType.newInstance();
        Field collectionField = getSolutionDestinations(solution);
        setSolutionDestinationArrivalTimes(destinationList, bestSolution.getArrivalTimes());
        collectionField.set(solution, orderedList);

        return solution;
    }

    private void setSolutionDestinationArrivalTimes(List<Object> destinationList, List<Double> arrivalTimes) throws IllegalAccessException {
        if(arrivalTimes != null && !arrivalTimes.isEmpty()) {
            int index = 0;
            for (Object destination : destinationList) {
                for (Field field : destination.getClass().getFields()) {
                    if (field.isAnnotationPresent(ArrivalTime.class)) {
                        field.set( destination, arrivalTimes.get(index));
                        break;
                    }
                }
                index++;
            }
        }
    }

    private Field getDestinationsField(Object tsp) {

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

    private void configureTimeWindows(Collection<?> tspDestination) throws IllegalAccessException {
        Map<Integer, List<Double>> timeWindowMap = new HashMap<>();

        if (tspDestination == null) {
            throw new IllegalStateException("Please provide valid destination list.");
        }

        int index = 0;
        for (Object dest : tspDestination) {
            for (Field field : dest.getClass().getFields()) {
                if (field.isAnnotationPresent(TimeWindow.class)) {

                    if (!Collection.class.isAssignableFrom(field.getType())) {
                        throw new IllegalStateException("Time windows of TSP must represent a collection.");
                    }

                    timeWindowMap.put(index, (List<Double>) field.get(dest));

                    break;
                }
            }
            index++;
        }

        Chromosome.setTimeWindows(timeWindowMap);
    }
}

