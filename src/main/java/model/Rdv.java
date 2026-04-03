package com.medicin.demo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rdv")
public class Rdv {
    @Id
    private String idrdv;

    @Column(name = "idmed")
    private String idmed;

    @Column(name = "idpat")
    private int idpat;

    @Column(name = "date_rdv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRdv;

    // Constructeurs
    public Rdv() {}

    // Getters et Setters
    public String getIdrdv() { return idrdv; }
    public void setIdrdv(String idrdv) { this.idrdv = idrdv; }
    public String getIdmed() { return idmed; }
    public void setIdmed(String idmed) { this.idmed = idmed; }
    public int getIdpat() { return idpat; }
    public void setIdpat(int idpat) { this.idpat = idpat; }
    public Date getDateRdv() { return dateRdv; }
    public void setDateRdv(Date dateRdv) { this.dateRdv = dateRdv; }
}
