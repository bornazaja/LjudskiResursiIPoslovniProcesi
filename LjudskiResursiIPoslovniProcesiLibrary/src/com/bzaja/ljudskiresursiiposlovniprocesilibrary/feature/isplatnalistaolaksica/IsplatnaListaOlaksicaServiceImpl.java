package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IsplatnaListaOlaksicaServiceImpl implements IsplatnaListaOlaksicaService {

    @Autowired
    private IsplatnaListaOlaksicaRepository isplatnaListaOlaksicaRepository;

    @Override
    public List<IsplatnaListaOlaksicaDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista) {
        List<IsplatnaListaOlaksica> isplatneListeOlaksice = isplatnaListaOlaksicaRepository.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);
        return ObjectMapperUtils.mapAll(isplatneListeOlaksice, IsplatnaListaOlaksicaDto.class);
    }
}
