package com.qtref.service;

import com.qtref.exception.INGException;
import com.qtref.model.TransactionData;
import com.qtref.model.TransactionTypeValues;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITransactionProxyService {

    /**
     * Fetches the list of Transaction retrieved from Banking Service
     *
     * @return list of Transaction Data
     * @throws INGException
     */
    List<TransactionData> getTransactionList() throws INGException;

    /**
     * Fetches the list of Transaction for the given transaction type from Banking Service
     *
     * @param transactionType
     * @return list of Transaction Data
     * @throws INGException
     */
    List<TransactionData> getTransactionListForType(String transactionType) throws INGException;

    /**
     * Calculates the transaction amount for the given transaction type
     *
     * @param transactionType
     * @return Transaction value for type
     * @throws INGException
     */
    TransactionTypeValues getTransactionTotalAmountForType(String transactionType) throws INGException;


}
