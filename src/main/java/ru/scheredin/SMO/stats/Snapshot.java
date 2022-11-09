package ru.scheredin.SMO.stats;

import ru.scheredin.SMO.components.Request;

import java.util.List;


public record Snapshot(List<Request> buyers,
                       List<Request> buffer,
                       List<Request> couriers,
                       int nextInsertIndex,
                       int nextTakeIndex,
                       String message) {


    @Override
    public String toString() {

       StringBuilder builder = new StringBuilder();
        builder.append(message);
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