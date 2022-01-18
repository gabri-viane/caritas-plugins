/*
 * Copyright 2022 gabri.
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
package theopenhand.plugins.borse.controllers.borse;

import javafx.scene.layout.AnchorPane;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.borse.data.holders.ElementiHolder;
import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public class ElementiController implements ReferenceController {

    public static ElementiHolder rs;

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            //Non fare nulla
        };
    }

    @Override
    public String getAssocButtonName() {
        return "Mostra Elementi Borsa";
    }

    @Override
    public String getGroupName() {
        return "Borse";
    }

    @Override
    public String getID() {
        return "PLG-ELBOR-WND";
    }

    @Override
    public AnchorPane getNode() {
        return null;
    }

}
