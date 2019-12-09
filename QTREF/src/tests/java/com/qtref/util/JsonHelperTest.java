package com.qtref.util;

import com.qtref.constants.Constants;
import com.qtref.helper.JsonHelper;
import com.qtref.model.TransactionData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JsonHelper.class)
public class JsonHelperTest {


    private RestTemplate restTemplate;

    @Before
    public void init() throws Exception {
        restTemplate = new RestTemplate();
    }

    @Test
    public void testGetTransactionsListFromJSON() throws Exception {

        try {
            String responseText = restTemplate.getForObject(Constants.URL_END_POINT, String.class);
            List<TransactionData> txnData = JsonHelper.getTransactionsListFromJSON(responseText, "transactions" );
            Assert.assertTrue(txnData.size() > 0);
            Assert.assertTrue(txnData.get(0).getId().length() > 0);
        }
        catch(Exception exp) {
         throw exp;
        }
    }

}
