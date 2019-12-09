package com.qtref.service;

import com.qtref.constants.Constants;
import com.qtref.exception.INGException;
import com.qtref.helper.EventManager;
import com.qtref.helper.JsonHelper;

import com.qtref.util.EHCacheManager;
import com.qtref.util.IngLoggerUtil;
import com.qtref.util.ObjectDebuggerUtil;
import com.qtref.model.TransactionData;
import com.qtref.service.ITransactionProxyService;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * This class is used as
 */
@Service
public class TransactionProxyServiceImpl implements ITransactionProxyService {

    private ReentrantLock lock = new ReentrantLock();
    private static final String className = TransactionProxyServiceImpl.class.getName();


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<TransactionData> getTransactionList() throws INGException {

        IngLoggerUtil.logInfo(String.format ( " :  %s invoking transaction service", className));

        String responseText = restTemplate.getForObject(Constants.URL_END_POINT, String.class);

        IngLoggerUtil.logInfo(String.format ( " : %s Transaction Service completed", className));

        //Fetches the List of Response Object
        List<TransactionData> transactionsVal = JsonHelper.getTransactionsListFromJSON(responseText, "transactions");

        //Changes the State for the given event
        EventManager.getInstance().setCurrentEvent(Constants.GET_TRANSACTION_LIST);

        //get the current and previous state from the Event Manager
        IngLoggerUtil.logInfo(String.format ( " : %s Current State : %s , Previous State %s",
                className,
                EventManager.getInstance().getCurrentState().toString(),
                (EventManager.getInstance().getPreviousState() != null)
                        ? EventManager.getInstance().getPreviousState().toString() : " "));

        changeRecordState(transactionsVal, EventManager.getInstance().getCurrentState(),
                EventManager.getInstance().getPreviousState());

        return transactionsVal;
    }

    @Override
    public List<TransactionData> getTransactionListForType(String transactionType) throws INGException {

        IngLoggerUtil.logInfo(String.format ( " : %s fetching transaction list from Cache  for %s type", className, transactionType));

        //Fetch the record from the Cache
        Cache<TransactionMetaData, TransactionData>  transactionsCache = EHCacheManager.getInstance().getTransactionsCache();
        List<TransactionData> transactionsValList = new ArrayList<>();

        //iterate cache key for the transaction type
        for (Cache.Entry<TransactionMetaData, TransactionData> transactionMetaDataTransactionDataEntry : transactionsCache) {

            TransactionData txnData = transactionMetaDataTransactionDataEntry.getValue();

            //Transaction Type contains double quotes remove them
            if( transactionType.equalsIgnoreCase(txnData.getTransactionType())) {
                transactionsValList.add(txnData);
            }
        }

        IngLoggerUtil.logInfo(String.format ( " : %s Number of Records fetched  from Cache %d ", className, transactionsValList.size()));
        return transactionsValList;
    }

    @Override
    public TransactionTypeValues getTransactionTotalAmountForType(String transactionType) throws INGException {
        IngLoggerUtil.logInfo(String.format ( " : %s fetching transaction list from Cache  for %s type", className, transactionType));

        Cache<TransactionMetaData, TransactionData>  transactionsCache = EHCacheManager.getInstance().getTransactionsCache();
        TransactionTypeValues transactionsTypeValue = new TransactionTypeValues();
        transactionsTypeValue.setTransactionType(transactionType);

        int numberOfTotalTransactions = 0;
        double transactionTypeAmountSum = 0.0d;

        //iterate the transaction record for type
        for (Cache.Entry<TransactionMetaData, TransactionData> transactionMetaDataTransactionDataEntry : transactionsCache) {

            TransactionData txnData = transactionMetaDataTransactionDataEntry.getValue();

            //fetch the matching records
            if( transactionType.equalsIgnoreCase(txnData.getTransactionType())) {
                numberOfTotalTransactions++;
                transactionTypeAmountSum += txnData.getTransactionAmount();
            }
        }

        transactionsTypeValue.setTotalAmountForTransactionType(transactionTypeAmountSum);
        transactionTypeAmountSum = Math.round(transactionTypeAmountSum * 100.0) / 100.0;
        transactionsTypeValue.setTotalNoOfTransactions(numberOfTotalTransactions);

        IngLoggerUtil.logInfo(String.format ( " %s Transaction amount %.2f ", className, transactionTypeAmountSum));
        return transactionsTypeValue;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        //creating the RestTemplate
        return builder.build();
    }

    /*
     * Persist the Data into the EHCache
     * using lock to make sure the Cache update is thread safe
     */
    private void changeRecordState(List<TransactionData>  transactionsList,
                                   StateEnum currentState, StateEnum previousState) {

        Cache<TransactionMetaData, TransactionData>  transactionsCache = EHCacheManager.getInstance().getTransactionsCache();

        for( TransactionData transactionData : transactionsList ) {
            TransactionMetaData transactionMetadata = new TransactionMetaData();
            transactionMetadata.setTransactionId(transactionData.getId());

            //check weather the key is already present or not
            if(transactionsCache.get(transactionMetadata) == null ) {
                transactionMetadata.setCurrentState(StateEnum.DRAFT);
                transactionMetadata.setPreviousState(null);
            }
            else {
                transactionMetadata.setCurrentState(currentState);
                transactionMetadata.setPreviousState(previousState);
            }

            //Lock the Cache as it is distributed
            lock.lock();
            try {
                transactionsCache.put(transactionMetadata, transactionData);
            }
            finally {
                lock.lock();
            }

        }

    }

    //used for unit test as bean instantiation exception
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /*
     * Group By Transaction Data by account ids
     */
    private Map<String, List<TransactionData>> filterTransactionData() throws INGException {

        List<TransactionData> tractionDataMapforAccountIdstransctionData = getTransactionListForType(TransactionTypes.TRANSFER.toString());
        Map<String, List<TransactionData>> transactionDataMapForAccountIds = tractionDataMapforAccountIdstransctionData.parallelStream().collect(
                Collectors.groupingBy(TransactionData::getAccountId));
        return transactionDataMapForAccountIds;
    }

    /*
     * Transaction having best credit score
     */
    private TransactionData bestTransactionData() throws INGException, IllegalAccessException {

        List<TransactionData> tractionDataMapforAccountIdstransctionData = getTransactionListForType(TransactionTypes.TRANSFER.toString());
        Optional<TransactionData> transactionDataMapForAccountIds =
                tractionDataMapforAccountIdstransctionData.parallelStream().reduce(
                        (a,b) -> a.getTxnScorePercentage() > b.getTxnScorePercentage()? a : b);

        transactionDataMapForAccountIds.orElseThrow(()-> new RuntimeException(("No txn found ")));

        TransactionData txnDataReturnValue = transactionDataMapForAccountIds.get();
        ObjectDebuggerUtil.getInstance().formatPrintableStringForObject(txnDataReturnValue);

        return txnDataReturnValue;
    }


    /*
     * Transaction have currency or not
     */
    private boolean hasTxnCurrency (String currency) throws INGException {

        List<TransactionData> tractionDataMapforAccountIdstransctionData = getTransactionListForType(TransactionTypes.TRANSFER.toString());
        Optional<TransactionData> transactionDataMapForAccountIds =
                tractionDataMapforAccountIdstransctionData.parallelStream().filter(
                        currrentTxnData -> currency.equals(currrentTxnData.getTransactionCurrency())).findFirst();

        transactionDataMapForAccountIds.orElseThrow(()-> new RuntimeException(("No txn found ")));
        return Objects.nonNull(transactionDataMapForAccountIds.get()) ? Boolean.TRUE : Boolean.FALSE;
    }


}
