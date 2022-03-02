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
package theopenhand.plugins.famiglie.window.pickers;

import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.controllers.famiglie.FamiglieController;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.fams.add.FamRegister;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class FamPicker {

    private FamPicker() {

    }

    public static PickerDialogCNTRL<Famiglia, FamigliaHolder> createPicker() {
        ReferenceQuery<Famiglia, FamigliaHolder> rq = new ReferenceQuery<>(PluginRegisterFamiglie.frr, Famiglia.class, SharedReferenceQuery.getController(FamiglieController.class).getRH(), 0);
        FamRegister frcntrl = new FamRegister();
        PickerDialogCNTRL<Famiglia, FamigliaHolder> createPicker = DialogCreator.createPicker(rq, "Lista Famiglie", frcntrl);
        createPicker.setTransformer_id(f -> {
            return "" + f.getIdfam();
        });
        return createPicker;
    }

}
