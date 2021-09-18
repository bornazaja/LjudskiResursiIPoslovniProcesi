package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.api.hnb.HNBTecajnaListaAPI;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.api.hnb.TecajnaListaItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ValutaServiceImpl implements ValutaService {

    @Autowired
    private ValutaRepository valutaRepository;

    @Override
    public ValutaDto findById(Integer id) {
        Valuta valuta = valutaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(valuta, ValutaDto.class);
    }

    @Override
    public List<ValutaDto> findAll() {
        tryUpdate();
        List<Valuta> valute = (List<Valuta>) valutaRepository.findAll();
        return ObjectMapperUtils.mapAll(valute, ValutaDto.class);
    }

    private void tryUpdate() {
        if (valutaRepository.count() != valutaRepository.countByDatumTecaja(LocalDate.now())) {
            List<TecajnaListaItemDto> tecajnaListaItems = HNBTecajnaListaAPI.get();

            tecajnaListaItems.forEach((tecajnaListaItem) -> {
                Valuta trazenaValuta = valutaRepository.findByNaziv(tecajnaListaItem.getValuta());
                if (trazenaValuta != null) {
                    trazenaValuta.setSrednjiTecaj(tecajnaListaItem.getSrednjiZaDevize());
                    trazenaValuta.setDatumTecaja(tecajnaListaItem.getDatumPrimjene());
                    valutaRepository.save(trazenaValuta);
                }
            });

            Valuta domacaValuta = valutaRepository.findByDrzaveJeDomovinaTrue();
            domacaValuta.setDatumTecaja(tecajnaListaItems.get(0).getDatumPrimjene());
            valutaRepository.save(domacaValuta);
        }
    }

    @Override
    public ValutaDto findByDrzaveJeDomovinaTrue() {
        Valuta valuta = valutaRepository.findByDrzaveJeDomovinaTrue();
        return ObjectMapperUtils.map(valuta, ValutaDto.class);
    }

    @Override
    public List<ValutaDto> findAll(String text, String... columns) {
        List<Valuta> valute = valutaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(valute, ValutaDto.class);
    }
}
