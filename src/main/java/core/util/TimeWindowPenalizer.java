package core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeWindowPenalizer implements Penalizer {
    private List<List<Double>> timeRelationMatrix;

    private TimeWindowPenalizer() {
    }

    protected TimeWindowPenalizer(List<List<Double>> timeRelationMatrix) {
        this.timeRelationMatrix = timeRelationMatrix;
    }


    @Override
    public void penalizeAll(Chromosome chromosome) {
        penalizeBeforeAndAfterTimeWindow(chromosome);
    }

    private void penalizeBeforeAndAfterTimeWindow(Chromosome chromosome) {
        calculateArrivalTimes(chromosome);
        Map<Integer, List<Double>> timeWindows = Chromosome.getTimeWindows();

        for (int i = 0; i < chromosome.getArrivalTimes().size(); i++) {
            Double arrivalTime = chromosome.getArrivalTimes().get(i);
            List<Double> timeWindow = timeWindows.get(i);

            if (timeWindow.size() < 2)
                throw new IllegalStateException("Time window list must contain at least two items (earliest start and latest end).");

            // TODO : configure multiple time windows later
            float penalty = 0.0f;
            if (arrivalTime < timeWindow.get(0)) {
                penalty = (float) (Math.max(timeWindow.get(0), timeWindow.get(1)) - arrivalTime);
            }
            if (arrivalTime > timeWindow.get(1)) {
                penalty = (float) (arrivalTime - Math.min(timeWindow.get(0), timeWindow.get(1)));
            }

            chromosome.setFitness(chromosome.getFitness() - 1 / penalty);
        }
    }

    private void calculateArrivalTimes(Chromosome chromosome) {
        List<Double> arrivalTimes = new ArrayList<>();

        double arrivalTime = 0.0;
        arrivalTimes.add(arrivalTime);

        for (int i = 0; i < chromosome.getGenotype().size() - 1; i++) {
            arrivalTime += timeRelationMatrix.get(chromosome.getGenotype().get(i)).get(chromosome.getGenotype().get(i + 1));
            arrivalTimes.add(arrivalTime);
        }

        chromosome.setArrivalTimes(arrivalTimes);
    }
}

