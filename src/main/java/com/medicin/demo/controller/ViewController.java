package com.medicin.demo.controller;

import com.medicin.demo.model.Medecin;
import com.medicin.demo.repository.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ViewController {

    @Autowired
    private MedecinRepository medecinRepository;

    @GetMapping("/medecin")
    public String indexMedecin() {
        return "medecin/index";
    }

    @GetMapping("/medecin/liste-docteur")
    public String listeMedecins(Model model) {
        model.addAttribute("medecins", medecinRepository.findAll());
        return "medecin/liste"; 
    }

    @GetMapping("/loginadmin")
    public String pageLogin() {
        return "medecin/login-admin";
    }

    @GetMapping("/Admin-acceuil")
    public String pageAcceuil() {
        return "medecin/Admin-acceuil";
    }

    // --- PATIENT VIEWS ---
    @GetMapping("/patient")
    public String indexPatient(Model model) {
        model.addAttribute("medecins", medecinRepository.findAll());
        return "patient/index";
    }

    @GetMapping("/patient/inscription")
    public String formInscription() {
        return "patient/inscription";
    }

    @GetMapping("/patient/mes-rendez-vous")
    public String mesRdv() {
        return "patient/mes-rdv";
    }

    // --- SEARCH & STATS ---
    @GetMapping("/medecin/recherche")
    public String pageRecherche() {
        return "medecin/recherche";
    }

    @GetMapping("/medecin/top")
    public String pageTopDoctors(Model model) {
        // We'll let the frontend fetch via API or pass data here
        return "medecin/top5";
    }

    @GetMapping("/medecin/rendez-vous")
    public String pagePlanning() {
        return "medecin/planning";
    }

    @PostMapping("/loginadmin")
    public String authentification(@RequestParam("email") String email, 
                                   @RequestParam("password") String password, 
                                   Model model) {
        Optional<Medecin> med = medecinRepository.findByEmailAndPassword(email, password);
        if (med.isPresent()) {
            return "redirect:/Admin-acceuil";
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "medecin/login-admin";
        }
    }

    // AFFICHER LE FORMULAIRE
    @GetMapping("/medecin/modifierdocteur/{id}")
    public String afficherFormulaire(@PathVariable("id") String id, Model model) {
        Optional<Medecin> med = medecinRepository.findById(id);
        if (med.isPresent()) {
            model.addAttribute("medecin", med.get());
            // VERIFIE BIEN LE NOM DE CE FICHIER SUR TON DISQUE DUR
            return "medecin/modifier_docteur"; 
        }
        return "redirect:/medecin/liste-docteur";
    }

    // ENREGISTRER
    @PostMapping("/medecin/update")
    public String modifierMedecin(@ModelAttribute("medecin") Medecin medecin) {
        medecinRepository.save(medecin); 
        return "redirect:/medecin/liste-docteur";
    }

    // SUPPRIMER
    @GetMapping("/medecin/supprimer/{id}")
    public String supprimerMedecin(@PathVariable("id") String id) {
        medecinRepository.deleteById(id);
        return "redirect:/medecin/liste-docteur";
    }
}