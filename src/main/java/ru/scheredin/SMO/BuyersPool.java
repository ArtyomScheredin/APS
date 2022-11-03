package ru.scheredin.SMO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BuyersPool implements Runnable {
    @Value("#{new Integer(${buyers.number})}")
    private int buyersNumber;
    private ExecutorService executors = Executors.newFixedThreadPool(buyersNumber);
    private ArrayList<Buyer> buyers = new ArrayList<>(buyersNumber);

    @Override
    public void run() {
        for (int buyerSerial = 0; buyerSerial < buyersNumber; buyerSerial++) {
            Buyer buyer = new Buyer(buyerSerial);
            buyers.add(buyer);
            executors.submit(buyer);
        }
    }
}
