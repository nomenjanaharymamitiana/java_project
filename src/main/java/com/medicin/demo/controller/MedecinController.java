package com.medicin.demo.controller;

import com.medicin.demo.model.Medecin;
import com.medicin.demo.repository.MedecinRepository;
import com.medicin.demo.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medecins")
public class MedecinController {
    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @GetMapping("/liste")
    public List<Medecin> getAllMedecins() {
        return medecinRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medecin> getById(@PathVariable String id) {
        return medecinRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recherche")
    public List<Medecin> searchMedecin(@RequestParam String nom) {
        return medecinRepository.findByNommedContaining(nom); // Traitement LIKE %..%
    }

    @GetMapping("/specialite/{spec}")
    public List<Medecin> getBySpecialite(@PathVariable String spec) {
        return medecinRepository.findBySpecialite(spec);
    }
    
    @PostMapping("/ajouter")
    public Medecin addMedecin(@RequestBody Medecin medecin) {
        if (medecin.getIdmed() == null || medecin.getIdmed().isBlank()) {
            medecin.setIdmed(idGeneratorService.nextMedecinId());
        }
        return medecinRepository.save(medecin);
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<Medecin> updateMedecin(@PathVariable String id, @RequestBody Medecin details) {
        return medecinRepository.findById(id).map(existing -> {
            existing.setNommed(details.getNommed());
            existing.setSpecialite(details.getSpecialite());
            existing.setTaux_horaire(details.getTaux_horaire());
            existing.setLieu(details.getLieu());
            existing.setEmail(details.getEmail());
            if (details.getPassword() != null && !details.getPassword().isBlank()) {
                existing.setPassword(details.getPassword());
            }
            return ResponseEntity.ok(medecinRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> deleteMedecin(@PathVariable String id) {
        if (!medecinRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        medecinRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
