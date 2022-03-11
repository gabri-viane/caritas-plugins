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
package theopenhand.plugins.famiglie.window.fams;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.connector.runtimes.PluginSettings;
import theopenhand.plugins.famiglie.controllers.famiglie.FamiglieController;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.window.fams.add.FamRegister;
import theopenhand.plugins.famiglie.window.fams.shower.FamShower;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.creators.ActionCreator;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class FamMain extends AnchorPane implements Refreshable {

    @FXML
    private VBox containerVB;

    @FXML
    private BorderPane mainBP;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink regFamHL;

    @FXML
    private Hyperlink showFamHL;

    private DisplayTableValue<Famiglia> table;
    private final FamiglieController controller;

    public FamMain() {
        controller = SharedReferenceQuery.getController(FamiglieController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/famiglie/window/fams/FamMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FamMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        regFamHL.setOnAction(a -> {
            FamRegister pr = new FamRegister();
            DialogCreator.showDialog(pr, () -> {
                onRefresh(true);
            }, null);
            regFamHL.setVisited(false);
        });
        showFamHL.setOnAction(a -> {
            Famiglia selectedItem = PluginSettings.table_prop.getValue() ? table.getSelectionModel().getSelectedItem() : null;
            showFam(selectedItem);
        });
        refreshHL.setOnAction(a -> {
            onRefresh(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(Famiglia.class);
        table.setValueAcceptListener((f) -> {
            if (f != null) {
                showFam(f);
            }
        });
        PluginSettings.table_prop.addListener((previus_value, new_value) -> {
            changeType(new_value);
        });
        changeType(PluginSettings.table_prop.getValue());
        ActionCreator.setHyperlinkAction(ActionCreator.generateOrderAction(new ReferenceQuery(PluginRegisterFamiglie.frr,Famiglia.class,controller.getRH(),7), this), orderHL);
        /*reloadElements(true);*/
    }

    private void changeType(boolean b) {
        if (b) {
            mainBP.setCenter(table);
        } else {
            mainBP.setCenter(containerVB);
        }
    }

    @Override
    public void onRefresh(boolean reload) {
        if (reload) {
            controller.setRH(
                ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 0, Famiglia.class, null).orElse(null));
        }
        if (controller.getRH() != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                controller.getRH().getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<Famiglia> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.toString();
                    }, null);
                    els.add(selectableElement);
                });
            } else {
                table.setData(controller.getRH().getList());
            }
        }
    }

    private void showFam(Famiglia f) {
        FamShower fs = new FamShower(f);
        fs.onExitPressed(() -> {
            StaticReferences.getMainWindowReference().setCenterNode(this);
        });
        StaticReferences.getMainWindowReference().setCenterNode(fs);
    }

}
