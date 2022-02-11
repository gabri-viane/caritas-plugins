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
import java.util.Objects;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;

/**
 *
 * @author gabri
 */
public class Confezione implements BindableResult {

    @QueryField(name = "ID", fieldID = 0, registerOut = true)
    protected BigInteger id;

    @QueryCustom(displayName = "Nome", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @QueryField(name = "Dimensione", fieldID = 1)
    protected String dimensione;

    @QueryCustom(displayName = "Nome", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @QueryField(name = "Confezione", fieldID = 2)
    protected String confezione;

    public Confezione() {
    }

    public Confezione(String dimensione) {
        this.id = null;
        this.dimensione = dimensione;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public void setDimensione(String dimensione) {
        this.dimensione = dimensione;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDimensione() {
        return dimensione;
    }

    public String getConfezione() {
        return confezione;
    }

    @Override
    public String toString() {
        return dimensione != null ? dimensione : confezione;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Confezione c) {
            return c.id != null & id != null ? id.longValue() == c.id.longValue() : false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.dimensione);
        return hash;
    }

}
