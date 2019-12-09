package com.qtref.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is common logger class used and this follows Single ton pattern
 * The log information is displayed in the console at the moment.
 */
public class IngLoggerUtil {

    private static final Logger logger = LoggerFactory.getLogger(" Ing Transaction ");

    /**
     * This method used for logging info message
     * @param message message
     */
    public static void logInfo( String message) {
        logger.info(message);

    }

    /**
     * This method used for logging error message
     * @param message message
     */
    public static void logError( String message) {
        logger.error(message);
    }

    /**
     * This method used for logging debug message
     * @param message message
     */
    public static void logDebug( String message) {

       if( logger.isDebugEnabled() ) {
           logger.debug(message);
       }
    }

}
