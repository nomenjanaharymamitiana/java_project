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
    private String idpat;

    @Column(name = "date_rdv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRdv;

    private String status; // e.g., "CONFIRMED", "CANCELLED"

    // Constructeurs
    public Rdv() {}

    public Rdv(String idrdv, String idmed, String idpat, Date dateRdv) {
        this.idrdv = idrdv;
        this.idmed = idmed;
        this.idpat = idpat;
        this.dateRdv = dateRdv;
        this.status = "CONFIRMED";
    }

    // Getters et Setters
    public String getIdrdv() { return idrdv; }
    public void setIdrdv(String idrdv) { this.idrdv = idrdv; }
    public String getIdmed() { return idmed; }
    public void setIdmed(String idmed) { this.idmed = idmed; }
    public String getIdpat() { return idpat; }
    public void setIdpat(String idpat) { this.idpat = idpat; }
    public Date getDateRdv() { return dateRdv; }
    public void setDateRdv(Date dateRdv) { this.dateRdv = dateRdv; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
