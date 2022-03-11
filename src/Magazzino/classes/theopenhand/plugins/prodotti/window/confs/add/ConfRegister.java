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
package theopenhand.plugins.prodotti.window.confs.add;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import static theopenhand.commons.DataUtils.softValidText;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.data.Confezione;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class ConfRegister extends AnchorPane implements DialogComponent {

    @FXML
    private Button exitBTN;

    @FXML
    private TextField nameConfB;

    @FXML
    private Button registerBTN;

    public ConfRegister() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/confs/add/ConfRegister.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ConfRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        registerBTN.setOnAction(a -> saveProd());
    }

    private void saveProd() {
        String conf_name = nameConfB.getText();
        if (softValidText(conf_name)) {
                Confezione c = new Confezione(conf_name);
                ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 1, Confezione.class, c);
                if (after_accept != null) {
                    after_accept.onClick();
                }
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Operazione completata", null, null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Dati errati", "Dimensione della confezione non valida", null);
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
        nameConfB.clear();
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
        return "Registra Confezione";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

}
