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
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.connector.runtimes.PluginSettings;
import theopenhand.plugins.famiglie.controllers.famiglie.FamiglieController;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.fams.add.FamRegister;
import theopenhand.plugins.famiglie.window.fams.shower.FamShower;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.dialogs.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class FamMain extends AnchorPane {

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

    public FamMain() {
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
                reloadElements(true);
            }, null);
            regFamHL.setVisited(false);
        });
        showFamHL.setOnAction(a -> {
            Famiglia selectedItem = PluginSettings.table_prop.getValue() ? table.getSelectionModel().getSelectedItem() : null;
            FamShower fs = new FamShower(selectedItem);
            fs.onExitPressed(() -> {
                StaticReferences.getMainWindowReference().setCenterNode(this);
            });
            StaticReferences.getMainWindowReference().setCenterNode(fs);
        });
        refreshHL.setOnAction(a -> {
            reloadElements(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(Famiglia.class, FamiglieController.rs);
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
            FamiglieController.rs = (FamigliaHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 0, Famiglia.class, null).orElse(null);
        }
        if (FamiglieController.rs != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                FamiglieController.rs.getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<Famiglia> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.toString();
                    }, null);
                    els.add(selectableElement);
                });
            } else {
                table.setData(FamiglieController.rs.getList());
            }
        }
    }

}
