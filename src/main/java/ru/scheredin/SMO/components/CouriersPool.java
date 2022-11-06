package ru.scheredin.SMO.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CouriersPool {
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

    public List<Courier> getState() {
        return Collections.unmodifiableList(couriers);
    }
}
