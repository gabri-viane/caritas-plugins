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
import java.util.Date;
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
public class Entrata implements BindableResult, TableAssoc {

    @QueryField(name = "ID", fieldID = 0, registerOut = true)
    protected BigInteger id;

    @QueryCustom(displayName = "Nome prodotto", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Prodotto", order = 0)
    @QueryField(name = "Prodotto", fieldID = 1)
    private String nome_prodotto;

    @QueryCustom(displayName = "Nome donatore", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Donatore", order = 1)
    @QueryField(name = "Donatore", fieldID = 2)
    private String nome_donatore;

    @QueryCustom(displayName = "Quantità", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Quantità", order = 2)
    @QueryField(name = "Totale", fieldID = 3)
    private Integer totale;

    @QueryCustom(displayName = "Data", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE, ClauseType.GROUP_BY})
    @ColumnData(Title = "Data entrata", order = 3)
    @QueryField(name = "Arrivo", fieldID = 4)
    private Date data_arrivo;

    @QueryField(name = "IDProdotti", fieldID = 5)
    private BigInteger id_prodotto;

    @QueryField(name = "IDDonatori", fieldID = 6)
    private BigInteger id_donatore;

    public Entrata() {
    }

    public Entrata(Integer totale, Date data_arrivo, long id_prodotto, long id_donatore) {
        this.id = null;
        this.nome_prodotto = null;
        this.nome_donatore = null;
        this.totale = totale;
        this.data_arrivo = data_arrivo;
        this.id_prodotto = BigInteger.valueOf(id_prodotto);
        this.id_donatore = BigInteger.valueOf(id_donatore);
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public BigInteger getId() {
        return id;
    }

    public Date getData_arrivo() {
        return data_arrivo;
    }

    public String getNome_prodotto() {
        return nome_prodotto;
    }

    public void setNome_prodotto(String nome_prodotto) {
        this.nome_prodotto = nome_prodotto;
    }

    public String getNome_donatore() {
        return nome_donatore;
    }

    public void setNome_donatore(String nome_donatore) {
        this.nome_donatore = nome_donatore;
    }

    public Integer getTotale() {
        return totale;
    }

    public void setTotale(Integer totale) {
        this.totale = totale;
    }

    public BigInteger getId_prodotto() {
        return id_prodotto;
    }

    public void setId_prodotto(BigInteger id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public BigInteger getId_donatore() {
        return id_donatore;
    }

    public void setId_donatore(BigInteger id_donatore) {
        this.id_donatore = id_donatore;
    }

    @Override
    public String toString() {
        return nome_prodotto + " +" + totale;
    }

}
