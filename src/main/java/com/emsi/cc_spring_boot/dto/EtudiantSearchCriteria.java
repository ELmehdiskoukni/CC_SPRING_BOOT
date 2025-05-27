package com.emsi.cc_spring_boot.dto;
public class EtudiantSearchCriteria {
    private String nom;
    private String prenom;
    private String email;

    public EtudiantSearchCriteria() {}

    public EtudiantSearchCriteria(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean hasSearchCriteria() {
        return (nom != null && !nom.trim().isEmpty()) ||
                (prenom != null && !prenom.trim().isEmpty()) ||
                (email != null && !email.trim().isEmpty());
    }
}
