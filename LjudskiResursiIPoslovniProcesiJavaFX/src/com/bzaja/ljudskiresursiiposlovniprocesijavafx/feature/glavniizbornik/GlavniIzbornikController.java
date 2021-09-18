/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.glavniizbornik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.MenuBarBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.MenuBarResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.EnumUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class GlavniIzbornikController implements Initializable, ControllerInterface<PrijavljeniZaposlenikDto> {

    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private AnchorPane contentPane;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private PrijavljeniZaposlenikDto prijavljeniZaposlenikDto;
    private String imeIPrezimeUlogiranogZaposlenika;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initData(PrijavljeniZaposlenikDto t) {
        prijavljeniZaposlenikDto = t;

        imeIPrezimeUlogiranogZaposlenika = String.format("%s %s", t.getZaposlenik().getIme(), t.getZaposlenik().getPrezime());

        MenuBarResultDto menuBarResultDto = createMenu();
        menuBar.getMenus().addAll(menuBarResultDto.getMenus());
        menuBarResultDto.getMenuItemRunnable().run();
    }

    private MenuBarResultDto createMenu() {
        Role role = EnumUtils.fromValue(prijavljeniZaposlenikDto.getTrenutnaRola().getIdRola(), Role.values(), "getId");

        switch (role) {
            case ADMINISTRATOR:
                return createAdministatorMenu();
            case OSOBLJE:
                return createOsobljeMenu();
            default:
                throw new IllegalArgumentException("Ova rola ne postoji.");
        }
    }

    private MenuBarResultDto createAdministatorMenu() {
        return new MenuBarBuilder(0)
                .addMenu(imeIPrezimeUlogiranogZaposlenika)
                .addMenuItem("Dashboard", () -> paneManager.switchPanes(contentPane, FxmlView.ADMINISTRATOR_DASHBOARD, prijavljeniZaposlenikDto.getZaposlenik()))
                .addMenuItem("Vaši podaci", () -> paneManager.switchPanes(contentPane, FxmlView.OPSEZNI_PODACI_ZAPOSLENIKA, prijavljeniZaposlenikDto))
                .addMenuItem("Odjava", () -> doLogout())
                .addMenuItem("Izlaz", () -> System.exit(0))
                .addMenu("Tvrtka")
                .addMenuItem("Podaci o tvrtki", () -> stageManager.showSecondaryStage(FxmlView.PODACI_O_TVRTKI))
                .addMenu("Države i gradovi")
                .addMenuItem("Države", () -> paneManager.switchPanes(contentPane, FxmlView.DRZAVE))
                .addMenuItem("Gradovi", () -> paneManager.switchPanes(contentPane, FxmlView.GRADOVI))
                .addMenu("Odjeli i radna mjesta")
                .addMenuItem("Odjeli", () -> paneManager.switchPanes(contentPane, FxmlView.ODJELI))
                .addMenuItem("Radna mjesta", () -> paneManager.switchPanes(contentPane, FxmlView.RADNA_MJESTA))
                .addMenu("Računovodstvene stavke")
                .addMenuItem("Vrste dodataka", () -> paneManager.switchPanes(contentPane, FxmlView.VRSTE_DODATAKA))
                .addMenuItem("Vrste obustava", () -> paneManager.switchPanes(contentPane, FxmlView.VRSTE_OBUSTAVA))
                .addMenuItem("Vrste prekovremenih radova", () -> paneManager.switchPanes(contentPane, FxmlView.VRSTE_PREKOVREMENIH_RADOVA))
                .addMenuItem("Vrste davanja", () -> paneManager.switchPanes(contentPane, FxmlView.VRSTE_DAVANJA))
                .addMenuItem("Vrste olakšica", () -> paneManager.switchPanes(contentPane, FxmlView.VRSTE_OLAKSICA))
                .addMenuItem("Studentski poslovi cjenik", () -> paneManager.switchPanes(contentPane, FxmlView.STUDENTSKI_POSLOVI_CJENIK))
                .addMenuItem("Porezi", () -> paneManager.switchPanes(contentPane, FxmlView.POREZI))
                .addMenuItem("Parametri za obračun plaće", () -> stageManager.showSecondaryStage(FxmlView.PARAMETRI_ZA_OBRACUN_PLACE))
                .addMenu("Zaposlenici")
                .addMenuItem("Zaposlenici", () -> paneManager.switchPanes(contentPane, FxmlView.ZAPOSLENICI))
                .addMenuItem("Povijest prijava", () -> paneManager.switchPanes(contentPane, FxmlView.POVIJEST_PRIJAVA))
                .addMenu("Poslovni partneri")
                .addMenuItem("Poslovni partneri", () -> paneManager.switchPanes(contentPane, FxmlView.POSLOVNI_PARTNERI))
                .addMenu("Prihodi i rashodi")
                .addMenuItem("Kategorije transakcija", () -> paneManager.switchPanes(contentPane, FxmlView.KATEGORIJE_TRANSAKCIJA))
                .addMenuItem("Transakcije", () -> paneManager.switchPanes(contentPane, FxmlView.TRANSAKCIJE))
                .addMenu("Obračuni")
                .addMenuItem("Obračuni ugovora", () -> paneManager.switchPanes(contentPane, FxmlView.OBRACUNI_UGOVORA))
                .build();
    }

    private MenuBarResultDto createOsobljeMenu() {
        return new MenuBarBuilder(0)
                .addMenu(imeIPrezimeUlogiranogZaposlenika)
                .addMenuItem("Dashboard", () -> paneManager.switchPanes(contentPane, FxmlView.OSOBLJE_DASHBOARD, prijavljeniZaposlenikDto.getZaposlenik()))
                .addMenuItem("Vaši podaci", () -> paneManager.switchPanes(contentPane, FxmlView.OPSEZNI_PODACI_ZAPOSLENIKA, prijavljeniZaposlenikDto))
                .addMenuItem("Odjava", () -> doLogout())
                .addMenuItem("Izlaz", () -> System.exit(0))
                .build();
    }

    public void doLogout() {
        AppUtils.clearPrijavljeniZaposlenik();
        stageManager.showPrimaryStage(NodeUtils.getStageFromNode(contentPane), FxmlView.PRIJAVA);
    }
}
