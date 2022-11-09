package ru.scheredin.SMO.stats;

public record BuyerStats(int index,
                         int requestsCount,
                         double rejectProbability,
                         double avgTime,
                         double avgBufferTime,
                         double avgProcessingTime,
                         double dispersionWaitTime,
                         double dispersionProcessingTime) {
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
                        dispersionWaitTime,
                        dispersionProcessingTime);
    }
}
