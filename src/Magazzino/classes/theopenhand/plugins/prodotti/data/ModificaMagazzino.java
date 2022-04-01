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
package theopenhand.plugins.prodotti.data;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.interfaces.graphics.ColumnData;
import theopenhand.commons.interfaces.graphics.TableAssoc;

/**
 *
 * @author gabri
 */
public class ModificaMagazzino implements TableAssoc {

    @QueryField(name = "ID", fieldID = 0, registerOut = true)
    protected BigInteger id;

    @QueryField(name = "IDProdotti", fieldID = 1)
    private BigInteger id_prodotto;

    @QueryField(name = "IDMotivi", fieldID = 2)
    private BigInteger id_motivi;

    @QueryCustom(displayName = "Quantità", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Quantità", order = 1)
    @QueryField(name = "Totale", fieldID = 3)
    private Long totale;

    @QueryCustom(displayName = "Sottratto", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Sottratto (Sì)", order = 2)
    @QueryField(name = "IsSottrai", fieldID = 4)
    private Boolean sottrai;

    @QueryCustom(displayName = "Data", enabled = {ClauseType.GROUP_BY, ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Data", order = 3)
    @QueryField(name = "Data", fieldID = 5)
    private Date data;

    @QueryCustom(displayName = "Prodotto", enabled = {ClauseType.GROUP_BY, ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Prodotto", order = 0)
    @QueryField(name = "Prodotto", fieldID = 6)
    private String prodotto;

    @QueryCustom(displayName = "Motivo", enabled = {ClauseType.GROUP_BY, ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Motivo", order = 7)
    @QueryField(name = "Motivo", fieldID = 6)
    private String motivo;

    @QueryCustom(displayName = "A magazzino", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @QueryField(name = "IsMagazzino", fieldID = 8)
    private Boolean is_mag;

    @QueryField(name = "Descrizione", fieldID = 9)
    private String descrizione;

    public ModificaMagazzino() {
    }

    public ModificaMagazzino(long id_prodotto, long id_motivi, Long totale, Boolean sottrai) {
        this.id = null;
        this.id_prodotto = BigInteger.valueOf(id_prodotto);
        this.id_motivi = BigInteger.valueOf(id_motivi);
        this.totale = totale;
        this.sottrai = sottrai;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getId_prodotto() {
        return id_prodotto;
    }

    public void setId_prodotto(BigInteger id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public BigInteger getId_motivi() {
        return id_motivi;
    }

    public void setId_motivi(BigInteger id_motivi) {
        this.id_motivi = id_motivi;
    }

    public Long getTotale() {
        return totale;
    }

    public void setTotale(Long totale) {
        this.totale = totale;
    }

    public Boolean getSottrai() {
        return sottrai;
    }

    public void setSottrai(Boolean sottrai) {
        this.sottrai = sottrai;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getProdotto() {
        return prodotto;
    }

    public void setProdotto(String prodotto) {
        this.prodotto = prodotto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Boolean getIs_mag() {
        return is_mag;
    }

    public void setIs_mag(Boolean is_mag) {
        this.is_mag = is_mag;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModificaMagazzino mm) {
            return mm.id_prodotto != null && id_prodotto != null && mm.id_motivi != null && id_motivi != null && mm.totale != null && totale != null && data != null && mm.data != null
                    && mm.id_prodotto.equals(id_prodotto) && mm.id_motivi.equals(id_motivi) && mm.totale.equals(totale) && mm.data.equals(data);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.id_prodotto);
        hash = 89 * hash + Objects.hashCode(this.id_motivi);
        hash = 89 * hash + Objects.hashCode(this.totale);
        hash = 89 * hash + Objects.hashCode(this.data);
        return hash;
    }

}
