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
package theopenhand.plugins.famiglie.window.fams.add;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.DataUtils;
import static theopenhand.commons.DataUtils.softValidText;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;

/**
 *
 * @author gabri
 */
public class FamRegister extends AnchorPane implements DialogComponent {

    @FXML
    private TextField idFamTB;

    @FXML
    private TextField nameDichTB;

    @FXML
    private TextField surnameDichTB;

    @FXML
    private TextField codDichTB;

    @FXML
    private TextField addressTB;

    @FXML
    private TextField phoneTB;

    @FXML
    private CheckBox enableCon;

    @FXML
    private VBox conControlsVB;

    @FXML
    private TextField nameConTB;

    @FXML
    private TextField surnameConTB;

    @FXML
    private DatePicker dateConDP;

    @FXML
    private Button exitBTN;

    @FXML
    private Button registerBTN;

    public FamRegister() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/famiglie/window/fams/add/FamRegister.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FamRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        ElementCreator.transformNumericField(idFamTB);
        registerBTN.setOnAction(a -> saveFam());
        conControlsVB.disableProperty().bind(enableCon.selectedProperty().not());
    }

    private void saveFam() {
        String dich_name = nameDichTB.getText();
        String dich_surn = surnameDichTB.getText();
        String con_name = nameConTB.getText();
        String con_surn = surnameConTB.getText();
        String cod_dich = codDichTB.getText();
        String address = addressTB.getText();
        String phone = phoneTB.getText();
        LocalDate ld = dateConDP.getConverter().fromString(dateConDP.getEditor().getText());
        Date d = null;
        if (softValidText(dich_name) && softValidText(dich_surn) && (cod_dich != null ? cod_dich.length() < 46 : true)) {
            if (validText(phone, false) && softValidText(address)) {
                boolean con_control = enableCon.isSelected();
                boolean checkd_ = true;
                if (con_control) {
                    if (softValidText(con_name) && softValidText(con_surn)) {
                        if (ld != null) {
                            d = DataUtils.toDate(ld);
                            if (d.compareTo(new Date()) > 0) {
                                DialogCreator.showAlert(Alert.AlertType.ERROR, "Dati errati", "Data non possibile (futura), inserire una data uguale o precedente ad oggi.", null);
                                checkd_ = false;
                            }
                        } else {
                            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati del coniuge incompleti: data di nascita non valida.", null);
                            checkd_ = false;
                        }
                    } else {
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati del coniuge non validi.", null);
                        checkd_ = false;
                    }
                }
                if (checkd_) {
                    long id = getIDFam();
                    if (id > 0) {
                        Famiglia fam = new Famiglia(id, dich_name, dich_surn, address, phone, con_name, con_surn, d, cod_dich);
                        ConnectionExecutor.getInstance().executeCall(PluginRegisterFamiglie.frr, 1, Famiglia.class, fam);
                        if (after_accept != null) {
                            after_accept.onClick();
                        }
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Operazione completata", null, null);
                        clearFields();
                    } else {
                        DialogCreator.showAlert(Alert.AlertType.ERROR, "Errori registrazione", "La famiglia non è stata registrata.", null);
                    }
                }
            } else {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati della famiglia (indirizzo o telefono) non validi", null);
            }
        } else {
            //   throw new Exception();
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dati del dichiarante non validi", null);
        }

    }

    private boolean validText(String s, boolean only_letter) {
        return (s != null && !s.isEmpty() && !s.trim().isEmpty() && s.length() < 256
                && (only_letter ? s.matches("[a-zA-Z\\s]*") : s.matches("[\\d\\s]++")));
    }

    private long getIDFam() {
        String text_id = idFamTB.getText();
        if (text_id != null && !text_id.isEmpty() && !text_id.trim().isEmpty()) {
            try {
                long val = Long.parseLong(text_id);
                if (val < 0) {
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "L'ID famiglia deve essere maggiore di 0.", null);
                } else {
                    return val;
                }
            } catch (NumberFormatException nfe) {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati incompleti", "ID famiglia non valido.", null);
            }
        }
        return -1;
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        exitBTN.setOnAction(a -> cl.onClick());
    }

    ClickListener after_accept = () -> {
    };

    private void clearFields() {
        idFamTB.clear();
        nameConTB.clear();
        surnameConTB.clear();
        codDichTB.clear();
        nameDichTB.clear();
        surnameDichTB.clear();
        addressTB.clear();
        phoneTB.clear();
        enableCon.setSelected(false);
        dateConDP.setValue(null);
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
        return "Registra Famiglia";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }
}
