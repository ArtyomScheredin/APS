package ru.scheredin.SMO.components;


import ru.scheredin.SMO.dto.Request;
import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.services.ClockService;
import ru.scheredin.SMO.services.SnapshotService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;


public class BuyersPool implements Dumpable {
    private int buyersNumber;
    private double lambda;
    private double endTime;
    private final Buffer buffer;
    private final CouriersPool couriersPool;
    private AutoModeStatsService autoModeStatsService;
    private SnapshotService snapshotService;
    private ClockService clock;
    private ArrayList<Request> state;

    public BuyersPool(int buyersNumber, double lambda, double duration, Buffer buffer, CouriersPool couriersPool,
                      AutoModeStatsService autoModeStatsService, SnapshotService stepModeStatsService,
                      ClockService clock) {
        this.buyersNumber = buyersNumber;
        this.lambda = lambda;
        this.endTime = clock.getTime() + duration;
        this.buffer = buffer;
        this.couriersPool = couriersPool;
        this.autoModeStatsService = autoModeStatsService;
        this.snapshotService = stepModeStatsService;
        this.clock = clock;
        Request[] requests = new Request[buyersNumber];
        Arrays.fill(requests, null);
        state = new ArrayList<>(Arrays.asList(requests));
    }

    public TreeMap<Double, Runnable> generateActions() {
        TreeMap<Double, Runnable> actions = new TreeMap<>();
        for (int buyer = 0; buyer < buyersNumber; buyer++) {
            int curBuyer = buyer;
            for (double time = 0; time < endTime; time += getPuassonInterval()) {
                actions.put(time, () -> {
                    Request request = new Request(curBuyer);
                    autoModeStatsService.save(request);
                    request.setBufferInsertedTime(clock.getTime());

                    state.set(curBuyer, request);
                    snapshotService.save("Created request " + request);

                    buffer.insert(request);
                    state.set(curBuyer, null);
                    snapshotService.save("Inserted request " + request);
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
