package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import java.util.List;

public interface ObracunUgovoraService {

    void save(ObracunUgovoraDto obracunUgovoraDto);

    void delete(Integer id);

    ObracunUgovoraDto findById(Integer id);

    List<ObracunUgovoraDto> findAll();

    ObracunUgovoraDto findByIdAndIsplatneListeUgovorZaposlenikIdZaposlenik(Integer idObracunUgovora, Integer idZaposlenik);
    
    Long count();
}
