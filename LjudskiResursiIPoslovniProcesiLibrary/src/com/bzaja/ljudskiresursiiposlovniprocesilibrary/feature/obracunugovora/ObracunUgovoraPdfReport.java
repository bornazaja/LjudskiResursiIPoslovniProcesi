package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

public interface ObracunUgovoraPdfReport {

    void createByIdObracunUgovora(Integer idObracunUgovora, String path);

    void createByIdIsplatnaLista(Integer idIsplatnaLista, String path);
}
