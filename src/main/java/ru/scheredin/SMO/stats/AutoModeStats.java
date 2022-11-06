package ru.scheredin.SMO.stats;

import ru.scheredin.SMO.components.Request;

import java.util.ArrayList;
import java.util.Collections;

public class AutoModeStats {
    private ArrayList<Request> requests = new ArrayList<>();
    private ArrayList<Double> couriersRestTime;
    private ArrayList<Double> couriersWorkTime;
    private int buyersNumber;

    private static AutoModeStats instance;

    public AutoModeStats() {
    }

    public void init(int buyersNumber, int couriersNumber) {
        couriersRestTime = new ArrayList<>(couriersNumber);
        couriersWorkTime = new ArrayList<>(couriersNumber);
        for (int i = 0; i < couriersNumber; i++) {
            couriersRestTime.add(0.);
            couriersWorkTime.add(0.);
        }
    }

    public void clear() {
        requests.clear();
        couriersRestTime = null;
        couriersRestTime = null;
    }

    public void save(Request request) {
        requests.add(request);
    }

    public void notifyRestEnded(int index, double duration) {
        Double time = couriersRestTime.get(index);
        couriersRestTime.set(index, time + duration);
    }

    public void notifyWorkEnded(int index, double duration) {
        Double time = couriersWorkTime.get(index);
        couriersWorkTime.set(index, time + duration);
    }

    /*public void getResults() {
        double rejectionRate();
    }*/

    public static AutoModeStats INSTANCE() {
        if (instance == null) {
            instance = new AutoModeStats();
        }
        return instance;
    }
}
