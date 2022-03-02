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
package theopenhand.plugins.famiglie.data;

import java.math.BigInteger;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;

/**
 *
 * @author gabri
 */
public class InformazioniFamiglia implements BindableResult {

    @QueryField(name = "ID", fieldID = 1, registerOut = true)
    private BigInteger id;

    @QueryCustom(displayName = "ID Famiglia", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "IDFAM", fieldID = 2)
    private BigInteger idfam;

    @QueryCustom(displayName = "Indirizzo", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "Indirizzo", fieldID = 3)
    private String indirizzo;

    @QueryCustom(displayName = "Telefono", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "Telefono", fieldID = 4)
    private String telefono;

    public InformazioniFamiglia() {

    }

    public InformazioniFamiglia(long idfam, String indirizzo, String telefono) {
        this.id = null;
        this.idfam = BigInteger.valueOf(idfam);
        this.indirizzo = indirizzo;
        this.telefono = telefono;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public BigInteger getId() {
        return id;
    }

    public BigInteger getIdfam() {
        return idfam;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

}
