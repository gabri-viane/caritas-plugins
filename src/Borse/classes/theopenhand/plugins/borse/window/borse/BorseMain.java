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
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.dialogs.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;
import theopenhand.plugins.borse.window.borse.add.BorsaRegister;
import theopenhand.plugins.borse.window.borse.shower.BorsaShower;
import theopenhand.statics.StaticReferences;

/**
 *
 * @author gabri
 */
public class BorseMain extends AnchorPane {

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

    public BorseMain() {
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
                reloadElements(true);
            }, null);
            regBorsaHL.setVisited(false);
        });
        showBorsaHL.setOnAction(a -> {
            Borsa selectedItem = PluginSettings.table_prop.getValue() ? table.getSelectionModel().getSelectedItem() : null;
            BorsaShower bs = new BorsaShower(selectedItem);
            bs.onExitPressed(() -> {
                StaticReferences.getMainWindowReference().setCenterNode(this);
            });
            StaticReferences.getMainWindowReference().setCenterNode(bs);
        });
        refreshHL.setOnAction(a -> {
            reloadElements(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(Borsa.class, BorseController.rs);
        PluginSettings.table_prop.addListener((previus_value, new_value) -> {
            changeType(new_value);
        });
        changeType(PluginSettings.table_prop.getValue());
        reloadElements(true);
    }

    private void changeType(boolean b) {
        if (b) {
            mainBP.setCenter(table);
        } else {
            mainBP.setCenter(containerVB);
        }
    }

    public void reloadElements(boolean refresh) {
        if (refresh) {
            BorseController.rs = (BorsaHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 0, Borsa.class, null).orElse(null);
        }
        if (BorseController.rs != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                BorseController.rs.getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<Borsa> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.toString();
                    }, null);
                    els.add(selectableElement);
                });
            } else {
                table.setData(BorseController.rs.getList());
            }
        }
    }

}
