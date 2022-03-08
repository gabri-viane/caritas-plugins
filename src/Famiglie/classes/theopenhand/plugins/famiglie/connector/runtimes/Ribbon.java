/*
 * Copyright 2022 gabri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theopenhand.plugins.famiglie.connector.runtimes;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.data.Componente;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.comps.CompMain;
import theopenhand.plugins.famiglie.window.comps.component.CompElement;
import theopenhand.plugins.famiglie.window.fams.FamMain;
import theopenhand.plugins.famiglie.window.fams.add.FamRegister;
import theopenhand.plugins.famiglie.window.fams.shower.FamShower;
import theopenhand.plugins.famiglie.window.pickers.FamPicker;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.ribbon.RibbonFactory;
import theopenhand.window.graphics.ribbon.elements.RibbonGroup;

/**
 *
 * @author gabri
 */
public class Ribbon {

    private Famiglia f;
    private final FamigliaRR rr;
    private FamMain fm;
    private CompMain cm;
    private FamShower fs;

    public Ribbon(FamigliaRR rr) {
        this.rr = rr;
        createRibbon();
    }

    private void createRibbon() {
        RibbonGroup reg_grp = RibbonFactory.createGroup(rr, "Crea", "Famiglia");
        RibbonGroup show_grp = RibbonFactory.createGroup(rr, "Visualizza", "Famiglia");
        RibbonGroup hand_grp = RibbonFactory.createGroup(rr, "Gestisci", "Famiglia");

        //Registrazione dati
        Button register_fam = new Button("Famiglia");
        register_fam.setOnAction(a -> {
            createFamiglia();
        });
        Button register_comp = new Button("Componente");
        register_comp.setOnAction(a -> {
            createComponente();
        });
        VBox vb = new VBox(register_fam, register_comp);
        vb.setSpacing(5);
        vb.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vb.setMinHeight(Region.USE_COMPUTED_SIZE);
        reg_grp.addNode(vb);

        //Gestione dati
        Button fam_main = new Button("Elenco Famiglie");
        fam_main.setOnAction(a -> homeFamiglia());
        Button comp_main = new Button("Componenti");
        comp_main.setOnAction(a -> homeComponenti());
        VBox vb2 = new VBox(fam_main, comp_main);
        vb2.setSpacing(5);
        vb2.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vb2.setMinHeight(Region.USE_COMPUTED_SIZE);
        hand_grp.addNode(vb2).addNode(new Button("Parentele"));

        //Visualizza dati
        Button fam_show = new Button("Mostra Famiglia");
        fam_show.setOnAction(a -> showFamiglia());
        show_grp.addNode(fam_show);
    }

    public void selectFamiglia() {
        PickerDialogCNTRL<Famiglia, FamigliaHolder> frcntrl = FamPicker.createPicker();
        Stage s = DialogCreator.createDialog(frcntrl, frcntrl.getPrefWidth(), frcntrl.getPrefHeight(), "Seleziona famiglia");
        ClickListener cl = () -> {
            s.close();
        };
        frcntrl.onExitPressed(cl);
        frcntrl.onAcceptPressed(() -> {
            f = frcntrl.getValue();
            cl.onClick();
        });
        frcntrl.onRefresh(true);
        if (f != null) {
            frcntrl.select(f);
        }
        s.showAndWait();
    }

    public void createFamiglia() {
        FamRegister fr = new FamRegister();
        Stage dialog = DialogCreator.createDialog(fr);
        fr.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void createComponente() {
        selectFamiglia();
        if (f != null) {
            CompElement cecntrl = new CompElement(null);
            cecntrl.setCreationMode();
            Stage showDialog = DialogCreator.showDialog(cecntrl, cecntrl.getPrefWidth(), cecntrl.getPrefHeight(), "Crea componente");
            ClickListener cl = () -> showDialog.close();
            cecntrl.onExitPressed(cl);
            cecntrl.onAcceptPressed(() -> {
                if (f != null) {
                    Componente value = cecntrl.getValue();
                    value.setIdfam(f.getIdfam());
                    ConnectionExecutor.getInstance().executeCall(PluginRegisterFamiglie.frr, 1, Componente.class, value);
                }
                cl.onClick();
            });
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Operazione bloccata", "Per aggiungere un componente è necessario innanzitutto selezionare una famiglia.", null);
        }
    }

    public void homeFamiglia() {
        if (fm == null) {
            fm = new FamMain();
        }
        fm.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(fm);
    }

    public void homeComponenti() {
        if (cm == null) {
            cm = new CompMain();
        }
//        cm.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(cm);
    }

    public void showFamiglia() {
        if (fs == null) {
            fs = new FamShower();
        }
        StaticReferences.getMainWindowReference().setCenterNode(fs);
    }
}
