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
import theopenhand.plugins.prodotti.controllers.prodotti.ConfezioniController;
import theopenhand.plugins.prodotti.data.Confezione;
import theopenhand.plugins.prodotti.data.holders.ConfezioneHolder;
import theopenhand.plugins.prodotti.window.confs.add.ConfRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class ConfPicker {

    private ConfPicker() {

    }

    public static PickerDialogCNTRL<Confezione, ConfezioneHolder> createPicker() {
        ReferenceQuery<Confezione, ConfezioneHolder> rq = new ReferenceQuery<>(PluginRegisterProdotti.prr, Confezione.class, SharedReferenceQuery.getController(ConfezioniController.class).getRH(), 0);
        ConfRegister cr = new ConfRegister();
        PickerDialogCNTRL<Confezione, ConfezioneHolder> createPicker = DialogCreator.createPicker(rq, "Lista Confezioni", cr);
        createPicker.setTransformer_id(null);
        return createPicker;
    }
}
