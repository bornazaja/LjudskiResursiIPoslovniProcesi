package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovor;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjelu;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORadu;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora.VrsteUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.EnumUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PayrollCalculatorUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UgovorServiceImpl implements UgovorService {

    @Autowired
    private UgovorRepository ugovorRepository;

    @Override
    public UgovorDto findById(Integer id) {
        Ugovor ugovor = ugovorRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(ugovor, UgovorDto.class);
    }

    @Override
    public List<UgovorDto> findAll() {
        List<Ugovor> ugovori = (List<Ugovor>) ugovorRepository.findAll();
        return ObjectMapperUtils.mapAll(ugovori, UgovorDto.class);
    }

    @Override
    public Long countByZaposlenikId(Integer idZaposlenik) {
        return ugovorRepository.countByZaposlenikIdZaposlenik(idZaposlenik);
    }

    @Override
    public Double getPlacaByZaposlenikId(Integer idZaposlenik) {
        Ugovor ugovor = ugovorRepository.findAktivniUgovorByZaposlenikId(idZaposlenik);
        VrsteUgovora vrsteUgovora = EnumUtils.fromValue(ugovor.getRadniOdnos().getVrstaUgovora().getIdVrstaUgovora(), VrsteUgovora.values(), "getId");

        switch (vrsteUgovora) {
            case UGOVOR_O_RADU:
                UgovorORadu ugovorORadu = (UgovorORadu) ugovor;
                return ugovorORadu.getBrutoPlaca();
            case UGOVOR_O_DJELU:
                UgovorODjelu ugovorODjelu = (UgovorODjelu) ugovor;
                return ugovorODjelu.getBrutoIznos();
            case STUDENTSKI_UGOVOR:
                StudentskiUgovor studentskiUgovor = (StudentskiUgovor) ugovor;
                return PayrollCalculatorUtils.getIznosByCijenaPoSatuIBrojOdradjenihSati(studentskiUgovor.getCijenaPoSatu(), studentskiUgovor.getBrojOdradjenihSati());
            default:
                return 0.0;
        }
    }
}
