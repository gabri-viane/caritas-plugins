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
import theopenhand.plugins.prodotti.connector.PluginRegisterProdotti;
import theopenhand.plugins.prodotti.controllers.prodotti.DonatoriController;
import theopenhand.plugins.prodotti.data.Donatore;
import theopenhand.plugins.prodotti.data.holders.DonatoreHolder;
import theopenhand.plugins.prodotti.window.dons.add.DonRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class DonPicker {

    private DonPicker() {

    }

    public static PickerDialogCNTRL<Donatore, DonatoreHolder> createPicker() {
        ReferenceQuery<Donatore, DonatoreHolder> rq = new ReferenceQuery<>(PluginRegisterProdotti.prr, Donatore.class, DonatoriController.rs, 0);
        DonRegister cr = new DonRegister();
        PickerDialogCNTRL<Donatore, DonatoreHolder> createPicker = DialogCreator.createPicker(rq, "Lista Donatori", cr);
        createPicker.setTransformer_id(null);
        return createPicker;
    }
}
