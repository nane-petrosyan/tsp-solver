package core.util;

import java.util.List;

public class AlgorithmSetting {
    // TODO : make this a proper termination condition
    private int maxIterations;
    private int mutationRate;
    private int populationSize;
    private java.util.List<List<Double>> calculationCriterion;

    private AlgorithmSetting() {}

    private AlgorithmSetting(Builder builder) {
        this.maxIterations = builder.maxIterations;
        this.mutationRate = builder.mutationRate;
        this.populationSize = builder.populationSize;
        this.calculationCriterion = builder.calculationCriterion;
    }

    public static class Builder {
        private int maxIterations = 100;
        private int mutationRate = 10;
        private int populationSize = 100;
        private List<List<Double>> calculationCriterion;

        public Builder setMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;

            return this;
        }

        public Builder setMutationRate(int mutationRate) {
            this.mutationRate = mutationRate;
            return this;
        }

        protected Builder setCalculationCriterion(List<List<Double>> calculationCriterion) {
            this.calculationCriterion = calculationCriterion;
            return this;
        }

        protected Builder setPopulationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public AlgorithmSetting build() {
            return new AlgorithmSetting(this);
        }
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int getMutationRate() {
        return mutationRate;
    }

    protected int getPopulationSize() {
        return this.populationSize;
    }

    protected List<List<Double>> getCalculationCriterion() {
        return this.calculationCriterion;
    }

    protected void setCalculationCriterion(List<List<Double>> calculationCriterion) {
        this.calculationCriterion = calculationCriterion;
    }
}
