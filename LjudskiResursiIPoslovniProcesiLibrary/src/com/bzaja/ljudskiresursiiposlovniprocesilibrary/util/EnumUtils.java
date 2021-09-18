package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class EnumUtils {

    private EnumUtils() {

    }

    public static <T> T fromValue(Object value, T[] array, String methodName) {
        for (T t : array) {
            try {
                Method method = t.getClass().getMethod(methodName);
                Object returnValue = method.invoke(t);
                if (returnValue.equals(value)) {
                    return t;
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return null;
    }
}
