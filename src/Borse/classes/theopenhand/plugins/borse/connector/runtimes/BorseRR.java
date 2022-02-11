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
package theopenhand.plugins.borse.connector.runtimes;

import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.controllers.borse.ElementiController;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class BorseRR extends RuntimeReference {

    Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>>[] res;

    public BorseRR(String ref_name) {
        super(ref_name);
        init();
    }

    private void init() {
        addReferenceController(new BorseController());
        addReferenceController(new ElementiController());
        
        /*InterchangeRequest.register("borsa_picker", () -> {
            return BorPicker.createPicker();
        });*/
    }

}
