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
package theopenhand.plugins.prodotti.window.mag.shower.entrate;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.DataUtils;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.connector.runtimes.PluginSettings;
import theopenhand.plugins.prodotti.controllers.prodotti.EntrateController;
import theopenhand.plugins.prodotti.data.Entrata;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.mag.add.entrate.EntRegister;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.commons.ValueDialog;
import theopenhand.window.graphics.creators.ActionCreator;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class EntMain extends AnchorPane implements Refreshable {

    @FXML
    private VBox containerVB;

    @FXML
    private BorderPane mainBP;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink regEntrataHL;

    @FXML
    private Hyperlink showEntrDate;

    @FXML
    private Hyperlink showEntrDon;

    @FXML
    private Hyperlink showEntrProd;

    private DisplayTableValue<Entrata> table;
    private final EntrateController controller;

    public EntMain() {
        controller = SharedReferenceQuery.getController(EntrateController.class);
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/mag/shower/entrate/EntMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ConfShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        showEntrDate.setVisited(false);
        showEntrDon.setVisited(false);
        showEntrProd.setVisited(false);
        regEntrataHL.setOnAction(a -> {
            EntRegister pr = new EntRegister();
            DialogCreator.showDialog(pr, () -> {
                onRefresh(true);
            }, null);
            regEntrataHL.setVisited(false);
        });
        refreshHL.setOnAction(a -> {
            onRefresh(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(Entrata.class);
        generatePopupControls();
        table.setValueAcceptListener((value) -> {
            showEditQt(value);
        });
        PluginSettings.table_prop.addListener((previus_value, new_value) -> {
            changeType(new_value);
        });
        changeType(PluginSettings.table_prop.getValue());
        ActionCreator.setHyperlinkAction(ActionCreator.generateOrderAction(new ReferenceQuery(PluginRegisterProdotti.prr, Entrata.class, controller.getRH(), 7), this), orderHL);
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
            controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 0, Entrata.class, null).orElse(null));
        }
        if (controller.getRH() != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                controller.getRH().getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<Entrata> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.getNome_prodotto() + " +" + element.getTotale() + "\t(" + element.getNome_donatore() + " - " + DataUtils.format(element.getData_arrivo()) + ")";
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
        MenuItem edit_element = new MenuItem("Modifica");
        cm.getItems().add(edit_element);
        cm.getItems().add(remove_element);
        remove_element.setOnAction(a -> {
            Entrata selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTotale(0);
            ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 4, Entrata.class, selectedItem);
            onRefresh(true);
        });
        edit_element.setOnAction(a -> {
            showEditQt(table.getSelectionModel().getSelectedItem());
        });

        table.setPopUpMenu(cm);
    }

    public void showEditQt(Entrata eb) {
        if (eb != null) {
            TextField tf = ElementCreator.buildNumericField();
            tf.setPrefWidth(80);
            tf.setText(eb.getTotale().toString());
            ValueDialog vd = new ValueDialog("Inserisci quantità", "Cambia la quantità dell'entrata.", tf);
            ClickListener cl = () -> {
                String qt = tf.getText();
                if (qt != null && !qt.isBlank()) {
                    eb.setTotale(Integer.parseInt(qt));
                    ConnectionExecutor.getInstance().executeCall(PluginRegisterProdotti.prr, 4, Entrata.class, eb);
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifica completata", "La quantità è stata aggiornata", null);
                    onRefresh(true);
                }
            };
            DialogCreator.showDialog(vd, cl, null);
        }
    }

}
