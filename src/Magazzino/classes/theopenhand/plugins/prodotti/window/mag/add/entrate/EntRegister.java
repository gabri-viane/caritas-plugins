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
package theopenhand.plugins.prodotti.window.mag.add.entrate;

import java.io.IOException;
import java.math.BigInteger;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import theopenhand.commons.DataUtils;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.data.DataBuilder;
import theopenhand.plugins.prodotti.data.Donatore;
import theopenhand.plugins.prodotti.data.ElementoMagazzino;
import theopenhand.plugins.prodotti.data.Entrata;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.DonatoreHolder;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.plugins.prodotti.window.pickers.DonPicker;
import theopenhand.plugins.prodotti.window.pickers.ProdPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.objects.TextFieldBuilder;

/**
 *
 * @author gabri
 */
public class EntRegister extends AnchorPane implements DialogComponent {

    @FXML
    private DatePicker dateEntDP;

    @FXML
    private Button exitBTN;

    @FXML
    private TextField qntEntrTB;

    @FXML
    private Button registerBTN;

    @FXML
    private Button selectDonBTN;

    @FXML
    private Button selectProdBTN;

    private Entrata e;
    private Prodotto pr;
    private Donatore dn;

    private boolean creator_enabled = false;

    public EntRegister() {
        init();
    }

    public EntRegister(Entrata e) {
        if (e != null) {
            this.e = new Entrata(e.getTotale(), e.getData_arrivo(), e.getId_prodotto().longValue(), e.getId_donatore().longValue());
            this.e.setNome_donatore(e.getNome_donatore());
            this.e.setNome_prodotto(e.getNome_prodotto());
            this.creator_enabled = true;
        }
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/mag/add/entrate/EntRegister.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(EntRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        TextFieldBuilder.transformNumericField(qntEntrTB);
        qntEntrTB.setText("1");
        registerBTN.setOnAction(a -> saveProd());
        selectDonBTN.setOnAction(a -> {
            PickerDialogCNTRL<Donatore, DonatoreHolder> ppcntrl = DonPicker.createPicker();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona donatore");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                dn = ppcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            ppcntrl.onRefresh(true);
            if (dn != null) {
                ppcntrl.select(DataBuilder.generateDonatoreByID(dn.getId()));
            }
        });
        selectProdBTN.setOnAction(a -> {
            PickerDialogCNTRL<Prodotto, ProdottoHolder> ppcntrl = ProdPicker.createPicker();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona prodotto");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                pr = ppcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            ppcntrl.onRefresh(true);
            if (pr != null) {
                ppcntrl.select(DataBuilder.generateProdottoByID(pr.getId()));
            }
        });
        dateEntDP.setValue(LocalDate.now());
        if (creator_enabled) {
            refreshValues();
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Cambiare dati", "Per continuare è necessario cambiare la data selezionata.", null);
        }
    }

    public void selectProd(ElementoMagazzino p){
        if(p!=null){
            pr = DataBuilder.generateProdottoByID(p.getId_prodotto());
            pr.setNome(p.getNome());
            refreshValues();
        }
    }
    
    private void saveProd() {
        String ent_qnt = qntEntrTB.getText();
        LocalDate ld = dateEntDP.getValue();
        if (pr != null) {
            if (dn != null) {
                BigInteger iddon = dn.getId();
                BigInteger idprod = pr.getId();
                Integer qnt;
                try {
                    qnt = Integer.parseInt(ent_qnt);
                } catch (NumberFormatException nfe) {
                    qnt = null;
                }
                if (ent_qnt != null && !ent_qnt.isEmpty() && qnt != null && qnt > 0) {
                    if (idprod != null && idprod.longValue() > -1) {
                        if (iddon != null && iddon.longValue() > -1) {
                            if (ld != null) {
                                e = new Entrata(qnt, DataUtils.toDate(ld), idprod.longValue(), iddon.longValue());
                                ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 1, Entrata.class, e);
                                if (after_accept != null) {
                                    after_accept.onClick();
                                }
                            } else {
                                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati dell'entrata (data arrivo) non validi", null);
                            }
                        } else {
                            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati dell'entrata (donatore) non validi", null);
                        }
                    } else {
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati dell'entrata (prodotto) non validi", null);
                    }

                } else {
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Quantità del prodotto non valida", null);
                }
            } else {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare un donatore per procedere alla registrazione", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare un prodotto per procedere alla registrazione", null);
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
        qntEntrTB.setText("1");
        selectProdBTN.setText("Seleziona prodotto");
        selectDonBTN.setText("Seleziona donatore");
        dateEntDP.setValue(LocalDate.now());
        dn = null;
        pr = null;
        e = null;
    }

    private void refreshValues() {
        if (dn != null) {
            selectDonBTN.setText(dn.getName());
        }
        if (pr != null) {
            selectProdBTN.setText(pr.getNome());
        }
        if (e != null) {
            qntEntrTB.setText("" + e.getTotale());
            dateEntDP.setValue(DataUtils.toLocalDate(e.getData_arrivo()));
            if (dn == null) {
                selectDonBTN.setText(e.getNome_donatore());
                dn = DataBuilder.generateDonatoreByID(e.getId_donatore());
            }
            if (pr == null) {
                selectProdBTN.setText(e.getNome_prodotto());
                pr = DataBuilder.generateProdottoByID(e.getId_prodotto());
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
        return "Registra Entrata";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

}
