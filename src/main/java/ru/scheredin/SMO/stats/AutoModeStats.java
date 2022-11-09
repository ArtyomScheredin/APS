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
                result.add(i, new CourierStats(i, work / rest));
            }
        }
        return result;
    }

    public List<BuyerStats> getBuyersResults() {
        int[] requestsCount = new int[buyersNumber];
        int[] completed = new int[buyersNumber];
        double[] bufferTime = new double[buyersNumber];
        double[] processTime = new double[buyersNumber];

        Arrays.fill(requestsCount, 0);
        Arrays.fill(completed, 0);
        Arrays.fill(bufferTime, 0);
        Arrays.fill(processTime, 0);

        for (Request request : requests) { //Counting requests, overall buffer
            // and process time and number of completed requests
            int buyer = request.getBuyer();
            requestsCount[buyer]++;

            if (request.getCompletionTime() != null) {
                completed[buyer]++;
            }

            bufferTime[buyer] += request.getBufferTookTime() - request.getBufferInsertedTime();
            processTime[buyer] += request.getBufferTookTime() - request.getCompletionTime();
        }

        double[] avgBufferTime = new double[buyersNumber];
        double[] avgProcessingTime = new double[buyersNumber];
        for (int i = 0; i < buyersNumber; i++) {        //Calculating avg time
            avgBufferTime[i] = bufferTime[i] / requestsCount[i];
            avgProcessingTime[i] = processTime[i] / requestsCount[i];
        }

        for (Request request : requests) { //Calculating dispersion

        }
        return null;
    }

    public static AutoModeStats INSTANCE() {
        if (instance == null) {
            instance = new AutoModeStats();
        }
        return instance;
    }
}
