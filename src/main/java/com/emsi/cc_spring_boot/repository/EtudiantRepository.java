package com.emsi.cc_spring_boot.repository;

import com.emsi.cc_spring_boot.entity.Etudiant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT e FROM Etudiant e WHERE " +
            "(:nom IS NULL OR :nom = '' OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:prenom IS NULL OR :prenom = '' OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "ORDER BY e.nom, e.prenom")
    List<Etudiant> searchEtudiants(@Param("nom") String nom,
                                   @Param("prenom") String prenom,
                                   @Param("email") String email);
}