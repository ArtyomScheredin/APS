package ru.scheredin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Buffer with circled, separated insert/take operations and fifo reject policy
 */
@Component
public class Buffer {

    @Value("#{new Integer(${buffer.capacity})}")
    private final int bufferSize = 0;
    /**
     * fixed size buffer. Write/read from multiple threads.
     * Null values and completed requests are cells available for insertion
     */
    private final ArrayList<Request> buffer = new ArrayList<>(bufferSize);
    /**
     * non-clustered index for buffer to access Request in O(1)
     */
    private final TreeSet<Request> requestsIndex = new TreeSet<>();
    private final ReentrantLock lock = new ReentrantLock();
    private volatile int numberOfRequests = 0;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Autowired
    @Qualifier("bufferInsertedMonitor")
    Object monitor;

    public boolean isOccupiedPosition(int index) {
        lock.lock();
        try {
            return (buffer.get(index) != null) || (!buffer.get(index).getResponse().isDone());
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void rejectLastInserted() {
        Request request;
        lock.lock();
        try {
            while ((request = requestsIndex.pollLast()) == null) {/*empty*/}
            numberOfRequests--;
        } finally {
            lock.unlock();
        }
        request.setResponse(new Response(Response.State.REJECTED));
    }

    public void insert(@NonNull Request request, int index) {
        lock.lock();
        try {
            if (isOccupiedPosition(index)) {
                throw new RuntimeException("Attempt to insert into occupied position");
            }
            buffer.set(index, request);
            requestsIndex.add(request);
            numberOfRequests++;
            support.firePropertyChange("numberOfRequests", numberOfRequests, numberOfRequests);
        } finally {
            lock.unlock();
        }
    }

    public Request take(int index) {
        lock.lock();
        try {
            if (!isOccupiedPosition(index)) {
                return null;
            }
            Request oldRequest = buffer.set(index, null);
            requestsIndex.remove(oldRequest);
            numberOfRequests--;
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<Request> getRequestsBuffer() {
        return new ArrayList<>(buffer);
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
