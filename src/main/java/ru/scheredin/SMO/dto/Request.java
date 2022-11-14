package ru.scheredin.SMO.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Request implements Comparable<Request> {
    @JsonProperty
    private int buyerNumber;
    @JsonProperty
    private int serial;
    @JsonProperty
    private Double bufferInsertedTime;
    @JsonProperty
    private Double bufferTookTime;
    @JsonProperty
    private Double completionTime;


    public Request(int buyerNumber) {
        this.buyerNumber = buyerNumber;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    @Override
    public int compareTo(Request o) {
        return bufferInsertedTime.compareTo(o.getBufferInsertedTime());
    }

    public double getBufferInsertedTime() {
        return bufferInsertedTime;
    }

    public void setBufferInsertedTime(double bufferInsertedTime) {
        this.bufferInsertedTime = bufferInsertedTime;
    }

    public void setBufferTookTime(double bufferTookTime) {
        this.bufferTookTime = bufferTookTime;
    }

    public void setCompletionTime(double completedTime) {
        this.completionTime = completedTime;
    }

    public Double getBufferTookTime() {
        return bufferTookTime;
    }

    public Double getCompletionTime() {
        return completionTime;
    }

    public int getBuyerNumber() {
        return buyerNumber;
    }


    @Override
    public String toString() {
        return serial + "";
    }
}
