package ru.scheredin.SMO.internal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.scheredin.SMO.Request;

import java.util.ArrayList;
import java.util.TreeSet;

public class IndexedArray {
    private static Logger LOGGER = LoggerFactory.getLogger(IndexedArray.class);
    private final ArrayList<Request> buffer;
    /**
     * non-clustered index for buffer to access Request in O(1)
     */
    private final TreeSet<Request> indexes = new TreeSet<>();


    public IndexedArray(int size) {
        buffer = new ArrayList<>(size);
    }

    public synchronized void set(int index, Request request) {
        if (request == null) {
            Request requestToDelete = buffer.get(index);
            if(!indexes.remove(requestToDelete)) {
                LOGGER.error("SA");
                throw new RuntimeException("Consistency violation: indexes don't contain element to remove" + request);
            }
        }
        buffer.set(index, request);
        indexes.add(request);
    }
}
