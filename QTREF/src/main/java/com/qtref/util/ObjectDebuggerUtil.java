package com.qtref.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ObjectDebuggerUtil {

    private static ObjectDebuggerUtil instance;

    private ObjectDebuggerUtil(){}

    /**
     * This is single ton pattern instance
     * one instance is used in this application
     *
     * @return ObjectDebuggerUtil instance
     */
    public static ObjectDebuggerUtil getInstance() {

        if(instance == null) {
            synchronized(ObjectDebuggerUtil.class) {
                if(instance == null) {
                    instance = new ObjectDebuggerUtil();
                }
            }
        }
        return instance;
    }

    /**
     * This class iterates classes and super classes of the source objects
     * and fetches the attributes and values using java reflection API.
     *
     * @param sourceObject printable Object
     * @return printable format of attributes and values.
     * @throws IllegalAccessException
     */
    public String formatPrintableStringForObject(Object sourceObject) throws IllegalAccessException{

        StringBuilder strBuilder = new StringBuilder();
        if (Objects.nonNull(sourceObject)) {

            List<Class> classesList = Collections.emptyList();
            fetchAllSuperClasses(sourceObject.getClass(), classesList);


                classesList.stream().forEach(classRef -> {
                    try{
                        getPrintableFormatForObject(classRef, sourceObject,strBuilder);
                    }
                    catch (IllegalAccessException exp) {
                      IngLoggerUtil.logError(exp.getMessage());
                    }
                    catch (Exception exp) {
                        throw exp;
                    }
                });

        }

        return strBuilder.toString();
    }

    private void getPrintableFormatForObject(Class classRef, Object sourceObject, StringBuilder strBuilder)
            throws IllegalAccessException {

        if (Objects.nonNull(sourceObject) && classRef.isLocalClass()) {

            Arrays.stream(classRef.getDeclaredFields()).
                    forEach(fieldObject ->
                    {
                        try {
                            fieldObject.setAccessible(Boolean.TRUE);

                            //if() {
                            if(checkPrimitive(fieldObject.getType())) {

                                //append field name and value
                                strBuilder.append(String.format("[ %s : %s]",
                                        fieldObject.getName(),
                                        String.valueOf( fieldObject.get(sourceObject))));
                            }
                            else {
                                //iterate for one more time
                                getPrintableFormatForObject(
                                        fieldObject.getType(),
                                        fieldObject.get(sourceObject),
                                        strBuilder);
                            }

                        }
                        catch(IllegalAccessException exception) {
                            //consume the exception no need for rethrowing it
                            IngLoggerUtil.logError(exception.getMessage());
                        }
                        catch(Exception exception) {
                            //consume the exception no need for rethrowing it
                            IngLoggerUtil.logError(" Exception occured at Reflection API usage");
                            throw exception;
                        }

                    });

        }
    }

    //fetch all class for the given Object
    private void fetchAllSuperClasses(Class classType, List<Class> classesList ) {

        if(Objects.nonNull(classType)) {
            classesList.add(classType);
            fetchAllSuperClasses(classType.getClass().getSuperclass(), classesList);
        }
    }

    // check the class in primitive type otherwise iterate one more time
    private static boolean checkPrimitive(Class<?> classType) {
        return (classType.isPrimitive() && classType != void.class) ||
                classType == Double.class || classType == Float.class || classType == Long.class ||
                classType == Integer.class || classType == Short.class || classType == Character.class ||
                classType == Byte.class || classType == Boolean.class || classType == String.class;
    }

}
