package com.qtref.util;

import com.qtref.model.TransactionData;
import com.qtref.model.TransactionMetaData;
import org.ehcache.Cache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EHCacheManager.class)
public class EHCacheManagerTest {

    @Test
    public void testGetInstance() {
        Assert.assertNotNull(EHCacheManager.getInstance());
    }

    @Test
    public void testGetTransactionsCache() {
        Cache<TransactionMetaData, TransactionData>  txnCache = EHCacheManager.getInstance().getTransactionsCache();
        Assert.assertNotNull(txnCache);
    }

    @Test
    public void testPutEntriesIntoTheCache() {
        Cache<TransactionMetaData, TransactionData>  txnCache = EHCacheManager.getInstance().getTransactionsCache();
        TransactionData txnData = new TransactionData();
        TransactionMetaData txnMetaData = new TransactionMetaData();
        txnMetaData.setTransactionId("txnId");
        txnCache.put(txnMetaData, txnData);
        Assert.assertNotNull(txnCache.get(txnMetaData));
    }
}
