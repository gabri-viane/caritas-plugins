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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.DataUtils;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.controllers.borse.ElementiController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.ElementoBorsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.data.holders.ElementiHolder;
import theopenhand.plugins.borse.window.picker.BorPicker;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.commons.PickerElementCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.dialogs.ElementCreator;
import theopenhand.window.graphics.inner.DisplayTableValue;
import theopenhand.window.objects.TextFieldBuilder;

/**
 *
 * @author gabri
 */
public class BorsaShower extends AnchorPane implements DialogComponent, ValueHolder<Borsa> {

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

    public BorsaShower() {
        init();
    }

    public BorsaShower(Borsa f) {
        this.b = f;
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
        TextFieldBuilder.transformNumericField(idTB);
        table = ElementCreator.generateTable(ElementoBorsa.class, ElementiController.rs);
        clearAll();
        updateValuesHL.setOnAction((a) -> {
            updateValuesHL.setVisited(false);
            refreshValues();
        });
        editBorHL.setOnAction((a) -> {
            editBorHL.setVisited(false);
            refreshValues();
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
                refreshValues();
            });
            frcntrl.reloadElements(true);
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
            refreshValues();
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

    private void refreshValues() {
        if (b != null) {
            BorseController.rs = (BorsaHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 3, Borsa.class, b).orElse(null);
            b = ((BorsaHolder) BorseController.rs).find(b.getID().longValue());
            f = new Famiglia(b.getId_fam().longValue(), null, null, null, null, null, null, null);
            if (b != null) {
                clearAll();
                setData(b);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore selezione",
                        "La borsa selezionata non è correttamente registrata.", null).show();
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricerca",
                    "Non è stata selezionata nessuna borsa per cui visualizzare i dati.", null).show();
        }
    }

    private void deleteBorsa() {
        Optional<ButtonType> res = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Eliminazione dati",
                "Eliminando la borsa eliminerai anche tutti i dati ad essa correlati:\n\t-Elementi registrati\nProcedendo eliminerai tutti i dati, continuare?", null).showAndWait();
        res.ifPresent((r) -> {
            if (r.equals(ButtonType.YES)) {
                if (this.b != null) {
                    BorseController.rs = (BorsaHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 5, Borsa.class, this.b).orElse(null);
                } else {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore eliminazione",
                            "Non è stata selezionata nessuna borsa da eliminare.", null).show();
                }
                reset();
            }
        });
    }

    private void updateBorsa() {
        if (b != null) {
            LocalDate date = dataConsegnaDP.getValue();
            if (date != null && f != null) {
                b.setId_fam(f.getIdfam());
                b.setConsegna(DataUtils.toDate(date));
                b.setConsegnata(consegnataCB.isSelected());
                b.setNote(noteTA.getText());
                ConnectionExecutor.getInstance().executeCall(PluginRegisterBorse.brr, 4, Borsa.class, b).orElse(null);
            }
            refreshValues();
            editable(false);
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Modifiche salvate",
                    "I dati della borsa sono stati aggiornati.", null).show();
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore modifica",
                    "Non è stata selezionata nessuna borse da modificare.", null).show();
        }
    }

    public void editable(boolean ed) {
        editing = ed;
        idTB.setEditable(ed);
        dichTB.setEditable(ed);
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
            idBorLBL.setText("" + bor.getID());
            idTB.setText("" + bor.getId_fam());
            dichTB.setText(bor.getDichiarante());
            dataConsegnaDP.setValue(DataUtils.toLocalDate(bor.getConsegna()));
            noteTA.setText(bor.getNote());
            consegnataCB.setSelected(bor.getConsegnata());
            ElementiController.rs = (ElementiHolder) ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 6, ElementoBorsa.class,
                    new ElementoBorsa(bor.getID().longValue(), 0, 0, false)
            ).orElse(null);
            if (ElementiController.rs != null) {
                if (!PluginSettings.table_prop.getValue()) {
                    ObservableList<Node> cls = componentsVB.getChildren();
                    cls.clear();
                    ElementiController.rs.getList().forEach(c -> {
                        PickerElementCNTRL<ElementoBorsa> selectableElement = new PickerElementCNTRL<>(c, false, (element) -> {
                            return element.toString();
                        }, null);
                        cls.add(selectableElement);
                    });
                } else {
                    table.setData(ElementiController.rs.getList());
                }
            }
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
