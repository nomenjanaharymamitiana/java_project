package com.medicin.demo.controller;

import com.medicin.demo.model.Medecin;
import com.medicin.demo.repository.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medecins")
public class MedecinController {
    @Autowired
    private MedecinRepository medecinRepository;

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
        return medecinRepository.save(medecin);
    }
}
