package ru.scheredin.SMO.components;

import ru.scheredin.SMO.services.ClockService;
import ru.scheredin.SMO.services.OrchestratorService;
import ru.scheredin.SMO.services.AutoModeStatsService;
import ru.scheredin.SMO.services.SnapshotService;

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
    private ClockService clock;


    public Courier(int index, double processingTime, Buffer buffer, SnapshotService snapshotService,
                   AutoModeStatsService autoModeStatsService, ClockService clock,
                   OrchestratorService orchestratorService) {
        this.index = index;
        this.processingTime = processingTime;
        this.buffer = buffer;
        this.snapshotService = snapshotService;
        this.autoModeStatsService = autoModeStatsService;
        this.clock = clock;
        changeStateTime = clock.getTime();
        this.orchestratorService = orchestratorService;
    }

    public boolean isFree() {
        return State.FREE == state;
    }

    public void submitTask() {
        if (state == State.WORKING) {
            return;
        }
        changeState(State.WORKING);

        orchestratorService.addAction(clock.getTime(), this::startTask);
    }

    private void startTask() {
        request = buffer.take();
        request.setBufferTookTime(clock.getTime());
        snapshotService.save("Took request " + request);
        orchestratorService.addAction(clock.getTime() + processingTime, this::finishTask);
    }

    private void finishTask() {
        request.setCompletionTime(clock.getTime());
        request = null;

        snapshotService.save("Completed request " + request);
        if (buffer.isEmpty()) {
            changeState(State.FREE);
        } else {
            orchestratorService.addAction(clock.getTime(), this::startTask);
        }
    }

    private void changeState(State newState) {
        state = newState;
        double duration = clock.getTime() - changeStateTime;
        if (newState.equals(State.FREE)) {
            autoModeStatsService.notifyWorkEnded(index, duration);
        } else {
            autoModeStatsService.notifyRestEnded(index, duration);
        }
        changeStateTime = clock.getTime();
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
