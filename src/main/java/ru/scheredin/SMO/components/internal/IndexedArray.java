package ru.scheredin.SMO.components.internal;


import ru.scheredin.SMO.dto.Request;
import ru.scheredin.SMO.services.ClockService;
import ru.scheredin.SMO.services.SnapshotService;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

public class IndexedArray implements Iterable<Request> {
    /**
     * nullable circled array. Nulls == deleted requests
     */
    private final ArrayList<Request> buffer;
    private SnapshotService snapshotService;
    private ClockService clock;
    /**
     * non-clustered index for buffer to access Request in buffer with O(1) complexity
     * sorted by insertion date
     */
    private final TreeMap<Request, Integer> indexes = new TreeMap<>();
    private int size;

    public IndexedArray(int capacity, SnapshotService stepModeStatsService, ClockService clock) {
        buffer = new ArrayList<>(capacity);
        this.snapshotService = stepModeStatsService;
        this.clock = clock;
        for (int i = 0; i < capacity; i++) {
            buffer.add(null);
        }
    }

    public void set(int index, Request request) {
        if (request == null) {
            Request requestToDelete = buffer.get(index);
            if (indexes.remove(requestToDelete) == null) {
                throw new RuntimeException("Consistency violation: indexes don't contain element to remove");
            }
            buffer.set(index, null);
            size--;
        } else {
            buffer.set(index, request);
            indexes.put(request, index);
            size++;
        }
    }

    public Request rejectNewest() {
        Map.Entry<Request, Integer> entry = indexes.pollFirstEntry();
        Integer newestIndex = entry.getValue();
        buffer.set(newestIndex, null);
        size--;
        Request rejected = entry.getKey();
        rejected.setBufferTookTime(clock.getTime());
        return rejected;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return (buffer.size() - size) == 0;
    }

    public Iterator iterator() {
        return new Iterator();
    }

    public List<Request> getState() {
        return buffer;
    }

    public class Iterator implements ListIterator<Request> {
        private int cur;
        private int prev;

        /**
         * @return next element. Can't cause infinite loop. Assure that collection isn't empty
         */
        @Override
        public Request next() {
            Request request;
            prev = cur;
            cur = (cur == buffer.size() - 1) ? 0 : cur + 1;
            while ((request = buffer.get(prev)) == null) { //iterating over buffer
                snapshotService.save("Finding request to take. Current index = " + cur);
                prev = cur;
                cur = (cur == buffer.size() - 1) ? 0 : cur + 1;
            }
            return request;
        }


        @Override
        public void remove() {
            if (buffer.get(prev) == null) {
                throw new RuntimeException("Prev element has already been removed!");
            }
            IndexedArray.this.set(prev, null);
        }

        @Override
        public void add(Request request) {
            while (buffer.get(prev) != null) { //iterating over buffer
                prev = cur;
                snapshotService.save("Finding place to insert. Next index = " + cur);
                cur = (cur == buffer.size() - 1) ? 0 : cur + 1;
            }
            IndexedArray.this.set(prev, request);
        }

        @Override
        public int nextIndex() {
            return cur;
        }

        @Override
        public void set(Request request) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Request previous() {
            throw new UnsupportedOperationException();
        }


        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return true;
        }
    }
}
