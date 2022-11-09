package ru.scheredin.SMO.components;

public final class Request implements Comparable<Request> {
    private int buyerNumber;
    private int serial;

    private Double bufferInsertedTime;
    private Double bufferTookTime;
    private Double completedTime;


    public Request(int buyerNumber, int serial) {
        this.buyerNumber = buyerNumber;
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
        this.completedTime = completedTime;
    }

    public Double getBufferTookTime() {
        return bufferTookTime;
    }

    public Double getCompletionTime() {
        return completedTime;
    }

    public int getBuyer() {
        return buyerNumber;
    }

    @Override
    public String toString() {
        return serial + "";
    }
}
