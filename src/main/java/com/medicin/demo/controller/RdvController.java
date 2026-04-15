package com.medicin.demo.controller;

import com.medicin.demo.model.Medecin;
import com.medicin.demo.model.Patient;
import com.medicin.demo.model.Rdv;
import com.medicin.demo.repository.MedecinRepository;
import com.medicin.demo.repository.PatientRepository;
import com.medicin.demo.repository.RdvRepository;
import com.medicin.demo.service.EmailService;
import com.medicin.demo.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rdv")
public class RdvController {

    @Autowired
    private RdvRepository rdvRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @PostMapping("/reserver")
    public ResponseEntity<?> reserverRdv(@RequestBody Rdv rdv) {
        // 1. Vérifier si l'horaire est disponible
        List<Rdv> existing = rdvRepository.findByMedecinAndDate(rdv.getIdmed(), rdv.getDateRdv());
        if (!existing.isEmpty()) {
            return ResponseEntity.badRequest().body("Cet horaire est déjà réservé.");
        }

        // 2. Enregistrer le RDV
        if (rdv.getIdrdv() == null || rdv.getIdrdv().isBlank()) {
            rdv.setIdrdv(idGeneratorService.nextRdvId());
        }
        rdv.setStatus("CONFIRMED");
        Rdv saved = rdvRepository.save(rdv);

        // 3. Envoyer mail de confirmation
        Optional<Patient> patient = patientRepository.findById(rdv.getIdpat());
        if (patient.isPresent() && patient.get().getEmail() != null) {
            emailService.sendEmail(patient.get().getEmail(), 
                "Confirmation de votre rendez-vous", 
                "Votre rendez-vous avec le docteur " + rdv.getIdmed() + " est confirmé pour le " + rdv.getDateRdv());
        }

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/annuler/{id}")
    public ResponseEntity<?> annulerRdv(@PathVariable String id) {
        Optional<Rdv> rdvOpt = rdvRepository.findById(id);
        if (rdvOpt.isPresent()) {
            Rdv rdv = rdvOpt.get();
            rdv.setStatus("CANCELLED");
            rdvRepository.save(rdv);

            // Mail annulation
            Optional<Patient> patient = patientRepository.findById(rdv.getIdpat());
            if (patient.isPresent() && patient.get().getEmail() != null) {
                emailService.sendEmail(patient.get().getEmail(), 
                    "Annulation de votre rendez-vous", 
                    "Votre rendez-vous du " + rdv.getDateRdv() + " a été annulé.");
            }
            return ResponseEntity.ok("Rendez-vous annulé.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/disponibilites/{idmed}")
    public List<String> getDisponibilites(@PathVariable String idmed, 
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        // Hypothèse: 8h à 17h
        String[] hours = {"08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00"};
        List<Rdv> rdvs = rdvRepository.findByIdmed(idmed);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        List<String> bookedHours = rdvs.stream()
            .filter(r -> !"CANCELLED".equals(r.getStatus()))
            .map(r -> {
                Calendar c = Calendar.getInstance();
                c.setTime(r.getDateRdv());
                if (c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month && c.get(Calendar.DAY_OF_MONTH) == day) {
                    return String.format("%02d:00", c.get(Calendar.HOUR_OF_DAY));
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return Arrays.stream(hours)
            .filter(h -> !bookedHours.contains(h))
            .collect(Collectors.toList());
    }

    @GetMapping("/liste")
    public List<Rdv> getAllRdvs() {
        return rdvRepository.findAll();
    }

    @GetMapping("/patient/{idpat}")
    public List<Rdv> getRdvsByPatient(@PathVariable String idpat) {
        return rdvRepository.findByIdpat(idpat);
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<?> updateRdv(@PathVariable String id, @RequestBody Rdv details) {
        Optional<Rdv> rdvOpt = rdvRepository.findById(id);
        if (rdvOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Rdv existing = rdvOpt.get();
        Date newDate = details.getDateRdv() != null ? details.getDateRdv() : existing.getDateRdv();
        String newMedecin = details.getIdmed() != null && !details.getIdmed().isBlank() ? details.getIdmed() : existing.getIdmed();
        List<Rdv> slotConflict = rdvRepository.findByMedecinAndDate(newMedecin, newDate).stream()
                .filter(r -> !r.getIdrdv().equals(id))
                .toList();
        if (!slotConflict.isEmpty()) {
            return ResponseEntity.badRequest().body("Cet horaire est déjà réservé.");
        }

        existing.setIdmed(newMedecin);
        if (details.getIdpat() != null && !details.getIdpat().isBlank()) {
            existing.setIdpat(details.getIdpat());
        }
        existing.setDateRdv(newDate);
        if (details.getStatus() != null && !details.getStatus().isBlank()) {
            existing.setStatus(details.getStatus());
        }
        return ResponseEntity.ok(rdvRepository.save(existing));
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> deleteRdv(@PathVariable String id) {
        if (rdvRepository.existsById(id)) {
            rdvRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/top5")
    public List<Medecin> getTop5Doctors() {
        List<String> ids = rdvRepository.findTopDoctorIds(PageRequest.of(0, 5));
        return medecinRepository.findAllById(ids);
    }
}
