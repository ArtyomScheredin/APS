package ru.scheredin.SMO.stats;

import com.google.inject.Singleton;
import ru.scheredin.SMO.components.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class AutoModeStats {
    private ArrayList<Request> requests = new ArrayList<>();
    private ArrayList<Double> couriersRestTime;
    private ArrayList<Double> couriersWorkTime;
    private int buyersNumber;

    private static AutoModeStats instance;

    public AutoModeStats() {
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
    }

    public void save(Request request) {
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
        Request nullRequest = null;
        for (Request request : requests) { //Counting requests, overall buffer
            // and process time and number of completed requests
            int buyer = request.getBuyer();
            requestsCount[buyer]++;

            if (request.isRejected()) {
                completed[buyer]++;
            }

            bufferTime[buyer] += request.getBufferTookTime() - request.getBufferInsertedTime();
            Double completionTime = request.getCompletionTime();
            if (completionTime != null) {
                processTime[buyer] += completionTime - request.getBufferTookTime();
            }
        }

        double[] avgBufferTime = new double[buyersNumber];
        double[] avgProcessingTime = new double[buyersNumber];
        for (int i = 0; i < buyersNumber; i++) {        //Calculating avg time
            avgBufferTime[i] = bufferTime[i] / requestsCount[i];
            avgProcessingTime[i] = processTime[i] / requestsCount[i];
        }

        double[] dispBufferTime = new double[buyersNumber];
        double[] dispProcessingTime = new double[buyersNumber];
        Arrays.fill(dispProcessingTime, 0);
        Arrays.fill(dispBufferTime, 0);
        for (Request request : requests) {   //Calculating dispersion  disp = (sum(x_avg - x_i)^2) /n
            int buyer = request.getBuyer();
            dispBufferTime[buyer] += avgBufferTime[buyer] + request.getBufferInsertedTime() - request.getBufferTookTime();
            if (request.getCompletionTime() != null) {
                dispProcessingTime[buyer] += avgBufferTime[buyer] + request.getBufferTookTime() - request.getCompletionTime();
            }
        }

        for (int i = 0; i < buyersNumber; i++) {
            dispBufferTime[i] /= buyersNumber;
            dispProcessingTime[i] /= buyersNumber;
        }

        ArrayList<BuyerStats> result = new ArrayList<>(buyersNumber);
        for (int i = 0; i < buyersNumber; i++) {
            double rejectProbability = (double) completed[i] / requestsCount[i];
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

    public static AutoModeStats INSTANCE() {
        if (instance == null) {
            instance = new AutoModeStats();
        }
        return instance;
    }
}
