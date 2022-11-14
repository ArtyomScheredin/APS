package ru.scheredin.SMO.dto;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.List;

import static ru.scheredin.SMO.dto.Utils.round;

public record Snapshot(List<Request> buyers,
                       List<Request> buffer,
                       List<Request> couriers,
                       int nextInsertIndex,
                       int nextTakeIndex,
                       String message,
                       double time) {
    @Named("MATH_ACCURACY")
    @Inject
    private static int ACCURACY;

    public Snapshot(List<Request> buyers, List<Request> buffer, List<Request> couriers, int nextInsertIndex,
                    int nextTakeIndex, String message, double time) {
        this.buyers = buyers;
        this.buffer = buffer;
        this.couriers = couriers;
        this.nextInsertIndex = nextInsertIndex;
        this.nextTakeIndex = nextTakeIndex;
        this.message = message;
        this.time = round(time);
    }

    @Override
    public String toString() {
       StringBuilder builder = new StringBuilder();
        builder.append(time + ": ").append(message);
        builder.append("\nbuyers:\n");
        for (int i = 0; i < buyers.size(); i++) {
            builder.append(buyers.get(i) == null ? '-' : buyers.get(i)).append(" ");
        }
        builder.append("\nbuffer:\n");
        for (int i = 0; i < buffer.size(); i++) {
            builder.append(buffer.get(i) == null ? '-' : buffer.get(i)).append(" ");
        }
        builder.append("\ncouriers:\n");
        for (int i = 0; i < couriers.size(); i++) {
            builder.append(couriers.get(i) == null ? '-' : couriers.get(i)).append(" ");
        }
        builder.append("\nnext insert index: " + nextInsertIndex);
        builder.append("\nnext take index: " + nextTakeIndex + '\n');
        return builder.toString();
    }
}