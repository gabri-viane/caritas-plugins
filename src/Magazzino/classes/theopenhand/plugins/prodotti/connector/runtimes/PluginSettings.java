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

import theopenhand.commons.programm.loader.LinkableBoolean;
import theopenhand.commons.programm.loader.LinkableInteger;
import theopenhand.runtime.annotations.SettingProperty;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class PluginSettings extends Settings {

    @SettingProperty(id = "table_prop", name = "Usa tabelle", description = "Utilizza le tabelle per mostrare liste di dati")
    public static LinkableBoolean table_prop = new LinkableBoolean(true);

    @SettingProperty(id = "qlimit", name = "Limita dati", description = "Imposta un limite di righe richiedibile per singola query")
    public static LinkableInteger query_limit = new LinkableInteger(-1);
}
