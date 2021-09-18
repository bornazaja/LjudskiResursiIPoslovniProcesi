package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.config.ApplicationContextUtils;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

public final class BeanUtils {

    private BeanUtils() {

    }

    public static Object getPropertyValue(Object object, String propertyName, Object defaultValue) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        try {
            Object value = PropertyUtils.getNestedProperty(object, propertyName);
            return value != null ? value : defaultValue;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NestedNullException ex) {
            return defaultValue;
        }
    }

    public static Object getPropertyValue(Object object, String propertyName) {
        return getPropertyValue(object, propertyName, null);
    }

    public static String getFormattedPropertyValue(Object object, String propertyName, String defaultValue) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        try {
            int plusCharCount = org.apache.commons.lang3.StringUtils.countMatches(propertyName, '+');

            if (plusCharCount > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] propertyNames = propertyName.split("\\+");
                for (int i = 0; i < propertyNames.length; i++) {
                    if (i == propertyNames.length - 1) {
                        stringBuilder.append(getFormatedValue(object, propertyNames[i], defaultValue));
                    } else {
                        stringBuilder.append(getFormatedValue(object, propertyNames[i], defaultValue)).append(" ");
                    }
                }
                return stringBuilder.toString();
            } else {
                return getFormatedValue(object, propertyName, defaultValue);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NestedNullException ex) {
            return StringUtils.DEFAULT_EMPTY_VALUE;
        }
    }

    private static String getFormatedValue(Object object, String propertyName, String defaultValue) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException {
        Object value = PropertyUtils.getNestedProperty(object, propertyName);
        Class<?> type = PropertyUtils.getPropertyType(object, propertyName);

        if (value != null) {
            if (type.isAssignableFrom(String.class)) {
                return value.toString();
            } else if (type.isAssignableFrom(Integer.class)) {
                return value.toString();
            } else if (type.isAssignableFrom(Double.class)) {
                return NumberUtils.format(Double.parseDouble(value.toString()), NumberFormatPatterns.DECIMAL_HR);
            } else if (type.isAssignableFrom(Boolean.class)) {
                return BooleanUtils.toStringDaNe(Boolean.valueOf(value.toString()));
            } else if (type.isAssignableFrom(LocalDate.class)) {
                return LocalDateUtils.format(LocalDate.parse(value.toString()), LocalDatePattern.HR);
            } else if (type.isAssignableFrom(LocalDateTime.class)) {
                return LocalDateTimeUtils.format(LocalDateTime.parse(value.toString()), LocalDateTimePattern.HR);
            } else {
                throw new IllegalArgumentException("This type is not supported.");
            }
        } else {
            return defaultValue != null ? defaultValue : StringUtils.DEFAULT_EMPTY_VALUE;
        }
    }

    public static String getFormattedPropertyValue(Object object, String propertyName) {
        return getFormattedPropertyValue(object, propertyName, null);
    }

    public static boolean isPropertyTextType(String className, String propertyName, boolean useEntityPackage) {
        Class<?> propertyType = getPropertyTypeByClassAndPropertyName(className, propertyName, useEntityPackage);
        return propertyType.isAssignableFrom(String.class);
    }

    public static boolean isPropertyNumericType(String className, String propertyName, boolean useEntityPackage) {
        Class<?> propertyType = getPropertyTypeByClassAndPropertyName(className, propertyName, useEntityPackage);
        return propertyType.isAssignableFrom(Integer.class) || propertyType.isAssignableFrom(Double.class);
    }

    public static boolean isPropertyDateTimeType(String className, String propertyName, boolean useEnityPackage) {
        Class<?> propertyType = getPropertyTypeByClassAndPropertyName(className, propertyName, useEnityPackage);
        return propertyType.isAssignableFrom(LocalDate.class) || propertyType.isAssignableFrom(LocalDateTime.class);
    }

    public static boolean isPropertyBooleanType(String className, String propertyName, boolean useEntityPackage) {
        Class<?> propertyType = getPropertyTypeByClassAndPropertyName(className, propertyName, useEntityPackage);
        return propertyType.isAssignableFrom(Boolean.class);
    }

    private static Class<?> getPropertyTypeByClassAndPropertyName(String className, String propertyName, boolean useEntityPackage) {
        try {
            int count = org.apache.commons.lang3.StringUtils.countMatches(propertyName, ".");
            String[] parts = propertyName.split("\\.");

            if (useEntityPackage) {
                BeanManager beanManager = ApplicationContextUtils.getBean(BeanManager.class);
                className = beanManager.getFullEntityClassNameByClassBaseName(className);
            }

            Class<?> clazz = Class.forName(className);

            switch (count) {
                case 0:
                    return clazz.getDeclaredField(parts[0]).getType();
                case 1:
                    return clazz.getDeclaredField(parts[0]).getType().getDeclaredField(parts[1]).getType();
                case 2:
                    return clazz.getDeclaredField(parts[0]).getType().getDeclaredField(parts[1]).getType().getDeclaredField(parts[2]).getType();
                default:
                    throw new IllegalArgumentException("Cannot search more than 2 nested objects.");
            }
        } catch (NoSuchFieldException | SecurityException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
