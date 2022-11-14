package ru.scheredin.SMO.services;

import ru.scheredin.SMO.dto.Request;
import ru.scheredin.SMO.dto.BuyerStats;
import ru.scheredin.SMO.dto.CourierStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoModeStatsService {
    private ArrayList<Request> requests = new ArrayList<>();
    private ArrayList<Double> couriersRestTime;
    private ArrayList<Double> couriersWorkTime;
    private int buyersNumber;
    private boolean isReady = false;
    private int requestSerial = 1;

    public AutoModeStatsService() {
    }

    public void init(int buyersNumber, int couriersNumber) {
        couriersRestTime = new ArrayList<>(couriersNumber);
        couriersWorkTime = new ArrayList<>(couriersNumber);
        this.buyersNumber = buyersNumber;
        for (int i = 0; i < couriersNumber; i++) {
            couriersRestTime.add(0.);
            couriersWorkTime.add(0.);
        }
    }

    public void clear() {
        requests.clear();
        couriersRestTime = null;
        couriersRestTime = null;
        isReady = false;
        requestSerial = 1;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean status) {
        isReady = status;
    }

    public void save(Request request) {
        request.setSerial(requestSerial++);
        requests.add(request);
    }

    public void notifyRestEnded(int index, double duration) {
        Double time = couriersRestTime.get(index);
        couriersRestTime.set(index, time + duration);
    }

    public void notifyWorkEnded(int index, double duration) {
        Double time = couriersWorkTime.get(index);
        couriersWorkTime.set(index, time + duration);
    }

    /**
     * efficiency: work to rest time ratio
     *
     * @return
     */
    public List<CourierStats> getCourierResults() {
        ArrayList<CourierStats> result = new ArrayList<>(couriersRestTime.size());
        for (int i = 0; i < couriersWorkTime.size(); i++) {
            Double rest = couriersRestTime.get(i);
            Double work = couriersWorkTime.get(i);
            if (rest == null) {
                result.add(i, new CourierStats(i, Double.MAX_VALUE));
            } else {
                result.add(i, new CourierStats(i, work / (rest + work)));
            }
        }
        return result;
    }

    public List<BuyerStats> getBuyersResults() {
        int[] requestsCount = new int[buyersNumber];
        int[] completed = new int[buyersNumber];
        double[] bufferTime = new double[buyersNumber]; //buffer time for all requests from buyer
        double[] processTime = new double[buyersNumber]; //processing time for all requests from buyer

        Arrays.fill(requestsCount, 0);
        Arrays.fill(completed, 0);
        Arrays.fill(bufferTime, 0);
        Arrays.fill(processTime, 0);
        for (Request request : requests) { //Counting requests, overall buffer
            // and process time and number of completed requests
            int buyer = request.getBuyerNumber();
            requestsCount[buyer]++;

            if (request.getCompletionTime() != null) {
                completed[buyer]++;
                processTime[buyer] += request.getCompletionTime() - request.getBufferTookTime();
            }
            bufferTime[buyer] += request.getBufferTookTime() - request.getBufferInsertedTime();
        }

        double[] avgBufferTime = new double[buyersNumber];
        double[] avgProcessingTime = new double[buyersNumber];
        for (int i = 0; i < buyersNumber; i++) {        //Calculating avg time
            avgBufferTime[i] = bufferTime[i] / requestsCount[i];
            avgProcessingTime[i] = processTime[i] / completed[i];
        }

        double[] dispBufferTime = new double[buyersNumber];
        double[] dispProcessingTime = new double[buyersNumber];
        Arrays.fill(dispProcessingTime, 0);
        Arrays.fill(dispBufferTime, 0);
        for (Request request : requests) {   //Calculating dispersion  disp = (sum(x_avg - x_i)^2) /n
            int buyer = request.getBuyerNumber();
            dispBufferTime[buyer] += Math.pow(avgBufferTime[buyer]
                    + request.getBufferInsertedTime()
                    - request.getBufferTookTime(), 2);
            if (request.getCompletionTime() != null) {
                dispProcessingTime[buyer] += Math.pow(avgBufferTime[buyer]
                        + request.getBufferTookTime()
                        - request.getCompletionTime(), 2);
            }
        }

        for (int i = 0; i < buyersNumber; i++) {
            dispBufferTime[i] /= buyersNumber;
            dispProcessingTime[i] /= buyersNumber;
        }

        ArrayList<BuyerStats> result = new ArrayList<>(buyersNumber);
        for (int i = 0; i < buyersNumber; i++) {
            double rejectProbability = (double) (requestsCount[i] - completed[i]) / requestsCount[i];
            double avgTime = avgBufferTime[i] + avgProcessingTime[i];
            result.add(new BuyerStats(i,
                    requestsCount[i],
                    rejectProbability,
                    avgTime,
                    avgBufferTime[i],
                    avgProcessingTime[i],
                    dispBufferTime[i],
                    dispProcessingTime[i]));
        }
        return result;
    }
}
