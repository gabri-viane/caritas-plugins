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
package theopenhand.plugins.famiglie.controllers.famiglie;

import javafx.scene.layout.AnchorPane;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.famiglie.data.holders.ComponenteHolder;
import theopenhand.plugins.famiglie.window.comps.CompMain;
import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public class ComponentiController extends ReferenceController<ComponenteHolder> {

    private static CompMain cm;

    private static CompMain getCM() {
        if (cm == null) {
            cm = new CompMain();
        }
        return cm;
    }

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            getCM().changeFam();
        };
    }

    @Override
    public String getAssocButtonName() {
        return "Mostra componenti";
    }

    @Override
    public String getGroupName() {
        return "Famiglie";
    }

    @Override
    public String getID() {
        return "PLG-COMP-WND";
    }

    @Override
    public AnchorPane getNode() {
        return getCM();
    }

}
