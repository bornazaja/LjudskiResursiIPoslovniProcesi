package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace;

import org.springframework.data.repository.CrudRepository;

public interface ParametriZaObracunPlaceRepository extends CrudRepository<ParametriZaObracunPlace, Integer> {

    ParametriZaObracunPlace findFirstByOrderByIdParametriZaObracunPlaceAsc();
}
