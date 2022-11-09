package ru.scheredin.SMO.stats;

import ru.scheredin.SMO.components.Buffer;
import ru.scheredin.SMO.components.BuyersPool;
import ru.scheredin.SMO.components.CouriersPool;

import java.util.ArrayList;
import java.util.ListIterator;

public class StepModeStatsService {

    private BuyersPool buyersPool;
    private Buffer buffer;
    private CouriersPool couriersPool;

    private ArrayList<Snapshot> snapshots;

    private static StepModeStatsService instance;



    private boolean isReady = false;

    public StepModeStatsService() {
    }

    public void init(BuyersPool buyersPool, Buffer buffer, CouriersPool couriersPool) {
        this.buyersPool = buyersPool;
        this.buffer = buffer;
        this.couriersPool = couriersPool;
        snapshots = new ArrayList<>();
    }

    public void clear() {
        snapshots = null;
        buyersPool = null;
        buffer = null;
        couriersPool = null;
        isReady = false;
    }
    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean status) {
        isReady = status;
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

    public static StepModeStatsService INSTANCE() {
        if (instance == null) {
            instance = new StepModeStatsService();
        }
        return instance;
    }

    public ListIterator<Snapshot> iterator() {
        return snapshots.listIterator();
    }



    public ArrayList<Snapshot> getSnapshots() {
        return snapshots;
    }
}
