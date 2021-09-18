package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorRepository;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.EntitySearchSpecification;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PasswordUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ZaposlenikServiceImpl implements ZaposlenikService {

    @Autowired
    private ZaposlenikRepository zaposlenikRepository;

    @Autowired
    private UgovorRepository ugovorRepository;

    @Override
    public void insert(AddZaposlenikDto addZaposlenikDto) {
        Zaposlenik zaposlenik = ObjectMapperUtils.map(addZaposlenikDto, Zaposlenik.class);
        zaposlenik.setLozinka(PasswordUtils.hash(zaposlenik.getLozinka()));
        zaposlenikRepository.save(zaposlenik);
    }

    @Override
    public void update(EditZaposlenikDto editZaposlenikDto) {
        Zaposlenik zaposlenik = ObjectMapperUtils.map(editZaposlenikDto, Zaposlenik.class);
        String lozinka = zaposlenikRepository.findById(zaposlenik.getIdZaposlenik()).orElse(null).getLozinka();
        zaposlenik.setLozinka(lozinka);
        zaposlenikRepository.save(zaposlenik);
    }

    @Override
    public void changePassword(ChangePasswordZaposlenikDto changePasswordZaposlenikDto) {
        Zaposlenik zaposlenik = zaposlenikRepository.findById(changePasswordZaposlenikDto.getIdZaposlenik()).orElse(null);
        zaposlenik.setLozinka(PasswordUtils.hash(changePasswordZaposlenikDto.getNovaLozinka()));
        zaposlenikRepository.save(zaposlenik);
    }

    @Override
    public void delete(Integer id) {
        zaposlenikRepository.deleteById(id);
    }

    @Override
    public ZaposlenikDetailsDto findById(Integer id) {
        Zaposlenik zaposlenik = zaposlenikRepository.findById(id).orElse(null);
        return ObjectMapperUtils.map(zaposlenik, ZaposlenikDetailsDto.class);
    }

    @Override
    public List<ZaposlenikDetailsDto> findAll() {
        List<Zaposlenik> zaposlenici = (List<Zaposlenik>) zaposlenikRepository.findAll();
        return ObjectMapperUtils.mapAll(zaposlenici, ZaposlenikDetailsDto.class);
    }

    @Override
    public Boolean existsByOibAndIdZaposlenikNot(String oib, Integer idZaposlenik) {
        return zaposlenikRepository.existsByOibAndIdZaposlenikNot(oib, idZaposlenik);
    }

    @Override
    public Boolean existsByEmailAndIdZaposlenikNot(String email, Integer idZaposlenik) {
        return zaposlenikRepository.existsByEmailAndIdZaposlenikNot(email, idZaposlenik);
    }

    @Override
    public Boolean existsByOib(String oib) {
        return zaposlenikRepository.existsByOib(oib);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return zaposlenikRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByIdZaposlenikAndLozinka(Integer idZaposlenik, String lozinka) {
        return zaposlenikRepository.existsByIdZaposlenikAndLozinka(idZaposlenik, PasswordUtils.hash(lozinka));
    }

    @Override
    public Long countAktivneZaposlenike() {
        return ugovorRepository.countAktivneUgovore();
    }

    @Override
    public Page<ZaposlenikDetailsDto> findAll(Pageable pageable) {
        Page<Zaposlenik> zaposlenikPage = zaposlenikRepository.findAll(pageable);
        List<ZaposlenikDetailsDto> zaposlenici = ObjectMapperUtils.mapAll(zaposlenikPage.getContent(), ZaposlenikDetailsDto.class);
        return new PageImpl(zaposlenici, pageable, zaposlenikPage.getTotalElements());
    }

    @Override
    public Page<ZaposlenikDetailsDto> findAll(QueryCriteriaDto queryCriteriaDto) {
        Page<Zaposlenik> zaposlenikPage = zaposlenikRepository.findAll(new EntitySearchSpecification(queryCriteriaDto.getSearchCriterias(), queryCriteriaDto.getOperator()), queryCriteriaDto.getPageable());
        List<ZaposlenikDetailsDto> zaposlenici = ObjectMapperUtils.mapAll(zaposlenikPage.getContent(), ZaposlenikDetailsDto.class);
        return new PageImpl(zaposlenici, queryCriteriaDto.getPageable(), zaposlenikPage.getTotalElements());
    }
}
