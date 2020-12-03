package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

public final class PayrollCalculatorUtils {

    private PayrollCalculatorUtils() {

    }

    public static Double fromStopaToIznosByOsnovica(Double osnovniIznos, Double stopa) {
        return osnovniIznos / 100 * stopa;
    }

    public static Double getIznosZaPrekovremeniRad(Double cijenaPoSatu, Double brojOdradjenihSati, Double koeficjent) {
        return cijenaPoSatu * brojOdradjenihSati * koeficjent;
    }

    public static Double getIznosByCijenaPoSatuIBrojOdradjenihSati(Double cijenaPoSatu, Double brojOdradjenihSati) {
        return cijenaPoSatu * brojOdradjenihSati;
    }
}
