package com.medicin.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medecin")
public class Medecin {
    @Id
    private String idmed;
    private String nommed;
    private String specialite;
    private int taux_horaire;
    private String lieu;
    private String email;
    private String password;

    public Medecin() {}

    // Getters et Setters existants
    public String getIdmed() { return idmed; }
    public void setIdmed(String idmed) { this.idmed = idmed; }
    public String getNommed() { return nommed; }
    public void setNommed(String nommed) { this.nommed = nommed; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public int getTaux_horaire() { return taux_horaire; }
    public void setTaux_horaire(int taux_horaire) { this.taux_horaire = taux_horaire; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // AJOUTÉS : Indispensables pour le Password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}