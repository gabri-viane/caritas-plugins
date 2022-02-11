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
package theopenhand.plugins.prodotti.data;

import java.math.BigInteger;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;

/**
 *
 * @author gabri
 */
public class Motivo implements BindableResult {

    @QueryField(name = "ID", fieldID = 0, registerOut = true)
    protected BigInteger id;

    @QueryCustom(displayName = "Nome motivo", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "Nome", fieldID = 1)
    private String name;

    @QueryField(name = "Descrizione", fieldID = 2)
    private String descrizione;

    public Motivo() {
    }

    public Motivo(String name, String descrizione) {
        this.id = null;
        this.name = name;
        this.descrizione = descrizione;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return name;
    }

}
