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
import java.util.Objects;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;

/**
 *
 * @author gabri
 */
public class Parentela implements BindableResult {

    @QueryField(name = "ID", fieldID = 1, registerOut = true)
    protected BigInteger id;

    @QueryField(name = "Tipo", fieldID = 2)
    private String tipo;

    public Parentela() {
    }

    public Parentela(String tipo) {
        this.id = null;
        this.tipo = tipo;
    }

    public BigInteger getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    @Override
    public String toString() {
        return tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Parentela p) {
            return p.id != null && id != null ? p.id.longValue() == id.longValue() : false; //To change body of generated methods, choose Tools | Templates.
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
