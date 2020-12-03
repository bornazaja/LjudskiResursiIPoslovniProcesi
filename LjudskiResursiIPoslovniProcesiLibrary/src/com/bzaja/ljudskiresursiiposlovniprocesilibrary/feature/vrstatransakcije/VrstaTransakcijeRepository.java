package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface VrstaTransakcijeRepository extends CrudRepository<VrstaTransakcije, Integer>, JpaSpecificationExecutor<VrstaTransakcije> {

}
