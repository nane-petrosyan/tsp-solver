package core.util;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    private float fitness;
    private List<Integer> genotype;

    public Chromosome(Chromosome chromosome) {
        this.fitness = chromosome.getFitness();
        this.genotype = new ArrayList<>(chromosome.getGenotype());
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
}
