package com.medicin.demo.repository;

import com.medicin.demo.model.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional; // IMPORTANT

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, String> {
    
    // CETTE LIGNE EST INDISPENSABLE POUR LE LOGIN
    Optional<Medecin> findByEmailAndPassword(String email, String password);

    List<Medecin> findByNommedContaining(String nom);
    List<Medecin> findBySpecialite(String specialite);
}