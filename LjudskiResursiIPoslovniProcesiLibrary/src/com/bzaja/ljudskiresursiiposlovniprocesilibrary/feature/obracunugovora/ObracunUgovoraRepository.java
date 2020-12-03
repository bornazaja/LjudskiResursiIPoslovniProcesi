package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import org.springframework.data.repository.CrudRepository;

public interface ObracunUgovoraRepository extends CrudRepository<ObracunUgovora, Integer> {

    ObracunUgovora findByIdObracunUgovoraAndIsplatneListeUgovorZaposlenikIdZaposlenik(Integer idObracunUgovora, Integer idZaposlenik);
}
