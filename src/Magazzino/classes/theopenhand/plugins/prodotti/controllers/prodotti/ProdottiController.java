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
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.plugins.prodotti.window.prods.ProdMain;
import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public class ProdottiController extends ReferenceController<ProdottoHolder> {

    private static final ProdMain fp = new ProdMain();

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            Optional<ResultHolder> eq = ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 0, Prodotto.class, null);
            if (eq.isPresent()) {
                setRH(eq.get());
            }
        };
    }

    @Override
    public String getAssocButtonName() {
        return "Mostra prodotti";
    }

    @Override
    public String getGroupName() {
        return "Magazzino";
    }

    @Override
    public String getID() {
        return "PLG-MAG-WND";
    }

    @Override
    public AnchorPane getNode() {
        return fp;
    }

    public static AnchorPane getNodeMain() {
        return fp;
    }

}
