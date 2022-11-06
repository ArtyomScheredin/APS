package ru.scheredin.SMO.old;

import org.springframework.beans.factory.annotation.Autowired;
import ru.scheredin.SMO.Statistics;

public class Action implements Runnable, Comparable<Action> {
    private Runnable runnable;
    private Long timestamp;

    @Autowired
    Statistics statistics;

    public Action(Runnable runnable, long timestamp) {
        this.runnable = runnable;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Action o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    @Override
    public void run() {
        runnable.run();
        Statistics.saveSnapshot();
    }
}
