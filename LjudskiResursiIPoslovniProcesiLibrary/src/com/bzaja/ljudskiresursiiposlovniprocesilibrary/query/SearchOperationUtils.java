/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Borna
 */
public final class SearchOperationUtils {

    private static final List<SearchOperation> TEXT_SEARCH_OPERATIONS = Arrays.asList(SearchOperation.EQUAL, SearchOperation.STARTS_WITH, SearchOperation.CONTAINS, SearchOperation.ENDS_WITH);
    private static final List<SearchOperation> NUMERIC_AND_DATETIME_SEARCH_OPERATIONS = Arrays.asList(SearchOperation.LESS_THAN, SearchOperation.LESS_THAN_OR_EQUAL_TO, SearchOperation.EQUAL, SearchOperation.GREATER_THEN, SearchOperation.GREATER_THEN_OR_EQUAL_TO);
    private static final List<SearchOperation> BOOLEAN_SEARCH_OPERATIONS = Arrays.asList(SearchOperation.EQUAL);

    private SearchOperationUtils() {

    }

    public static List<SearchOperation> getSearchOperationsByClassAndPropertyName(String className, String propertyName) {
        if (BeanUtils.isPropertyTextType(className, propertyName, true)) {
            return TEXT_SEARCH_OPERATIONS;
        } else if (BeanUtils.isPropertyNumericType(className, propertyName, true) || BeanUtils.isPropertyDateTimeType(className, propertyName, true)) {
            return NUMERIC_AND_DATETIME_SEARCH_OPERATIONS;
        } else if (BeanUtils.isPropertyBooleanType(className, propertyName, true)) {
            return BOOLEAN_SEARCH_OPERATIONS;
        } else {
            throw new RuntimeException("This type of operations does not exists.");
        }
    }
}
