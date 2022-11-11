package ru.scheredin.SMO.dto;

import com.google.inject.name.Named;

import java.math.BigDecimal;

public record BuyerStats(int index,
                         int requestsCount,
                         Double rejectProbability,
                         Double avgTime,
                         Double avgBufferTime,
                         Double avgProcessingTime,
                         Double dispersionBufferTime,
                         Double dispersionProcessingTime) {
    @Named("MATH_ACCURACY")
    private static int ACCURACY;

    public BuyerStats(int index, int requestsCount, Double rejectProbability, Double avgTime, Double avgBufferTime,
                      Double avgProcessingTime, Double dispersionBufferTime, Double dispersionProcessingTime) {
        this.index = index;

        this.requestsCount = requestsCount;
        this.rejectProbability = new BigDecimal(rejectProbability).setScale(ACCURACY).doubleValue();
        this.avgTime = new BigDecimal(avgTime).setScale(ACCURACY).doubleValue();;
        this.avgBufferTime = new BigDecimal(avgBufferTime).setScale(ACCURACY).doubleValue();;
        this.avgProcessingTime = new BigDecimal(avgProcessingTime).setScale(ACCURACY).doubleValue();
        this.dispersionBufferTime = new BigDecimal(dispersionBufferTime).setScale(ACCURACY).doubleValue();
        this.dispersionProcessingTime = new BigDecimal(dispersionProcessingTime).setScale(ACCURACY).doubleValue();
    }

    public static String getHeader() {
        return "buyer  |rejprob |avgTime |avgBuf  |avgProc |dispBuf |dispProc";
    }

    @Override
    public String toString() {
        return "%7d|%.6f|%.6f|%.6f|%.6f|%.6f|%.6f"
                .formatted(
                        index,
                        rejectProbability,
                        avgTime,
                        avgBufferTime,
                        avgProcessingTime,
                        dispersionBufferTime,
                        dispersionProcessingTime);
    }
}
