package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IsplatnaListaDavanjeServiceImpl implements IsplatnaListaDavanjeService {

    @Autowired
    private IsplatnaListaDavanjeRepository isplatnaListaDavanjeRepository;

    @Override
    public List<IsplatnaListaDavanjeDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista) {
        List<IsplatnaListaDavanje> isplatneListeDavanja = isplatnaListaDavanjeRepository.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);
        return ObjectMapperUtils.mapAll(isplatneListeDavanja, IsplatnaListaDavanjeDto.class);
    }
}
