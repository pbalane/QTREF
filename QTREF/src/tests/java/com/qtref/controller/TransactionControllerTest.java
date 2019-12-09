package com.qtref.controller;

import com.qtref.model.TransactionData;
import com.qtref.model.TransactionTypeValues;
import com.qtref.service.ITransactionProxyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITransactionProxyService iTransactionProxyService;

    @Before
    public void init() {

        TransactionData txnData = new TransactionData();
        txnData.setId("id_1");
        txnData.setTransactionAmount(20);
        List<TransactionData> txnList = new ArrayList();
        txnList.add(txnData);

        try {

            when(iTransactionProxyService.getTransactionList()).thenReturn(txnList);
        }
        catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    @Test
    public void testTransactionController() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/transactionList")).andReturn();
        Assert.assertNotNull(mvcResult);
    }


    @Test
    public void testTransactionForTypeController() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/getTransactionListForType?transactionType=sand-box")).andReturn();
        Assert.assertNotNull(mvcResult);
    }

    @Test
    public void testTransactionAmountFilterController() throws Exception {

        TransactionTypeValues transactionTypeValues = new TransactionTypeValues();

        try {

            when(iTransactionProxyService.getTransactionTotalAmountForType("sand-box")).thenReturn(transactionTypeValues);
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        MvcResult mvcResult = mockMvc.perform(get("/getTransactionValueForType?transactionType=sand-box")).andReturn();
        Assert.assertNotNull(mvcResult);
    }

}
