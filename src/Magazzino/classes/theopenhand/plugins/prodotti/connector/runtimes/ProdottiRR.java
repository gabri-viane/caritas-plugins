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
package theopenhand.plugins.prodotti.connector.runtimes;

import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.handlers.InterchangeRequest;
import theopenhand.plugins.prodotti.controllers.prodotti.ConfezioniController;
import theopenhand.plugins.prodotti.controllers.prodotti.DonatoriController;
import theopenhand.plugins.prodotti.controllers.prodotti.EntrateController;
import theopenhand.plugins.prodotti.controllers.prodotti.MagazzinoController;
import theopenhand.plugins.prodotti.controllers.prodotti.MotiviController;
import theopenhand.plugins.prodotti.controllers.prodotti.ProdottiController;
import theopenhand.plugins.prodotti.controllers.prodotti.secondary.NewConfezioneController;
import theopenhand.plugins.prodotti.controllers.prodotti.secondary.NewDonatoreController;
import theopenhand.plugins.prodotti.controllers.prodotti.secondary.NewEntrataController;
import theopenhand.plugins.prodotti.controllers.prodotti.secondary.NewMotivoController;
import theopenhand.plugins.prodotti.controllers.prodotti.secondary.NewProdottoController;
import theopenhand.plugins.prodotti.window.pickers.ConfPicker;
import theopenhand.plugins.prodotti.window.pickers.DonPicker;
import theopenhand.plugins.prodotti.window.pickers.ProdPicker;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class ProdottiRR extends RuntimeReference {

    Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>>[] res;

    public ProdottiRR(String ref_name) {
        super(ref_name);
        init();
    }

    private void init() {
        addReferenceController(new ConfezioniController());
        addReferenceController(new MagazzinoController());
        addReferenceController(new ProdottiController());
        addReferenceController(new DonatoriController());
        addReferenceController(new MotiviController());
        addReferenceController(new EntrateController());

        addReferenceController(new NewProdottoController());
        addReferenceController(new NewConfezioneController());
        addReferenceController(new NewDonatoreController());
        addReferenceController(new NewEntrataController());
        addReferenceController(new NewMotivoController());

        InterchangeRequest.register("prodotto_picker", ProdPicker::createPicker);
        InterchangeRequest.register("confezione_picker", ConfPicker::createPicker);
        InterchangeRequest.register("donatore_picker", DonPicker::createPicker);
    }

}
