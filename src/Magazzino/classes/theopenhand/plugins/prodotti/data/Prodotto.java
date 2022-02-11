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
public class Prodotto implements BindableResult {

    @QueryField(name = "ID", fieldID = 0, registerOut = true)
    protected BigInteger id;

    @QueryCustom(displayName = "Nome", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "Nome", fieldID = 1)
    private String nome;

    @QueryField(name = "IDConfezioni", fieldID = 2)
    private BigInteger id_confezioni;

    @QueryCustom(displayName = "A magazzino", enabled = ClauseType.WHERE)
    @QueryField(name = "IsMagazzino", fieldID = 3)
    private Boolean is_magazzino;

    @QueryCustom(displayName = "Fresco", enabled = ClauseType.WHERE)
    @QueryField(name = "IsFresco", fieldID = 4)
    private Boolean is_fresco;

    @QueryCustom(displayName = "Igiene", enabled = ClauseType.WHERE)
    @QueryField(name = "IsIgiene", fieldID = 5)
    private Boolean is_igiene;

    @QueryCustom(displayName = "Extra", enabled = ClauseType.WHERE)
    @QueryField(name = "IsExtra", fieldID = 6)
    private Boolean is_extra;

    @QueryField(name = "IDMagazzino", fieldID = 7)
    private BigInteger id_mag;

    @QueryCustom(displayName = "Confezione", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "Confezione", fieldID = 8)
    private String nome_confezione;

    @QueryField(name = "NomeFormatted", fieldID = 9)
    private String nome_formatted;

    public Prodotto() {
    }

    public Prodotto(String nome, Long id_confezioni, Boolean is_magazzino, Boolean is_fresco, Boolean is_igiene, Boolean is_extra) {
        this.id = null;
        this.nome = nome;
        this.id_confezioni = BigInteger.valueOf(id_confezioni);
        this.is_magazzino = is_magazzino;
        this.is_fresco = is_fresco;
        this.is_igiene = is_igiene;
        this.is_extra = is_extra;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    public BigInteger getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigInteger getId_confezioni() {
        return id_confezioni;
    }

    public void setId_confezioni(BigInteger id_confezioni) {
        this.id_confezioni = id_confezioni;
    }

    public Boolean getIs_magazzino() {
        return is_magazzino;
    }

    public void setIs_magazzino(Boolean is_magazzino) {
        this.is_magazzino = is_magazzino;
    }

    public Boolean getIs_fresco() {
        return is_fresco;
    }

    public void setIs_fresco(Boolean is_fresco) {
        this.is_fresco = is_fresco;
    }

    public Boolean getIs_igiene() {
        return is_igiene;
    }

    public void setIs_igiene(Boolean is_igiene) {
        this.is_igiene = is_igiene;
    }

    public Boolean getIs_extra() {
        return is_extra;
    }

    public void setIs_extra(Boolean is_extra) {
        this.is_extra = is_extra;
    }

    public BigInteger getId_mag() {
        return id_mag;
    }

    public String getNome_confezione() {
        return nome_confezione;
    }

    public void setNome_confezione(String nome_confezione) {
        this.nome_confezione = nome_confezione;
    }

    public String getNome_formatted() {
        return nome_formatted;
    }

    @Override
    public String toString() {
        return nome_formatted != null ? nome_formatted : nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Prodotto p) {
            return p.id != null && id != null ? p.id.longValue() == id.longValue() : false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.nome);
        return hash;
    }

}
