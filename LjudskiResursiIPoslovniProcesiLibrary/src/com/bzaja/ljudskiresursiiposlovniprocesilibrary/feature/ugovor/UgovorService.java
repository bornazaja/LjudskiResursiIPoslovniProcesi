package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor;

import java.util.List;

public interface UgovorService {

    UgovorDto findById(Integer id);

    List<UgovorDto> findAll();
    
    Long countByZaposlenikId(Integer idZaposlenik);
    
    Double getPlacaByZaposlenikId(Integer idZaposlenik);
}
