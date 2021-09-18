package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchCriteriaDto {

    private SearchCriteriaType searchCriteriaType;
    private String column;
    private SearchOperation searchOperation;
    private Object value;
}
