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
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.controllers.famiglie.ParentelaController;
import theopenhand.plugins.famiglie.data.Parentela;
import theopenhand.plugins.famiglie.data.holders.ParentelaHolder;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class ParPicker {

    private ParPicker(){
        
    }
    
    public static PickerDialogCNTRL<Parentela, ParentelaHolder> createPicker() {
        ReferenceQuery<Parentela, ParentelaHolder> rq = new ReferenceQuery<>(PluginRegisterFamiglie.frr, Parentela.class, ParentelaController.rs, 0);
        //ParRegisterCNTRL frcntrl = new ParRegisterCNTRL();
        return DialogCreator.createPicker(rq, "Lista Parentele");
    }
}
