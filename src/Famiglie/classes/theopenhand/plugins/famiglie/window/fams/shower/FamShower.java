/*
 * Copyright 2021 gabri.
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
package theopenhand.plugins.famiglie.window.fams.shower;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.connector.runtimes.PluginSettings;
import theopenhand.plugins.famiglie.controllers.famiglie.ComponentiController;
import theopenhand.plugins.famiglie.controllers.famiglie.FamiglieController;
import theopenhand.plugins.famiglie.data.Componente;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.comps.component.ShortCompElement;
import theopenhand.plugins.famiglie.window.pickers.FamPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.dialogs.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class FamShower extends AnchorPane implements DialogComponent, ValueHolder<Famiglia> {

    @FXML
    private TextField addressTb;

    @FXML
    private Hyperlink changeFamHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private ScrollPane compSP;

    @FXML
    private VBox compVB;

    @FXML
    private VBox componentsVB;

    @FXML
    private Button deleteFamBTN;

    @FXML
    private TextField dichNameTB;

    @FXML
    private TextField dichSurnTB;

    @FXML
    private Hyperlink editFamHL;

    @FXML
    private Label idFamLBL;

    @FXML
    private Label modifingLBL;

    @FXML
    private TextField phoneTB;

    @FXML
    private Button saveModBTN;

    @FXML
    private Hyperlink updateValuesHL;
    private DisplayTableValue<Componente> table;

    public FamShower() {
        init();
    }

    public FamShower(Famiglia f) {
        this.f = f;
        init();
    }

    Famiglia f;
    private boolean editing = false;

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/famiglie/window/fams/shower/FamShower.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FamShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearAll();
        updateValuesHL.setOnAction((a) -> {
            updateValuesHL.setVisited(false);
            refreshValues();
        });
        editFamHL.setOnAction((a) -> {
            editFamHL.setVisited(false);
            refreshValues();
            editable(!editing);
        });
        changeFamHL.setOnAction(a -> {
            PickerDialogCNTRL<Famiglia, FamigliaHolder> frcntrl = FamPicker.createPicker();
            /*FamPickerCNTRL frcntrl = new FamPickerCNTRL();*/
            Stage s = DialogCreator.showDialog(frcntrl, frcntrl.getPrefWidth(), frcntrl.getPrefHeight(), "Seleziona famiglia");
            ClickListener cl = () -> {
                s.close();
            };
            frcntrl.onExitPressed(cl);
            frcntrl.onAcceptPressed(() -> {
                f = frcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            frcntrl.reloadElements(true);
            if (f != null) {
                frcntrl.select(f);
            }
            changeFamHL.setVisited(false);
        });

        table = ElementCreator.generateTable(Componente.class, ComponentiController.rs);
        PluginSettings.table_prop.addListener((Boolean previus_value, Boolean new_value) -> {
            enableTable(new_value);
        });
        enableTable(PluginSettings.table_prop.getValue());
        deleteFamBTN.setOnAction(a -> deleteFam());
        saveModBTN.setOnAction(a -> updateFam());
        editable(false);
        if (f == null) {
            changeFamHL.fire();
        } else {
            refreshValues();
        }
    }

    private void enableTable(boolean new_value) {
        if (new_value) {
            var vb = compVB.getChildren();
            vb.remove(compSP);
            vb.add(table);
        } else {
            var vb = compVB.getChildren();
            vb.remove(table);
            if (!vb.contains(compSP)) {
                vb.add(compSP);
            }
        }
    }

    private void refreshValues() {
        if (f != null) {
            FamiglieController.rs = (FamigliaHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 3, Famiglia.class, f).orElse(null);
            f = ((FamigliaHolder) FamiglieController.rs).find(f.getID().longValue());
            if (f != null) {
                clearAll();
                setData(f);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "La famiglia selezionata non è correttamente registrata.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stata selezionata nessuna famiglia per cui visualizzare i dati.", null);
        }
    }

    private void deleteFam() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando la famiglia eliminerai anche tutti i dati ad essa correlati:\n\t-Informazioni famiglia\n\t-Componenti\n\t-Storico borse\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((b) -> {
            if (b.equals(ButtonType.YES)) {
                if (f != null) {
                    FamiglieController.rs = (FamigliaHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 5, Famiglia.class, f).orElse(null);
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stata selezionata nessuna famiglia da eliminare.", null);
                }
                reset();
            }
        });
    }

    private void updateFam() {
        if (f != null) {
            f.setCogn_dic(dichSurnTB.getText());
            f.setNome_dic(dichNameTB.getText());
            f.setIndirizzo(addressTb.getText());
            f.setTelefono(phoneTB.getText());
            ConnectionExecutor.getInstance().executeCall(PluginRegisterFamiglie.frr, 4, Famiglia.class, f).orElse(null);
            refreshValues();
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati della famiglia sono stati aggiornati.", null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stata selezionata nessuna famiglia da modificare.", null);
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        dichNameTB.setEditable(ed);
        dichSurnTB.setEditable(ed);
        addressTb.setEditable(ed);
        phoneTB.setEditable(ed);
        saveModBTN.setDisable(!ed);
        deleteFamBTN.setDisable(!ed);
        modifingLBL.setVisible(ed);
        changeFamHL.setDisable(ed);
        updateValuesHL.setDisable(ed);
        if (ed) {
            editFamHL.setText("Scarta modifiche");
        } else {
            editFamHL.setText("Modifica");
        }
    }

    private void clearAll() {
        idFamLBL.setText(null);
        dichNameTB.setText(null);
        dichSurnTB.setText(null);
        addressTb.setText(null);
        phoneTB.setText(null);
        componentsVB.getChildren().clear();
    }

    private void reset() {
        clearAll();
        f = null;
        editable(false);
        changeFamHL.fire();
    }

    public void setData(Famiglia fam) {
        if (fam != null) {
            idFamLBL.setText("" + fam.getIdfam());
            dichNameTB.setText(fam.getNome_dic());
            dichSurnTB.setText(fam.getCogn_dic());
            addressTb.setText(fam.getIndirizzo());
            phoneTB.setText(fam.getTelefono());
            ComponentiController.rs = ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 0, Componente.class, new Componente(fam.getIdfam(), null, null, null, 0)).orElse(null);
            if (ComponentiController.rs != null) {
                if (!PluginSettings.table_prop.getValue()) {
                    ComponentiController.rs.getList().forEach(c -> {
                        componentsVB.getChildren().add(new ShortCompElement(c));
                    });
                } else {
                    table.setData(ComponentiController.rs.getList());
                }
            }
        }
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        closeHL.setOnAction(a -> cl.onClick());
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {

    }

    @Override
    public Famiglia getValue() {
        return f;
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

    @Override
    public double getDialogWidth() {
        return getPrefWidth();
    }

    @Override
    public double getDialogHeight() {
        return getPrefHeight();
    }

    @Override
    public String getTitle() {
        return "Mostra Famiglie";
    }

}
