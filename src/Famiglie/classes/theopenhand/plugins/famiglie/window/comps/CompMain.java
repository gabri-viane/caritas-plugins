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
package theopenhand.plugins.famiglie.window.comps;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.controllers.famiglie.ComponentiController;
import theopenhand.plugins.famiglie.data.Componente;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.comps.component.CompElement;
import theopenhand.plugins.famiglie.window.pickers.FamPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.ActionCreator;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class CompMain extends AnchorPane implements ValueHolder<Famiglia>, DialogComponent, Refreshable {

    @FXML
    private Hyperlink changeFamHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink addHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private Label idfamLBL;

    @FXML
    private VBox contentVB;

    private final Componente ricerca_tmp;
    private Famiglia f;
    private final ComponentiController controller;

    public CompMain() {
        controller = SharedReferenceQuery.getController(ComponentiController.class);
        ricerca_tmp = new Componente();
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/famiglie/window/comps/CompMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(CompMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshHL.setOnAction(a -> {
            refreshHL.setVisited(false);
            onRefresh(true);
        });
        changeFamHL.setOnAction(a -> {
            PickerDialogCNTRL<Famiglia, FamigliaHolder> frcntrl = FamPicker.createPicker();
            Stage s = DialogCreator.showDialog(frcntrl, frcntrl.getPrefWidth(), frcntrl.getPrefHeight(), "Seleziona famiglia");
            ClickListener cl = () -> {
                s.close();
            };
            frcntrl.onExitPressed(cl);
            frcntrl.onAcceptPressed(() -> {
                f = frcntrl.getValue();
                ricerca_tmp.setIdfam(f.getIdfam());
                cl.onClick();
                onRefresh(true);
            });
            frcntrl.onRefresh(true);
            if (f != null) {
                frcntrl.select(f);
            }
            changeFamHL.setVisited(false);
        });
        addHL.setOnAction(a -> {
            if (f != null) {
                CompElement cecntrl = new CompElement(null);
                cecntrl.setCreationMode();
                Stage showDialog = DialogCreator.showDialog(cecntrl, cecntrl.getPrefWidth(), cecntrl.getPrefHeight(), "Crea componente");
                ClickListener cl = () -> showDialog.close();
                cecntrl.onExitPressed(cl);
                cecntrl.onAcceptPressed(() -> {
                    if (f != null) {
                        Componente value = cecntrl.getValue();
                        value.setIdfam(f.getIdfam());
                        ConnectionExecutor.getInstance().executeCall(PluginRegisterFamiglie.frr, 1, Componente.class, value);
                        onRefresh(true);
                    }
                    cl.onClick();
                });
            } else {
                DialogCreator.showAlert(Alert.AlertType.WARNING, "Operazione bloccata", "Per aggiungere un componente è necessario innanzitutto selezionare una famiglia.", null);
            }
            addHL.setVisited(false);
        });
        ActionCreator.setHyperlinkAction(ActionCreator.generateOrderAction(new ReferenceQuery(PluginRegisterFamiglie.frr, Componente.class, controller.getRH(), 0), this, ricerca_tmp), orderHL);
    }

    public void changeFam() {
        changeFamHL.fire();
    }

    @Override
    public void onRefresh(boolean reload) {
        if (f != null) {
            idfamLBL.setText("" + f.getIdfam());
            ObservableList<Node> cmp = contentVB.getChildren();
            cmp.clear();
            if (reload) {
                controller.setRH(
                        ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 0, Componente.class, new Componente(f.getIdfam(), null, null, null, -1)).orElse(null));
            }
            if (controller.getRH() != null) {
                controller.getRH().getList().stream().forEach(c -> {
                    CompElement cecntrl = new CompElement(c);
                    cmp.add(cecntrl);
                    cecntrl.onExitPressed(() -> {
                        cmp.remove(cecntrl);
                    });
                });
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Nessun dato", "Non è stata selezionata nessuna famiglia, non possono essere perciò mostrati i relativi dati.", null);
        }
    }

    @Override
    public Famiglia getValue() {
        return f;
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
        return "Gestione Componenti";
    }
}
