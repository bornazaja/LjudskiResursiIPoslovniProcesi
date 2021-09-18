package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PodaciOTvrtkiServiceImpl implements PodaciOTvrtkiService {

    @Autowired
    private PodaciOTvrtkiRepository podaciOTvrtkiRepository;

    @Override
    public void save(PodaciOTvrtkiDto podaciOTvrtkiDto) {
        PodaciOTvrtki podaciOTvrtki = ObjectMapperUtils.map(podaciOTvrtkiDto, PodaciOTvrtki.class);
        podaciOTvrtkiRepository.save(podaciOTvrtki);
    }

    @Override
    public PodaciOTvrtkiDto findFirst() {
        PodaciOTvrtki podaciOTvrtki = podaciOTvrtkiRepository.findFirstByOrderByIdPodaciOTvrtkiAsc();
        return ObjectMapperUtils.map(podaciOTvrtki, PodaciOTvrtkiDto.class);
    }
}
