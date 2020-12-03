package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki;

import org.springframework.data.repository.CrudRepository;

public interface PodaciOTvrtkiRepository extends CrudRepository<PodaciOTvrtki, Integer> {

    PodaciOTvrtki findFirstByOrderByIdPodaciOTvrtkiAsc();

    Boolean existsByOib(String oib);

    Boolean existsByEmail(String email);
}
