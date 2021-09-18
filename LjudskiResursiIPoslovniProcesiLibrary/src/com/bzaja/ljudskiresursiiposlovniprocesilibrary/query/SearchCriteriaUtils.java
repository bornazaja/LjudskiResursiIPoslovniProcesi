package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import java.util.ArrayList;
import java.util.List;

public final class SearchCriteriaUtils {

    private SearchCriteriaUtils() {

    }

    public static List<SearchCriteriaDto> toSearchCriterias(Object value, SearchOperation searchOperation, String... columns) {
        List<SearchCriteriaDto> searchCriterias = new ArrayList<>();
        for (String column : columns) {
            searchCriterias.add(new SearchCriteriaDto(SearchCriteriaType.INCLUDE, column, searchOperation, value));
        }
        return searchCriterias;
    }
}
