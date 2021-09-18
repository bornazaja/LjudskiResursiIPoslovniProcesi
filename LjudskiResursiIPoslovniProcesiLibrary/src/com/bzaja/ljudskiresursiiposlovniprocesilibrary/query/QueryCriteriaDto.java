package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QueryCriteriaDto {

    private Operator operator;
    private List<SearchCriteriaDto> searchCriterias;
    private Pageable pageable;
}
