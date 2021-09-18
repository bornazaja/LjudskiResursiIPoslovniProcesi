package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MapUtils {

    private MapUtils() {

    }

    public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Two list must have same size.");
        }

        LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            linkedHashMap.put(keys.get(i), values.get(i));
        }

        return linkedHashMap;
    }

    public static <E extends Enum<E>> Map<E, String> toEnumMap(Class<E> clazz) {
        return toEnumMap(clazz, clazz.getEnumConstants());
    }

    public static <E extends Enum<E>> Map<E, String> toEnumMap(E[] values) {
        return toEnumMap(values[0].getDeclaringClass(), values);
    }

    public static <E extends Enum<E>> Map<E, String> toEnumMap(List<E> values) {
        return toEnumMap(values.get(0).getDeclaringClass(), values.toArray((E[]) new Enum[0]));
    }

    private static <E extends Enum<E>> Map<E, String> toEnumMap(Class<E> clazz, E[] values) {
        if (Descriptable.class.isAssignableFrom(clazz)) {
            Map<E, String> map = new LinkedHashMap<>();
            for (E value : values) {
                Descriptable descriptable = (Descriptable) value;
                map.put(value, descriptable.getDescription());
            }
            return map;
        }

        throw new RuntimeException("This enum does not implement Descriptable interface.");
    }

    public static <TKey, TValue> Map<String, String> toStringString(Map<TKey, TValue> map) {
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString(), (x, y) -> x, LinkedHashMap::new));
    }
}
