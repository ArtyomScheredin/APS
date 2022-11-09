package ru.scheredin.SMO.components;


import ru.scheredin.SMO.Orchestrator;
import ru.scheredin.SMO.stats.AutoModeStats;
import ru.scheredin.SMO.stats.StepModeStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;


public class BuyersPool implements Dumpable {
    private int buyersNumber;
    private double lambda;
    private double endTime;
    private final Buffer buffer;
    private final CouriersPool couriersPool;
    private ArrayList<Request> state;

    public BuyersPool(int buyersNumber, double lambda, double duration, Buffer buffer, CouriersPool couriersPool) {
        this.buyersNumber = buyersNumber;
        this.lambda = lambda;
        this.endTime = Orchestrator.INSTANCE().getCurTime() + duration;
        this.buffer = buffer;
        this.couriersPool = couriersPool;
        Request[] requests = new Request[buyersNumber];
        Arrays.fill(requests, null);
        state = new ArrayList<>(Arrays.asList(requests));
    }

    public TreeMap<Double, Runnable> generateActions() {
        TreeMap<Double, Runnable> actions = new TreeMap<>();
        int requestSerial = 0;
        for (int buyer = 0; buyer < buyersNumber; buyer++) {
            int curBuyer = buyer;
            for (double time = 0; time < endTime; time += getPuassonInterval()) {
                final int serial = requestSerial++;
                actions.put(time, () -> {
                    Request request = new Request(curBuyer, serial);
                    AutoModeStats.INSTANCE().save(request);
                    request.setBufferInsertedTime(Orchestrator.INSTANCE().getCurTime());

                    state.set(curBuyer, request);
                    StepModeStats.INSTANCE().saveSnapshot("Created request " + request);

                    buffer.insert(request);
                    state.set(curBuyer, null);
                    StepModeStats.INSTANCE().saveSnapshot("Inserted request " + request);
                    couriersPool.notifyNewRequest();
                });
            }
        }
        return actions;
    }

    /**
     * time interval for puasson distribution
     */
    private double getPuassonInterval() {
        return (-1.0 / lambda) * Math.log(Math.random());
    }

    @Override
    public ArrayList<Request> getDump() {
        return new ArrayList<>(state);
    }


}
