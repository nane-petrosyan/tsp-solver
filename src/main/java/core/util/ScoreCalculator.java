package core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScoreCalculator {
    // 1. Create a random population
    //        - randomOrder() to randomly create chromosomes
    // 2. Calculate fitness probability for each chromosome
    //        - calculateFitness() for the order
    //        - normalizeFitness()
    // 3. Next generation
    //        - pick a chromosome (pool selection)
    //        - mutate
    //        - repeat
    // TODO : add crossover

    public ScoreCalculator(List<List<Double>> distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }


    private final int POPULATION_SIZE = 10;
    private List<List<Double>> distanceMatrix;


    public List<Chromosome> createPopulation() {
        List<Chromosome> population = new ArrayList<>();

        List<Integer> initialOrder = IntStream.rangeClosed(0, distanceMatrix.size() - 1)
                .boxed().collect(Collectors.toList());

        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Integer> randomOrder = randomOrder(initialOrder);
            population.add(new Chromosome(calculateFitness(randomOrder), randomOrder));
        }
        normalizeFitness(population);

        return population;
    }

    public Chromosome bestInThePopulation(List<Chromosome> chromosomes) {
        Chromosome best = chromosomes.get(0);
        for (Chromosome ch : chromosomes) {
            if (ch.getFitness() > best.getFitness()) {
                best = ch;
            }
        }

        System.out.println("BEST IN THE POPULATION : " + best.getFitness());
        return best;
    }


    // TODO : maybe change this later for better performance
    public List<Chromosome> nextGeneration(List<Chromosome> initialPopulation) {
        // copy population
        List<Chromosome> population = new ArrayList<>();
        for (Chromosome chromosome : initialPopulation) {
            population.add(new Chromosome(chromosome));
        }

        List<Chromosome> newGen = new ArrayList<>();

        for (int i = 0; i < population.size(); i++) {
            Chromosome chromosome = new Chromosome(pickAChromosome(population));
            mutate(chromosome.getValue());
            calculateFitness(chromosome.getValue());
            newGen.add(chromosome);
        }

        normalizeFitness(newGen);

        // print fitness
        for (Chromosome chromosome : newGen) {
            System.out.println(chromosome.getFitness());
        }

        return newGen;
    }

    public void mutate(List<Integer> chromosome) {
        Random randGenerator = new Random();
        int index1 = randGenerator.nextInt(chromosome.size());
        int index2 = randGenerator.nextInt(chromosome.size());

        swap(chromosome, index1, index2);
    }

    public void swap(List<Integer> arr, int index1, int index2) {
        Integer tmp = arr.get(index1);
        arr.set(index1, arr.get(index2));
        arr.set(index2, tmp);
    }

    public Chromosome pickAChromosome(List<Chromosome> population) {
        int index = 0;
        double random = new Random().nextDouble();

        do {
            random -= population.get(index++).getFitness();
        } while (random > 0);

        return population.get(--index);
    }

    public List<Integer> randomOrder(List<Integer> order) {
        List<Integer> newOrder = copyAnArray(order);
        Collections.shuffle(newOrder);

        return newOrder;
    }

    public List<Integer> copyAnArray(List<Integer> list) {
        return new ArrayList<>(list);
    }

    public void normalizeFitness(List<Chromosome> population) {
        double sum = 0.0;
        for (Chromosome chromosome : population) {
            sum += chromosome.getFitness();
        }

        for (Chromosome chromosome : population) {
            chromosome.setFitness(chromosome.getFitness() / sum);
        }
    }

    public double calculateFitness(List<Integer> sequence) {
        double score = 0;

        for (int i = 0; i < sequence.size() - 1; i++) {
            score += (getDistance(sequence.get(i), sequence.get(i + 1)));
        }

        return (double) (1 / (score));
    }

    public double getDistance(int index1, int index2) {
        if (distanceMatrix.size() <= index1 || distanceMatrix.get(index1).size() <= index2) {
            throw new IllegalStateException("Given distance matrix does not match with destination points. Make sure the matrix contains all distances between " +
                    "a salesperson and destinations.");
        }

        return (Double) distanceMatrix.get(index1).get(index2);
    }
}
