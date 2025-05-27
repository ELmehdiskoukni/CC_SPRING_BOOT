package com.emsi.cc_spring_boot.service;

import com.emsi.cc_spring_boot.entity.Etudiant;

import java.util.List;

public interface EtudiantService {
    List<Etudiant> getAllEtudiants();
    Etudiant getEtudiantById(Long id);
    Etudiant saveEtudiant(Etudiant etudiant);
    boolean deleteEtudiant(Long id);
    List<Etudiant> searchEtudiants(String nom, String prenom, String email);
    boolean emailExists(String email);
    boolean emailExistsExcept(String email, Long id);
    long countEtudiants();
}