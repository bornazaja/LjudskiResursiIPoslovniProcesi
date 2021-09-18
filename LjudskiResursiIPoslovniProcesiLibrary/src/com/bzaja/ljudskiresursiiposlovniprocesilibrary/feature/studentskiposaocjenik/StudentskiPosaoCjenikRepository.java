package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentskiPosaoCjenikRepository extends PagingAndSortingRepository<StudentskiPosaoCjenik, Integer>, JpaSpecificationExecutor<StudentskiPosaoCjenik> {

}
