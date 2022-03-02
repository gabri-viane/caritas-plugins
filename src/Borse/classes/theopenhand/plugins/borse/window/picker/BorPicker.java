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
package theopenhand.plugins.borse.window.picker;

import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.window.borse.add.BorsaRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class BorPicker {

    private BorPicker() {

    }

    public static PickerDialogCNTRL<Borsa, BorsaHolder> createPicker() {
        ReferenceQuery<Borsa, BorsaHolder> rq = new ReferenceQuery<>(PluginRegisterBorse.brr, Borsa.class, SharedReferenceQuery.getController(BorseController.class).getRH(), 0);
        BorsaRegister frcntrl = new BorsaRegister();
        PickerDialogCNTRL<Borsa, BorsaHolder> createPicker = DialogCreator.createPicker(rq, "Lista Borse", frcntrl);
        return createPicker;
    }

}
