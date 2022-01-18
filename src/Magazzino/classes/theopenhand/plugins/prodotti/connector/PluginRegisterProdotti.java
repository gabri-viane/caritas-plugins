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
package theopenhand.plugins.prodotti.connector;

import theopenhand.plugins.prodotti.connector.runtimes.PluginSettings;
import theopenhand.plugins.prodotti.connector.runtimes.ProdottiRR;
import theopenhand.plugins.prodotti.data.Confezione;
import theopenhand.plugins.prodotti.data.Donatore;
import theopenhand.plugins.prodotti.data.ElementoMagazzino;
import theopenhand.plugins.prodotti.data.Entrata;
import theopenhand.plugins.prodotti.data.Motivo;
import theopenhand.plugins.prodotti.data.Prodotto;
import theopenhand.plugins.prodotti.data.holders.ConfezioneHolder;
import theopenhand.plugins.prodotti.data.holders.DonatoreHolder;
import theopenhand.plugins.prodotti.data.holders.EntrataHolder;
import theopenhand.plugins.prodotti.data.holders.MagazzinoHolder;
import theopenhand.plugins.prodotti.data.holders.MotivoHolder;
import theopenhand.plugins.prodotti.data.holders.ProdottoHolder;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class PluginRegisterProdotti implements LinkableClass {

    public static final ProdottiRR prr = new ProdottiRR("Gestione Prodotti");
    private static final Settings sf = new PluginSettings();

    @Override
    public void load() {
        SubscriptionHandler.subscribeToLoading(prr);
        SubscriptionHandler.subscribeToDBObjects(prr, ConfezioneHolder.class, Confezione.class);
        SubscriptionHandler.subscribeToDBObjects(prr, ProdottoHolder.class, Prodotto.class);
        SubscriptionHandler.subscribeToDBObjects(prr, MagazzinoHolder.class, ElementoMagazzino.class);
        SubscriptionHandler.subscribeToDBObjects(prr, DonatoreHolder.class, Donatore.class);
        SubscriptionHandler.subscribeToDBObjects(prr, MotivoHolder.class, Motivo.class);
        SubscriptionHandler.subscribeToDBObjects(prr, EntrataHolder.class, Entrata.class);
    }

    @Override
    public void unload() {
        SubscriptionHandler.unsubscribeToLoading(prr);
    }

    @Override
    public Settings getSettings() {
        return sf;
    }
}
