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
package theopenhand.plugins.famiglie.window.comps.component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import theopenhand.commons.DataUtils;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.controllers.famiglie.ComponentiController;
import theopenhand.plugins.famiglie.data.Componente;
import theopenhand.plugins.famiglie.data.DataBuilder;
import theopenhand.plugins.famiglie.data.Parentela;
import theopenhand.plugins.famiglie.data.holders.ParentelaHolder;
import theopenhand.plugins.famiglie.window.pickers.ParPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class CompElement extends AnchorPane implements ValueHolder<Componente>, DialogComponent {

    @FXML
    private TextField nameTF;

    @FXML
    private TextField surnameTf;

    @FXML
    private Button typeBTN;

    @FXML
    private DatePicker bornDP;

    @FXML
    private Hyperlink editHL;

    @FXML
    private Button deleteBTN;

    @FXML
    private Button updateBTN;

    private Componente c;
    private boolean creation_mode = false;

    public CompElement(Componente comp) {
        c = comp;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/famiglie/window/comps/component/CompElement.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(CompElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (c != null) {
            nameTF.setText(c.getNome_com());
            surnameTf.setText(c.getCogn_com());
            bornDP.setValue(DataUtils.toLocalDate(c.getD_nascita()));
            typeBTN.setText(c.getParentela());
        }
        editHL.setOnAction(a -> {
            if (!editing) {
                refreshValues();
                editable(!editing);
            } else {
                editable(!editing);
                refreshValues();
            }
        });
        typeBTN.setOnAction(a -> {
            PickerDialogCNTRL<Parentela, ParentelaHolder> ppcntrl = ParPicker.createPicker();
            //ParPickerCNTRL ppcntrl = new ParPickerCNTRL();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona parentela");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                c.setId_parentela(ppcntrl.getValue().getId());
                c.setParentela(ppcntrl.getValue().getTipo());
                cl.onClick();
                refreshValues();
            });
            ppcntrl.onRefresh(true);
            if (c != null) {
                ppcntrl.select(DataBuilder.generateParentelaByID(c.getId_parentela()));
            }
        });
        updateBTN.setOnAction(a -> {
            boolean correct = updateValues();
            if (correct && accept_listener != null) {
                accept_listener.onClick();
            }
        });
        deleteBTN.setOnAction(a -> {
            deleteFam();
        });
        editable(false);
    }

    private boolean editing = false;

    public void setCreationMode() {
        c = new Componente();
        editable(true);
        editHL.setVisible(false);
        creation_mode = true;
    }

    private void deleteFam() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando il componente eliminerai anche tutti i dati ad esso correlati.\n\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((b) -> {
            if (b.equals(ButtonType.YES)) {
                if (c != null) {
                    SharedReferenceQuery.getController(ComponentiController.class).setRH(
                            ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 5, Componente.class, c).orElse(null));
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stato selezionato nessun componente da eliminare.", null);
                }
                this.setDisable(true);
                if (exit_listener != null) {
                    exit_listener.onClick();
                }
            }
        });
    }

    private void editable(boolean b) {
        editing = b;
        nameTF.setEditable(b);
        surnameTf.setEditable(b);
        bornDP.setEditable(b);
        typeBTN.setDisable(!b);
        deleteBTN.setDisable(!b);
        updateBTN.setDisable(!b);
        if (editing) {
            editHL.setText("Scarta modifiche");
        } else {
            editHL.setText("Modifica");
        }
    }

    public void setComponent(Componente c) {
        this.c = c;
    }

    @Override
    public Componente getValue() {
        return c;
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

    public void refreshValues() {
        if (c != null) {
            /**
             * Altrimenti se creo il componente e cambio la parentela mi
             * ricarica i dati dal componente vuoto e mi elimina le modifiche
             * scritte
             */
            if (!editing) {
                nameTF.setText(c.getNome_com());
                surnameTf.setText(c.getCogn_com());
                bornDP.setValue(DataUtils.toLocalDate(c.getD_nascita()));
            }
            typeBTN.setText(c.getParentela());
        }
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        if (creation_mode) {
            deleteBTN.setOnAction(a -> cl.onClick());
        } else {
            exit_listener = cl;
        }
    }

    private boolean updateValues() {
        if (c != null) {
            String con_name = nameTF.getText();
            String con_surn = surnameTf.getText();
            LocalDate ld = bornDP.getValue();
            BigInteger id = c.getId_parentela();
            if (DataUtils.softValidText(con_surn) && DataUtils.softValidText(con_name)) {
                if (ld != null && ld.isBefore(LocalDate.now())) {
                    if (id != null && id.longValue() > -1) {
                        c.setCogn_com(surnameTf.getText());
                        c.setNome_com(nameTF.getText());
                        c.setD_nascita(DataUtils.toDate(ld));
                        //La parentela è direttamente gestita dal pulsante, non devo cambiare nulla qui
                        if (!creation_mode && c.getId() != null && c.getID().longValue() > -1) {
                            //Aggiorna valori in database
                            ConnectionExecutor.getInstance().executeCall(PluginRegisterFamiglie.frr, 2, Componente.class, c);
                        }
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati salvati", "I dati del componente sono stati aggiornati con successo! ", null);
                        editable(false);
                        return true;
                    } else {
                        DialogCreator.showAlert(Alert.AlertType.WARNING, "Dati non validi", "La parentela selezionata non è valida.", null);
                    }
                } else {
                    DialogCreator.showAlert(Alert.AlertType.WARNING, "Dati non validi", "La data di nascita immessa non è valida.", null);
                }
            } else {
                DialogCreator.showAlert(Alert.AlertType.WARNING, "Dati non validi", "Il nome e/o il cognome inseriti non sono validi, ricontrollare i campi.", null);
            }
        }
        return false;
    }

    ClickListener accept_listener = null;
    ClickListener exit_listener = null;

    @Override
    public void onAcceptPressed(ClickListener cl) {
        accept_listener = cl;
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
        return "Gestisci componente";
    }
}
