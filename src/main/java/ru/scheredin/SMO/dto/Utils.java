package ru.scheredin.SMO.dto;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {
    @Named("MATH_ACCURACY")
    @Inject
    private static int ACCURACY;

    static double round(Double dispersionProcessingTime) {
        if (dispersionProcessingTime.isInfinite() || dispersionProcessingTime.isNaN()) {
            return Double.MAX_VALUE;
        }
        return new BigDecimal(dispersionProcessingTime).setScale(ACCURACY, RoundingMode.UP).doubleValue();
    }
}
