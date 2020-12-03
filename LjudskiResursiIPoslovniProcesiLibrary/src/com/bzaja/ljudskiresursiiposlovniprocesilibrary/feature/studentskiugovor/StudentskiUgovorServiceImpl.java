package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor;

import com.bzaja.myjavalibrary.springframework.data.query.EntitySearchSpecification;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.springframework.data.query.SearchCriteriaUtils;
import com.bzaja.myjavalibrary.springframework.data.query.SearchOperation;
import com.bzaja.myjavalibrary.util.ObjectMapperUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentskiUgovorServiceImpl implements StudentskiUgovorService {

    @Autowired
    private StudentskiUgovorRepository studentskiUgovorRepository;

    @Override
    public void save(StudentskiUgovorDto studentskiUgovorDto) {
        StudentskiUgovor studentskiUgovor = ObjectMapperUtils.map(studentskiUgovorDto, StudentskiUgovor.class);
        studentskiUgovorRepository.save(studentskiUgovor);
    }

    @Override
    public void delete(Integer id) {
        studentskiUgovorRepository.deleteById(id);
    }

    @Override
    public StudentskiUgovorDto findById(Integer id) {
        StudentskiUgovor studentskiUgovor = studentskiUgovorRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(studentskiUgovor, StudentskiUgovorDto.class);
    }

    @Override
    public List<StudentskiUgovorDto> findAll() {
        List<StudentskiUgovor> studentskiUgovori = (List<StudentskiUgovor>) studentskiUgovorRepository.findAll();
        return ObjectMapperUtils.mapAll(studentskiUgovori, StudentskiUgovorDto.class);
    }

    @Override
    public List<StudentskiUgovorDto> findByIdUgovorIn(List<Integer> ids) {
        List<StudentskiUgovor> studentskiUgovori = studentskiUgovorRepository.findByIdUgovorIn(ids);
        return ObjectMapperUtils.mapAll(studentskiUgovori, StudentskiUgovorDto.class);
    }

    @Override
    public Page<StudentskiUgovorDto> findAll(Integer idZaposlenik, Pageable pageable) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        Page<StudentskiUgovor> studentskiUgovorPage = studentskiUgovorRepository.findAll(specification, pageable);
        List<StudentskiUgovorDto> studentskiUgovori = ObjectMapperUtils.mapAll(studentskiUgovorPage.getContent(), StudentskiUgovorDto.class);
        return new PageImpl(studentskiUgovori, pageable, studentskiUgovorPage.getTotalElements());
    }

    @Override
    public Page<StudentskiUgovorDto> findAll(Integer idZaposlenik, QueryCriteriaDto queryCriteriaDto) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND))
                .and(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()));

        Page<StudentskiUgovor> studentskiUgovorPage = studentskiUgovorRepository.findAll(specification, queryCriteriaDto.getPageable());
        List<StudentskiUgovorDto> studentskiUgovori = ObjectMapperUtils.mapAll(studentskiUgovorPage.getContent(), StudentskiUgovorDto.class);
        return new PageImpl(studentskiUgovori, queryCriteriaDto.getPageable(), studentskiUgovorPage.getTotalElements());
    }

    @Override
    public List<StudentskiUgovorDto> findAll(Integer idZaposlenik) {
        List<StudentskiUgovor> studentskiUgovori = studentskiUgovorRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(idZaposlenik, SearchOperation.EQUAL, "zaposlenik.idZaposlenik"), Operator.AND));
        return ObjectMapperUtils.mapAll(studentskiUgovori, StudentskiUgovorDto.class);
    }

    @Override
    public List<StudentskiUgovorDto> findAllNeObracunateUgovore(String text, String... columns) {
        Specification specification = Specification.where(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(0, SearchOperation.EQUAL, "jeObracunat"), Operator.AND))
                .and(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));

        List<StudentskiUgovor> studentskiUgovori = studentskiUgovorRepository.findAll(specification);
        return ObjectMapperUtils.mapAll(studentskiUgovori, StudentskiUgovorDto.class);
    }
}
