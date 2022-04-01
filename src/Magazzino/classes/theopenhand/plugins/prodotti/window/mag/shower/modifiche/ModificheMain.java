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
package theopenhand.plugins.prodotti.window.mag.shower.modifiche;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.DataUtils;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.connector.runtimes.PluginSettings;
import theopenhand.plugins.prodotti.controllers.prodotti.ModificheController;
import theopenhand.plugins.prodotti.data.ModificaMagazzino;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.mag.add.modifiche.ModificaRegister;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.creators.ActionCreator;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class ModificheMain extends AnchorPane implements Refreshable {

    @FXML
    private VBox containerVB;

    @FXML
    private BorderPane mainBP;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink regModificaHL;

    @FXML
    private Hyperlink showModDate;

    @FXML
    private Hyperlink showModMot;

    @FXML
    private Hyperlink showModProd;

    private DisplayTableValue<ModificaMagazzino> table;
    private final ModificheController controller;

    public ModificheMain() {
        controller = SharedReferenceQuery.getController(ModificheController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/mag/shower/modifiche/ModificheMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ConfShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        showModDate.setVisited(false);
        showModMot.setVisited(false);
        showModProd.setVisited(false);
        regModificaHL.setOnAction(a -> {
            ModificaRegister pr = new ModificaRegister();
            DialogCreator.showDialog(pr, () -> {
                onRefresh(true);
            }, null);
            regModificaHL.setVisited(false);
        });
        refreshHL.setOnAction(a -> {
            onRefresh(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(ModificaMagazzino.class);
        generatePopupControls();
        table.setValueAcceptListener((value) -> {
            ModificaRegister pr = new ModificaRegister(value);
            DialogCreator.showDialog(pr, () -> {
                onRefresh(true);
            }, null);
        });
        PluginSettings.table_prop.addListener((previus_value, new_value) -> {
            changeType(new_value);
        });
        changeType(PluginSettings.table_prop.getValue());
        ActionCreator.setHyperlinkAction(ActionCreator.generateOrderAction(new ReferenceQuery(PluginRegisterProdotti.prr, ModificaMagazzino.class, controller.getRH(), 7), this), orderHL);
        onRefresh(true);
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
            controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 5, ModificaMagazzino.class, null).orElse(null));
        }
        if (controller.getRH() != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                controller.getRH().getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<ModificaMagazzino> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.getProdotto() + " +" + element.getTotale() + "\t(" + element.getMotivo() + " - " + DataUtils.format(element.getData()) + ")";
                    }, null);
                    els.add(selectableElement);
                });
            } else {
                table.setData(controller.getRH().getList());
            }
        }
    }

    private void generatePopupControls() {
        ContextMenu cm = new ContextMenu();
        MenuItem remove_element = new MenuItem("Rimuovi");
        cm.getItems().add(remove_element);
        remove_element.setOnAction(a -> {
            ModificaMagazzino selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTotale(0l);
            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 4, ModificaMagazzino.class, selectedItem);
            onRefresh(true);
        });

        table.setPopUpMenu(cm);
    }

}
