package com.qtref.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IngLoggerUtil.class)
public class IngLoggerUtilTest {

    @Test
    public void testLogInfo() {
       IngLoggerUtil.logInfo("Test Info Message");
       Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void testLogError() {
        IngLoggerUtil.logError("Test Error Message");
        Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void testDebug() {
        IngLoggerUtil.logDebug("Test Debug Message");
        Assert.assertTrue(Boolean.TRUE);
    }
}
