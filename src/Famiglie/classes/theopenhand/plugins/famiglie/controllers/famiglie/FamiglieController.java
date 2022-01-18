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

import java.util.Optional;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.plugins.famiglie.connector.PluginRegisterFamiglie;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.window.fams.FamMain;
import theopenhand.runtime.templates.ReferenceController;

/**
 * F
 *
 * @author gabri
 */
public class FamiglieController implements ReferenceController {

    public static FamigliaHolder rs;
    private final FamMain fp = new FamMain();

    @Override
    public ClickListener getOnAssocButtonClick() {
        return () -> {
            Optional<ResultHolder> eq = ConnectionExecutor.getInstance().executeQuery(PluginRegisterFamiglie.frr, 0, Famiglia.class, null);
            if (eq.isPresent()) {
                rs = (FamigliaHolder) eq.get();
            }
        };
    }

    @Override
    public String getAssocButtonName() {
        return "Mostra famiglie";
    }

    @Override
    public String getGroupName() {
        return "Famiglie";
    }

    @Override
    public String getID() {
        return "PLG-FAM-WND";
    }

    @Override
    public AnchorPane getNode() {
        fp.reloadElements(true);
        return fp;
    }

}