/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.plugins.famiglie.data;

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
public class Componente implements TableAssoc {

    @QueryField(name = "ID", fieldID = 1, registerOut = true)
    private BigInteger id;

    @QueryCustom(displayName = "ID Famiglia", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY, ClauseType.GROUP_BY})
    @QueryField(name = "IDFAM", fieldID = 2)
    private BigInteger idfam;

    @QueryCustom(displayName = "Nome", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Nome", order = 0)
    @QueryField(name = "Nome", fieldID = 3)
    private String nome_com;

    @ColumnData(Title = "Cognome", order = 1)
    @QueryField(name = "Cognome", fieldID = 4)
    private String cogn_com;

    @QueryCustom(displayName = "Data Nascita", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Data Nascita", order = 2)
    @QueryField(name = "Nascita", fieldID = 5)
    private Date d_nascita;

    @QueryField(name = "IDParentela", fieldID = 6)
    private BigInteger id_parentela;

    @QueryCustom(displayName = "Parentela", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY, ClauseType.GROUP_BY})
    @ColumnData(Title = "Grado di Parentela", order = 3)
    @QueryField(name = "Parentela", fieldID = 7)
    private String parentela;

    public Componente() {
    }

    public Componente(long idfam, String nome_con, String cogn_con, Date d_nascita, long id_parentela) {
        this.idfam = BigInteger.valueOf(idfam);
        this.nome_com = nome_con;
        this.cogn_com = cogn_con;
        this.d_nascita = d_nascita;
        this.id_parentela = BigInteger.valueOf(id_parentela);
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

    public String getNome_com() {
        return nome_com;
    }

    public String getCogn_com() {
        return cogn_com;
    }

    public Date getD_nascita() {
        return d_nascita;
    }

    public BigInteger getId_parentela() {
        return id_parentela;
    }

    public String getParentela() {
        return parentela;
    }

    public void setId_parentela(BigInteger id_parentela) {
        this.id_parentela = id_parentela;
    }

    public void setCogn_com(String cogn_com) {
        this.cogn_com = cogn_com;
    }

    public void setNome_com(String nome_com) {
        this.nome_com = nome_com;
    }

    public void setD_nascita(Date d_nascita) {
        this.d_nascita = d_nascita;
    }

    public void setParentela(String parentela) {
        this.parentela = parentela;
    }

    public void setIdfam(long idfam) {
        this.idfam = BigInteger.valueOf(idfam);
    }

    @Override
    public String toString() {
        return nome_com + " " + cogn_com + (parentela != null ? " - " + parentela : ""); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Componente c) {
            return c.id != null & id != null ? id.longValue() == c.id.longValue() : false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.idfam);
        return hash;
    }

}
