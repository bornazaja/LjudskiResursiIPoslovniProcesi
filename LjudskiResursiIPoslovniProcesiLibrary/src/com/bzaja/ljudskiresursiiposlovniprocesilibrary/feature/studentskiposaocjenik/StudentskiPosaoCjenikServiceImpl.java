package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik;

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
public class StudentskiPosaoCjenikServiceImpl implements StudentskiPosaoCjenikService {

    @Autowired
    private StudentskiPosaoCjenikRepository studentskiPosaoCjenikRepository;

    @Override
    public void save(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        StudentskiPosaoCjenik studentskiPosaoCjenik = ObjectMapperUtils.map(studentskiPosaoCjenikDto, StudentskiPosaoCjenik.class);
        studentskiPosaoCjenikRepository.save(studentskiPosaoCjenik);
    }

    @Override
    public void delete(Integer id) {
        studentskiPosaoCjenikRepository.deleteById(id);
    }

    @Override
    public StudentskiPosaoCjenikDto findById(Integer id) {
        StudentskiPosaoCjenik studentskiPosaoCjenik = studentskiPosaoCjenikRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(studentskiPosaoCjenik, StudentskiPosaoCjenikDto.class);
    }

    @Override
    public List<StudentskiPosaoCjenikDto> findAll() {
        List<StudentskiPosaoCjenik> studentskiPosloviCjenik = (List<StudentskiPosaoCjenik>) studentskiPosaoCjenikRepository.findAll();
        return ObjectMapperUtils.mapAll(studentskiPosloviCjenik, StudentskiPosaoCjenikDto.class);
    }

    @Override
    public Page<StudentskiPosaoCjenikDto> findAll(Pageable pageable) {
        Page<StudentskiPosaoCjenik> studentskiPosaoCjenikPage = studentskiPosaoCjenikRepository.findAll(pageable);
        List<StudentskiPosaoCjenikDto> studentskiPosloviCjenik = ObjectMapperUtils.mapAll(studentskiPosaoCjenikPage.getContent(), StudentskiPosaoCjenikDto.class);
        return new PageImpl(studentskiPosloviCjenik, pageable, studentskiPosaoCjenikPage.getTotalElements());
    }

    @Override
    public Page<StudentskiPosaoCjenikDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<StudentskiPosaoCjenik> studentskiPosaoCjenikPage = studentskiPosaoCjenikRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<StudentskiPosaoCjenikDto> studentskiPosloviCjenik = ObjectMapperUtils.mapAll(studentskiPosaoCjenikPage.getContent(), StudentskiPosaoCjenikDto.class);
        return new PageImpl(studentskiPosloviCjenik, queryCriteriaDto.getPageable(), studentskiPosaoCjenikPage.getTotalElements());
    }

    @Override
    public List<StudentskiPosaoCjenikDto> findAll(String text, String... columns) {
        List<StudentskiPosaoCjenik> studentskiPosloviCjenik = studentskiPosaoCjenikRepository.findAll(new EntitySearchSpecification(SearchCriteriaUtils.toSearchCriterias(text, SearchOperation.CONTAINS, columns), Operator.OR));
        return ObjectMapperUtils.mapAll(studentskiPosloviCjenik, StudentskiPosaoCjenikDto.class);
    }
}
