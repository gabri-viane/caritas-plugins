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
package theopenhand.plugins.famiglie.connector.runtimes;

import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.programm.FutureObjectRequest;
import theopenhand.commons.handlers.InterchangeRequest;
import theopenhand.plugins.famiglie.controllers.famiglie.ComponentiController;
import theopenhand.plugins.famiglie.controllers.famiglie.FamiglieController;
import theopenhand.plugins.famiglie.controllers.famiglie.ParentelaController;
import theopenhand.plugins.famiglie.data.DataBuilder;
import theopenhand.plugins.famiglie.window.pickers.FamPicker;
import theopenhand.plugins.famiglie.window.pickers.ParPicker;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class FamigliaRR extends RuntimeReference {

    Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>>[] res;

    public FamigliaRR(String ref_name) {
        super(ref_name);
        init();
    }

    private void init() {
        addReferenceController(new FamiglieController());
        addReferenceController(new ComponentiController());
        addReferenceController(new ParentelaController());

        InterchangeRequest.register("famiglia_picker", FamPicker::createPicker);
        InterchangeRequest.register("parentela_picker", ParPicker::createPicker);
        InterchangeRequest.register("famiglia_class", new FutureObjectRequest<BindableResult>() {
            @Override
            public BindableResult castToObject(Long l) {
                if (l == null) {
                    return null;
                }
                return DataBuilder.generateFamigliaByID(l);
            }

            @Override
            public BindableResult findObject(Long l) {
                return castToObject(l);
            }
        });
        Ribbon r = new Ribbon(this);
    }
}
