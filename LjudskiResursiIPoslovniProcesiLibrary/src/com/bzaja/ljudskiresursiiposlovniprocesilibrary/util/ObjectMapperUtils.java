package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public final class ObjectMapperUtils {

    private static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ObjectMapperUtils() {
    }

    public static <D, T> D map(final T entity, Class<D> outClass) {
        return entity != null ? modelMapper.map(entity, outClass) : null;
    }

    public static <D, T> List<D> mapAll(final List<T> entityList, Class<D> outCLass) {
        return entityList != null ? entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList()) : null;
    }
}
