package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijava;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Rola;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.Zaposlenik;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PasswordUtils;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrijavaServiceImpl implements PrijavaService {

    @Autowired
    private ZaposlenikRepository zaposlenikRepository;

    @Autowired
    private PovijestPrijavaRepository povijestPrijavaRepository;

    @Autowired
    private RolaRepository rolaRepository;

    @Override
    public PrijavaResult pokusajPrijave(String email, String lozinka, Integer idRola) {
        Zaposlenik zaposlenik = zaposlenikRepository.findByEmailAndLozinkaAndZaposleniciRoleRolaIdRola(email, PasswordUtils.hash(lozinka), idRola);
        Rola rola = rolaRepository.findById(idRola).orElse(null);

        if (zaposlenik != null) {
            PovijestPrijava povijestPrijava = new PovijestPrijava();
            povijestPrijava.setVrijemePrijave(LocalDateTime.now());
            povijestPrijava.setZaposlenik(zaposlenik);
            povijestPrijava.setRola(rola);
            povijestPrijavaRepository.save(povijestPrijava);

            ZaposlenikDetailsDto zaposlenikInfoDto = ObjectMapperUtils.map(zaposlenik, ZaposlenikDetailsDto.class);
            RolaDto rolaDto = ObjectMapperUtils.map(rola, RolaDto.class);

            PrijavljeniZaposlenikDto prijavljeniZaposlenikDto = new PrijavljeniZaposlenikDto();
            prijavljeniZaposlenikDto.setZaposlenik(zaposlenikInfoDto);
            prijavljeniZaposlenikDto.setTrenutnaRola(rolaDto);

            return new PrijavaResult(true, prijavljeniZaposlenikDto);
        } else {
            return new PrijavaResult(false, null);
        }
    }

}
