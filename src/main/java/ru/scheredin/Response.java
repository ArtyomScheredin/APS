package ru.scheredin;

public record Response(State state, Long bufferInsertedTime, Long bufferTookTime, Long completedTime) {
    public Response(State state) {
        this(state, null, null, null);
        if (state == State.ACCEPTED) {
            throw new IllegalArgumentException("For accepted responses time stats must be specified");
        }
    }

    public enum State {
        ACCEPTED, PENDING, REJECTED
    }
}
