package ru.scheredin.SMO.components;

import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.services.ClockService;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.services.SnapshotService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CouriersPool implements Dumpable {
    private List<Courier> couriers;

    public CouriersPool(int couriersNumber,
                        double processingTime,
                        Buffer buffer, AutoModeStatsService autoModeStatsService, SnapshotService snapshotService,
                        ClockService clock, OrchestratorService orchestratorService) {
        couriers = new ArrayList<>(couriersNumber);
        for (int index = 0; index < couriersNumber; index++) {
            couriers.add(new Courier(index, processingTime, buffer, snapshotService, autoModeStatsService, clock, orchestratorService));
        }
    }

    public void notifyNewRequest() {
        Optional<Courier> freeCourier = couriers.stream().filter(Courier::isFree).findFirst();
        freeCourier.ifPresent(Courier::submitTask);
    }

    @Override
    public ArrayList<Request> getDump() {
        return couriers.stream().map(Courier::getRequest).collect(Collectors.toCollection(ArrayList::new));
    }
}
