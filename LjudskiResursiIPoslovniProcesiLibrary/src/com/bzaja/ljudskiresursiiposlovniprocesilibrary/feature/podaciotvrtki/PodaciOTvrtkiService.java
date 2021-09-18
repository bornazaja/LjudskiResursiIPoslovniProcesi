package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki;

public interface PodaciOTvrtkiService {

    void save(PodaciOTvrtkiDto podaciOTvrtkiDto);

    PodaciOTvrtkiDto findFirst();
}
