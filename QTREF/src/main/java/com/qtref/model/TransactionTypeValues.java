package com.qtref.model;

/**
 * This is used for holding the Transaction amount value
 */
public class TransactionTypeValues {
    private String transactionType;
    private int totalNoOfTransactions;
    private double totalAmountForTransactionType;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getTotalNoOfTransactions() {
        return totalNoOfTransactions;
    }

    public void setTotalNoOfTransactions(int totalNoOfTransactions) {
        this.totalNoOfTransactions = totalNoOfTransactions;
    }

    public double getTotalAmountForTransactionType() {
        return totalAmountForTransactionType;
    }

    public void setTotalAmountForTransactionType(double totalAmountForTransactionType) {
        this.totalAmountForTransactionType = totalAmountForTransactionType;
    }
}
