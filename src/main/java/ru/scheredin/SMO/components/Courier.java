package ru.scheredin.SMO.components;

import ru.scheredin.SMO.Orchestrator;
import ru.scheredin.SMO.stats.AutoModeStats;
import ru.scheredin.SMO.stats.StepModeStats;

public class Courier {
    private State state = State.FREE;
    private Double changeStateTime;
    private Request request;

    private final int index;
    private final double processingTime;
    private final Buffer buffer;


    public Courier(int index, double processingTime, Buffer buffer) {
        this.index = index;
        this.processingTime = processingTime;
        this.buffer = buffer;
        changeStateTime = Orchestrator.INSTANCE().getCurTime();
    }

    public boolean isFree() {
        return State.FREE == state;
    }

    public void submitTask() {
        if (state == State.WORKING) {
            return;
        }
        changeState(State.WORKING);

        Orchestrator.INSTANCE().addAction(Orchestrator.INSTANCE().getCurTime(), this::startTask);
    }

    private void startTask() {
        request = buffer.take();
        request.setBufferTookTime(Orchestrator.INSTANCE().getCurTime());
        StepModeStats.INSTANCE().saveSnapshot("Took request " + request);
        Orchestrator.INSTANCE().addAction(Orchestrator.INSTANCE().getCurTime() + processingTime, this::finishTask);
    }

    private void finishTask() {
        request.setCompletionTime(Orchestrator.INSTANCE().getCurTime());
        request = null;

        StepModeStats.INSTANCE().saveSnapshot("Completed request " + request);
        if (buffer.isEmpty()) {
            changeState(State.FREE);
        } else {
            Orchestrator.INSTANCE().addAction(Orchestrator.INSTANCE().getCurTime(), this::startTask);
        }
    }

    private void changeState(State newState) {
        state = newState;
        double duration = Orchestrator.INSTANCE().getCurTime() - changeStateTime;
        if (newState.equals(State.FREE)) {
            AutoModeStats.INSTANCE().notifyWorkEnded(index, duration);
        } else {
            AutoModeStats.INSTANCE().notifyRestEnded(index, duration);
        }
        changeStateTime = Orchestrator.INSTANCE().getCurTime();
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
