package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
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
public class ObracunStudentskihUgovoraServiceImpl implements ObracunStudentskihUgovoraService {

    @Autowired
    private ObracunStudentskihUgovoraRepository obracunStudentskihUgovoraRepository;

    @Autowired
    private StudentskiUgovorRepository studentskiUgovorRepository;

    @Autowired
    private ObracunStudentskihUgovoraMaker obracunStudentskihUgovoraMaker;

    @Override
    public void save(AddObracunStudentskihUgovoraDto addObracunStudentskihUgovoraDto) {
        ObracunStudentskihUgovora obracunStudentskihUgovora = obracunStudentskihUgovoraMaker.make(addObracunStudentskihUgovoraDto);
        obracunStudentskihUgovoraRepository.save(obracunStudentskihUgovora);

        List<StudentskiUgovor> studentskiUgovori = studentskiUgovorRepository.findByIdUgovorIn(addObracunStudentskihUgovoraDto.getIdeviUgovora());
        studentskiUgovori.forEach((studentskiUgovor) -> {
            studentskiUgovor.setJeObracunat(true);
            studentskiUgovorRepository.save(studentskiUgovor);
        });
    }

    @Override
    public void delete(Integer id) {
        List<StudentskiUgovor> studentskiUgovori = studentskiUgovorRepository.findByIsplatneListeObracunUgovoraIdObracunUgovora(id);
        studentskiUgovori.forEach((studentskiUgovor) -> {
            studentskiUgovor.setJeObracunat(false);
            studentskiUgovorRepository.save(studentskiUgovor);
        });

        obracunStudentskihUgovoraRepository.deleteById(id);
    }

    @Override
    public ObracunStudentskihUgovoraDto findById(Integer id) {
        ObracunStudentskihUgovora obracunStudentskihUgovora = obracunStudentskihUgovoraRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(obracunStudentskihUgovora, ObracunStudentskihUgovoraDto.class);
    }

    @Override
    public List<ObracunStudentskihUgovoraDto> findAll() {
        List<ObracunStudentskihUgovora> obracuniStudentskihUgovora = (List<ObracunStudentskihUgovora>) obracunStudentskihUgovoraRepository.findAll();
        return ObjectMapperUtils.mapAll(obracuniStudentskihUgovora, ObracunStudentskihUgovoraDto.class);
    }

    @Override
    public Page<ObracunStudentskihUgovoraDto> findAll(Pageable pageable) {
        Page<ObracunStudentskihUgovora> obracunStudentskihUgovoraPage = obracunStudentskihUgovoraRepository.findAll(pageable);
        List<ObracunStudentskihUgovoraDto> obracuniStudentskihUgovora = ObjectMapperUtils.mapAll(obracunStudentskihUgovoraPage.getContent(), ObracunStudentskihUgovoraDto.class);
        return new PageImpl(obracuniStudentskihUgovora, pageable, obracunStudentskihUgovoraPage.getTotalElements());
    }

    @Override
    public Page<ObracunStudentskihUgovoraDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<ObracunStudentskihUgovora> obracunStudentskihUgovoraPage = obracunStudentskihUgovoraRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<ObracunStudentskihUgovoraDto> obracuniStudentskihUgovora = ObjectMapperUtils.mapAll(obracunStudentskihUgovoraPage.getContent(), ObracunStudentskihUgovoraDto.class);
        return new PageImpl(obracuniStudentskihUgovora, queryCriteriaDto.getPageable(), obracunStudentskihUgovoraPage.getTotalElements());
    }
}
