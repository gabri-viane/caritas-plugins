/*
 * Copyright 2022 gabri.
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
package theopenhand.plugins.borse.window.borse;

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
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.dialogs.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;
import theopenhand.plugins.borse.window.borse.add.BorsaRegister;
import theopenhand.plugins.borse.window.borse.shower.BorsaShower;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.dialogs.ActionCreator;

/**
 *
 * @author gabri
 */
public class BorseMain extends AnchorPane implements Refreshable {

    @FXML
    private VBox containerVB;

    @FXML
    private BorderPane mainBP;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink regBorsaHL;

    @FXML
    private Hyperlink showBorsaHL;

    private DisplayTableValue<Borsa> table;
    private final BorseController controller;

    public BorseMain() {
        controller = SharedReferenceQuery.getController(BorseController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/borse/window/borse/BorseMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BorseMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        regBorsaHL.setOnAction(a -> {
            BorsaRegister pr = new BorsaRegister();
            DialogCreator.showDialog(pr, () -> {
                onRefresh(true);
            }, null);
            regBorsaHL.setVisited(false);
        });
        showBorsaHL.setOnAction(a -> {
            Borsa selectedItem = PluginSettings.table_prop.getValue() ? table.getSelectionModel().getSelectedItem() : null;
            showBorsa(selectedItem);
            showBorsaHL.setVisited(false);
        });
        refreshHL.setOnAction(a -> {
            onRefresh(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(Borsa.class);
        table.setValueAcceptListener((b) -> {
            if (b != null) {
                showBorsa(b);
            }
        });
        PluginSettings.table_prop.addListener((previus_value, new_value) -> {
            changeType(new_value);
        });
        changeType(PluginSettings.table_prop.getValue());
        ActionCreator.setHyperlinkAction(ActionCreator.generateOrderAction(new ReferenceQuery(PluginRegisterBorse.brr, Borsa.class, controller.getRH(), 7), this), orderHL);
    }

    private void showBorsa(Borsa b) {
        BorsaShower bs = new BorsaShower(b);
        bs.onExitPressed(() -> {
            StaticReferences.getMainWindowReference().setCenterNode(this);
        });
        StaticReferences.getMainWindowReference().setCenterNode(bs);
    }

    private void changeType(boolean b) {
        if (b) {
            mainBP.setCenter(table);
        } else {
            mainBP.setCenter(containerVB);
        }
    }

    @Override
    public void onRefresh(boolean refresh) {
        if (refresh) {
            controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 0, Borsa.class, null)
                    .orElse(null));
        }
        if (controller.getRH() != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                controller.getRH().getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<Borsa> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.toString();
                    }, null);
                    els.add(selectableElement);
                });
            } else {
                table.setData(controller.getRH().getList());
            }
        }
    }

}
