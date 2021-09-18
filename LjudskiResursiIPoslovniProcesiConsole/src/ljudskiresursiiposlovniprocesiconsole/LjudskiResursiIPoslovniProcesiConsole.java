/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljudskiresursiiposlovniprocesiconsole;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.AddObracunStudentskihUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.AddObracunUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrstaObracunaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author Borna
 */
@SpringBootApplication(scanBasePackages = "com.bzaja.ljudskiresursiiposlovniprocesilibrary.config")
public class LjudskiResursiIPoslovniProcesiConsole implements CommandLineRunner {

    @Autowired
    private VrstaObracunaService vrstaObracunaService;

    @Autowired
    private ObracunUgovoraORaduService obracunUgovoraORaduService;

    @Autowired
    private ObracunStudentskihUgovoraService obracunStudentskihUgovoraService;

    @Autowired
    private ObracunUgovoraODjeluService obracunUgovoraODjeluService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private ParametriZaObracunPlaceService parametriZaObracunPlaceService;

    @Autowired
    private ObracunUgovoraPdfReportFactory obracunUgovoraPdfReportFactory;

    private static final String ROOT_PATH = String.format("C:\\Users\\%s\\Documents\\LRIPP - Inicijalni PDF obracuni", System.getProperty("user.name"));
    private static final String OBRACUN_UGOVORA_O_RADU_PDF_PATH = String.format("%s\\Inicijalni_Obracun_Ugovora_O_Radu.pdf", ROOT_PATH);
    private static final String OBRACUN_UGOVORA_O_DJELU_PDF_PATH = String.format("%s\\Inicijalni_Obracun_Ugovora_O_Djelu.pdf", ROOT_PATH);
    private static final String OBRACUN_STUDENTSKIH_UGOVORA_PDF_PATH = String.format("%s\\Inicijalni_Obracun_Studentskih_Ugovora.pdf", ROOT_PATH);

    public static void main(String[] args) {
        SpringApplication.run(LjudskiResursiIPoslovniProcesiConsole.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            tryCreateInicijalniObracuni();
            tryExportInicijalniObracuniToPdf();
        } catch (Exception e) {
            System.out.println("Desila se greška.");
        }
    }

    private void tryCreateInicijalniObracuni() {
        if (isObracuniEmpty()) {
            createInicijalniObracuni();
            System.out.println("Inicijalni obracuni su uspjesno kreirani.");
        } else {
            System.out.println("Inicijalni obracuni su vec kreirani.");
        }
    }

    private boolean isObracuniEmpty() {
        Page<ObracunUgovoraORaduDto> obracunUgovoraORaduPage = obracunUgovoraORaduService.findAll(PageRequest.of(0, 10));
        Page<ObracunUgovoraODjeluDto> obracunUgovoraODjeluPage = obracunUgovoraODjeluService.findAll(PageRequest.of(0, 10));
        Page<ObracunStudentskihUgovoraDto> obracunStudentskihUgovoraPage = obracunStudentskihUgovoraService.findAll(PageRequest.of(0, 10));
        return obracunUgovoraORaduPage.isEmpty() && obracunUgovoraODjeluPage.isEmpty() && obracunStudentskihUgovoraPage.isEmpty();
    }

    private void createInicijalniObracuni() {
        ParametriZaObracunPlaceDto parametriZaObracunPlaceDto = parametriZaObracunPlaceService.findFirst();

        ObracunUgovoraORaduDto obracunUgovoraORaduDto = new ObracunUgovoraORaduDto();
        obracunUgovoraORaduDto.setVrstaObracuna(vrstaObracunaService.findById(VrsteObracuna.OBRACUN_UGOVORA_O_RADU.getId()));
        obracunUgovoraORaduDto.setOpis("veljača 2019.");
        obracunUgovoraORaduDto.setDatumObracuna(LocalDate.of(2019, Month.FEBRUARY, 28));
        obracunUgovoraORaduDto.setValuta(valutaService.findByDrzaveJeDomovinaTrue());
        obracunUgovoraORaduDto.setDatumOd(LocalDate.of(2019, Month.FEBRUARY, 1));
        obracunUgovoraORaduDto.setDatumDo(LocalDate.of(2019, Month.FEBRUARY, 28));
        obracunUgovoraORaduDto.setOsnovniOsobniOdbitak(parametriZaObracunPlaceDto.getOsnovniOsobniOdbitak());
        obracunUgovoraORaduDto.setOsnovicaOsobnogOdbitka(parametriZaObracunPlaceDto.getOsnovicaOsobnogOdbitka());
        obracunUgovoraORaduService.save(obracunUgovoraORaduDto);

        AddObracunUgovoraODjeluDto addObracunUgovoraODjeluDto = new AddObracunUgovoraODjeluDto();
        addObracunUgovoraODjeluDto.setVrstaObracuna(vrstaObracunaService.findById(VrsteObracuna.OBRACUN_UGOVORA_O_DJELU.getId()));
        addObracunUgovoraODjeluDto.setOpis("Obračun ugovora o djelu");
        addObracunUgovoraODjeluDto.setDatumObracuna(LocalDate.of(2019, Month.FEBRUARY, 28));
        addObracunUgovoraODjeluDto.setValuta(valutaService.findByDrzaveJeDomovinaTrue());
        addObracunUgovoraODjeluDto.setIdeviUgovora(Arrays.asList(11));
        obracunUgovoraODjeluService.save(addObracunUgovoraODjeluDto);

        AddObracunStudentskihUgovoraDto addObracunStudentskihUgovoraDto = new AddObracunStudentskihUgovoraDto();
        addObracunStudentskihUgovoraDto.setVrstaObracuna(vrstaObracunaService.findById(VrsteObracuna.OBRACUN_STUDENTSKIH_UGOVORA.getId()));
        addObracunStudentskihUgovoraDto.setOpis("Obračun studentskih ugovora");
        addObracunStudentskihUgovoraDto.setDatumObracuna(LocalDate.of(2019, Month.FEBRUARY, 28));
        addObracunStudentskihUgovoraDto.setValuta(valutaService.findByDrzaveJeDomovinaTrue());
        addObracunStudentskihUgovoraDto.setIdeviUgovora(Arrays.asList(4));
        addObracunStudentskihUgovoraDto.setLimitGodisnjegIznosaZaStudenta(parametriZaObracunPlaceDto.getLimitGodisnjegIznosaZaStudenta());
        obracunStudentskihUgovoraService.save(addObracunStudentskihUgovoraDto);
    }

    private void tryExportInicijalniObracuniToPdf() {
        if (!isObracuniEmpty() && !doesInicijalniObracuniPdfPathExists()) {
            makeDirectory();
            exportInicijalniObracuniToPdf();
            System.out.println("Inicijalni obracuni su uspjesno eksportirani u PDF.");
        } else {
            System.out.println("Inicijalni obracuni nisu eksportirani, jer nisu kreirani ili su vec eksportirani u PDF.");
        }
    }

    private boolean doesInicijalniObracuniPdfPathExists() {
        boolean existsObracunUgovoraORaduPdfPath = Files.exists(Paths.get(OBRACUN_UGOVORA_O_RADU_PDF_PATH));
        boolean existsObracunUgovoraODjeluPdfPath = Files.exists(Paths.get(OBRACUN_UGOVORA_O_DJELU_PDF_PATH));
        boolean existsObracunStudentskihUgovoraPdfPath = Files.exists(Paths.get(OBRACUN_STUDENTSKIH_UGOVORA_PDF_PATH));
        return existsObracunUgovoraORaduPdfPath && existsObracunUgovoraODjeluPdfPath && existsObracunStudentskihUgovoraPdfPath;
    }

    private void makeDirectory() {
        new File(ROOT_PATH).mkdir();
    }

    private void exportInicijalniObracuniToPdf() {
        obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_UGOVORA_O_RADU).createByIdObracunUgovora(1, OBRACUN_UGOVORA_O_RADU_PDF_PATH);
        obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_UGOVORA_O_DJELU).createByIdObracunUgovora(2, OBRACUN_UGOVORA_O_DJELU_PDF_PATH);
        obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_STUDENTSKIH_UGOVORA).createByIdObracunUgovora(3, OBRACUN_STUDENTSKIH_UGOVORA_PDF_PATH);
    }
}
