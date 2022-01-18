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

import java.util.Optional;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.borse.connector.PluginRegisterBorse;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.window.borse.BorseMain;
import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public class BorseController implements ReferenceController {

    public static BorsaHolder rs;
    private final BorseMain bp = new BorseMain();

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            Optional<ResultHolder> eq = ConnectionExecutor.getInstance().executeQuery(PluginRegisterBorse.brr, 0, Borsa.class, null);
            if (eq.isPresent()) {
                rs = (BorsaHolder) eq.get();
            }
        };
    }

    @Override
    public String getAssocButtonName() {
        return "Mostra borse";
    }

    @Override
    public String getGroupName() {
        return "Borse";
    }

    @Override
    public String getID() {
        return "PLG-BOR-WND";
    }

    @Override
    public AnchorPane getNode() {
        bp.reloadElements(true);
        return bp;
    }
}
