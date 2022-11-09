package ru.scheredin.SMO.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CouriersPool implements Dumpable {
    private List<Courier> couriers;

    public CouriersPool(int couriersNumber,
                        double processingTime,
                        int duration,
                        Buffer buffer) {
        couriers = new ArrayList<>(couriersNumber);
        for (int index = 0; index < couriersNumber; index++) {
            couriers.add(new Courier(index, processingTime, buffer, duration));
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
