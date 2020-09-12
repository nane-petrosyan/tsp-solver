package core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Reproducer {
    private final int mutationRate;

    private Reproducer(Builder builder) {
        this.mutationRate = builder.mutationRate;
    }

    public static class Builder {
        private int mutationRate = 10;

        protected Builder setMutationRate(int mutationRate) {
            this.mutationRate = mutationRate;

            return this;
        }

        protected Reproducer build() {
            return new Reproducer(this);
        }
    }


    void mutate(List<Integer> chromosomeValue) {
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

    List<Integer> crossover(List<Integer> genotypeA, List<Integer> genotypeB) {

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

        return child;
    }


    protected List<Integer> randomOrder(List<Integer> order) {
        List<Integer> newOrder = new ArrayList<>(order);
        Collections.shuffle(newOrder);

        return newOrder;
    }
}
