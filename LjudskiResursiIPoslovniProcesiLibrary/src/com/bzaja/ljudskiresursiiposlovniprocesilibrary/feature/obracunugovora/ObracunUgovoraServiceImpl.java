package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadavanje.IsplatnaListaDavanjeRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistadodatak.IsplatnaListaDodatakRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaobustava.IsplatnaListaObustavaRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaolaksica.IsplatnaListaOlaksicaRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalistaprekovremenirad.IsplatnaListaPrekovremeniRadRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ObracunUgovoraServiceImpl implements ObracunUgovoraService {

    @Autowired
    private ObracunUgovoraRepository obracunPlacaRepository;

    @Autowired
    private IsplatnaListaRepository isplatnaListaRepository;

    @Autowired
    private IsplatnaListaDavanjeRepository isplatnaListaDavanjeRepository;

    @Autowired
    private IsplatnaListaDodatakRepository isplatnaListaDodatakRepository;

    @Autowired
    private IsplatnaListaObustavaRepository isplatnaListaObustavaRepository;

    @Autowired
    private IsplatnaListaOlaksicaRepository isplatnaListaOlaksicaRepository;

    @Autowired
    private IsplatnaListaPrekovremeniRadRepository isplatnaListaPrekovremeniRadRepository;

    @Override
    public void save(ObracunUgovoraDto obracunPlacaDto) {

    }

    @Override
    public void delete(Integer id) {
        obracunPlacaRepository.deleteById(id);
    }

    @Override
    public ObracunUgovoraDto findById(Integer id) {
        ObracunUgovora obracunPlaca = obracunPlacaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(obracunPlaca, ObracunUgovoraDto.class);
    }

    @Override
    public List<ObracunUgovoraDto> findAll() {
        List<ObracunUgovora> obracuniPlaca = (List<ObracunUgovora>) obracunPlacaRepository.findAll();
        return ObjectMapperUtils.mapAll(obracuniPlaca, ObracunUgovoraDto.class);
    }

    @Override
    public ObracunUgovoraDto findByIdAndIsplatneListeUgovorZaposlenikIdZaposlenik(Integer idObracunUgovora, Integer idZaposlenik) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long count() {
        return obracunPlacaRepository.count();
    }
}
