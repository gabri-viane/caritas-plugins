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
package theopenhand.plugins.prodotti.window.mag;

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
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.connector.runtimes.PluginSettings;
import theopenhand.plugins.prodotti.controllers.prodotti.MagazzinoController;
import theopenhand.plugins.prodotti.data.ElementoMagazzino;
import theopenhand.plugins.prodotti.data.holders.MagazzinoHolder;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.prods.add.ProdRegister;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.dialogs.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class MagMain extends AnchorPane {

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
    private Hyperlink regProdHL;

    private DisplayTableValue<ElementoMagazzino> table;

    public MagMain() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/mag/MagMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ConfShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        regProdHL.setOnAction(a -> {
            ProdRegister pr = new ProdRegister();
            DialogCreator.showDialog(pr, () -> {
                reloadElements(true);
            }, null);
            regProdHL.setVisited(false);
        });
        refreshHL.setOnAction(a -> {
            reloadElements(true);
            refreshHL.setVisited(false);
        });
        table = ElementCreator.generateTable(ElementoMagazzino.class, MagazzinoController.rs);
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
            MagazzinoController.rs = (MagazzinoHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 0, ElementoMagazzino.class, null).orElse(null);
        }
        if (MagazzinoController.rs != null) {
            if (!PluginSettings.table_prop.getValue()) {
                ObservableList<Node> els = containerVB.getChildren();
                els.clear();
                MagazzinoController.rs.getList().stream().forEachOrdered(e -> {
                    PickerElementCNTRL<ElementoMagazzino> selectableElement = new PickerElementCNTRL<>(e, false, (element) -> {
                        return element.getNome() + " = " + element.getTotale();
                    }, null);
                    els.add(selectableElement);
                });
            } else {
                table.setData(MagazzinoController.rs.getList());
            }
        }
    }

}
