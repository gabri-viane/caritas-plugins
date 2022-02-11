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
import theopenhand.commons.interfaces.graphics.ColumnData;
import theopenhand.commons.interfaces.graphics.TableAssoc;

/**
 *
 * @author gabri
 */
public class ElementoMagazzino implements TableAssoc {

    @QueryField(name = "ID", fieldID = 0, registerOut = true)
    protected BigInteger id;

    @QueryField(name = "IDProdotto", fieldID = 1)
    private BigInteger id_prodotto;

    @QueryCustom(displayName = "Quantità", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Quantità", order = 1)
    @QueryField(name = "Totale", fieldID = 2)
    private Long totale;

    @QueryCustom(displayName = "Nome prodotto", enabled = {ClauseType.ORDER_BY, ClauseType.WHERE})
    @ColumnData(Title = "Prodotto", order = 0)
    @QueryField(name = "Nome", fieldID = 3)
    private String nome;

    public ElementoMagazzino() {
    }

    public ElementoMagazzino(long id_prodotto, Long totale, String nome) {
        this.id = null;
        this.id_prodotto = BigInteger.valueOf(id_prodotto);
        this.totale = totale;
        this.nome = nome;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public BigInteger getId() {
        return id;
    }

    public BigInteger getId_prodotto() {
        return id_prodotto;
    }

    public Long getTotale() {
        return totale;
    }

    public String getNome() {
        return nome;
    }

    public void setId_prodotto(BigInteger id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public void setTotale(Long totale) {
        this.totale = totale;
    }

    public void setTotale(long totale) {
        this.totale = totale;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
