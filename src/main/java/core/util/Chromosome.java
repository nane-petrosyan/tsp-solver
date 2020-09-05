package core.util;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    private double fitness;
    private List<Integer> value;

    public Chromosome(Chromosome chromosome) {
        this.fitness = chromosome.getFitness();
        this.value = new ArrayList<>(chromosome.getValue());
    }

    public Chromosome(double fitness, List<Integer> value) {
        this.fitness = fitness;
        this.value = value;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<Integer> getValue() {
        return value;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
    }
}
