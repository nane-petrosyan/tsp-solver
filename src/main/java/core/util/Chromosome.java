package core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chromosome {
    private float fitness;
    private List<Integer> genotype;
    private static Map<Integer, List<Double>> timeWindows;

    private List<Double> arrivalTimes;

    public Chromosome(Chromosome chromosome) {
        this.fitness = chromosome.getFitness();
        this.genotype = new ArrayList<>(chromosome.getGenotype());
        this.arrivalTimes = new ArrayList<>(chromosome.getArrivalTimes());
    }

    public Chromosome(float fitness, List<Integer> value) {
        this.fitness = fitness;
        this.genotype = value;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public List<Integer> getGenotype() {
        return genotype;
    }

    public void setGenotype(List<Integer> genotype) {
        this.genotype = genotype;
    }

    public List<Double> getArrivalTimes() {
        return arrivalTimes;
    }

    public void setArrivalTimes(List<Double> arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }

    public static Map<Integer, List<Double>> getTimeWindows() {
        return timeWindows;
    }

    public static void setTimeWindows(Map<Integer, List<Double>> timeWindows) {
        Chromosome.timeWindows = timeWindows;
    }
}
