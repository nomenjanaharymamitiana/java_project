package com.medicin.demo.controller;

import com.medicin.demo.model.Patient;
import com.medicin.demo.repository.PatientRepository;
import com.medicin.demo.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @GetMapping("/liste")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @PostMapping("/inscription")
    public Patient createPatient(@RequestBody Patient patient) {
        if (patient.getIdpat() == null || patient.getIdpat().isBlank()) {
            patient.setIdpat(idGeneratorService.nextPatientId());
        }
        return patientRepository.save(patient);
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patientDetails) {
        return patientRepository.findById(id).map(patient -> {
            patient.setNom_pat(patientDetails.getNom_pat());
            patient.setDatenais(patientDetails.getDatenais());
            patient.setEmail(patientDetails.getEmail());
            return ResponseEntity.ok(patientRepository.save(patient));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable String id) {
        return patientRepository.findById(id).map(patient -> {
            patientRepository.delete(patient);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
