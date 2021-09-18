package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {

    private ListUtils() {

    }

    public static <T> List<T> concate(List<T>... lists) {
        List<T> listResult = new ArrayList<>();
        for (List<T> list : lists) {
            listResult.addAll(list);
        }
        return listResult;
    }
}
