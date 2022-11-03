package ru.scheredin.SMO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.SynchronousQueue;

@Component
public class Seller implements Runnable {

    @Autowired
    private Buffer buffer;
    private int indexToInsert = 0;
    private SynchronousQueue<Request> singleRequestQueue = new SynchronousQueue<>();

    public void accept(@NonNull Request request) throws InterruptedException {
        singleRequestQueue.put(request);
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Request request;
            try {
                request = singleRequestQueue.take();
            } catch (InterruptedException e) {
                return;
            }
            buffer.insert(request, indexToInsert);

            request.setBufferInsertedTime(System.currentTimeMillis());
            int initialIndex = indexToInsert;
            do {
                indexToInsert = ((indexToInsert - 1) != buffer.getBufferSize()) ? ++indexToInsert : 0;
                if (initialIndex == indexToInsert) {
                    //buffer.rejectLastInserted();
                }
            } while (buffer.isOccupiedPosition(indexToInsert));
        }
    }
}
