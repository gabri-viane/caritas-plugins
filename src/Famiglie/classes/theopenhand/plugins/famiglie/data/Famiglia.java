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
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.interfaces.graphics.ColumnData;
import theopenhand.commons.interfaces.graphics.TableAssoc;

/**
 *
 * @author gabri
 */
public class Famiglia implements BindableResult, TableAssoc {

    @QueryField(name = "ID", fieldID = 1, registerOut = true)
    protected BigInteger id;

    @QueryCustom(displayName = "ID", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "ID Famiglia", order = 0)
    @QueryField(name = "IDFAM", fieldID = 2)
    private BigInteger idfam;

    @QueryCustom(displayName = "Dichiarante", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Dichiarante", order = 1)
    @QueryField(name = "Dichiarante", fieldID = 3)
    private String dichiarante;

    @QueryCustom(displayName = "Nome dich.", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "NDic", fieldID = 4)
    private String nome_dic;

    @QueryCustom(displayName = "Cognome dich.", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "CDic", fieldID = 5)
    private String cogn_dic;

    @QueryCustom(displayName = "Indirizzo", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Indirizzo", order = 2)
    @QueryField(name = "Indirizzo", fieldID = 6)
    private String indirizzo;

    @QueryCustom(displayName = "Telefono", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @QueryField(name = "Telefono", fieldID = 7)
    private String telefono;

    @QueryField(name = "NCon", fieldID = 8)
    private String nome_con;

    @QueryField(name = "CCon", fieldID = 9)
    private String cogn_con;

    @QueryField(name = "DNsacita", fieldID = 10)
    private Date d_nascita_con;

    @QueryField(name = "IDInfo", fieldID = 11, registerOut = true)
    private BigInteger id_info;

    @QueryField(name = "IDCon", fieldID = 12, registerOut = true)
    private BigInteger id_coniuge;

    @QueryCustom(displayName = "Codice Fiscale", enabled = {ClauseType.WHERE, ClauseType.ORDER_BY})
    @ColumnData(Title = "Codice Fiscale", order = 3)
    @QueryField(name = "CodiceF", fieldID = 13)
    private String codice_fiscale;

    @Override
    public BigInteger getID() {
        return id;
    }

    public Famiglia() {
    }

    public Famiglia(long idfam, String nome_dic, String cogn_dic, String indirizzo, String telefono, String nome_con, String cogn_con, Date d_nascita_con, String codicef) {
        this.id = null;
        this.idfam = BigInteger.valueOf(idfam);
        this.nome_dic = nome_dic;
        this.cogn_dic = cogn_dic;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.nome_con = nome_con;
        this.cogn_con = cogn_con;
        this.d_nascita_con = d_nascita_con;
        this.codice_fiscale = codicef;
    }

    public Long getId() {
        return id.longValue();
    }

    public Long getIdfam() {
        return idfam.longValue();
    }

    public String getDichiarante() {
        return dichiarante;
    }

    public String getNome_dic() {
        return nome_dic;
    }

    public String getCogn_dic() {
        return cogn_dic;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNome_con() {
        return nome_con;
    }

    public String getCogn_con() {
        return cogn_con;
    }

    public Date getD_nascita_con() {
        return d_nascita_con;
    }

    public Long getId_info() {
        return id_info.longValue();
    }

    public Long getId_coniuge() {
        return id_coniuge.longValue();
    }

    public String getCodice_fiscale() {
        return codice_fiscale;
    }

    public void setNome_dic(String nome_dic) {
        this.nome_dic = nome_dic;
    }

    public void setCogn_dic(String cogn_dic) {
        this.cogn_dic = cogn_dic;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    @Override
    public String toString() {
        return dichiarante != null ? dichiarante : cogn_dic;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Famiglia f) {
            return f.id != null && id != null ? f.id.longValue() == id.longValue() : false; //To change body of generated methods, choose Tools | Templates.
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
