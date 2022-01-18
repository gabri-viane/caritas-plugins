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
package theopenhand.plugins.prodotti.controllers.prodotti;

import java.util.Optional;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.data.Entrata;
import theopenhand.plugins.prodotti.data.holders.EntrataHolder;
import theopenhand.plugins.prodotti.window.mag.shower.entrate.EntMain;
import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public class EntrateController implements ReferenceController {

    public static EntrataHolder rs;

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            Optional<ResultHolder> eq = ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 0, Entrata.class, null);
            if (eq.isPresent()) {
                rs = (EntrataHolder) eq.get();
            }
        };
    }

    @Override
    public String getAssocButtonName() {
        return "Mostra Entrate";
    }

    @Override
    public String getGroupName() {
        return "Magazzino";
    }

    @Override
    public String getID() {
        return "PLG-ENT-WND";
    }

    @Override
    public AnchorPane getNode() {
        return new EntMain();
    }
    
}
