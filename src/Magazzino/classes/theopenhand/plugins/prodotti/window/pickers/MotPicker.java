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
import theopenhand.plugins.prodotti.controllers.prodotti.MotiviController;
import theopenhand.plugins.prodotti.data.Motivo;
import theopenhand.plugins.prodotti.data.holders.MotivoHolder;
import theopenhand.plugins.prodotti.window.mots.add.MotRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class MotPicker {
    private MotPicker() {

    }

    public static PickerDialogCNTRL<Motivo, MotivoHolder> createPicker() {
        ReferenceQuery<Motivo, MotivoHolder> rq = new ReferenceQuery<>(PluginRegisterProdotti.prr, Motivo.class, MotiviController.rs, 0);
        MotRegister cr = new MotRegister();
        PickerDialogCNTRL<Motivo, MotivoHolder> createPicker = DialogCreator.createPicker(rq, "Lista Motivi", cr);
        createPicker.setTransformer_id(null);
        return createPicker;
    }
}
