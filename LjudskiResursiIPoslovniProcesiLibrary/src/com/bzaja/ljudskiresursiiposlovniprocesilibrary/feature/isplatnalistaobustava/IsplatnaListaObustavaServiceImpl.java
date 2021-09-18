package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IsplatnaListaObustavaServiceImpl implements IsplatnaListaObustavaService {

    @Autowired
    private IsplatnaListaObustavaRepository isplatnaListaObustavaRepository;

    @Override
    public List<IsplatnaListaObustavaDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista) {
        List<IsplatnaListaObustava> isplatneListeObustave = isplatnaListaObustavaRepository.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);
        return ObjectMapperUtils.mapAll(isplatneListeObustave, IsplatnaListaObustavaDto.class);
    }
}
