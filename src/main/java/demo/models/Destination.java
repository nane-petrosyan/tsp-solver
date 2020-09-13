package demo.models;

import core.annotations.ArrivalTime;
import core.annotations.TimeWindow;

import java.util.List;

public class Destination {
    private String name;

    @ArrivalTime
    public Double arrivalTime;

    @TimeWindow
    public List<Double> timeWindows;

    public Destination(String name, List<Double> timeWindows) {
        this.name = name;
        this.timeWindows = timeWindows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
