package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IsplatnaListaDodatakServiceImpl implements IsplatnaListaDodatakService {

    @Autowired
    private IsplatnaListaDodatakRepository isplatnaListaDodatakRepository;

    @Override
    public List<IsplatnaListaDodatakDto> findAllByIsplatnaListaIdIsplatnaLista(Integer idIsplatnaLista) {
        List<IsplatnaListaDodatak> isplatneListeDodatci = isplatnaListaDodatakRepository.findAllByIsplatnaListaIdIsplatnaLista(idIsplatnaLista);
        return ObjectMapperUtils.mapAll(isplatneListeDodatci, IsplatnaListaDodatakDto.class);
    }
}
