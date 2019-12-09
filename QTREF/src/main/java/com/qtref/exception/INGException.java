package com.qtref.exception;

/**
 * This is the custom exception message will be received by the Controller layer
 */
public class INGException extends Exception {

    /**
     * Constructor
     * @param message  message to be logged.
     */
    public INGException(String message) {
        super(message);
    }

}
