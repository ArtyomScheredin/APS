package ru.scheredin.SMO.components.internal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.scheredin.SMO.components.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

public class IndexedArray implements Iterable<Request> {
    /**
     * nullable circled array. Nulls == deleted requests
     */
    private final ArrayList<Request> buffer;
    /**
     * non-clustered index for buffer to access Request in buffer with O(1) complexity
     * sorted by insertion date
     */
    private final TreeMap<Request, Integer> indexes = new TreeMap<>();
    private int size;

    public IndexedArray(int capacity) {
        buffer = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buffer.add(null);
        };
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

    public void rejectNewest() {
        Integer newestIndex = indexes.pollFirstEntry().getValue();
        buffer.set(newestIndex, null);
        size--;
    }

    public Request get(int index) {
        return buffer.get(index);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return (buffer.size() - size) == 0;
    }

    public Itr iterator() {
        return new Itr();
    }

    public List<Request> getState() {
        return Collections.unmodifiableList(buffer);
    }

    private class Itr implements ListIterator<Request> {
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
                cur = (cur == buffer.size() - 1) ? 0 : cur + 1;
            }
            IndexedArray.this.set(prev, request);
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
        public int nextIndex() {
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
