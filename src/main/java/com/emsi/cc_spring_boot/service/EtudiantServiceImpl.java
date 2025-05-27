package com.emsi.cc_spring_boot.service;

import com.emsi.cc_spring_boot.entity.Etudiant;
import com.emsi.cc_spring_boot.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Etudiant getEtudiantById(Long id) {
        Optional<Etudiant> etudiant = etudiantRepository.findById(id);
        return etudiant.orElse(null);
    }

    @Override
    public Etudiant saveEtudiant(Etudiant etudiant) {
        try {
            return etudiantRepository.save(etudiant);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteEtudiant(Long id) {
        try {
            if (etudiantRepository.existsById(id)) {
                etudiantRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Etudiant> searchEtudiants(String nom, String prenom, String email) {
        return etudiantRepository.searchEtudiants(nom, prenom, email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return etudiantRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExistsExcept(String email, Long id) {
        return etudiantRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countEtudiants() {
        return etudiantRepository.count();
    }
}
