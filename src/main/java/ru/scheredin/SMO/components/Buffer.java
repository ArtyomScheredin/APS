package ru.scheredin.SMO.components;

import ru.scheredin.SMO.Orchestrator;
import ru.scheredin.SMO.components.internal.IndexedArray;
import ru.scheredin.SMO.stats.StepModeStats;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Buffer with circled, separated insert/take operations and fifo reject policy
 */
public class Buffer implements Dumpable {

    /**
     * fixed size buffer. Write/read from multiple threads.
     * Null values and completed requests are cells available for insertion
     */

    private final IndexedArray storage;
    private ListIterator<Request> insertIterator;
    private ListIterator<Request> takeIterator;


    public Buffer(int bufferCapacity) {
        storage = new IndexedArray(bufferCapacity);
        insertIterator = storage.iterator();
        takeIterator = storage.iterator();
    }

    public void insert(Request request) {
        if (storage.isFull()) {
            Request rejected = storage.rejectNewest();
            StepModeStats.INSTANCE().saveSnapshot("Reject request: " + rejected);
        }
        insertIterator.add(request);
    }

    public Request take() {
        if (storage.isEmpty()) {
            return null;
        }
        Request request = takeIterator.next();
        takeIterator.remove();
        return request;
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public ArrayList<Request> getDump() {
        return new ArrayList<>(storage.getState());
    }

    public int getInsertPointer() {
        return insertIterator.nextIndex();
    }

    public int getTakePointer() {
        return takeIterator.nextIndex();
    }
}
