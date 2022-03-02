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
package theopenhand.plugins.prodotti.window.dons.shower;

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
import theopenhand.plugins.prodotti.controllers.prodotti.DonatoriController;
import theopenhand.plugins.prodotti.data.Donatore;
import theopenhand.plugins.prodotti.data.holders.DonatoreHolder;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.pickers.DonPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class DonShower extends AnchorPane implements DialogComponent, Refreshable {

    @FXML
    private Hyperlink changeDonHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private TextField nameTF;

    @FXML
    private TextArea descTF;

    @FXML
    private Button deleteDonBTN;

    @FXML
    private Hyperlink editDonHL;

    @FXML
    private Label modifingLBL;

    @FXML
    private Label donNameLBL;

    @FXML
    private Button saveModBTN;

    @FXML
    private Hyperlink updateValuesHL;

    private Donatore d;
    private boolean editing = false;
    private final DonatoriController controller;

    public DonShower() {
        controller = SharedReferenceQuery.getController(DonatoriController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/dons/shower/DonShower.fxml");
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
        editDonHL.setOnAction((a) -> {
            editDonHL.setVisited(false);
            onRefresh(true);
            editable(!editing);
        });
        changeDonHL.setOnAction(a -> {
            PickerDialogCNTRL<Donatore, DonatoreHolder> pdcntrl = DonPicker.createPicker();
            Stage s = DialogCreator.showDialog(pdcntrl, pdcntrl.getPrefWidth(), pdcntrl.getPrefHeight(), "Seleziona donatore");
            ClickListener cl = () -> {
                s.close();
            };
            pdcntrl.onExitPressed(cl);
            pdcntrl.onAcceptPressed(() -> {
                d = pdcntrl.getValue();
                cl.onClick();
                onRefresh(true);
            });
            pdcntrl.onRefresh(true);
            if (d != null) {
                pdcntrl.select(d);
            }
            changeDonHL.setVisited(false);
        });
        deleteDonBTN.setOnAction(a -> deleteDon());
        saveModBTN.setOnAction(a -> updateDon());
        editable(false);
        changeDonHL.fire();
    }

    @Override
    public void onRefresh(boolean reload) {
        if (d != null) {
            if (reload) {
                controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 2, Donatore.class, d).orElse(null));
            }
            d = controller.getRH().find(d.getID().longValue());
            if (d != null) {
                clearAll();
                setData(d);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "Il donatore selezionato non è correttamente registrato.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stato selezionato nessun donatore per cui visualizzare i dati.", null);
        }
    }

    private void deleteDon() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando il donatore eliminerai anche tutti i dati ad esso correlati:\n\t-Registri entrate\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((b) -> {
            if (b.equals(ButtonType.YES)) {
                if (d != null) {
                    controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 5, Donatore.class, d).orElse(null));
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stato selezionato nessun donatore da eliminare.", null);
                }
                reset();
            }
        });
    }

    private void updateDon() {
        if (d != null) {
            d.setDescrizione(descTF.getText());
            d.setName(nameTF.getText());
            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 3, Donatore.class, d).orElse(null);
            onRefresh(true);
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati del donatore sono stati aggiornati.", null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stato selezionato nessun donatore da modificare.", null);
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        nameTF.setEditable(ed);
        descTF.setEditable(ed);
        saveModBTN.setDisable(!ed);
        deleteDonBTN.setDisable(!ed);
        modifingLBL.setVisible(ed);
        changeDonHL.setDisable(ed);
        updateValuesHL.setDisable(ed);
        if (ed) {
            editDonHL.setText("Scarta modifiche");
        } else {
            editDonHL.setText("Modifica");
        }
    }

    private void clearAll() {
        donNameLBL.setText("Seleziona donatore");
        nameTF.setText(null);
        descTF.setText(null);
    }

    private void reset() {
        clearAll();
        d = null;
        editable(false);
        changeDonHL.fire();
    }

    public void setData(Donatore don) {
        if (don != null) {
            donNameLBL.setText(don.getName());
            nameTF.setText(don.getName());
            descTF.setText(don.getDescrizione());
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
        return "Mostra Donatore";
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
