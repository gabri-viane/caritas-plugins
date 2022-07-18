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
package theopenhand.plugins.prodotti.window.pickers;

import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.controllers.prodotti.ProdottiController;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.plugins.prodotti.window.prods.add.ProdRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class ProdPicker {

    private ProdPicker() {

    }

    public static PickerDialogCNTRL<Prodotto, ProdottoHolder> createPicker() {
        ReferenceQuery<Prodotto, ProdottoHolder> rq = new ReferenceQuery<>(PluginRegisterProdotti.prr, Prodotto.class, SharedReferenceQuery.getController(ProdottiController.class).getRH(), 10);
        ProdRegister pr = new ProdRegister();
        PickerDialogCNTRL<Prodotto, ProdottoHolder> createPicker = DialogCreator.createPicker(rq, "Lista Prodotti", pr);
        createPicker.setTransformer_id(null);
        return createPicker;
    }
}
