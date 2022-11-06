package ru.scheredin.SMO.old;

public record Response(State state, Long bufferInsertedTime, Long bufferTookTime, Long completedTime, int buyerNumber) {
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
