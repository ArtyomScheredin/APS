package ru.scheredin.SMO.components;

import ru.scheredin.SMO.components.internal.IndexedArray;
import ru.scheredin.SMO.stats.StepModeStats;

import java.util.List;
import java.util.ListIterator;

/**
 * Buffer with circled, separated insert/take operations and fifo reject policy
 */
public class Buffer {

    private final int bufferCapacity;
    /**
     * fixed size buffer. Write/read from multiple threads.
     * Null values and completed requests are cells available for insertion
     */

    private final IndexedArray storage;
    private ListIterator<Request> insertIterator;
    private ListIterator<Request> takeIterator;


    public Buffer(int bufferCapacity) {
        this.bufferCapacity = bufferCapacity;
        storage = new IndexedArray(bufferCapacity);
        insertIterator = storage.iterator();
        takeIterator = storage.iterator();
    }

    public void insert(Request request) {
        if (storage.isFull()) {
            storage.rejectNewest();
        }
        insertIterator.add(request);

        StepModeStats.INSTANCE().saveSnapshot();
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

    public List<Request> getState() {
        return storage.getState();
    }
}
