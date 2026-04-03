package com.medicin.demo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    private String idpat;
    private String nom_pat;
    
    @Temporal(TemporalType.DATE)
    private Date datenais;
    private String email;    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Patient() {}

    // Getters et Setters
    public String getIdpat() { return idpat; }
    public void setIdpat(String idpat) { this.idpat = idpat; }
    public String getNom_pat() { return nom_pat; }
    public void setNom_pat(String nom_pat) { this.nom_pat = nom_pat; }
    public Date getDatenais() { return datenais; }
    public void setDatenais(Date datenais) { this.datenais = datenais; }
}
