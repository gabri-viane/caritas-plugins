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

import java.math.BigInteger;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.plugins.borse.connector.runtimes.BorseRR;
import theopenhand.plugins.borse.connector.runtimes.PluginSettings;
import theopenhand.plugins.borse.controllers.borse.BorseController;
import theopenhand.plugins.borse.controllers.borse.ElementiController;
import theopenhand.plugins.borse.data.Borsa;
import theopenhand.plugins.borse.data.ElementoBorsa;
import theopenhand.plugins.borse.data.holders.BorsaHolder;
import theopenhand.plugins.borse.data.holders.ElementiHolder;
import theopenhand.plugins.borse.data.savable.SavedBorseHolder;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.data.PluginEnvironmentHandler;
import theopenhand.runtime.data.SubscribeData;
import theopenhand.runtime.data.components.ListDataElement;
import theopenhand.runtime.data.components.MapDataElement;
import theopenhand.runtime.data.components.PairDataElement;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class PluginRegisterBorse implements LinkableClass {

    public static final String SAVED_BAGS_FILE = "borse_salvate.dat";

    public static final BorseRR brr = new BorseRR("plugin_borse");
    public static final Settings sf = new PluginSettings();
    public static PluginEnvironmentHandler peh;

    @Override
    public void load() {
        SubscriptionHandler.subscribeToLoading(brr);
        SubscriptionHandler.subscribeToDBObjects(brr, BorsaHolder.class, Borsa.class);
        SubscriptionHandler.subscribeToDBObjects(brr, ElementiHolder.class, ElementoBorsa.class);

        SharedReferenceQuery.getInstance().register(brr, "borsa", Borsa.class, BorseController.class);
        SharedReferenceQuery.getInstance().register(brr, "elementoborsa", ElementoBorsa.class, ElementiController.class);

        setupPEH();
    }

    private void setupPEH() {
        peh = SubscribeData.requestEnv(sf);
        MapDataElement<String, ListDataElement<PairDataElement<BigInteger, Integer>>> data = (MapDataElement<String, ListDataElement<PairDataElement<BigInteger, Integer>>>) peh.getData(SAVED_BAGS_FILE);
        if (data == null) {
            data = new MapDataElement<>(SAVED_BAGS_FILE);
            peh.addData(data);
        }
        SavedBorseHolder.getInstance().setContainerFile(data);
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
