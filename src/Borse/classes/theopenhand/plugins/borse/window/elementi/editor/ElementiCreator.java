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
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import theopenhand.commons.DataUtils;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.controllers.borse.ElementiController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.ElementoBorsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.data.savable.SavedBorseHolder;
import theopenhand.plugins.borse.window.borse.BorseMain;
import theopenhand.plugins.borse.window.elementi.editor.single.SingleElemento;
import theopenhand.plugins.borse.window.picker.BorPicker;
import theopenhand.plugins.prodotti.controllers.prodotti.ProdottiController;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.runtime.data.components.ListDataElement;
import theopenhand.runtime.data.components.PairDataElement;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.commons.ValueDialog;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;

/**
 *
 * @author gabri
 */
public class ElementiCreator extends AnchorPane implements Refreshable {

    @FXML
    private Hyperlink clearSelectionHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private FlowPane containerFP;

    @FXML
    private Hyperlink exportDataHL;

    @FXML
    private Hyperlink importDataHL;

    @FXML
    private BorderPane mainBP;

    @FXML
    private Hyperlink orderHL;

    @FXML
    private Hyperlink orderInternalHL;

    @FXML
    private Hyperlink refreshHL;

    @FXML
    private Hyperlink saveToBorsaHL;

    private final ProdottiController controller;

    private final HashMap<BigInteger, Integer> selections;
    private final HashMap<BigInteger, SingleElemento> graphic_elements;
//ID elemento, ID Prodotto
    private HashMap<BigInteger, BigInteger> editing_elements;//Elementi che sono già stati aggiunti e ora li modifico: solo se sto visualizzando una borsa

    private boolean save_elems = true;
    private boolean save_selection = true;

    public ElementiCreator() {
        selections = new HashMap<>();
        graphic_elements = new HashMap<>();
        controller = SharedReferenceQuery.getController(ProdottiController.class);
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
        clearSelectionHL.setOnAction(a -> {
            save_elems = false;
            save_selection = false;
            selections.clear();
            onRefresh(true);
            save_selection = false;
            save_elems = true;
            clearSelectionHL.setVisited(false);
        });
        exportDataHL.setOnAction(a -> {
            exportData();
            exportDataHL.setVisited(false);
        });
        importDataHL.setOnAction(a -> {
            importData();
            importDataHL.setVisited(false);
        });
        refreshHL.setOnAction(a -> {
            save_selection = true;
            onRefresh(true);
            refreshHL.setVisited(false);
        });
        orderHL.setOnAction(a -> {
            mainBP.setLeft(null);
            SharedReferenceQuery.execute(SharedReferenceQuery.getInstance().forceGenerate("plugin_prodotti", "prodotto", 8),
                    new Prodotto(),
                    SharedReferenceQuery.EXECUTION_REQUEST.CUSTOM_QUERY,
                    ((args) -> {
                        save_selection = true;
                        generateElements();
                        return null;
                    })
            );
            orderHL.setVisited(false);
        });
        orderInternalHL.setOnAction(a -> {
            mainBP.setLeft(ElementCreator.getOrdableControl(SharedReferenceQuery.getInstance().forceGenerate("plugin_prodotti", "prodotto", 8),
                    new Prodotto(), ((args) -> {
                        save_selection = true;
                        generateElements();
                        return null;
                    }), (b) -> {
                        mainBP.setLeft(null);
                        save_selection = true;
                        onRefresh(true);
                        return null;
                    }
            ));
            orderInternalHL.setVisited(false);
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
                onRefresh(true);
            });
            bpcntrl.onRefresh(true);
            saveToBorsaHL.setVisited(false);
        });
        closeHL.setVisible(false);
        onRefresh(true);
    }

    @Override
    public void onRefresh(boolean reload) {
        if (reload) {
            SharedReferenceQuery.execute(SharedReferenceQuery.getInstance().forceGenerate("plugin_prodotti", "prodotto", 7),
                    null,
                    SharedReferenceQuery.EXECUTION_REQUEST.QUERY, (args) -> {
                        controller.setRH(((Optional<ResultHolder>) args[0]).orElse(null));
                        return null;
                    });
        }
        if (controller.getRH() != null) {
            generateElements();
        }
    }

    private void generateElements() {
        if (save_elems) {
            saveSelection();
        }
        ObservableList<Node> els = containerFP.getChildren();
        els.clear();
        graphic_elements.clear();
        controller.getRH().getList().stream().forEachOrdered(e -> {
            SingleElemento se = new SingleElemento(e);
            if (selections.containsKey(e.getID())) {
                se.getSelectionProperty().setValue(true);
                se.setQuantity(selections.get(e.getID()));
            }
            graphic_elements.put(se.getValue().getID(), se);
            els.add(se);
        });
    }

    private void saveSelection() {
        if (!save_selection) {
            selections.clear();
        }
        ObservableList<Node> els = containerFP.getChildren();
        els.forEach(el -> {
            SingleElemento se = (SingleElemento) el;
            if (se.isSelected()) {
                selections.put(se.getValue().getID(), se.getQuantity());
            }
        });
    }

    public ArrayList<ElementoBorsa> getSelection(Borsa b) {
        saveSelection();
        ArrayList<ElementoBorsa> elementi = new ArrayList<>();
        selections.forEach((id, value) -> {
            ElementoBorsa eb = ElementoBorsa.create(id, BigInteger.valueOf(value));
            eb.setIdborsa(b.getID());
            eb.setSubtr(PluginSettings.remove_qt.getValue());
            elementi.add(eb);
        });
        return elementi;
    }

    public void exportData() {
        saveSelection();
        TextField tf = new TextField("Salvataggio_" + DataUtils.format(new Date()));
        ValueDialog vd = new ValueDialog("Salva selezione", "Inserisci un nome per il salvataggio", tf);
        DialogCreator.showDialog(vd, () -> {
            if (!tf.getText().isBlank()) {
                var sb = SavedBorseHolder.createBorsa(tf.getText());
                selections.forEach((id, qnt) -> {
                    SavedBorseHolder.addElementToBorsa(sb, id, qnt, graphic_elements.get(id).getValue().toString());
                });
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Salavataggio completato!", null, null);
            }
        }, null);
    }

    public void importData() {
        selections.clear();
        save_selection = false;
        save_elems = false;
        onRefresh(true);
        SavedBorseHolder.getInstance().selectFromAvailable((args) -> {
            select((ListDataElement<PairDataElement<BigInteger, Integer>>) args[0]);
            return null;
        });
    }

    private void select(ListDataElement<PairDataElement<BigInteger, Integer>> borsa) {
        borsa.getList().forEach((pair) -> {
            var id = pair.getSxValue();
            var qnt = pair.getDxValue();
            selections.put(id, qnt);
        });
        ObservableList<Node> els = containerFP.getChildren();
        els.forEach(el -> {
            SingleElemento se = (SingleElemento) el;
            var id = se.getValue().getID();
            if (selections.containsKey(id)) {
                se.getSelectionProperty().setValue(true);
                se.setQuantity(selections.get(id));
            }
        });
    }

    public void editBorsa(Borsa b, ClickListener on_exit) {
        selections.clear();
        save_selection = false;
        save_elems = false;
        onRefresh(true);
        ElementiController controller2 = SharedReferenceQuery.getController(ElementiController.class);
        controller2.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 6, ElementoBorsa.class,
                new ElementoBorsa(b.getID().longValue(), 0, 0, false)
        ).orElse(null));

        editing_elements = new HashMap<>();

        if (controller2.getRH() != null) {
            controller2.getRH().getList().forEach((eb) -> {
                var id = eb.getIdprodotto();
                var qnt = eb.getTot().intValue();
                selections.put(id, qnt);
                editing_elements.put(eb.getID(), id);
            });
            ObservableList<Node> els = containerFP.getChildren();
            els.forEach(el -> {
                SingleElemento se = (SingleElemento) el;
                var id = se.getValue().getID();
                if (selections.containsKey(id)) {
                    se.getSelectionProperty().setValue(true);
                    se.setQuantity(selections.get(id));
                }
            });
        }

        saveToBorsaHL.setText("Salva borsa");
        saveToBorsaHL.setOnAction(a -> {
            ArrayList<ElementoBorsa> selection = getSelection(b);
            for (ElementoBorsa eb : selection) {
                boolean ex = editing_elements.containsValue(eb.getIdprodotto());
                eb.setExisting(ex);
                ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 1, ElementoBorsa.class, eb);
                if (ex) {
                    editing_elements.remove(eb.getIdprodotto());
                }
            }
            editing_elements.forEach((id, pid) -> {
                ElementoBorsa eb = ElementoBorsa.createExising(id, pid);
                eb.setIdborsa(b.getID());
                ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 5, ElementoBorsa.class, eb);
            });
            editing_elements.clear();
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Borsa modificata!", "Gli articoli sono stati aggiunti/modificati con le relative quantità alla borsa.", null);
            on_exit.onClick();
        });

        closeHL.setVisited(true);
        closeHL.setOnAction(a->{
            on_exit.onClick();
        });
    }
}
