package ru.scheredin.SMO.components;

import ru.scheredin.SMO.dto.Request;
import ru.scheredin.SMO.services.ClockService;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.services.SnapshotService;

import java.util.Random;

public class Courier {
    private State state = State.FREE;
    private Double changeStateTime;
    private OrchestratorService orchestratorService;
    private Request request;

    private final int index;
    private final double processingTime;
    private final Buffer buffer;
    private SnapshotService snapshotService;
    private AutoModeStatsService autoModeStatsService;
    private CouriersPool couriersPool;
    private ClockService clock;
    private Random random = new Random();


    public Courier(int index, double processingTime, Buffer buffer, SnapshotService snapshotService,
                   AutoModeStatsService autoModeStatsService, ClockService clock,
                   OrchestratorService orchestratorService, CouriersPool couriersPool) {
        this.index = index;
        this.processingTime = processingTime;
        this.buffer = buffer;
        this.snapshotService = snapshotService;
        this.autoModeStatsService = autoModeStatsService;
        this.clock = clock;
        changeStateTime = clock.getTime();
        this.orchestratorService = orchestratorService;
        this.couriersPool = couriersPool;
    }

    public boolean isFree() {
        return State.FREE == state;
    }

    public void startTask() {
        changeState(State.WORKING);
        snapshotService.save("Start work. Courier " + index);
        request = buffer.take();
        request.setBufferTookTime(clock.getTime());
        snapshotService.save("Took request " + request);
        orchestratorService.addAction(clock.getTime() + getGaussianIntervalTime(), this::finishTask);
    }

    private void finishTask() {
        request.setCompletionTime(clock.getTime());
        snapshotService.save("Completed request " + request);
        request = null;
        changeState(State.FREE);
        if (!buffer.isEmpty()) {
            orchestratorService.addAction(clock.getTime(), couriersPool::notifyFindCourier);
        }
    }

    private void changeState(State newState) {
        if (newState == state) {
            return;
        }
        state = newState;
        double duration = clock.getTime() - changeStateTime;
        if (newState.equals(State.FREE)) {
            autoModeStatsService.notifyWorkEnded(index, duration);
        } else {
            autoModeStatsService.notifyRestEnded(index, duration);
        }
        changeStateTime = clock.getTime();
    }

    private double getGaussianIntervalTime() {
      return Math.abs(random.nextGaussian() * processingTime / 6 + processingTime / 2) % processingTime;
    }

    @Override
    public String toString() {
        return request == null ? "-" : request.toString();
    }

    public Request getRequest() {
        return request;
    }

    private enum State {
        FREE, WORKING
    }
}
