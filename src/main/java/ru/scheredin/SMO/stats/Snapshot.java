package ru.scheredin.SMO.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.scheredin.SMO.components.Courier;
import ru.scheredin.SMO.components.Request;

import java.util.List;


public record Snapshot(List<Request> buyers,
                        List<Request> buffer,
                        List<Courier> couriers) {


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("buyers:\n");
        for (int i = 0; i < buyers.size(); i++) {
            builder.append(i).append(": ").append(buyers.get(i) == null ? '-' : buyers.get(i)).append("\n");
        }
        builder.append("buffer:\n");
        for (int i = 0; i < buffer.size(); i++) {
            builder.append(i).append(": ").append(buffer.get(i) == null ? '-' : buffer.get(i)).append("\n");
        }
        builder.append("couriers:\n");
        for (int i = 0; i < buffer.size(); i++) {
            builder.append(i).append(": ").append(couriers.get(i) == null ? '-' : couriers.get(i)).append("\n");
        }
        return builder.toString();
    }
}