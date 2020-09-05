package demo.models;

import core.annotations.Destinations;
import core.annotations.TSP;

import java.util.List;

@TSP
public class SampleTsp {
    @Destinations
    public List<Destination> destinations;

    public SampleTsp(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }
}
