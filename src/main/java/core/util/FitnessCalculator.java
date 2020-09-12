package core.util;

import java.util.List;

public class FitnessCalculator {
    private List<List<Double>> calculationCriterion;

    protected FitnessCalculator(List<List<Double>> calculationCriterion) {
        this.calculationCriterion = calculationCriterion;
    }

    private FitnessCalculator() {
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
        if (calculationCriterion.size() <= index1 || calculationCriterion.get(index1).size() <= index2) {
            throw new IllegalStateException("Given distance matrix does not match with destination points. Make sure the matrix contains all distances between " +
                    "a salesperson and destinations.");
        }

        return calculationCriterion.get(index1).get(index2);
    }

    protected void normalize(List<Chromosome> population) {
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

    protected float calculate(List<Integer> order) {
        float fitness = 0.0f;

        for (int i = 0; i < order.size() - 1; i++) {
            fitness += getDistance(order.get(i), order.get(i + 1));
        }

        fitness = 1 / fitness;

        return fitness;
    }


    @Deprecated
    private void normalizeFitness(List<Chromosome> population) {
        float fitnessSum = 0;
        for (Chromosome chromosome : population) {
            fitnessSum += chromosome.getFitness();
        }

        for (Chromosome chromosome : population) {
            chromosome.setFitness(chromosome.getFitness() / fitnessSum);
        }
    }

    protected Chromosome bestInPopulation(List<Chromosome> population) {
        Chromosome best = population.get(0);

        for (Chromosome chromosome : population) {
            if (chromosome.getFitness() > best.getFitness()) {
                best = chromosome;
            }
        }

        return best;
    }

}
