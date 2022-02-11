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
package theopenhand.plugins.borse.window.elementi.editor;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.ElementoBorsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.window.borse.BorseMain;
import theopenhand.plugins.borse.window.elementi.editor.single.SingleElemento;
import theopenhand.plugins.borse.window.picker.BorPicker;
import theopenhand.plugins.prodotti.controllers.prodotti.ProdottiController;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class ElementiCreator extends AnchorPane {

    @FXML
    private FlowPane containerFP;

    @FXML
    private VBox containerVB;

    @FXML
    private Hyperlink exportDataHL;

    @FXML
    private BorderPane mainBP;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink saveToBorsaHL;

    public ElementiCreator() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/borse/window/elementi/editor/ElementiCreator.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BorseMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshHL.setOnAction(a -> {
            reloadElements(true);
            refreshHL.setVisited(false);
        });
        orderHL.setOnAction(a -> {
            ProdottiController.requestProductOrderQuery(8, (value) -> {
                ObservableList<Node> els = containerFP.getChildren();
                els.clear();
                ProdottiController.rs.getList().stream().forEachOrdered(e -> {
                    SingleElemento se = new SingleElemento(e);
                    els.add(se);
                });
            });
        });
        saveToBorsaHL.setOnAction(a -> {
            PickerDialogCNTRL<Borsa, BorsaHolder> bpcntrl = BorPicker.createPicker();
            Stage s = DialogCreator.showDialog(bpcntrl, bpcntrl.getPrefWidth(), bpcntrl.getPrefHeight(), "Seleziona borsa");
            ClickListener cl = () -> {
                s.close();
            };
            bpcntrl.onExitPressed(cl);
            bpcntrl.onAcceptPressed(() -> {
                Borsa value = bpcntrl.getValue();
                if (value != null) {
                    ArrayList<ElementoBorsa> selection = getSelection(value);
                    for (ElementoBorsa eb : selection) {
                        ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 1, ElementoBorsa.class, eb);
                    }
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Articoli aggiunti!", "Gli articoli sono stati aggiunti con le relative quantità alla borsa selezionata.", null);
                }
                cl.onClick();
                reloadElements(true);
            });
            bpcntrl.reloadElements(true);
        });
        reloadElements(true);
    }

    public void reloadElements(boolean refresh) {
        if (refresh) {
            ProdottiController.requestProductQuery(7);
        }
        if (ProdottiController.rs != null) {
            ObservableList<Node> els = containerFP.getChildren();
            els.clear();
            ProdottiController.rs.getList().stream().forEachOrdered(e -> {
                SingleElemento se = new SingleElemento(e);
                els.add(se);
            });
        }
    }

    public ArrayList<ElementoBorsa> getSelection(Borsa b) {
        ArrayList<ElementoBorsa> elementi = new ArrayList<>();
        ObservableList<Node> els = containerFP.getChildren();
        els.forEach(el -> {
            SingleElemento se = (SingleElemento) el;
            if (se.isSelected()) {
                ElementoBorsa eb = ElementoBorsa.create(se.getValue().getId(), BigInteger.valueOf(se.getQuantity()));
                eb.setIdborsa(b.getID());
                eb.setSubtr(PluginSettings.remove_qt.getValue());
                elementi.add(eb);
            }
        });
        return elementi;
    }

}
