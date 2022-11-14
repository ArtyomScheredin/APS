package ru.scheredin.SMO.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static ru.scheredin.SMO.dto.Utils.round;

public record CourierStats(   @JsonProperty int index,
                              @JsonProperty double efficiency) {


    public CourierStats(int index, double efficiency) {
        this.index = index;
        this.efficiency = round(efficiency);
    }
}
