package ru.scheredin.SMO.dto;

public record Round(int buyersNumber,
                    int courierNumber,
                    double processingTime,
                    int bufferCapacity,
                    double lambda,
                    double duration) {
    public Round(int buyersNumber, int courierNumber, double processingTime, int bufferCapacity, double lambda,
                 double duration) {
        this.buyersNumber = buyersNumber;
        this.courierNumber = courierNumber;
        this.processingTime = processingTime;
        this.bufferCapacity = bufferCapacity;
        this.lambda = lambda;
        this.duration = duration;
        if (isBadParameters()) {
            throw new RuntimeException("Incorrect round parameters");
        }
    }

    private boolean isBadParameters() {
        if ((buyersNumber < 0) | (courierNumber < 0) | (processingTime < 0)
                | (bufferCapacity < 0) | (lambda < 0) | (duration < 0)) {
            return true;
        }
        return false;
    }
}
