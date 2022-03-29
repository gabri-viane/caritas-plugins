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
import java.util.Date;
import java.util.Objects;
import theopenhand.commons.DataUtils;
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
public class Borsa implements BindableResult, TableAssoc {

    @QueryField(fieldID = 0, name = "ID", registerOut = true)
    protected BigInteger id;

    @QueryField(fieldID = 1, name = "IDFAM")
    private BigInteger id_fam;

    @QueryCustom(displayName = "Data consegna", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY, ClauseType.GROUP_BY})
    @ColumnData(Title = "Data di consegna", order = 1)
    @QueryField(fieldID = 2, name = "DataConsegna")
    private Date consegna;

    @QueryField(fieldID = 3, name = "Note")
    private String note;

    @QueryCustom(displayName = "Consegnata", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Consegnata", order = 2)
    @QueryField(fieldID = 4, name = "Consegnata")
    private Boolean consegnata;

    @QueryCustom(displayName = "Famiglia", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY, ClauseType.GROUP_BY})
    @ColumnData(Title = "Famiglia", order = 0)
    @QueryField(fieldID = 5, name = "Famiglia")
    private String dichiarante;

    //@ColumnData(Title = "Elementi", order = 3)
    @QueryField(fieldID = 6, name = "NumeroElementi")
    private Long ne;

    public Borsa() {
    }

    public Borsa(long id_fam, Date consegna, String note, Boolean consegnata) {
        this.id = null;
        this.id_fam = BigInteger.valueOf(id_fam);
        this.consegna = consegna;
        this.note = note;
        this.consegnata = consegnata;
    }

    public BigInteger getId_fam() {
        return id_fam;
    }

    public void setId_fam(BigInteger id_fam) {
        this.id_fam = id_fam;
    }

    public void setId_fam(Long id_fam) {
        this.id_fam = BigInteger.valueOf(id_fam);
    }

    public Date getConsegna() {
        return consegna;
    }

    public void setConsegna(Date consegna) {
        this.consegna = consegna;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getConsegnata() {
        return consegnata;
    }

    public void setConsegnata(Boolean consegnata) {
        this.consegnata = consegnata;
    }

    public String getDichiarante() {
        return dichiarante;
    }

    public void setDichiarante(String dichiarante) {
        this.dichiarante = dichiarante;
    }

    public Long getNumeroElementi() {
        return ne;
    }

    @Override
    public BigInteger getID() {
        return id;
    }

    @Override
    public String toString() {
        return dichiarante + " - " + DataUtils.format(consegna);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Borsa b) {
            return b.id != null & id != null ? id.longValue() == b.id.longValue() : false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.id_fam);
        return hash;
    }

}
