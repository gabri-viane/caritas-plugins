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
package theopenhand.plugins.prodotti.window.mots.shower;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.controllers.prodotti.MotiviController;
import theopenhand.plugins.prodotti.data.Motivo;
import theopenhand.plugins.prodotti.data.holders.MotivoHolder;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.pickers.MotPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class MotShower extends AnchorPane implements DialogComponent,Refreshable {

    @FXML
    private Hyperlink changeMotHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private TextField nameTF;

    @FXML
    private TextArea descTF;

    @FXML
    private Button deleteMotBTN;

    @FXML
    private Hyperlink editMotHL;

    @FXML
    private Label modifingLBL;

    @FXML
    private Label motNameLBL;

    @FXML
    private Button saveModBTN;

    @FXML
    private Hyperlink updateValuesHL;

    private Motivo m;
    private boolean editing = false;
    private final MotiviController controller;

    public MotShower() {
        controller = SharedReferenceQuery.getController(MotiviController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/mots/shower/MotShower.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ConfShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearAll();
        updateValuesHL.setOnAction((a) -> {
            updateValuesHL.setVisited(false);
            onRefresh(true);
        });
        editMotHL.setOnAction((a) -> {
            editMotHL.setVisited(false);
            onRefresh(true);
            editable(!editing);
        });
        changeMotHL.setOnAction(a -> {
            PickerDialogCNTRL<Motivo, MotivoHolder> pdcntrl = MotPicker.createPicker();
            Stage s = DialogCreator.showDialog(pdcntrl, pdcntrl.getPrefWidth(), pdcntrl.getPrefHeight(), "Seleziona motivo");
            ClickListener cl = () -> {
                s.close();
            };
            pdcntrl.onExitPressed(cl);
            pdcntrl.onAcceptPressed(() -> {
                m = pdcntrl.getValue();
                cl.onClick();
                onRefresh(true);
            });
            pdcntrl.onRefresh(true);
            if (m != null) {
                pdcntrl.select(m);
            }
            changeMotHL.setVisited(false);
        });
        deleteMotBTN.setOnAction(a -> deleteMot());
        saveModBTN.setOnAction(a -> updateMot());
        editable(false);
        changeMotHL.fire();
    }

    @Override
    public void onRefresh(boolean reload) {
        if (m != null) {
            if (reload) {
                controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 2, Motivo.class, m).orElse(null));
            }
            m = controller.getRH().find(m.getID().longValue());
            if (m != null) {
                clearAll();
                setData(m);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "Il motivo selezionato non è correttamente registrato.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stato selezionato nessun motivo per cui visualizzare i dati.", null);
        }
    }

    private void deleteMot() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando il motivo eliminerai anche tutti i dati ad esso correlati:\n\t-Registri entrate\n\t-Alterazioni magazzino\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((b) -> {
            if (b.equals(ButtonType.YES)) {
                if (m != null) {
                    controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 5, Motivo.class, m).orElse(null));
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stato selezionato nessun motivo da eliminare.", null);
                }
                reset();
            }
        });
    }

    private void updateMot() {
        if (m != null) {
            m.setDescrizione(descTF.getText());
            m.setName(nameTF.getText());
            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 4, Motivo.class, m).orElse(null);
            onRefresh(true);
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati del motivo sono stati aggiornati.", null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stato selezionato nessun motivo da modificare.", null);
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        nameTF.setEditable(ed);
        saveModBTN.setDisable(!ed);
        deleteMotBTN.setDisable(!ed);
        modifingLBL.setVisible(ed);
        changeMotHL.setDisable(ed);
        updateValuesHL.setDisable(ed);
        if (ed) {
            editMotHL.setText("Scarta modifiche");
        } else {
            editMotHL.setText("Modifica");
        }
    }

    private void clearAll() {
        motNameLBL.setText("Seleziona motivo");
        nameTF.setText(null);
        descTF.setText(null);
    }

    private void reset() {
        clearAll();
        m = null;
        editable(false);
        changeMotHL.fire();
    }

    public void setData(Motivo mot) {
        if (mot != null) {
            motNameLBL.setText(mot.getName());
            nameTF.setText(mot.getName());
            descTF.setText(mot.getDescrizione());
        }
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
        return "Mostra Motivo";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        closeHL.setOnAction(a -> cl.onClick());
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {

    }

}
