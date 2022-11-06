package ru.scheredin.SMO.old;

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
            semaphore.acquire()
            Request request;
            try {
                buffer.insert(singleRequestQueue.take(), indexToInsert);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
