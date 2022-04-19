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
package theopenhand.plugins.borse.window.borse.shower;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.DataUtils;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.controllers.borse.ElementiController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.ElementoBorsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.window.picker.BorPicker;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.pickers.FamPicker;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.commons.ValueDialog;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class BorsaShower extends AnchorPane implements DialogComponent, ValueHolder<Borsa>, Refreshable {

    @FXML
    private Hyperlink changeBorHL;

    @FXML
    private Hyperlink closeHL;

    @FXML
    private ScrollPane compSP;

    @FXML
    private VBox compVB;

    @FXML
    private VBox componentsVB;

    @FXML
    private CheckBox consegnataCB;

    @FXML
    private DatePicker dataConsegnaDP;

    @FXML
    private Button deleteBorBTN;

    @FXML
    private TextField dichTB;

    @FXML
    private Hyperlink editBorHL;

    @FXML
    private Hyperlink editElemsHL;

    @FXML
    private Label idBorLBL;

    @FXML
    private TextField idTB;

    @FXML
    private Label modifingLBL;

    @FXML
    private Hyperlink moveToFamHL;

    @FXML
    private TextArea noteTA;

    @FXML
    private Button saveModBTN;

    @FXML
    private Hyperlink updateValuesHL;

    private DisplayTableValue<ElementoBorsa> table;
    private final BorseController controller;
    private final ElementiController controller2;

    public BorsaShower() {
        controller = SharedReferenceQuery.getController(BorseController.class);
        controller2 = SharedReferenceQuery.getController(ElementiController.class);
        init();
    }

    public BorsaShower(Borsa f) {
        this.b = f;
        controller = SharedReferenceQuery.getController(BorseController.class);
        controller2 = SharedReferenceQuery.getController(ElementiController.class);
        init();
    }

    Famiglia f;
    Borsa b;
    private boolean editing = false;

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/borse/window/borse/shower/BorsaShower.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BorsaShower.class.getName()).log(Level.SEVERE, null, ex);
        }
        ElementCreator.transformNumericField(idTB);
        table = ElementCreator.generateTable(ElementoBorsa.class);
        generatePopupControls();
        clearAll();
        updateValuesHL.setOnAction((a) -> {
            updateValuesHL.setVisited(false);
            onRefresh(true);
        });
        moveToFamHL.setOnAction((t) -> {
            PickerDialogCNTRL<Famiglia, FamigliaHolder> ppcntrl = FamPicker.createPicker();
            Stage s = DialogCreator.showDialog(ppcntrl, ppcntrl.getPrefWidth(), ppcntrl.getPrefHeight(), "Seleziona famiglia");
            ClickListener cl = () -> {
                s.close();
            };
            ppcntrl.onExitPressed(cl);
            ppcntrl.onAcceptPressed(() -> {
                f = ppcntrl.getValue();
                b.setId_fam(f.getIdfam());
                Optional<ResultHolder> executeCall = ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 4, Borsa.class, b);
                cl.onClick();
                if (executeCall.isPresent()) {
                    Exception e = executeCall.get().getExecutionException();
                    if (e == null) {
                        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Borsa aggiornata", "La famiglia è stata cambiata correttamente!", null);
                    }
                }
                onRefresh(true);
            });
            moveToFamHL.setVisited(false);
        });
        editElemsHL.setOnAction(a -> {

        });
        editBorHL.setOnAction((a) -> {
            editBorHL.setVisited(false);
            onRefresh(true);
            editable(!editing);
        });
        changeBorHL.setOnAction(a -> {
            PickerDialogCNTRL<Borsa, BorsaHolder> frcntrl = BorPicker.createPicker();
            /*FamPickerCNTRL frcntrl = new FamPickerCNTRL();*/
            Stage s = DialogCreator.showDialog(frcntrl, frcntrl.getPrefWidth(), frcntrl.getPrefHeight(), "Seleziona borsa");
            ClickListener cl = () -> {
                s.close();
            };
            frcntrl.onExitPressed(cl);
            frcntrl.onAcceptPressed(() -> {
                b = frcntrl.getValue();
                cl.onClick();
                onRefresh(true);
            });
            frcntrl.onRefresh(true);
            if (b != null) {
                frcntrl.select(b);
            }
            changeBorHL.setVisited(false);
        });
        PluginSettings.table_prop.addListener((Boolean previus_value, Boolean new_value) -> {
            enableTable(new_value);
        });
        enableTable(PluginSettings.table_prop.getValue());
        deleteBorBTN.setOnAction(a -> deleteBorsa());
        saveModBTN.setOnAction(a -> updateBorsa());
        editable(false);
        if (b == null) {
            changeBorHL.fire();
        } else {
            onRefresh(true);
        }
    }

    private void enableTable(boolean new_value) {
        if (new_value) {
            var vb = compVB.getChildren();
            vb.remove(compSP);
            vb.add(table);
        } else {
            var vb = compVB.getChildren();
            vb.remove(table);
            if (!vb.contains(compSP)) {
                vb.add(compSP);
            }
        }
    }

    @Override
    public void onRefresh(boolean reload) {
        if (b != null) {
            if (reload) {
                controller.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 3, Borsa.class, b).orElse(null));
            }
            b = controller.getRH().find(b.getID().longValue());
            f = new Famiglia(b.getId_fam().longValue(), null, null, null, null, null, null, null, null);
            if (b != null) {
                clearAll();
                setData(b);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "La borsa selezionata non è correttamente registrata.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stata selezionata nessuna borsa per cui visualizzare i dati.", null);
        }
    }

    private void deleteBorsa() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando la borsa eliminerai anche tutti i dati ad essa correlati:\n\t-Elementi registrati\nProcedendo eliminerai tutti i dati, continuare?", null);
        res.ifPresent((r) -> {
            if (r.equals(ButtonType.YES)) {
                if (this.b != null) {
                    controller.setRH(ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 5, Borsa.class, this.b).orElse(null));
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stata selezionata nessuna borsa da eliminare.", null);
                }
                reset();
            }
        });
    }

    private void updateBorsa() {
        if (b != null) {
            LocalDate date = dataConsegnaDP.getConverter().fromString(dataConsegnaDP.getEditor().getText());
            if (date != null && f != null) {
                b.setId_fam(f.getIdfam());
                b.setConsegna(DataUtils.toDate(date));
                b.setConsegnata(consegnataCB.isSelected());
                b.setNote(noteTA.getText());
                ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 4, Borsa.class, b).orElse(null);
            }
            onRefresh(true);
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati della borsa sono stati aggiornati.", null);
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stata selezionata nessuna borse da modificare.", null);
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        //idTB.setEditable(ed);
        //dichTB.setEditable(ed);
        dataConsegnaDP.setEditable(ed);
        dataConsegnaDP.setDisable(!ed);
        noteTA.setEditable(ed);
        consegnataCB.setDisable(!ed);
        changeBorHL.setDisable(ed);
        updateValuesHL.setDisable(ed);
        moveToFamHL.setDisable(!ed);
        editElemsHL.setDisable(!ed);
        deleteBorBTN.setDisable(!ed);
        saveModBTN.setDisable(!ed);
        modifingLBL.setVisible(ed);
        if (ed) {
            editBorHL.setText("Scarta modifiche");
        } else {
            editBorHL.setText("Modifica");
            dataConsegnaDP.setStyle("-fx-opacity : 1.0");
        }
    }

    private void clearAll() {
        idBorLBL.setText("Seleziona per visualizzare");
        idTB.setText(null);
        dichTB.setText(null);
        dataConsegnaDP.setValue(LocalDate.now());
        noteTA.setText(null);
        consegnataCB.setSelected(false);
        componentsVB.getChildren().clear();
        table.setData(new ArrayList<>());
    }

    private void reset() {
        clearAll();
        b = null;
        editable(false);
        changeBorHL.fire();
    }

    public void setData(Borsa bor) {
        if (bor != null) {
            idBorLBL.setText(bor.getId_fam() + " - " + DataUtils.format(bor.getConsegna()));
            idTB.setText("" + bor.getId_fam());
            dichTB.setText(bor.getDichiarante());
            dataConsegnaDP.setValue(DataUtils.toLocalDate(bor.getConsegna()));
            noteTA.setText(bor.getNote());
            consegnataCB.setSelected(bor.getConsegnata());
            controller2.setRH(ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 6, ElementoBorsa.class,
                    new ElementoBorsa(bor.getID().longValue(), 0, 0, false)
            ).orElse(null));
            if (controller2.getRH() != null) {
                if (!PluginSettings.table_prop.getValue()) {
                    ObservableList<Node> cls = componentsVB.getChildren();
                    cls.clear();
                    controller2.getRH().getList().forEach(c -> {
                        PickerElementCNTRL<ElementoBorsa> selectableElement = new PickerElementCNTRL<>(c, false, (element) -> {
                            return element.toString();
                        }, null);
                        cls.add(selectableElement);
                    });
                } else {
                    table.setData(controller2.getRH().getList());
                }
            }
        }
    }

    private void generatePopupControls() {
        ContextMenu cm = new ContextMenu();
        MenuItem remove_element = new MenuItem("Rimuovi");
        MenuItem edit_element = new MenuItem("Modifica");
        cm.getItems().add(edit_element);
        cm.getItems().add(remove_element);
        if (!PluginSettings.remove_qt.getValue()) {
            SeparatorMenuItem smi = new SeparatorMenuItem();
            MenuItem sub_from_mag = new MenuItem("Sottrai");
//            MenuItem add_from_mag = new MenuItem("Aggiungi");

            Menu mag_gest = new Menu("Magazzino");
            mag_gest.getItems().add(sub_from_mag);
//            mag_gest.getItems().add(add_from_mag);

            sub_from_mag.setOnAction(a -> {
                ElementoBorsa selectedItem = table.getSelectionModel().getSelectedItem();
                selectedItem.setSubtr(true);

                ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 1, ElementoBorsa.class, selectedItem);
                onRefresh(true);
            });
//
//            add_from_mag.setOnAction(a -> {
//                ElementoBorsa selectedItem = table.getSelectionModel().getSelectedItem();
//                selectedItem.setSubtr(false);
//                ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 1, ElementoBorsa.class, selectedItem);
//                refreshValues();
//            });

            cm.getItems().add(smi);
            cm.getItems().add(mag_gest);
        }

        remove_element.setOnAction(a -> {
            ElementoBorsa selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTot(0l);
            ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 5, ElementoBorsa.class, selectedItem);
            onRefresh(true);
        });
        edit_element.setOnAction(a -> {
            showEditQt(table.getSelectionModel().getSelectedItem());
        });

        table.setPopUpMenu(cm);
    }

    public void showEditQt(ElementoBorsa eb) {
        if (eb != null) {
            TextField tf = ElementCreator.buildNumericField();
            tf.setPrefWidth(80);
            tf.setText(eb.getTot().toString());
            ValueDialog vd = new ValueDialog("Inserisci quantità", "Cambia la quantità dell'elemento della borsa.", tf);
            ClickListener cl = () -> {
                String qt = tf.getText();
                if (qt != null && !qt.isBlank()) {
                    eb.setTot(Long.parseLong(qt));
                    ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 1, ElementoBorsa.class, eb);
                    DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifica completata", "La quantità è stata aggiornata", null);
                    onRefresh(true);
                }
            };
            DialogCreator.showDialog(vd, cl, null);
        }
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        closeHL.setOnAction(a -> cl.onClick());
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {

    }

    @Override
    public Borsa getValue() {
        return b;
    }

    @Override
    public Parent getParentNode() {
        return this;
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
        return "Mostra Borsa";
    }

}
