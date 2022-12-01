package ru.scheredin.SMO.services;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import ru.scheredin.SMO.Config;
import ru.scheredin.SMO.dto.Round;

import java.util.Map;

public class EvaluatorService {
    public static final double MAX_REQUEST_TIME = 0.5;
    public static final double MIN_COURIER_EFFICIENCY = 0.9;
    public static final double MAX_REJECT_PROBABILITY = 0.1;
    @Inject
    private AutoModeStatsService statsService;

    @Inject
    private OrchestratorService orchestrator;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Config());
        EvaluatorService evaluator = injector.getInstance(EvaluatorService.class);
        Map<Integer, Double> couriers = Map.of(1000, .2,
                                               2200, .1,
                                               1400, .15);

        evaluator.findOptimalConfiguration(5, 3,
                                           15, 2000, 3, 30,
                                           Map.of(1400, .15), 24, 25);
    }

    public Round findOptimalConfiguration(int buyersNumber, int minBufferCapacity, int maxBufferCapacity,
                                          int bufferCellPrice,
                                          int minCouriers, int maxCouriers,
                                          Map<Integer, Double> courierVariants, double lambda, double duration) {
        int minPrice = Integer.MAX_VALUE;
        Round bestRound = null;
        int courierKey = -1;

        int goodRoundsCount = 0;
        for (int bufferCapacity = minBufferCapacity; bufferCapacity < maxBufferCapacity; bufferCapacity++) {
            for (int couriersNumber = minCouriers; couriersNumber < maxCouriers; couriersNumber++) {
                for (Map.Entry<Integer, Double> courier : courierVariants.entrySet()) {
                    Round round = new Round(buyersNumber, couriersNumber, courier.getValue(), bufferCapacity, lambda,
                                            duration);
                    long start = System.currentTimeMillis();
                    orchestrator.runRound(round);
                    System.out.println("Round ended in " + (System.currentTimeMillis() - start) + "ms");
                    if (isSatisfied()) {
                        System.out.println(minPrice + ":" + bestRound);
                        goodRoundsCount++;
                        int currentPrice = courier.getKey() * couriersNumber + bufferCapacity * bufferCellPrice;
                        if (minPrice > currentPrice) {
                            minPrice = currentPrice;
                            bestRound = round;
                            courierKey = courier.getKey();
                        }
                    }
                }
            }
        }
        System.out.println("Total satisfactory rounds: " + goodRoundsCount);
        System.out.println("min price : " + minPrice);
        System.out.println("for round params: " + bestRound);
        System.out.println("for courier unit price: " + courierKey);
        return bestRound;
    }


    private boolean isSatisfied() {
        if (statsService.getRequests().stream()
                .anyMatch(
                        r -> r.getCompletionTime() != null && (r.getCompletionTime() - r.getBufferInsertedTime() > MAX_REQUEST_TIME))) {
            return false;
        }
        if (statsService.getCourierResults().stream()
                .anyMatch(r -> r.efficiency() < MIN_COURIER_EFFICIENCY)) {
            return false;
        }
        if (statsService.getBuyersResults().stream()
                .anyMatch(r -> r.rejectProbability() > MAX_REJECT_PROBABILITY)) {
            return false;
        }
        return true;
    }
}
