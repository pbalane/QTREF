package com.qtref.helper;

import com.qtref.constants.Constants;
import com.qtref.model.StateEnum;

/**
 * This class sets the state of the current and previous transaction
 */
public class EventManager {

    private StateEnum previousState = null;
    private StateEnum currentState = StateEnum.UN_ASSIGNED;

    private static EventManager instance;

    /**
     * This is method provides instance of this class
     * @return EventManager
     */
    public static EventManager getInstance() {

        if(instance == null) {
            synchronized (EventManager.class) {

                if(instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    private EventManager() {

    }

    /**
     * This method sets the current State for the given event type.
     * @param eventName Event name
     */
    public void setCurrentEvent(String eventName) {

        if(Constants.GET_TRANSACTION_LIST.equals(eventName)) {
            setCurrentState();
        }
    }

    private void setCurrentState() {
        switch(currentState) {
            case DRAFT:
                previousState = StateEnum.DRAFT;
                currentState =  StateEnum.PREPARED;
                break;

            case PREPARED:
                previousState = StateEnum.PREPARED;
                currentState =  StateEnum.CHECKED;
                break;

            case CHECKED:
                previousState = StateEnum.CHECKED;
                currentState =  StateEnum.COMPLTETED;
                break;

            default:
               currentState =  StateEnum.DRAFT;
        }
    }

    /**
     * This method returns the Previous State
     * @return previousState  previous transaction record state
     */
    public StateEnum getPreviousState() {
        return previousState;
    }

    /**
     * This method returns the Previous State
     * @return currentState  current transaction record state
     */
    public StateEnum getCurrentState() {
        return currentState;
    }

    public void resetStates() {
        previousState = null;
        currentState = StateEnum.UN_ASSIGNED;
    }

}
