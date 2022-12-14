package ru.scheredin.SMO.services;

import com.google.inject.Inject;
import ru.scheredin.SMO.dto.BuyerStats;
import ru.scheredin.SMO.dto.CourierStats;
import ru.scheredin.SMO.dto.Round;
import ru.scheredin.SMO.components.Buffer;
import ru.scheredin.SMO.components.BuyersPool;
import ru.scheredin.SMO.components.CouriersPool;

import java.util.Map;
import java.util.TreeMap;

public class OrchestratorService {
    private TreeMap<Double, Runnable> actions;
    @Inject
    private AutoModeStatsService autoModeStatsService;
    @Inject
    private SnapshotService snapshotService;
    @Inject
    private ClockService clock;

    public void runRound(Round round) {
        clock.setTime(0);
        autoModeStatsService.clear();
        snapshotService.clear();
        Buffer buffer = new Buffer(round.bufferCapacity(),
                snapshotService,
                clock);
        CouriersPool couriersPool = new CouriersPool(round.courierNumber(),
                round.processingTime(),
                buffer,
                autoModeStatsService,
                snapshotService,
                clock, this);
        BuyersPool buyersPool = new BuyersPool(
                round.buyersNumber(),
                round.lambda(),
                round.duration(),
                buffer,
                couriersPool,
                autoModeStatsService,
                snapshotService,
                clock);
        snapshotService.init(buyersPool, buffer, couriersPool);
        autoModeStatsService.init(round.buyersNumber(), round.courierNumber());


        actions = buyersPool.generateActions();

        for (Map.Entry<Double, Runnable> action = actions.pollFirstEntry();
             !actions.isEmpty();
             action = actions.pollFirstEntry()) {
            clock.setTime(action.getKey());
            action.getValue().run();
        }

        autoModeStatsService.setReady(true);
        snapshotService.setReady(true);
    }

    public void addAction(Double timestamp, Runnable runnable) {
        actions.put(timestamp, runnable);
    }
}