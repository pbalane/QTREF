package com.qtref.controller;


import com.qtref.model.TransactionData;
import com.qtref.model.TransactionTypeValues;
import com.qtref.service.ITransactionProxyService;
import com.qtref.util.IngLoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class exposes end points for
 * 1) Getting Transaction List
 * 2) Getting Transaction List for the type
 * 3) Getting the transaction amount for the given transaction type.
 *
 */
@RestController
public class TransactionController {

    private static final String className = TransactionController.class.getName();

    @Autowired
    private ITransactionProxyService transactionProxyServiceImpl;

    /**
     * This service fetches the transaction list.
     * @return  List of TransactionData in the JSON format
     */
    @GetMapping(value = "/getTransactionList", produces = {"application/json"})
    public List<TransactionData> getTransactionList() {

        List<TransactionData> transactions = null;

        try {
            IngLoggerUtil.logInfo(String.format(" %s getTransactionList is invoked", className) );
            transactions = transactionProxyServiceImpl.getTransactionList();
            IngLoggerUtil.logInfo(String.format(" : %s getTransactionList is invoked  number of transactions returned : %d",
                    className , transactions.size()));
        }
        catch(Exception exception) {
            IngLoggerUtil.logError(String.format(" %s Error Occurred  %s ", className, exception.getMessage()));
        }

        return transactions;
    }

    /**
     * This method fetches the list of Transaction for the given transaction type
     * @param transactionType  Transaction type
     * @return List of TransactionData in the JSON format
     */
    @GetMapping(value = "/getTransactionListForType", produces = {"application/json"})
    public List<TransactionData> getTransactionListForType(@RequestParam(required = true) String transactionType) {

        List<TransactionData> transactionsList = null;

        try {
            IngLoggerUtil.logInfo(String.format(" : %s getTransactionListForType is invoked", className) );
            transactionsList = transactionProxyServiceImpl.getTransactionListForType(transactionType);
            IngLoggerUtil.logInfo(String.format(" : %s getTransactionListForType is invoked  number of transactions returned : %d",
                    className , transactionsList.size()));
        }
        catch(Exception exception) {
            IngLoggerUtil.logError(String.format(" : %s  Error Occurred  %s ", className, exception.getMessage()));
        }

        return transactionsList;
    }

    /**
     * This method fetches the transaction amount for the given transaction type.
     * @param transactionType Transaction type
     * @return TransactionTypeValues
     */
    @GetMapping(value = "/getTransactionValueForType", produces = {"application/json"})
    public TransactionTypeValues getTransactionValueForType(@RequestParam(required = true) String transactionType) {

        TransactionTypeValues transactionTypeValues = null;

        try {
            IngLoggerUtil.logInfo(String.format(" : %s getTransactionListForType is invoked", className) );
            transactionTypeValues = transactionProxyServiceImpl.getTransactionTotalAmountForType(transactionType);
            IngLoggerUtil.logInfo(String.format(" : %s getTransactionTotalAmountForType is invoked  total amount : %.2f",
                    className ,transactionTypeValues.getTotalAmountForTransactionType()));
        }
        catch(Exception exception) {
            IngLoggerUtil.logError(String.format(" : %s  Error Occurred  %s ", className, exception.getMessage()));
        }

        return transactionTypeValues;
    }

}
