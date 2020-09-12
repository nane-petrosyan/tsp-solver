package core.util;

import java.util.List;
import java.util.Random;

public class GeneRoulette {
    protected static Chromosome pickOne(List<Chromosome> population) {
        int index = 0;
        float random = new Random().nextFloat() + 0.01f;

        while (random > 0) {
            random -= population.get(index++).getFitness();
        }

        return population.get(--index);
    }
}
