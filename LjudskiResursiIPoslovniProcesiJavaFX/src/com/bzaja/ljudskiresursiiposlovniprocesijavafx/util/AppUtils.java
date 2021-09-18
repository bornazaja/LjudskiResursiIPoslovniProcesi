package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import javafx.scene.control.Alert;

public final class AppUtils {

    private static PrijavljeniZaposlenikDto prijavljeniZaposlenikDto;

    private AppUtils() {

    }

    public static void setPrijavljeniZapolsenik(PrijavljeniZaposlenikDto prijavljeniZaposlenikDto) {
        AppUtils.prijavljeniZaposlenikDto = prijavljeniZaposlenikDto;
    }

    public static PrijavljeniZaposlenikDto getPrijavljeniZaposlenik() {
        return prijavljeniZaposlenikDto;
    }
    
    public static void clearPrijavljeniZaposlenik(){
        prijavljeniZaposlenikDto = null;
    }

    public static void runIfMatchesRole(Role currentRole, Role roleToMatch, Runnable runnable) {
        runIfMatchesObject(currentRole, roleToMatch, runnable);
    }

    public static void runIfMatchesSourcePrijavljenogZaposlenika(SourcePrijavljenogZaposlenika currentSourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenikaToMatch, Runnable runnable) {
        runIfMatchesObject(currentSourcePrijavljenogZaposlenika, sourcePrijavljenogZaposlenikaToMatch, runnable);
    }

    private static void runIfMatchesObject(Object sourceObject, Object targetObject, Runnable runnable) {
        if (sourceObject.equals(targetObject)) {
            runnable.run();
        }
    }

    public static void runWithTryCatch(String errorMessage, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", errorMessage);
        }
    }

    public static Boolean isPrijavljeniZaposlenik(int idZaposlenik) {
        return idZaposlenik == getPrijavljeniZaposlenik().getZaposlenik().getIdZaposlenik();
    }
}
