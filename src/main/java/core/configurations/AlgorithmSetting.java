package core.configurations;

public class AlgorithmSetting {
    // TODO : make this a proper termination condition
    private int maxIterations;
    private int mutationRate;

    private AlgorithmSetting() {}

    private AlgorithmSetting(Builder builder) {
        this.maxIterations = builder.maxIterations;
        this.mutationRate = builder.mutationRate;
    }

    public static class Builder {
        private int maxIterations = 100;
        private int mutationRate = 10;

        public Builder setMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;

            return this;
        }

        public Builder setMutationRate(int mutationRate) {
            this.mutationRate = mutationRate;
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
}
