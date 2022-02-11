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
package theopenhand.plugins.prodotti.window.prods.shower;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.controllers.prodotti.MagazzinoController;
import theopenhand.plugins.prodotti.controllers.prodotti.ProdottiController;
import theopenhand.plugins.prodotti.data.Confezione;
import theopenhand.plugins.prodotti.data.DataBuilder;
import theopenhand.plugins.prodotti.data.ElementoMagazzino;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.ConfezioneHolder;
import theopenhand.plugins.prodotti.data.holders.MagazzinoHolder;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.plugins.prodotti.window.pickers.ConfPicker;
import theopenhand.plugins.prodotti.window.pickers.ProdPicker;
import theopenhand.plugins.prodotti.window.prods.add.ProdRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class ProdShower extends AnchorPane implements DialogComponent {

    @FXML
    private Hyperlink changeProdHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private Button confTypeBTN;

    @FXML
    private Label contProdLBL;

    @FXML
    private Button deleteProdBTN;

    @FXML
    private Hyperlink editProdHL;

    @FXML
    private CheckBox extraCB;

    @FXML
    private CheckBox frescoCB;

    @FXML
    private CheckBox igieneCB;

    @FXML
    private CheckBox magazzinoCB;

    @FXML
    private Label modifingLBL;

    @FXML
    private Hyperlink newProdAs;

    @FXML
    private Label prodNameLBL;

    @FXML
    private TextField prodNameTB;

    @FXML
    private Button saveModBTN;

    @FXML
    private Hyperlink updateValuesHL;

    private Prodotto p;
    private Confezione c;
    private boolean editing = false;

    public ProdShower() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/prods/shower/ProdShower.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ProdShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearAll();
        updateValuesHL.setOnAction((a) -> {
            updateValuesHL.setVisited(false);
            refreshValues();
        });
        editProdHL.setOnAction((a) -> {
            editProdHL.setVisited(false);
            refreshValues();
            editable(!editing);
        });
        newProdAs.setOnAction(a -> {
            createSimilar();
            newProdAs.setVisited(false);
        });
        confTypeBTN.setOnAction(a -> {
            PickerDialogCNTRL<Confezione, ConfezioneHolder> pdcntrl = ConfPicker.createPicker();
            Stage s = DialogCreator.showDialog(pdcntrl, pdcntrl.getPrefWidth(), pdcntrl.getPrefHeight(), "Seleziona confezione");
            ClickListener cl = () -> {
                s.close();
            };
            pdcntrl.onExitPressed(cl);
            pdcntrl.onAcceptPressed(() -> {
                c = pdcntrl.getValue();
                p.setId_confezioni(pdcntrl.getValue().getId());
                p.setNome_confezione(pdcntrl.getValue().getConfezione());
                cl.onClick();
                confTypeBTN.setText(p.getNome_confezione());
            });
            pdcntrl.reloadElements(true);
            if (p != null) {
                pdcntrl.select(DataBuilder.generateConfezioneByID(p.getId_confezioni()));
            }
        });
        changeProdHL.setOnAction(a -> {
            PickerDialogCNTRL<Prodotto, ProdottoHolder> pdcntrl = ProdPicker.createPicker();
            Stage s = DialogCreator.showDialog(pdcntrl, pdcntrl.getPrefWidth(), pdcntrl.getPrefHeight(), "Seleziona prodotto");
            ClickListener cl = () -> {
                s.close();
            };
            pdcntrl.onExitPressed(cl);
            pdcntrl.onAcceptPressed(() -> {
                p = pdcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            pdcntrl.reloadElements(true);
            if (p != null) {
                pdcntrl.select(p);
            }
            changeProdHL.setVisited(false);
        });
        deleteProdBTN.setOnAction(a -> deleteProd());
        saveModBTN.setOnAction(a -> updateProd());
        editable(false);
        changeProdHL.fire();
    }

    private void refreshValues() {
        if (p != null) {
            c = null;
            ProdottiController.rs = (ProdottoHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 3, Prodotto.class, p).orElse(null);
            p = (ProdottiController.rs).find(p.getID().longValue());
            if (p != null) {
                clearAll();
                setData(p);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "Il prodotto selezionato non è correttamente registrato.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stato selezionato nessun prodotto per cui visualizzare i dati.", null);
        }
    }

    private void createSimilar() {
        if (p != null) {
            DialogComponent on_add = new ProdRegister(p);
            DialogCreator.showDialog(on_add, null, null);
        }
    }

    private void deleteProd() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando il prodotto eliminerai anche tutti i dati ad essa correlati:\n\t-Elementi nelle borse\n\t-Registri entrate\n\t-Magazzino\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((b) -> {
            if (b.equals(ButtonType.YES)) {
                if (p != null) {
                    ProdottiController.rs = (ProdottoHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 5, Prodotto.class, p).orElse(null);
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stato selezionato nessun prodotto da eliminare.", null);
                }
                reset();
            }
        });
    }

    private void updateProd() {
        if (p != null) {
            p.setNome(prodNameTB.getText());
            p.setIs_extra(extraCB.isSelected());
            p.setIs_fresco(frescoCB.isSelected());
            p.setIs_igiene(igieneCB.isSelected());
            p.setIs_magazzino(magazzinoCB.isSelected());
            if (c != null) {
                p.setId_confezioni(c.getId());
            }
            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 4, Prodotto.class, p).orElse(null);
            refreshValues();
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati del prodotto sono stati aggiornati.", null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stato selezionato nessun prodotto da modificare.", null);
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        prodNameTB.setEditable(ed);
        extraCB.setDisable(!ed);
        frescoCB.setDisable(!ed);
        igieneCB.setDisable(!ed);
        magazzinoCB.setDisable(!ed);
        confTypeBTN.setDisable(!ed);
        saveModBTN.setDisable(!ed);
        deleteProdBTN.setDisable(!ed);
        modifingLBL.setVisible(ed);
        changeProdHL.setDisable(ed);
        updateValuesHL.setDisable(ed);
        if (ed) {
            editProdHL.setText("Scarta modifiche");
        } else {
            editProdHL.setText("Modifica");
        }
    }

    private void clearAll() {
        prodNameLBL.setText("Seleziona prodotto");
        prodNameTB.setText(null);
        confTypeBTN.setText("Seleziona confezione");
        extraCB.setSelected(false);
        frescoCB.setSelected(false);
        igieneCB.setSelected(false);
        magazzinoCB.setSelected(false);
        contProdLBL.setText("0");
    }

    private void reset() {
        clearAll();
        p = null;
        c = null;
        editable(false);
        changeProdHL.fire();
    }

    public void setData(Prodotto prod) {
        if (prod != null) {
            prodNameLBL.setText(prod.getNome_formatted());
            prodNameTB.setText(prod.getNome());
            if (c != null) {
                confTypeBTN.setText(c.getConfezione());
            } else {
                confTypeBTN.setText(prod.getNome_confezione());
            }
            frescoCB.setSelected(prod.getIs_fresco());
            extraCB.setSelected(prod.getIs_extra());
            magazzinoCB.setSelected(prod.getIs_magazzino());
            igieneCB.setSelected(prod.getIs_igiene());
            if (prod.getIs_magazzino()) {
                MagazzinoController.rs = (MagazzinoHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 6, ElementoMagazzino.class, new ElementoMagazzino(prod.getID().longValue(), null, null)).orElse(null);
                if (MagazzinoController.rs != null) {
                    ElementoMagazzino lastInsert = MagazzinoController.rs.getLastInsert();
                    contProdLBL.setText(lastInsert.getTotale().toString());
                }
            } else {
                contProdLBL.setText("Il prodotto non è registrato a magazzino");
            }
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
        return "Mostra Prodotto";
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
