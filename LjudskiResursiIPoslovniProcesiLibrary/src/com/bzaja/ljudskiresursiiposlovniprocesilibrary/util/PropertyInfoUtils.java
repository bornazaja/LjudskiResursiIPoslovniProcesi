package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PropertyInfoUtils {

    private PropertyInfoUtils() {

    }

    public static List<PropertyInfoDto> toPropertiesInfo(List<String> displayNames, List<String> propertyNames) {
        Map<String, String> map = MapUtils.zipToMap(displayNames, propertyNames);
        List<PropertyInfoDto> propertiesInfo = new ArrayList<>();

        map.entrySet().forEach((entry) -> {
            propertiesInfo.add(new PropertyInfoDto(entry.getKey(), entry.getValue()));
        });

        return propertiesInfo;
    }
}
