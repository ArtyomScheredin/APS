package ru.scheredin.SMO.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static ru.scheredin.SMO.dto.Utils.round;

public record BuyerStats(@JsonProperty int index,
                         @JsonProperty
                         int requestsCount,
                         @JsonProperty
                         Double rejectProbability,
                         @JsonProperty
                         Double avgTime,
                         @JsonProperty
                         Double avgBufferTime,
                         @JsonProperty
                         Double avgProcessingTime,
                         @JsonProperty
                         Double dispersionBufferTime,
                         @JsonProperty
                         Double dispersionProcessingTime) {


    public BuyerStats(int index, int requestsCount, Double rejectProbability, Double avgTime, Double avgBufferTime,
                      Double avgProcessingTime, Double dispersionBufferTime, Double dispersionProcessingTime) {
        this.index = index;
        this.requestsCount = requestsCount;
        this.rejectProbability = round(rejectProbability);
        this.avgTime = round(avgTime);
        this.avgBufferTime = round(avgBufferTime);
        this.avgProcessingTime = round(avgProcessingTime);
        this.dispersionBufferTime = round(dispersionBufferTime);
        this.dispersionProcessingTime = round(dispersionProcessingTime);
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
