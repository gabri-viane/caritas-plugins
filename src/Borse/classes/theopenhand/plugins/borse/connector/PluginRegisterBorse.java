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
package theopenhand.plugins.borse.connector;

import theopenhand.plugins.borse.connector.runtimes.BorseRR;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.ElementoBorsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.data.holders.ElementiHolder;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class PluginRegisterBorse implements LinkableClass {

    public static final BorseRR brr = new BorseRR("Gestione Borse");
    private static final Settings sf = new PluginSettings();

    @Override
    public void load() {
        SubscriptionHandler.subscribeToLoading(brr);
        SubscriptionHandler.subscribeToDBObjects(brr, BorsaHolder.class, Borsa.class);
        SubscriptionHandler.subscribeToDBObjects(brr, ElementiHolder.class, ElementoBorsa.class);
    }

    @Override
    public void unload() {
        SubscriptionHandler.unsubscribeToLoading(brr);
    }

    @Override
    public Settings getSettings() {
        return sf;
    }

}
