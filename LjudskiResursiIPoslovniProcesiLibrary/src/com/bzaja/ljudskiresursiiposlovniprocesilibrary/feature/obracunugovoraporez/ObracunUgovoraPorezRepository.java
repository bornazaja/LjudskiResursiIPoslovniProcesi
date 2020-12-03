package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraporez;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ObracunUgovoraPorezRepository extends CrudRepository<ObracunUgovoraPorez, Integer> {

    @Query("select oup.stopa from ObracunUgovoraPorez oup where oup.obracunUgovora.idObracunUgovora = :idObracunUgovora and (:dohodak >= oup.minOsnovica and :dohodak <= oup.maxOsnovica or :dohodak > oup.minOsnovica and oup.maxOsnovica is null)")
    Double findStopaByObracunUgovoraIdAndDohodak(@Param("idObracunUgovora") Integer idObracunUgovora, @Param("dohodak") Double dohodak);

    @Query("select oup.stopa from ObracunUgovoraPorez oup where oup.obracunUgovora.idObracunUgovora = :idObracunUgovora")
    Double findStopaByObracunUgovoraId(@Param("idObracunUgovora") Integer idObracunUgovora);
}
