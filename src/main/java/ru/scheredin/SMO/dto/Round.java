package ru.scheredin.SMO.dto;

public record Round(int buyersNumber,
                    int courierNumber,
                    double processingTime,
                    int bufferCapacity,
                    double lambda,
                    double duration) {
}
