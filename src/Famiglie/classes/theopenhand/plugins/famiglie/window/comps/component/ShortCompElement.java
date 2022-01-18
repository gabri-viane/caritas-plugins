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
package theopenhand.plugins.famiglie.window.comps.component;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.DataUtils;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.plugins.famiglie.data.Componente;

/**
 *
 * @author gabri
 */
public class ShortCompElement extends AnchorPane implements ValueHolder<Componente> {

    @FXML
    private Label nameLBL;

    @FXML
    private Label surnameLBL;

    @FXML
    private Label bornDateLBL;

    @FXML
    private Label typeLBL;

    private final Componente c;

    public ShortCompElement(Componente comp) {
        c = comp;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/famiglie/window/comps/component/ShortCompElement.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ShortCompElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        nameLBL.setText(c.getNome_com());
        surnameLBL.setText(c.getCogn_com());
        bornDateLBL.setText(DataUtils.format(c.getD_nascita()));
        typeLBL.setText(c.getParentela());
    }

    @Override
    public Componente getValue() {
        return c;
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

}
