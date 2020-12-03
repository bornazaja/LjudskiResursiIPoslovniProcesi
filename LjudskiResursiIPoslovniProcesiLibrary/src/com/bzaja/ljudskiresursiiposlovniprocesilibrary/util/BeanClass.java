/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesilibrary.util;

import com.bzaja.myjavalibrary.util.BeanClassInterface;

/**
 *
 * @author Borna
 */
public enum BeanClass implements BeanClassInterface {
    DAVANJE("davanje.Davanje"),
    DODATAK("dodatak.Dodatak"),
    DRZAVA("drzava.Drzava"),
    GRAD("grad.Grad"),
    ISPLATNA_LISTA("isplatnalista.IsplatnaLista"),
    KATEGORIJA_TRANSAKCIJE("kategorijatransakcije.KategorijaTransakcije"),
    OBRACUN_STUDENTSKIH_UGOVORA("obracunstudentskihugovora.ObracunStudentskihUgovora"),
    OBRACUN_UGOVORA("obracunugovora.ObracunUgovora"),
    OBRACUN_UGOVORA_O_DJELU("obracunugovoraodjelu.ObracunUgovorODjelu"),
    OBRACUN_UGOVORA_O_RADU("obracunugovoraoradu.ObracunUgovoraORadu"),
    OBUSTAVA("obustava.Obustava"),
    ODJEL("odjel.Odjel"),
    OLAKSICA("olaksica.Olaksica"),
    POREZ("porez.Porez"),
    POSLOVNI_PARTNER("poslovnipartner.PoslovniPartner"),
    POVIJEST_PRIJAVA("povijestprijava.PovijestPrijava"),
    PREBIVALISTE("prebivaliste.Prebivaliste"),
    PREKOVREMENI_RAD("prekovremenirad.PrekovremeniRad"),
    RADNI_ODNOS("radniodnos.RadniOdnos"),
    RADNO_MJESTO("radnomjesto.RadnoMjesto"),
    ROLA("rola.Rola"),
    SPOL("spol.Spol"),
    STUDENTSKI_POSAO_CJENIK("studentskiposaocjenik.StudentskiPosaoCjenik"),
    STUDENTSKI_UGOVOR("studentskiugovor.StudentskiUgovor"),
    TRANSAKCIJA("transakcija.Transakcija"),
    UGOVOR("ugovor.Ugovor"),
    UGOVOR_O_DJELU("ugovorodjelu.UgovorODjelu"),
    UGOVOR_O_RADU("ugovororadu.UgovorORadu"),
    VALUTA("valuta.Valuta"),
    VRSTA_DAVANJA("vrstadavanja.VrstaDavanja"),
    VRSTA_DODATKA("vrstadodatka.VrstaDodatka"),
    VRSTA_OBRACUNA("vrstaobracuna.VrstaObracuna"),
    VRSTA_OBUSTAVE("vrstaobustave.VrstaObustave"),
    VRSTA_OLAKSICE("vrstaolaksice.VrstaOlaksice"),
    VRSTA_POREZA("vrstaporeza.VrstaPoreza"),
    VRSTA_PREKOVREMENOG_RADA("vrstaprekovremenograda.VrstaPrekovremenogRada"),
    VRSTA_TRANSAKCIJE("vrstatransakcije.VrstaTransakcije"),
    VRSTA_UGOVORA("vrstaugovora.VrstaUgovora"),
    ZAPOSLENIK("zaposlenik.Zaposlenik"),
    ZAPOSLENIK_ROLA("zaposlenikrola.ZaposlenikRola");

    private String className;

    private BeanClass(String className) {
        this.className = className;
    }

    @Override
    public String getFullClassName() {
        return "com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature." + className;
    }
}
