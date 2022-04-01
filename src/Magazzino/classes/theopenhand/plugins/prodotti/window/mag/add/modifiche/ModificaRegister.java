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
package theopenhand.plugins.prodotti.window.mag.add.modifiche;

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
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.data.DataBuilder;
import theopenhand.plugins.prodotti.data.ElementoMagazzino;
import theopenhand.plugins.prodotti.data.ModificaMagazzino;
import theopenhand.plugins.prodotti.data.Motivo;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.MotivoHolder;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.plugins.prodotti.window.pickers.MotPicker;
import theopenhand.plugins.prodotti.window.pickers.ProdPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;

/**
 *
 * @author gabri
 */
public class ModificaRegister extends AnchorPane implements DialogComponent {

    @FXML
    private Button exitBTN;

    @FXML
    private TextField qntModTB;

    @FXML
    private Button registerBTN;

    @FXML
    private Button selectMotBTN;

    @FXML
    private Button selectProdBTN;

    @FXML
    private CheckBox subtractCB;

    private ModificaMagazzino mm;
    private Prodotto pr;
    private Motivo mt;

    private boolean creator_enabled = false;

    public ModificaRegister() {
        init();
    }

    public ModificaRegister(ModificaMagazzino mm) {
        if (mm != null) {
            this.mm = new ModificaMagazzino(mm.getId_prodotto().longValue(), mm.getId_motivi() != null ? mm.getId_motivi().longValue() : -1l, mm.getTotale(), mm.getSottrai());
            this.mm.setProdotto(mm.getProdotto());
            this.pr = DataBuilder.generateProdottoByID(mm.getId_prodotto());
            this.pr.setNome(mm.getProdotto());
            if (mm.getId_motivi() != null) {
                this.mm.setMotivo(mm.getMotivo());
            }
            this.creator_enabled = true;
        }
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/mag/add/modifiche/ModRegister.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ModificaRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        ElementCreator.transformNumericField(qntModTB);
        qntModTB.setText("1");
        registerBTN.setOnAction(a -> saveModifica());
        selectMotBTN.setOnAction(a -> {
            PickerDialogCNTRL<Motivo, MotivoHolder> ppcntrl = MotPicker.createPicker();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona motivo");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                mt = ppcntrl.getValue();
                cl.onClick();
                refreshValues();
            });
            ppcntrl.onRefresh(true);
            if (mt != null) {
                ppcntrl.select(DataBuilder.generateMotivoByID(mt.getId()));
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
        subtractCB.setSelected(false);
        if (creator_enabled) {
            refreshValues();
        }
    }

    public void selectProd(ElementoMagazzino p) {
        if (p != null) {
            pr = DataBuilder.generateProdottoByID(p.getId_prodotto());
            pr.setNome(p.getNome());
            refreshValues();
        }
    }

    private void saveModifica() {
        String ent_qnt = qntModTB.getText();
        Boolean sub = subtractCB.isSelected();
        if (pr != null) {
            if (mt != null) {
                BigInteger idmot = mt.getId();
                BigInteger idprod = pr.getId();
                Long qnt;
                try {
                    qnt = Long.parseLong(ent_qnt);
                } catch (NumberFormatException nfe) {
                    qnt = null;
                }
                if (ent_qnt != null && !ent_qnt.isEmpty() && qnt != null && qnt > 0) {
                    if (idprod != null && idprod.longValue() > -1) {
                        if (idmot != null && idmot.longValue() > -1) {
                            mm = new ModificaMagazzino(idprod.longValue(), idmot.longValue(), qnt, sub);
                            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 1, ModificaMagazzino.class, mm);
                            if (after_accept != null) {
                                after_accept.onClick();
                            }
                            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Operazione completata", null, null);
                        } else {
                            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati della modifica (motivo) non validi", null);
                        }
                    } else {
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati della modifica (prodotto) non validi", null);
                    }

                } else {
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Quantità del prodotto non valida", null);
                }
            } else {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Selezionare un motivo per procedere alla registrazione", null);
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
        qntModTB.setText("1");
        selectProdBTN.setText("Seleziona prodotto");
        selectMotBTN.setText("Seleziona motivo");
        subtractCB.setSelected(false);
        mt = null;
        pr = null;
        mm = null;
    }

    private void refreshValues() {
        if (mt != null) {
            selectMotBTN.setText(mt.getName());
        }
        if (pr != null) {
            selectProdBTN.setText(pr.getNome());
        }
        if (mm != null) {
            if (mm.getTotale() != null) {
                qntModTB.setText("" + mm.getTotale());
            }
            if (mm.getSottrai() != null) {
                subtractCB.setSelected(mm.getSottrai());
            }
            if (mt == null && mm.getId_motivi() != null) {
                selectMotBTN.setText(mm.getMotivo());
                mt = DataBuilder.generateMotivoByID(mm.getId_motivi());
            }
            if (pr == null) {
                selectProdBTN.setText(mm.getProdotto());
                pr = DataBuilder.generateProdottoByID(mm.getId_prodotto());
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
        return "Modifica magazzino";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

}
