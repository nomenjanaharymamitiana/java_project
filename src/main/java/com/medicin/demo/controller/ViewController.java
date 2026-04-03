package com.medicin.demo.controller;

import com.medicin.demo.model.Medecin; // VÉRIFIE BIEN CE CHEMIN
import com.medicin.demo.repository.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;   // AJOUTÉ
import org.springframework.web.bind.annotation.RequestParam; // AJOUTÉ
import java.util.Optional;                                   // AJOUTÉ
import org.springframework.web.bind.annotation.PathVariable; // AJOUTÉ
import org.springframework.web.bind.annotation.ModelAttribute; // AJOUTÉ


@Controller
public class ViewController {

    @Autowired
    private MedecinRepository medecinRepository;

    @GetMapping("/medecin")
    public String indexMedecin() {
        return "medecin/index";
    }

    @GetMapping("/medecin/liste")
    public String listeMedecins(Model model) {
        model.addAttribute("medecins", medecinRepository.findAll());
        return "medecin/liste"; 
    }

    // Affiche la page de login (quand on clique sur le bouton dans index.html)
    @GetMapping("/login-docteur")
    public String pageLogin() {
        return "medecin/login_docteur";
    }
    //affiche la page d'acceuil 
    @GetMapping("/M_acceuil")
    public String pageAcceuil() {
        return "medecin/M_acceuil";
    }

    // Reçoit les données du formulaire de login
    @PostMapping("/logindocteur")
    public String authentification(@RequestParam("email") String email, 
                                   @RequestParam("password") String password, 
                                   Model model) {
        
        // DEBUG : Affiche dans ton terminal mamitianaKely
        System.out.println("DEBUG >>> Tentative avec Email: [" + email + "] et Password: [" + password + "]");
    
        Optional<Medecin> med = medecinRepository.findByEmailAndPassword(email, password);
    
        if (med.isPresent()) {
            System.out.println("DEBUG >>> MÉDECIN TROUVÉ : " + med.get().getNommed());
            return "redirect:/M_acceuil";
        } else {
            System.out.println("DEBUG >>> AUCUN MÉDECIN TROUVÉ DANS POSTGRES");
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "login_docteur";
        }
    }
    // 1. Afficher le formulaire pré-rempli
@GetMapping("/medecin/modifier_docteur/{id}")
public String afficherFormulaireModification(@PathVariable("id") String id, Model model) {
    Optional<Medecin> medecin = medecinRepository.findById(id);
    if (medecin.isPresent()) {
        model.addAttribute("medecin", medecin.get());
        return "medecin/modifier"; // Nom du fichier HTML
    }
    return "redirect:/medecin/liste";
}

// 2. Enregistrer les modifications
@PostMapping("/medecin/update")
public String modifierMedecin(@ModelAttribute("medecin") Medecin medecin) {
    // save() fait un "update" si l'ID existe déjà en base
    medecinRepository.save(medecin); 
    return "redirect:/medecin/liste";
}
}