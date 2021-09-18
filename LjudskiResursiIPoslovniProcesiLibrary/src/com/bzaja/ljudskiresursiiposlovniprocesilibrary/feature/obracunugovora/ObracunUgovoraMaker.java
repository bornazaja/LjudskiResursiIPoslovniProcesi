package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

public interface ObracunUgovoraMaker<TModel, TDto> {

    TModel make(TDto obracunUgovora);
}
