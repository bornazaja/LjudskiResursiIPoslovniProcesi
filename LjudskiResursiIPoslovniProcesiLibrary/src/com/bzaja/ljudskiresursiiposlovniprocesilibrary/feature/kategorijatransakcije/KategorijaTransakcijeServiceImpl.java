package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KategorijaTransakcijeServiceImpl implements KategorijaTransakcijeService {

    @Autowired
    private KategorijaTransakcijeRepository kategorijaTransakcijaRepository;

    @Override
    public void save(KategorijaTransakcijeDto kategorijaTransakcijaDto) {
        KategorijaTransakcije kategorijaTransakcija = ObjectMapperUtils.map(kategorijaTransakcijaDto, KategorijaTransakcije.class);
        kategorijaTransakcijaRepository.save(kategorijaTransakcija);
    }

    @Override
    public void delete(Integer id) {
        kategorijaTransakcijaRepository.deleteById(id);
    }

    @Override
    public KategorijaTransakcijeDto findById(Integer id) {
        KategorijaTransakcije kategorijaTransakcija = kategorijaTransakcijaRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(kategorijaTransakcija, KategorijaTransakcijeDto.class);
    }

    @Override
    public List<KategorijaTransakcijeDto> findAll() {
        List<KategorijaTransakcije> kategorijeTransakcija = (List<KategorijaTransakcije>) kategorijaTransakcijaRepository.findAll();
        return ObjectMapperUtils.mapAll(kategorijeTransakcija, KategorijaTransakcijeDto.class);
    }

    @Override
    public Page<KategorijaTransakcijeDto> findAll(Pageable pageable) {
        Page<KategorijaTransakcije> kategorijaTransakcijePage = kategorijaTransakcijaRepository.findAll(pageable);
        List<KategorijaTransakcijeDto> kategorijeTranskacija = ObjectMapperUtils.mapAll(kategorijaTransakcijePage.getContent(), KategorijaTransakcijeDto.class);
        return new PageImpl(kategorijeTranskacija, pageable, kategorijaTransakcijePage.getTotalElements());
    }

    @Override
    public Page<KategorijaTransakcijeDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<KategorijaTransakcije> kategorijaTransakcijePage = kategorijaTransakcijaRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<KategorijaTransakcijeDto> kategorijeTransakcija = ObjectMapperUtils.mapAll(kategorijaTransakcijePage.getContent(), KategorijaTransakcijeDto.class);
        return new PageImpl(kategorijeTransakcija, queryCriteriaDto.getPageable(), kategorijaTransakcijePage.getTotalElements());
    }

    @Override
    public List<KategorijaTransakcijeDto> findAll(String text, String... columns) {
        List<KategorijaTransakcije> kategorijeTransakcija = kategorijaTransakcijaRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(kategorijeTransakcija, KategorijaTransakcijeDto.class);
    }
}
