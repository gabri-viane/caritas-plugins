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
package theopenhand.plugins.prodotti.window.confs.shower;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.controllers.prodotti.ConfezioniController;
import theopenhand.plugins.prodotti.data.Confezione;
import theopenhand.plugins.prodotti.data.holders.ConfezioneHolder;
import theopenhand.plugins.prodotti.window.pickers.ConfPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class ConfShower extends AnchorPane implements DialogComponent,Refreshable {

    @FXML
    private Hyperlink changeConfHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private TextField confNameTB;

    @FXML
    private Button deleteConfBTN;

    @FXML
    private Hyperlink editConfHL;

    @FXML
    private Label modifingLBL;

    @FXML
    private Label confNameLBL;

    @FXML
    private Button saveModBTN;

    @FXML
    private Hyperlink updateValuesHL;

    private Confezione c;
    private boolean editing = false;
    private final ConfezioniController controller;

    public ConfShower() {
        controller = SharedReferenceQuery.getController(ConfezioniController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/confs/shower/ConfShower.fxml");
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
        editConfHL.setOnAction((a) -> {
            editConfHL.setVisited(false);
            onRefresh(true);
            editable(!editing);
        });
        changeConfHL.setOnAction(a -> {
            PickerDialogCNTRL<Confezione, ConfezioneHolder> pdcntrl = ConfPicker.createPicker();
            Stage s = DialogCreator.showDialog(pdcntrl, pdcntrl.getPrefWidth(), pdcntrl.getPrefHeight(), "Seleziona confezione");
            ClickListener cl = () -> {
                s.close();
            };
            pdcntrl.onExitPressed(cl);
            pdcntrl.onAcceptPressed(() -> {
                c = pdcntrl.getValue();
                cl.onClick();
                onRefresh(true);
            });
            pdcntrl.onRefresh(true);
            if (c != null) {
                pdcntrl.select(c);
            }
            changeConfHL.setVisited(false);
        });
        deleteConfBTN.setOnAction(a -> deleteConf());
        saveModBTN.setOnAction(a -> updateConf());
        editable(false);
        changeConfHL.fire();
    }

    @Override
    public void onRefresh(boolean reload) {
        if (c != null) {
            if (reload) {
                controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 2, Confezione.class, c).orElse(null));
            }
            c = controller.getRH().find(c.getID().longValue());
            if (c != null) {
                clearAll();
                setData(c);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "La confezione selezionata non è correttamente registrata.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stata selezionata nessuna confezione per cui visualizzare i dati.", null);
        }
    }

    private void deleteConf() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando la confezione eliminerai anche tutti i dati ad essa correlati:\n\t-Prodotti\n\t-Elementi nelle borse\n\t-Registri entrate\n\t-Magazzino\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((b) -> {
            if (b.equals(ButtonType.YES)) {
                if (c != null) {
                    controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 5, Confezione.class, c).orElse(null));
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stata selezionata nessuna confezione da eliminare.", null);
                }
                reset();
            }
        });
    }

    private void updateConf() {
        if (c != null) {
            c.setDimensione(confNameTB.getText());
            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 4, Confezione.class, c).orElse(null);
            onRefresh(true);
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati della confezione sono stati aggiornati.", null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stata selezionata nessuna confezione da modificare.", null);
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        confNameTB.setEditable(ed);
        saveModBTN.setDisable(!ed);
        deleteConfBTN.setDisable(!ed);
        modifingLBL.setVisible(ed);
        changeConfHL.setDisable(ed);
        updateValuesHL.setDisable(ed);
        if (ed) {
            editConfHL.setText("Scarta modifiche");
        } else {
            editConfHL.setText("Modifica");
        }
    }

    private void clearAll() {
        confNameLBL.setText("Seleziona confezione");
        confNameTB.setText(null);
    }

    private void reset() {
        clearAll();
        c = null;
        editable(false);
        changeConfHL.fire();
    }

    public void setData(Confezione conf) {
        if (conf != null) {
            confNameLBL.setText(conf.getDimensione());
            confNameTB.setText(conf.getDimensione());
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
        return "Mostra Confezione";
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
