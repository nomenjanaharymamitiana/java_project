package com.medicin.demo.repository;

import com.medicin.demo.model.Rdv;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RdvRepository extends JpaRepository<Rdv, String> {
    List<Rdv> findByIdmed(String idmed);
    List<Rdv> findByIdpat(String idpat);
    
    @Query("SELECT r FROM Rdv r WHERE r.idmed = ?1 AND r.dateRdv = ?2 AND r.status <> 'CANCELLED'")
    List<Rdv> findByMedecinAndDate(String idmed, Date dateRdv);

    @Query("SELECT r.idmed FROM Rdv r GROUP BY r.idmed ORDER BY COUNT(r.idmed) DESC")
    List<String> findTopDoctorIds(Pageable pageable);
}
