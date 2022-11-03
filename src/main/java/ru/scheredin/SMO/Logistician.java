package ru.scheredin.SMO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.SynchronousQueue;

@Component
public class Logistician implements Runnable {

    @Autowired
    private Buffer buffer;
    private int indexToTake = 0;
    @Autowired
    @Qualifier("bufferInsertedMonitor")
    Object monitor;

    private SynchronousQueue<Request> singleRequestQueue = new SynchronousQueue<>();

    public void accept(@NonNull Request request) throws InterruptedException {
        singleRequestQueue.put(request);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int initialIndex = indexToTake;
            do {
                indexToTake = ((indexToTake - 1) != buffer.getBufferSize()) ? ++indexToTake : 0;
                if (initialIndex == indexToTake) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } while (buffer.isOccupiedPosition(indexToTake));
            try {
                buffer.take(indexToTake);
            } catch (IllegalArgumentException e) {
                //empty
            }

            buffer.get(indexToInsert++, request);
            return curRequest;
            Request request;
            try {
                request = singleRequestQueue.take();
            } catch (InterruptedException e) {
                break;
            }
            int initialIndex = indexToInsert;
            do {
                indexToInsert = ((indexToInsert - 1) != buffer.getBufferSize()) ? ++indexToInsert : 0;
                if (initialIndex == indexToInsert) {
                    buffer.rejectLastInserted();
                }
            } while (buffer.isOccupiedPosition(indexToInsert) && (initialIndex != indexToInsert));

            request.setBufferInsertedTime(System.currentTimeMillis());
        }
    }
}