package ru.scheredin.SMO;

import ru.scheredin.SMO.components.Buffer;
import ru.scheredin.SMO.components.BuyersPool;
import ru.scheredin.SMO.components.CouriersPool;
import ru.scheredin.SMO.stats.AutoModeStatsService;
import ru.scheredin.SMO.stats.StepModeStatsService;

import java.util.Map;
import java.util.TreeMap;

public class Orchestrator {
    private TreeMap<Double, Runnable> actions;
    private double curTime;
    private static Orchestrator instance;

    public void runRound(Round round) {
        Buffer buffer = new Buffer(round.bufferCapacity());
        CouriersPool couriersPool = new CouriersPool(round.courierNumber(),
                round.processingTime(),
                buffer);
        BuyersPool buyersPool = new BuyersPool(
                round.buyersNumber(),
                round.lambda(),
                round.duration(),
                buffer,
                couriersPool);
        StepModeStatsService.INSTANCE().clear();
        AutoModeStatsService.INSTANCE().clear();
        StepModeStatsService.INSTANCE().init(buyersPool, buffer, couriersPool);
        AutoModeStatsService.INSTANCE().init(round.buyersNumber(), round.courierNumber());


        actions = buyersPool.generateActions();

        for (Map.Entry<Double, Runnable> action = actions.pollFirstEntry();
             !actions.isEmpty();
             action = actions.pollFirstEntry()) {
            curTime = action.getKey();
            action.getValue().run();
        }

        AutoModeStatsService.INSTANCE().setReady(true);
        StepModeStatsService.INSTANCE().setReady(true);
    }

    public void addAction(Double timestamp, Runnable runnable) {
        actions.put(timestamp, runnable);
    }

    public double getCurTime() {
        return curTime;
    }

    public static Orchestrator INSTANCE() {
        if (instance == null) {
            instance = new Orchestrator();
        }
        return instance;
    }
}