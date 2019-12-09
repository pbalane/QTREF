package com.qtref.model;

/**
 * This is used as key for strong Transaction Data
 */
public class TransactionMetaData {
    private String transactionId;
    private StateEnum currentState;
    private StateEnum previousState;

    public int hashCode() {
        return transactionId.hashCode();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public StateEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(StateEnum currentState) {
        this.currentState = currentState;
    }

    public StateEnum getPreviousState() {
        return previousState;
    }

    public void setPreviousState(StateEnum previousState) {
        this.previousState = previousState;
    }
}
