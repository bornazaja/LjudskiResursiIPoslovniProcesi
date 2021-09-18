package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaLista;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraCommonMaker;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObracunStudentskihUgovoraMakerImpl implements ObracunStudentskihUgovoraMaker {

    @Autowired
    private StudentskiUgovorService studentskiUgovorService;

    @Autowired
    private IsplatnaListaMaker isplatnaListaMaker;

    @Autowired
    private ObracunUgovoraCommonMaker obracunUgovoraCommonMaker;

    @Override
    public ObracunStudentskihUgovora make(AddObracunStudentskihUgovoraDto addObracunStudentskihUgovoraDto) {
        ObracunStudentskihUgovora obracunStudentskihUgovora = ObjectMapperUtils.map(addObracunStudentskihUgovoraDto, ObracunStudentskihUgovora.class);

        List<StudentskiUgovor> studentskiUgovori = getStudentskiUgovori(addObracunStudentskihUgovoraDto.getIdeviUgovora());
        Set<IsplatnaLista> isplatneListe = new LinkedHashSet<>();

        studentskiUgovori.forEach((studentskiUgovor) -> {
            IsplatnaLista isplatnaLista = isplatnaListaMaker.make(obracunStudentskihUgovora, studentskiUgovor, studentskiUgovor.getDatumOd(), studentskiUgovor.getDatumDo());
            isplatneListe.add(isplatnaLista);
        });

        return (ObracunStudentskihUgovora) obracunUgovoraCommonMaker.make(obracunStudentskihUgovora, isplatneListe);
    }

    private List<StudentskiUgovor> getStudentskiUgovori(List<Integer> ids) {
        List<StudentskiUgovorDto> studentskiUgovoriDto = studentskiUgovorService.findByIdUgovorIn(ids);
        return ObjectMapperUtils.mapAll(studentskiUgovoriDto, StudentskiUgovor.class);
    }
}
