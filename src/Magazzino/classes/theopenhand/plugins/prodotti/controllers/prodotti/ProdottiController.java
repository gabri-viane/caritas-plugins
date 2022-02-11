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

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.events.programm.ValueAcceptListener;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.plugins.prodotti.window.prods.ProdMain;
import theopenhand.runtime.templates.ReferenceController;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class ProdottiController implements ReferenceController {

    public static ProdottoHolder rs;
    private static final ProdMain fp = new ProdMain();

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            Optional<ResultHolder> eq = ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, 0, Prodotto.class, null);
            if (eq.isPresent()) {
                rs = (ProdottoHolder) eq.get();
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

    public static void requestProductQuery(int query_id) {
        Optional<ResultHolder> eq = ConnectionExecutor.getInstance().executeQuery(PluginRegisterProdotti.prr, query_id, Prodotto.class, null);
        if (eq.isPresent()) {
            rs = (ProdottoHolder) eq.get();
        }
    }

    public static void requestProductOrderQuery(int query_id, ValueAcceptListener<Optional<ArrayList<Clause>>> future) {
        DialogCreator.createSearcher(new ReferenceQuery(PluginRegisterProdotti.prr, Prodotto.class, rs, query_id), future);
    }

}
