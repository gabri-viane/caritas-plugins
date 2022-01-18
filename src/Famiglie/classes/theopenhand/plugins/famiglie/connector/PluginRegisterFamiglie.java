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
package theopenhand.plugins.famiglie.connector;

import theopenhand.plugins.famiglie.connector.runtimes.FamigliaRR;
import theopenhand.plugins.famiglie.connector.runtimes.PluginSettings;
import theopenhand.plugins.famiglie.data.Componente;
import theopenhand.plugins.famiglie.data.holders.ComponenteHolder;
import theopenhand.plugins.famiglie.data.Famiglia;
import theopenhand.plugins.famiglie.data.holders.FamigliaHolder;
import theopenhand.plugins.famiglie.data.InformazioniFamiglia;
import theopenhand.plugins.famiglie.data.holders.InformazioniFamigliaHolder;
import theopenhand.plugins.famiglie.data.Parentela;
import theopenhand.plugins.famiglie.data.holders.ParentelaHolder;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class PluginRegisterFamiglie implements LinkableClass {

    public static final FamigliaRR frr = new FamigliaRR("Gestione Famiglie");
    private static final Settings sf = new PluginSettings();

    @Override
    public void load() {
        SubscriptionHandler.subscribeToLoading(frr);
        SubscriptionHandler.subscribeToDBObjects(frr, FamigliaHolder.class, Famiglia.class);
        SubscriptionHandler.subscribeToDBObjects(frr, ComponenteHolder.class, Componente.class);
        SubscriptionHandler.subscribeToDBObjects(frr, InformazioniFamigliaHolder.class, InformazioniFamiglia.class);
        SubscriptionHandler.subscribeToDBObjects(frr, ParentelaHolder.class, Parentela.class);
    }

    @Override
    public void unload() {
        SubscriptionHandler.unsubscribeToLoading(frr);
    }

    @Override
    public Settings getSettings() {
        return sf;
    }

}
