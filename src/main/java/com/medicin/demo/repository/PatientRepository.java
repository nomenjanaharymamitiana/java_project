package com.medicin.demo.repository;
import com.medicin.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    @Query("SELECT p.idpat FROM Patient p WHERE p.idpat LIKE CONCAT(?1, '%')")
    List<String> findIdsByPrefix(String prefix);
}
