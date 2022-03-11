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
package theopenhand.plugins.borse.window.elementi.editor.single;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.borse.window.borse.BorseMain;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.window.graphics.creators.ElementCreator;

/**
 *
 * @author gabri
 */
public class SingleElemento extends AnchorPane implements ValueHolder<Prodotto> {

    @FXML
    private HBox controlsHB;

    @FXML
    private Label dimensionLB;

    @FXML
    private CheckBox insertCB;
    @FXML
    private Label nameLB;

    private TextField quantity;
    private final Prodotto p;
    
    private final EventHandler<? super MouseEvent> on_select  =(t)->{
        insertCB.fire();
    };

    public SingleElemento(Prodotto eb) {
        this.p = eb;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/borse/window/elementi/editor/single/SingleElemento.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(BorseMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        qtSetup();
        nameLB.setText(p.getNome());
        dimensionLB.setText(p.getNome_confezione());
        nameLB.setOnMouseClicked(on_select);
        dimensionLB.setOnMouseClicked(on_select);
        controlsHB.setOnMouseClicked(on_select);
        insertCB.selectedProperty().addListener((ov, t, t1) -> {
            if (t1) {
                controlsHB.getChildren().add(quantity);
            } else {
                controlsHB.getChildren().remove(quantity);
            }
        });
    }

    private void qtSetup() {
        quantity = ElementCreator.buildNumericField();
        quantity.setTooltip(new Tooltip("Quantità da inserire"));
        quantity.setPrefWidth(80);
        quantity.setText("1");
        quantity.focusedProperty().addListener((ov, t, t1) -> {
            if (!t1) {
                String val = quantity.getText();
                if (val == null || val.isBlank() || Integer.parseInt(val) < 1) {
                    quantity.setText("1");
                }
            }
        });
    }

    public boolean isSelected() {
        return insertCB.isSelected();
    }

    public int getQuantity() {
        return Integer.parseInt(quantity.getText());
    }

    @Override
    public Prodotto getValue() {
        return p;
    }

    @Override
    public Parent getParentNode() {
        return null;
    }

}
