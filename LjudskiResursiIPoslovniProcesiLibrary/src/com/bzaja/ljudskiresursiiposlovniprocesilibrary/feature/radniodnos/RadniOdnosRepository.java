package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RadniOdnosRepository extends CrudRepository<RadniOdnos, Integer>, JpaSpecificationExecutor<RadniOdnos> {

}
