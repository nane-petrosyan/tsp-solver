package core.configurations;

import java.util.List;

public class TSPConfiguration {
    private List<List<Double>> distanceMatrix;
    private Object tsp;
    private Class<?> solutionClass;

    private TSPConfiguration() {

    }

    private TSPConfiguration(Builder builder) {
        this.distanceMatrix = builder.distanceMatrix;
        this.tsp = builder.tsp;
        this.solutionClass = builder.solutionClass;
    }

    public static class Builder {
        private List<List<Double>> distanceMatrix;
        private Object tsp;
        private Class<?> solutionClass;


        public Builder setDistanceMatrix(List<List<Double>> distanceMatrix) {
            this.distanceMatrix = distanceMatrix;

            return this;
        }

        public Builder setTsp(Object TSP) {
            this.tsp = TSP;

            return this;
        }

        public Builder setSolutionClass(Class<?> solutionClass) {
            this.solutionClass = solutionClass;

            return this;
        }

        public TSPConfiguration build() {
            return new TSPConfiguration(this);
        }
    }

    public List<List<Double>> getDistanceMatrix() {
        return distanceMatrix;
    }

    public Object getTsp() {
        return tsp;
    }

    public Class getSolutionClass() {
        return solutionClass;
    }
}
