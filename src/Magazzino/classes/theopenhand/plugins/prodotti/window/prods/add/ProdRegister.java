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
package theopenhand.plugins.prodotti.window.prods.add;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static theopenhand.commons.DataUtils.softValidText;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.data.Confezione;
import theopenhand.plugins.prodotti.data.DataBuilder;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.ConfezioneHolder;
import theopenhand.plugins.prodotti.window.pickers.ConfPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class ProdRegister extends AnchorPane implements DialogComponent {

    @FXML
    private Button exitBTN;

    @FXML
    private CheckBox extraCB;

    @FXML
    private CheckBox frescoCB;

    @FXML
    private CheckBox igieneCB;

    @FXML
    private CheckBox magCB;

    @FXML
    private TextField nameProdTB;

    @FXML
    private Button registerBTN;

    @FXML
    private Button selectConfBTN;

    private Prodotto p;
    private Confezione c;
    
    private boolean creator_enabled = false;

    public ProdRegister() {
        init();
    }

    public ProdRegister(Prodotto p) {
        if (p != null) {
            this.p = new Prodotto(p.getNome(), p.getId_confezioni().longValue(), p.getIs_magazzino(), p.getIs_fresco(), p.getIs_igiene(), p.getIs_extra());
            this.creator_enabled = true;
        }
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/prods/add/ProdRegister.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ProdRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        magCB.setSelected(true);
        registerBTN.setOnAction(a -> saveProd());
        selectConfBTN.setOnAction(a -> {
            PickerDialogCNTRL<Confezione, ConfezioneHolder> ppcntrl = ConfPicker.createPicker();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona confezione");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                c = ppcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            ppcntrl.onRefresh(true);
            if (c != null) {
                ppcntrl.select(DataBuilder.generateConfezioneByID(c.getId()));
            }
        });
        magCB.selectedProperty().addListener((o) -> {
            if (!magCB.isSelected()) {
                DialogCreator.showAlert(Alert.AlertType.WARNING, "Attenzione!", "Se non si registra il prodotto a magazzino non si potrà tenere conto nel registro entrate.", null);
            }
        });
        if(creator_enabled){
            refreshValues();
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Cambiare dati", "Per continuare è necessario cambiare la confezione selezionata.", null);
        }
    }

    private void saveProd() {
        String prod_name = nameProdTB.getText();
        boolean ismag = magCB.isSelected();
        boolean isextra = extraCB.isSelected();
        boolean isfresco = frescoCB.isSelected();
        boolean isigiene = igieneCB.isSelected();
        if (c != null) {
            BigInteger idconf = c.getId();
            if (softValidText(prod_name)) {
                if (idconf != null && idconf.longValue() > -1) {
                    p = new Prodotto(prod_name, idconf.longValue(), ismag, isfresco, isigiene, isextra);
                    ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 1, Prodotto.class, p);
                    if (after_accept != null) {
                        after_accept.onClick();
                    }
                } else {
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati della prodotto (confezione) non validi", null);
                }
            } else {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Nome del prodotto non valido", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare una confezione per procedere alla registrazione", null);
        }

    }

    @Override
    public void onExitPressed(ClickListener cl) {
        exitBTN.setOnAction(a -> cl.onClick());
    }

    ClickListener after_accept = () -> {
        clearFields();
    };

    private void clearFields() {
        nameProdTB.clear();
        selectConfBTN.setText("Seleziona confezione");
        magCB.setSelected(true);
        frescoCB.setSelected(false);
        extraCB.setSelected(false);
        igieneCB.setSelected(false);
        c = null;
        p = null;
    }

    private void refreshValues() {
        if (c != null) {
            selectConfBTN.setText(c.getConfezione());
        }
        if (p != null) {
            nameProdTB.setText(p.getNome());
            magCB.setSelected(p.getIs_magazzino());
            frescoCB.setSelected(p.getIs_fresco());
            extraCB.setSelected(p.getIs_extra());
            igieneCB.setSelected(p.getIs_igiene());
            if (c == null) {
                selectConfBTN.setText(p.getNome_confezione());
                c = DataBuilder.generateConfezioneByID(p.getId_confezioni());
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
        return "Registra Prodotto";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

}
