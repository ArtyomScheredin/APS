package ru.scheredin.SMO.old;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static java.lang.Math.round;
import static ru.scheredin.SMO.old.Response.State.PENDING;

/**
 * Supplier class for sending requests to {@link Buffer buffer}
 * Infinite source with Puasson distribution
 */
public class Buyer implements Runnable {
    @Value("#{new Integer(${time.unit.interval})}")
    private final int timeUnit = 0;

    private int serialNumber;
    private int counter;
    private ArrayList<CompletableFuture<Response>> responses = new ArrayList<>();

    @Autowired
    private Seller seller;
    @Autowired
    private Statistics statistics;
    @Value("${requests.per.second}")
    private int requestsPerSecond;

    public Buyer(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Request request = getNewRequest();
            CompletableFuture<Response> response = request.getResponse();
            responses.add(response);
            counter++;
            try {
                seller.accept(request);
                Thread.sleep(getPuassonInterval());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    private Request getNewRequest() {
        return new Request(serialNumber, counter);
    }

    public int getRejectProbability() {
        int rejectedRequests = 0;
        for (CompletableFuture<Response> bufferResponse : responses) {
            Response curResponse = bufferResponse.getNow(new Response(PENDING));
            switch (curResponse.state()) {
                case PENDING -> throw new UncompletedRequestsException();
                case REJECTED -> rejectedRequests++;
            }
        }
        return rejectedRequests / counter;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public int getCounter() {
        return counter;
    }
}
