package com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava;

public interface PrijavaService {

    PrijavaResult pokusajPrijave(String email, String lozinka, Integer idRola);
}
