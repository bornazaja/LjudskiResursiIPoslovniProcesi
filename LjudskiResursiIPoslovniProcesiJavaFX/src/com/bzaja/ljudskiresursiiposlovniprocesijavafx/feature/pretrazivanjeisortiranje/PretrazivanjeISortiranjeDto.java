package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.pretrazivanjeisortiranje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PretrazivanjeISortiranjeDto {

    private String className;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteria;
}
