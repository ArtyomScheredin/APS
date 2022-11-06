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
    private final double duration;


    public Courier(int index, double processingTime, Buffer buffer, double duration) {
        this.index = index;
        this.processingTime = processingTime;
        this.buffer = buffer;
        this.duration = duration;
        changeStateTime = Orchestrator.INSTANCE().getCurTime();
    }

    public boolean isFree() {
        return State.FREE == state;
    }

    public void submitTask() {
        if (state == State.WORKING) {
            return;
        }

        state = State.WORKING;
        AutoModeStats.INSTANCE().notifyRestEnded(index, changeStateTime - Orchestrator.INSTANCE().getCurTime());
        changeStateTime = Orchestrator.INSTANCE().getCurTime();
        Orchestrator.INSTANCE().addAction(Orchestrator.INSTANCE().getCurTime(), this::startTask);
    }

    private void startTask() {
        request = buffer.take();
        request.setBufferTookTime(Orchestrator.INSTANCE().getCurTime());
        StepModeStats.INSTANCE().saveSnapshot();


        if (Orchestrator.INSTANCE().getCurTime() + processingTime > duration) {
            return;
        }
        Orchestrator.INSTANCE().addAction(Orchestrator.INSTANCE().getCurTime() + processingTime, this::finishTask);
    }

    private void finishTask() {
        request.setCompletionTime(Orchestrator.INSTANCE().getCurTime());
        request = null;

        StepModeStats.INSTANCE().saveSnapshot();
        if (buffer.isEmpty()) {
            state = State.FREE;
            AutoModeStats.INSTANCE().notifyWorkEnded(index, changeStateTime - Orchestrator.INSTANCE().getCurTime());
        } else {
            Orchestrator.INSTANCE().addAction(Orchestrator.INSTANCE().getCurTime(), this::startTask);
        }
    }



    private enum State {
        FREE, WORKING
    }
}
