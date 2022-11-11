package ru.scheredin.SMO.services;

import com.google.inject.Inject;
import ru.scheredin.SMO.components.Buffer;
import ru.scheredin.SMO.components.BuyersPool;
import ru.scheredin.SMO.components.CouriersPool;
import ru.scheredin.SMO.dto.Snapshot;

import java.util.ArrayList;
import java.util.ListIterator;

public class SnapshotService {

    private BuyersPool buyersPool;
    private Buffer buffer;
    private CouriersPool couriersPool;

    private ArrayList<Snapshot> snapshots;
    @Inject
    private ClockService clock;



    private boolean isReady = false;

    public SnapshotService() {
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

    public void save(String msg) {
        if (buyersPool == null | buffer == null | couriersPool == null) {
            return;
        }
        Snapshot snapshot = new Snapshot(buyersPool.getDump(),
                buffer.getDump(),
                couriersPool.getDump(),
                buffer.getInsertPointer(),
                buffer.getTakePointer(),
                msg,
                                         clock.getTime());
        snapshots.add(snapshot);
    }
    public ListIterator<Snapshot> iterator() {
        return snapshots.listIterator();
    }



    public ArrayList<Snapshot> getSnapshots() {
        return snapshots;
    }
}
