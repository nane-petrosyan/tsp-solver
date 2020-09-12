package core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    // TODO : add penalty strategies, time criteria
    private final int POPULATION_SIZE;
    private final AlgorithmSetting algorithmSettings;
    private final Reproducer reproducer;
    private final FitnessCalculator fitnessCalculator;

    protected Generator(AlgorithmSetting algorithmSettings) {
        this.POPULATION_SIZE = algorithmSettings.getPopulationSize();
        this.algorithmSettings = algorithmSettings;
        this.reproducer = new Reproducer.Builder().setMutationRate(algorithmSettings.getMutationRate()).build();
        this.fitnessCalculator = new FitnessCalculator(algorithmSettings.getCalculationCriterion());
    }

    protected List<Chromosome> evolve() {
        List<Chromosome> lastPopulation = createInitialPopulation();

        for (int i = 0; i < algorithmSettings.getMaxIterations(); i++) {
            lastPopulation = generatePopulation(lastPopulation);
        }

        return lastPopulation;
    }

    private List<Chromosome> generatePopulation(List<Chromosome> population) {
        List<Chromosome> nextGeneration = new ArrayList<>();
        float fitness;

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome chromosomeA = GeneRoulette.pickOne(population);
            Chromosome chromosomeB = GeneRoulette.pickOne(population);

            List<Integer> childGene = reproducer.crossover(chromosomeA.getGenotype(), chromosomeB.getGenotype());
            Chromosome child = new Chromosome(fitnessCalculator.calculate(childGene), childGene);
            reproducer.mutate(child.getGenotype());
            fitness = fitnessCalculator.calculate(child.getGenotype());
            child.setFitness(fitness);
            nextGeneration.add(child);
        }
        fitnessCalculator.normalize(nextGeneration);

        return nextGeneration;
    }

    private List<Chromosome> createInitialPopulation() {
        List<Chromosome> initialPopulation = new ArrayList<>();

        List<Integer> initialOrder = IntStream.rangeClosed(0, algorithmSettings.getCalculationCriterion().size() - 1)
                .boxed().collect(Collectors.toList());

        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Integer> shuffledOrder = reproducer.randomOrder(initialOrder);
            float fitness = fitnessCalculator.calculate(shuffledOrder);

            initialPopulation.add(new Chromosome(fitness, shuffledOrder));
        }

        fitnessCalculator.normalize(initialPopulation);

        return initialPopulation;
    }

    protected Chromosome bestInPopulation(List<Chromosome> population) {
        return fitnessCalculator.bestInPopulation(population);
    }
}
