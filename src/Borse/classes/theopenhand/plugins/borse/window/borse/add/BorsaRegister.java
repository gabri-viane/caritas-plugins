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
package theopenhand.plugins.borse.window.borse.add;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import theopenhand.commons.DataUtils;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.famiglie.data.DataBuilder;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.pickers.FamPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class BorsaRegister extends AnchorPane implements DialogComponent {

    @FXML
    private DatePicker dateConsDP;

    @FXML
    private Button exitBTN;

    @FXML
    private TextArea noteTA;

    @FXML
    private Button registerBTN;

    @FXML
    private Button selectFamBTN;

    private Borsa b;
    private Famiglia f;

    private boolean creator_enabled = false;

    public BorsaRegister() {
        init();
    }

    public BorsaRegister(Borsa e) {
        if (e != null) {
            this.b = new Borsa(e.getId_fam().longValue(), e.getConsegna(), e.getNote(), e.getConsegnata());
            this.b.setDichiarante(e.getDichiarante());
            this.creator_enabled = true;
        }
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/borse/window/borse/add/BorsaRegister.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BorsaRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        registerBTN.setOnAction(a -> saveBorsa());
        selectFamBTN.setOnAction(a -> {
            PickerDialogCNTRL<Famiglia, FamigliaHolder> ppcntrl = FamPicker.createPicker();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona famiglia");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                f = ppcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            ppcntrl.onRefresh(true);
            if (f != null) {
                ppcntrl.select(DataBuilder.generateFamigliaByID(f.getId()));
            }
        });
        dateConsDP.setValue(LocalDate.now());
        if (creator_enabled) {
            refreshValues();
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Cambiare dati", "Per continuare è necessario cambiare la data selezionata.", null);
        }
    }

    private void saveBorsa() {
        String note = noteTA.getText();
        LocalDate ld = dateConsDP.getConverter().fromString(dateConsDP.getEditor().getText());
        if (f != null) {
            Long idfam = f.getIdfam();
            if (idfam != null && idfam > -1) {
                if (ld != null && ld.isAfter(LocalDate.now().minusDays(1))) {
                    b = new Borsa(idfam, DataUtils.toDate(ld), note, false);
                    ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 1, Borsa.class, b);
                    if (b.getID() == null || b.getID().longValue() < 0) {
                        DialogCreator.showAlert(Alert.AlertType.WARNING, "Operazione fallita", null, null);
                    } else {
                        if (after_accept != null) {
                            after_accept.onClick();
                        }
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Operazione completata", null, null);
                        clearFields();
                    }
                } else {
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare una data di consegna valida.\n(Ammessa solo odierna o futura)", null);
                }
            } else {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare una famiglia valida.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare una famiglia per procedere alla creazione.", null);
        }

    }

    @Override
    public void onExitPressed(ClickListener cl) {
        exitBTN.setOnAction(a -> cl.onClick());
    }

    ClickListener after_accept = () -> {
    };

    private void clearFields() {
        selectFamBTN.setText("Seleziona famiglia");
        dateConsDP.setValue(LocalDate.now());
        noteTA.setText("");
        f = null;
        b = null;
    }

    private void refreshValues() {
        if (f != null) {
            selectFamBTN.setText(f.getDichiarante());
        }
        if (b != null) {
            dateConsDP.setValue(DataUtils.toLocalDate(b.getConsegna()));
            noteTA.setText(b.getNote());
            if (f == null) {
                selectFamBTN.setText(b.getDichiarante());
                f = DataBuilder.generateFamigliaByID(b.getId_fam());
            }
        }
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {
        after_accept = cl;
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
        return "Nuova Borsa";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

}
