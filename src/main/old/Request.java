package ru.scheredin.SMO.old;

import java.util.concurrent.CompletableFuture;

public final class Request implements Comparable<Request> {
    private int buyerNumber;
    private int serialNumber;
    private Long creationDate;
    private Long bufferInsertedTime;
    private Long bufferTookTime;
    private Long completedTime;
    //private State state = new CompletableFuture<>();

    public Request(int buyerNumber, int serialNumber) {
        this.buyerNumber = buyerNumber;
        this.serialNumber = serialNumber;
    }

    public Request(int buyerNumber, int serialNumber, long bufferInsertedTime) {
        this.buyerNumber = buyerNumber;
        this.serialNumber = serialNumber;
        this.bufferInsertedTime = bufferInsertedTime;
    }

    @Override
    public int compareTo(Request another) {
        return bufferInsertedTime.compareTo(another.getBufferInsertedTime());
    }

    public void setBuyerNumber(int buyerNumber) {
        this.buyerNumber = buyerNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setBufferInsertedTime(Long bufferInsertedTime)  {
        if (this.bufferInsertedTime != null) {
            throw new RuntimeException("BufferInsertedTime is immutable");
        }
        this.bufferInsertedTime = bufferInsertedTime;
    }

    public void setBufferTookTime(Long bufferTookTime)  {
        if (bufferTookTime == null) {
            throw new RuntimeException("BufferInsertedTime is immutable");
        }
        this.bufferTookTime = bufferTookTime;
    }

    public void setCompletedTime(Long completedTime)  {
        if (completedTime == null) {
            throw new RuntimeException("BufferInsertedTime is immutable");
        }
        this.completedTime = completedTime;
    }

    public int getBuyerNumber() {
        return buyerNumber;
    }

    public Request(int buyerNumber) {
        this.buyerNumber = buyerNumber;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public Long getBufferInsertedTime() {
        return bufferInsertedTime;
    }

    public Long getBufferTookTime() {
        return bufferTookTime;
    }

    public Long getCompletedTime() {
        return completedTime;
    }

    public CompletableFuture<Response> getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response.complete(response);
    }
}
