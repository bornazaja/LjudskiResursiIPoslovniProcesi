package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IsplatnaListaPrekovremeniRadServiceImpl implements IsplatnaListaPrekovremeniRadService {

    @Autowired
    private IsplatnaListaPrekovremeniRadRepository isplatnaListaPrekovremeniRadRepository;

    @Override
    public List<IsplatnaListaPrekovremeniRadDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista) {
        List<IsplatnaListaPrekovremeniRad> isplatneListePrekovremeniRadovi = isplatnaListaPrekovremeniRadRepository.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);
        return ObjectMapperUtils.mapAll(isplatneListePrekovremeniRadovi, IsplatnaListaPrekovremeniRadDto.class);
    }
}
