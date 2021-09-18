package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ZaposlenikService {

    void insert(AddZaposlenikDto addZaposlenikDto);

    void update(EditZaposlenikDto editZaposlenikDto);

    void changePassword(ChangePasswordZaposlenikDto changePasswordZaposlenikDto);

    void delete(Integer id);

    ZaposlenikDetailsDto findById(Integer id);

    List<ZaposlenikDetailsDto> findAll();

    Boolean existsByOibAndIdZaposlenikNot(String oib, Integer idZaposlenik);

    Boolean existsByEmailAndIdZaposlenikNot(String email, Integer idZaposlenik);

    Boolean existsByOib(String oib);

    Boolean existsByEmail(String email);

    Boolean existsByIdZaposlenikAndLozinka(Integer idZaposlenik, String lozinka);

    Long countAktivneZaposlenike();

    Page<ZaposlenikDetailsDto> findAll(Pageable pageable);

    Page<ZaposlenikDetailsDto> findAll(QueryCriteriaDto queryCriteriaDto);
}
