package com.qtref.service;

import com.qtref.model.TransactionData;
import com.qtref.model.TransactionTypeValues;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionProxyServiceImplTest.class)
public class TransactionProxyServiceImplTest {

    private TransactionProxyServiceImpl iTransactionProxyService = null;

    @Before
    public void init() {
        //Add the constructor in the impl class to pass this test
        iTransactionProxyService = new TransactionProxyServiceImpl();
        iTransactionProxyService.setRestTemplate(new RestTemplate());
    }

    @Test
    public void testGetTransactionList() throws Exception {
        List<TransactionData> txnList = iTransactionProxyService.getTransactionList();
        Assert.assertTrue(txnList.size() > 0);
    }

    @Test
    public void testGetTransactionListForType() throws Exception {
        iTransactionProxyService.getTransactionList();
        List<TransactionData> txnList = iTransactionProxyService.getTransactionListForType("sandbox-payment");
        Assert.assertTrue(txnList.size() > 0);
    }

    @Test
    public void testGetTransactionAmountForType() throws Exception {
        iTransactionProxyService.getTransactionList();
        TransactionTypeValues values = iTransactionProxyService.getTransactionTotalAmountForType ("sandbox-payment");
        Assert.assertTrue(values.getTransactionType().equalsIgnoreCase("sandbox-payment"));
    }

}
