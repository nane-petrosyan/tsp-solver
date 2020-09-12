package core.util;

import core.configurations.AlgorithmSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO : THIS CLASS IS JUST A WORKING VERSION, DESIGN OF ALGORITHM MUST BE CHANGED
public class ScoreCalculator {
    // TODO : add constraints
    // TODO : salesperson ?

    private final int POPULATION_SIZE = 100;
    private final List<List<Double>> distanceMatrix;
    private final AlgorithmSetting algorithmSetting;

    public ScoreCalculator(List<List<Double>> distanceMatrix, AlgorithmSetting algorithmSetting) {
        this.distanceMatrix = distanceMatrix;
        this.algorithmSetting = algorithmSetting;
    }

    public List<Chromosome> createInitialPopulation() {
        List<Chromosome> initialPopulation = new ArrayList<>();

        List<Integer> initialOrder = IntStream.rangeClosed(0, distanceMatrix.size() - 1)
                .boxed().collect(Collectors.toList());

        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Integer> shuffledOrder = randomOrder(initialOrder);
            float fitness = calculateFitness(shuffledOrder);

            initialPopulation.add(new Chromosome(fitness, shuffledOrder));
        }

        normalizeFitness(initialPopulation);

        return initialPopulation;
    }

//    private void normalizeFitness(List<Chromosome> population) {
//        float fitnessSum = 0;
//        for (Chromosome chromosome : population) {
//            fitnessSum += chromosome.getFitness();
//        }
//
//        for (Chromosome chromosome : population) {
//            chromosome.setFitness(chromosome.getFitness() / fitnessSum);
//        }
//    }

    private void normalizeFitness(List<Chromosome> population) {
        float maxFitness = population.get(0).getFitness();
        float minFitness = population.get(0).getFitness();
        for (Chromosome chromosome : population) {
            if (chromosome.getFitness() > maxFitness) {
                maxFitness = chromosome.getFitness();
            }
            if (chromosome.getFitness() < minFitness) {
                minFitness = chromosome.getFitness();
            }
        }

        for (Chromosome chromosome : population) {
            chromosome.setFitness((chromosome.getFitness() - minFitness) / (maxFitness - minFitness));
        }

    }

    private float calculateFitness(List<Integer> order) {
        float fitness = 0.0f;

        for (int i = 0; i < order.size() - 1; i++) {
            fitness += getDistance(order.get(i), order.get(i + 1));
        }

        fitness = 1 / fitness;

        return fitness;
    }

    public List<Integer> randomOrder(List<Integer> order) {
        List<Integer> newOrder = copyAnArray(order);
        Collections.shuffle(newOrder);

        return newOrder;
    }

    public List<Integer> copyAnArray(List<Integer> list) {
        return new ArrayList<>(list);
    }

    public List<Chromosome> nextGeneration(List<Chromosome> population) {
        List<Chromosome> nextGeneration = new ArrayList<>();
        float fitness;

        for (int i = 0; i < POPULATION_SIZE; i++) {
//            System.out.println("iteration " + i);
            Chromosome chromosomeA = pickOneChromosome(population);
            Chromosome chromosomeB = pickOneChromosome(population);

            Chromosome child = crossover(chromosomeA, chromosomeB);
            mutate(child.getGenotype(), algorithmSetting.getMutationRate());
            fitness = calculateFitness(child.getGenotype());
            child.setFitness(fitness);
            nextGeneration.add(child);
        }
        normalizeFitness(nextGeneration);

        return nextGeneration;
    }

    private Chromosome crossover(Chromosome geneA, Chromosome geneB) {

        List<Integer> genotypeA = geneA.getGenotype();
        List<Integer> genotypeB = geneB.getGenotype();

        Random random = new Random();
        int indexA = random.nextInt(1) + (genotypeA.size());
        int indexB = random.nextInt((genotypeA.size() - indexA) + 1) + indexA;

        List<Integer> slicedOrder = genotypeA.subList(indexA, indexB);

        List<Integer> child = new ArrayList<>(slicedOrder);

        for (Integer order : genotypeB) {
            if (!child.contains(order)) {
                child.add(order);
            }
        }

        return new Chromosome(calculateFitness(child), child);
    }

    private void mutate(List<Integer> chromosomeValue, int mutationRate) {
        for (int i = 0; i < mutationRate; i++) {
            Random randGenerator = new Random();
            int index1 = randGenerator.nextInt(chromosomeValue.size());
            int index2 = randGenerator.nextInt(chromosomeValue.size());

            swap(chromosomeValue, index1, index2);
        }
    }

    public void swap(List<Integer> arr, int index1, int index2) {
        Integer tmp = arr.get(index1);
        arr.set(index1, arr.get(index2));
        arr.set(index2, tmp);
    }

    private Chromosome pickOneChromosome(List<Chromosome> population) {
        int index = 0;
        float random = new Random().nextFloat() + 0.01f;

        while (random > 0) {
            random -= population.get(index++).getFitness();
        }

        return population.get(--index);
    }

    public Chromosome bestInThePopulation(List<Chromosome> chromosomes) {
        Chromosome best = chromosomes.get(0);
        for (Chromosome ch : chromosomes) {
            if (ch.getFitness() > best.getFitness()) {
                best = ch;
            }
        }

        return best;
    }

    public double getDistance(int index1, int index2) {
        if (distanceMatrix.size() <= index1 || distanceMatrix.get(index1).size() <= index2) {
            throw new IllegalStateException("Given distance matrix does not match with destination points. Make sure the matrix contains all distances between " +
                    "a salesperson and destinations.");
        }

        return distanceMatrix.get(index1).get(index2);
    }
}
