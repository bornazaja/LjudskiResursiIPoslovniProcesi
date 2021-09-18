package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

public interface ObracunUgovoraKalkulator<T> {

    T getResultByIdObracunUgovora(Integer idObracunUgovora);

    T getResultByIdIsplatnaLista(Integer idIsplatnaLista);
}
