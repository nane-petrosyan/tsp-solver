package core.configurations;

import core.annotations.Destinations;
import core.util.Chromosome;
import core.util.ScoreCalculator;

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

    // TODO : make this a builder
    public Solver(TSPConfiguration configuration, AlgorithmSetting algorithmSetting) {
        this.configuration = configuration;
        this.algorithmSetting = algorithmSetting;
        this.solutionType = configuration.getSolutionClass();
    }


    // TODO : needs proper implementation
    public <solutionType> solutionType solve() throws IllegalAccessException, InstantiationException {

        // create generation
        ScoreCalculator scoreCalculator = new ScoreCalculator(configuration.getDistanceMatrix(), algorithmSetting);

        // main algorithm starts here
        List<Chromosome> population = scoreCalculator.createInitialPopulation();

        // generate new chromosomes
        int MAX_ITERATIONS = algorithmSetting.getMaxIterations();
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            population = scoreCalculator.nextGeneration(population);
        }
        Chromosome bestSolution = scoreCalculator.bestInThePopulation(population);

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


    public TSPConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(TSPConfiguration configuration) {
        this.configuration = configuration;
        this.solutionType = configuration.getSolutionClass();
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

