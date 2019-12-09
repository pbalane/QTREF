package com.qtref.util;

import com.qtref.constants.Constants;
import com.qtref.helper.EventManager;
import com.qtref.model.StateEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventManager.class)
public class EventManagerTest {

    @Test
    public void testGetInstance() {
        Assert.assertTrue( (EventManager.getInstance() instanceof  EventManager));
    }

    @Test
    public void testSetCurrentEvent() {
        EventManager.getInstance().resetStates();
        EventManager.getInstance().setCurrentEvent(Constants.GET_TRANSACTION_LIST);
        Assert.assertNull (EventManager.getInstance().getPreviousState());
        Assert.assertNotNull (EventManager.getInstance().getCurrentState());
    }

    @Test
    public void testGetCurrentState() {
        EventManager.getInstance().resetStates();
        EventManager.getInstance().setCurrentEvent(Constants.GET_TRANSACTION_LIST);
        EventManager.getInstance().setCurrentEvent(Constants.GET_TRANSACTION_LIST);
        Assert.assertTrue (StateEnum.DRAFT == EventManager.getInstance().getPreviousState());
        Assert.assertTrue (StateEnum.PREPARED == EventManager.getInstance().getCurrentState());
    }

}
