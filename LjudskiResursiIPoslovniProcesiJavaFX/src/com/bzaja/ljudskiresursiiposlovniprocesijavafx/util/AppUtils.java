package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.CssFiles;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import com.bzaja.myjavafxlibrary.util.CssPathable;
import com.bzaja.myjavalibrary.util.CommonUtils;

public final class AppUtils {

    private static PrijavljeniZaposlenikDto prijavljeniZaposlenikDto;

    private static final CssPathable[] CSS_PATHABLES = CssFiles.values();

    private AppUtils() {

    }

    public static void setPrijavljeniZapolsenik(PrijavljeniZaposlenikDto prijavljeniZaposlenikDto) {
        AppUtils.prijavljeniZaposlenikDto = prijavljeniZaposlenikDto;
    }

    public static PrijavljeniZaposlenikDto getPrijavljeniZaposlenik() {
        return prijavljeniZaposlenikDto;
    }

    public static void clearPrijavljeniZaposlenik() {
        prijavljeniZaposlenikDto = null;
    }

    public static void runIfMatchesRole(Role currentRole, Role roleToMatch, Runnable runnable) {
        CommonUtils.runIfMatchesObject(currentRole, roleToMatch, runnable);
    }

    public static void runIfMatchesSourcePrijavljenogZaposlenika(SourcePrijavljenogZaposlenika currentSourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenikaToMatch, Runnable runnable) {
        CommonUtils.runIfMatchesObject(currentSourcePrijavljenogZaposlenika, sourcePrijavljenogZaposlenikaToMatch, runnable);
    }

    public static Boolean isPrijavljeniZaposlenik(int idZaposlenik) {
        return idZaposlenik == getPrijavljeniZaposlenik().getZaposlenik().getIdZaposlenik();
    }

    public static CssPathable[] getCssPathables() {
        return CSS_PATHABLES;
    }
}
