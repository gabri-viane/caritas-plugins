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
package theopenhand.plugins.borse.data;

import java.math.BigInteger;
import java.util.Objects;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.interfaces.graphics.ColumnData;
import theopenhand.commons.interfaces.graphics.TableAssoc;

/**
 *
 * @author gabri
 */
public class ElementoBorsa implements BindableResult, TableAssoc {

    @QueryField(fieldID = 0, name = "ID", registerOut = true)
    protected BigInteger id;

    @QueryField(fieldID = 1, name = "IDBorse")
    private BigInteger idborsa;

    @QueryField(fieldID = 2, name = "IDProdotti")
    private BigInteger idprodotto;

    @QueryCustom(displayName = "Quantità", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Totale", order = 2)
    @QueryField(fieldID = 3, name = "Totale")
    private BigInteger tot;

    @ColumnData(Title = "Rimosso dal magazzino", order = 3)
    @QueryField(fieldID = 4, name = "IsSottratto")
    private Boolean subtr;

    @QueryCustom(displayName = "Prodotto", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Prodotto", order = 0)
    @QueryField(fieldID = 5, name = "Prodotto")
    private String prodotto;

    @QueryCustom(displayName = "Confezione", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Confezione", order = 1)
    @QueryField(fieldID = 6, name = "Confezione")
    private String confezione;

    @QueryField(fieldID = 7, name = "NO_ASSOC_DATA")
    private final Boolean existing;

    @QueryField(fieldID = 8, name = "NO_ASSOC_DATA")
    private Boolean restore_on_delete = true;

    public ElementoBorsa() {
        existing = true;
    }

    public ElementoBorsa(boolean existing) {
        this.existing = existing;
    }

    public ElementoBorsa(long idborsa, long idprodotto, long tot, boolean subtr) {
        this.id = null;
        this.idborsa = BigInteger.valueOf(idborsa);
        this.idprodotto = BigInteger.valueOf(idprodotto);
        this.tot = BigInteger.valueOf(tot);
        this.subtr = subtr;
        existing = false;
    }

    public static ElementoBorsa create(BigInteger idprodotto, BigInteger tot) {
        ElementoBorsa eb = new ElementoBorsa(false);
        eb.idprodotto = idprodotto;
        eb.tot = tot;
        eb.subtr = false;
        return eb;
    }

    public boolean alreadyExists() {
        return existing;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public BigInteger getIdborsa() {
        return idborsa;
    }

    public void setIdborsa(BigInteger idborsa) {
        this.idborsa = idborsa;
    }

    public BigInteger getIdprodotto() {
        return idprodotto;
    }

    public void setIdprodotto(BigInteger idprodotto) {
        this.idprodotto = idprodotto;
    }

    public BigInteger getTot() {
        return tot;
    }

    public void setTot(Long tot) {
        this.tot = BigInteger.valueOf(tot);
    }

    public void setTot(BigInteger tot) {
        this.tot = tot;
    }

    public Boolean getSubtr() {
        return subtr;
    }

    public void setSubtr(Boolean subtr) {
        this.subtr = subtr;
    }

    public String getProdotto() {
        return prodotto;
    }

    public void setProdotto(String prodotto) {
        this.prodotto = prodotto;
    }

    public String getConfezione() {
        return confezione;
    }

    public void setConfezione(String confezione) {
        this.confezione = confezione;
    }

    public Boolean getRestore_on_delete() {
        return restore_on_delete;
    }

    public void setRestore_on_delete(Boolean restore_on_delete) {
        this.restore_on_delete = restore_on_delete;
    }

    @Override
    public String toString() {
        return prodotto + " - " + confezione + " : " + tot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ElementoBorsa eb) {
            return eb.id != null & id != null ? id.longValue() == eb.id.longValue() : false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.idborsa);
        hash = 23 * hash + Objects.hashCode(this.idprodotto);
        return hash;
    }

}
