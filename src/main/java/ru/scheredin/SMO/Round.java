package ru.scheredin.SMO;

public record Round(int buyersNumber,
                    int courierNumber,
                    double processingTime,
                    int bufferCapacity,
                    double lambda,
                    double duration) {
}
