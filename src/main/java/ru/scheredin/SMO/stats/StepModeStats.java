package ru.scheredin.SMO.stats;

import ru.scheredin.SMO.components.Buffer;
import ru.scheredin.SMO.components.BuyersPool;
import ru.scheredin.SMO.components.CouriersPool;

import java.util.ArrayList;
import java.util.Iterator;

public class StepModeStats {

    private BuyersPool buyersPool;
    private Buffer buffer;
    private CouriersPool couriersPool;

    private ArrayList<Snapshot> snapshots;

    private static StepModeStats instance;

    public StepModeStats() {
    }

    public void init(BuyersPool buyersPool, Buffer buffer, CouriersPool couriersPool) {
        this.buyersPool = buyersPool;
        this.buffer = buffer;
        this.couriersPool = couriersPool;
        snapshots = new ArrayList<>();
    }

    public void saveSnapshot(String msg) {
        if (buyersPool == null | buffer == null | couriersPool == null) {
            return;
        }
        Snapshot snapshot = new Snapshot(buyersPool.getDump(),
                buffer.getDump(),
                couriersPool.getDump(),
                buffer.getInsertPointer(),
                buffer.getTakePointer(),
                msg);
        snapshots.add(snapshot);
    }

    public void clear() {
        snapshots = null;
        buyersPool = null;
        buffer = null;
        couriersPool = null;
    }

    public static StepModeStats INSTANCE() {
        if (instance == null) {
            instance = new StepModeStats();
        }
        return instance;
    }

    public Iterator<Snapshot> iterator() {
        return snapshots.iterator();
    }
}
